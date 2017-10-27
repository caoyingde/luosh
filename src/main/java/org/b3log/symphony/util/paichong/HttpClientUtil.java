/*
 * Copyright (c) 2012-2016, b3log.org & hacpai.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.b3log.symphony.util.paichong;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.util.Map;

import javax.net.ssl.SSLException;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;


/*********************************************************************************************
 * <pre>
 *     FileName: com.zy.util.http.HttpClientUtil
 *         Desc: Http請求客戶端工具類
 *       author: Z_Z.W - myhongkongzhen@gmail.com
 *      version: 2015-10-08 12:22
 *   LastChange: 2015-10-08 12:22
 *      History:
 * </pre>
 *********************************************************************************************/
public enum HttpClientUtil
{
	INSTANCE;

	private final int      MAX_TOTAL_CONNECTIONS = 200;
	private final int      MAX_ROUTE_CONNECTIONS = 50;
	private final HttpHost DEFAULT_TARGETHOST    = new HttpHost( "http://localhost", 8888 );
	private final int      CONNECT_TIMEOUT       = 61000;
	private final int      SOCKET_TIMEOUT        = 61000;
	private final int      CONN_MANAGER_TIMEOUT  = 61000;

	private final RequestConfig config = RequestConfig.custom().setSocketTimeout( SOCKET_TIMEOUT ).setConnectTimeout( CONNECT_TIMEOUT )
													  .setConnectionRequestTimeout( CONN_MANAGER_TIMEOUT ).build();

	private CloseableHttpClient httpClient = null;

	HttpClientUtil()
	{
		try
		{
			PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
			SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay( true ).build();
			connManager.setDefaultSocketConfig( socketConfig );
			MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount( 200 ).setMaxLineLength( 2000 ).build();
			ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction( CodingErrorAction.IGNORE )
																.setUnmappableInputAction( CodingErrorAction.IGNORE ).setCharset( Consts.UTF_8 )
																.setMessageConstraints( messageConstraints ).build();
			connManager.setDefaultConnectionConfig( connectionConfig );
			connManager.setMaxTotal( MAX_TOTAL_CONNECTIONS );
			connManager.setDefaultMaxPerRoute( MAX_ROUTE_CONNECTIONS );
			connManager.setMaxPerRoute( new HttpRoute( DEFAULT_TARGETHOST ), 50 );

			HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler()
			{
				@Override
				public boolean retryRequest( IOException exception, int executionCount, HttpContext context )
				{
					if ( executionCount >= 5 ) return false;
					if ( exception instanceof InterruptedIOException ) return false;
					if ( exception instanceof UnknownHostException ) return false;
					if ( exception instanceof ConnectTimeoutException ) return false;
					if ( exception instanceof SSLException ) return false;
					HttpClientContext clientContext = HttpClientContext.adapt( context );
					HttpRequest       request       = clientContext.getRequest();
					boolean           idempotent    = !( request instanceof HttpEntityEnclosingRequest );
					if ( idempotent ) return true;
					return false;
				}
			};

			httpClient = HttpClients.custom().setConnectionManager( connManager ).setRetryHandler( retryHandler ).build();
		}
		catch ( Exception e )
		{
			System.out.println( "HttpClientUtil error : "+e.getMessage()+e);
		}
	}
	
	/**
	 * header由調用者set
	 * Create by : 2015年9月14日 上午11:54:59
	 */
	private String doPost( HttpPost httpPost ) throws Exception
	{
		CloseableHttpResponse response =null;
		try
		{
			httpPost.setConfig( config );

			response = httpClient.execute( httpPost, HttpClientContext.create() );
			HttpEntity entity = response.getEntity();
			System.out.println("【HttpClientUtil】entity"+entity );
			
			return EntityUtils.toString( entity, "utf-8" );
		}
		catch ( ClientProtocolException e )
		{
			System.out.println("【HttpClientUtil】 error ="+ e.getMessage()+ e);
			throw e;
		}
		catch ( ParseException e )
		{
			System.out.println("【HttpClientUtil】 error ="+ e.getMessage()+ e );
			throw e;
		}
		catch ( IOException e )
		{
			System.out.println("【HttpClientUtil】 error ="+ e.getMessage()+ e );
			throw e;
		}
		catch ( Exception e )
		{
			System.out.println("【HttpClientUtil】 error ="+ e.getMessage()+ e );
			throw e;
		}finally
		{
			if ( null != response ) response.close();
		}
	}
	
	/**
	 * 避免特殊字符，請使用 URLEncoder.encode（content,"utf-8"）進行轉換
	 * JSON,XML 等 格式傳遞參數
	 * 自定義header
	 * Create by : 2015年9月2日 下午2:52:24
	 *
	 * @param url
	 * @param msg
	 * @param headerMap
	 * @param postType xml\json ..
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public String httpPost( String url, String msg, Map<String, String> headerMap, String postType ) throws Exception
	{
		try
		{
			StringEntity stringEntity = new StringEntity( msg, "utf-8" );// 解决中文乱码问题
			stringEntity.setContentType( "application/" + postType );
			System.out.println( "【HttpClientUtil】json param="+ msg);
			HttpPost httpPost = new HttpPost( url );
			httpPost.setEntity( stringEntity );

			setHeader( httpPost, headerMap );

			return doPost( httpPost );
		}
		catch ( Exception e )
		{
			System.out.println( "【HttpClientUtil】 error ="+e.getMessage()+ e );
			throw e;
		}
	}
	/**
	 * Create by : 2015年9月14日 上午11:03:15
	 */
	private void setHeader( HttpPost httpPost, Map<String, String> headerMap )
	{
		if ( ( null != headerMap ) && ( headerMap.size() > 0 ) ) for ( Map.Entry<String, String> entry : headerMap.entrySet() )
			httpPost.setHeader(entry.getKey().trim(),entry.getValue().trim() );
	}
	
	
}
