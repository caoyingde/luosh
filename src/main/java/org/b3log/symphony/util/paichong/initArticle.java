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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ansj.app.keyword.KeyWordComputer;
import org.b3log.latke.ioc.LatkeBeanManager;
import org.b3log.latke.ioc.Lifecycle;
import org.b3log.latke.ioc.inject.Inject;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.symphony.SymphonyServletListener;
import org.b3log.symphony.service.ArticleMgmtService;
import org.json.JSONObject;

import com.alibaba.druid.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@WebServlet({"/initArticle"})
public class initArticle extends HttpServlet
{

	/**
	 * Bean manager.
	 */
	private static LatkeBeanManager beanManager;
	private static final Logger LOGGER = Logger.getLogger(SymphonyServletListener.class.getName());
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public initArticle() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	  {
	    beanManager = Lifecycle.getBeanManager();
	    String action = request.getParameter("action");
	    String tagid = request.getParameter("tagid");
	    String channel = request.getParameter("channel");
	    String hot = request.getParameter("hot");
	    String start = request.getParameter("start");
	    String limit = request.getParameter("limit");
	    String tags = request.getParameter("tags");
	    try {
	      initDB(tagid, action, channel, hot, start, limit, tags);
	    } catch (Exception e) {
	      LOGGER.log(Level.ERROR, "初始db出错！", e);
	    }
	  }

  

  private static void initDB(String tagid, String action, String channel, String hot, String start, String limit, String tags)
  {
    LOGGER.info("Initializing Sym....");

	final ArticleMgmtService articleMgmtService = beanManager.getReference(ArticleMgmtService.class);
    beanManager = Lifecycle.getBeanManager();
    try
    {
      ArrayList<Map<String, String>> articleList = getChannelstorylist(tagid, action, channel, hot, start, limit, tags);
      for (int i = 0; i < articleList.size(); i++) {
        String[] oIds = { 
          "1452689709996", "1452696267206", "1452770023859", "1452770079306", "1452770131022", "1452782461180", 
          "1452782184796", "1452782206482", "1452782225058", "1452782303679", "1452782343711", "1452782374107", "1452782427549", "1452824158970", 
          "1452824018269", "1452824044888", "1452824063363", "1452824085364", "1452824101387", "1452824115920", "1452824140445", "1452824182719", 
          "1452824217128", "1452838801584", "1453275115994" };

        String[] userEmails = { 
          "alice@qq.com", "sz2975@qq.com", "lily@qq.com", "sz1988@qq.com", "liu@qq.com", "meimei@qq.com", 
          "meizi@qq.com", "gg@qq.com", "ad99@qq.com", "zh1992@qq.com", "faceme@qq.com", "fa002@qq.com ", "xue@qq.com", "withyou@qq.com", 
          "needyou@qq.com", "az@qq.com", "gread@qq.com", "header@qq.com", "leader@qq.com", "supper@qq.com", "supperman@qq.com", "suppergirl@qq.com", 
          "girlgirl@qq.com", "make@qq.con", "3121224751@qq.com" };

        Random r = new Random();
        int randomKey = r.nextInt(oIds.length);
        String content = (String)((Map)articleList.get(i)).get("content");
        String title = (String)((Map)articleList.get(i)).get("title");
        System.out.println(title);

        KeyWordComputer kwc = new KeyWordComputer(5);
        Collection result = kwc.computeArticleTfidf(title, content);
        String str = result.toString().replaceAll("\\d+", "").replaceAll("/.", "").replace("我们", "");

        System.out.println("标签：" + str);

        ResourceBundle.getBundle("init");

        JSONObject article = new JSONObject();
        article.put("articleTitle", title);
        article.put("articleTags", tags + "," + str);
        article.put("articleContent", content);
        article.put("articleEditorType", 0);

        /*article.put("articleAuthorEmail", userEmails[randomKey]);*/
        article.put("articleAuthorId", oIds[randomKey]);
        article.put("articleIsBroadcast", false);
        articleMgmtService.addArticle(article);
      }
      LOGGER.info("Initialized Sym");
    } catch (Exception e) {
      LOGGER.log(Level.ERROR, "Creates database tables failed", e);
    }
  }
  public static void main(String[] args)
  {
    KeyWordComputer kwc = new KeyWordComputer(5);
    String title = "情的结果不是我们所能掌控的";
    String content = "我，极寒之夜降生的嗜奶帝，夜啼魔，驭父者，催母官，蹬腿侠，七级翻脸天赋者，大器早成的继承人，我，就是翻滚在襁褓中的陈大桥。爸爸不服，他觉得他讲得更好，于是他是这么讲的：“从前，羊圈里有三兄弟，它们是，老大绵羊，老二山羊，老三羚羊，它们相亲相爱的在一起，直到有一天，它们看了一本叫《物种起源》的书，才知道原来它们并不是亲兄弟，大哭一场后，老大绵羊说，再见了，我要去找我的亲生父母！于是绵羊就离圈出走了。啊走啊，走到了一片大草原上，它看到了一团毛茸茸的雪白的东西，绵羊扑上去哭喊：妈妈！妈妈！你就是我的妈妈！毛茸茸的东西摇起了头：不不不！我不是你的妈妈！它摇头摇得太厉害，脑袋飞了起来，声音飘散在风中：老子是蒲公英...蒲...公...英....公....英....英....绵羊想，找妈妈太难了，我还是回家吧...”“这个故事告诉我们什么呢？”爸爸慈祥的看着我说“这个故事告诉我们，多读书可以让我们知道自己是谁....我的爸妈，真是渊博的爸妈。我就想好好吃个奶！我不要去斯巴达...据说家里多了我，开销变大了好多...妈妈给自己买了好多漂亮衣服和护肤品，她说，要从小就把我对异性的审美标准线拔高，这样我就不会被外面花花绿绿的小姑娘们拐走....爸爸斥巨资给自己买了心仪很久的珍藏款瑞士军刀，他说，家里有我，就得有传家宝刀....他还打算买台拳皇街机，放在车库里天天玩！科科，等我满十八周岁，他就可以领我进车库，指着街机说这是他留给我的财富....在这之前，他准备合着妈妈一起跟我说：车库不能进，那是爸爸闭关修炼的地方.....家里开销真的变得好大...我有钱，我也不想穿貂啊...爸爸妈妈终于认真的讨论要给我留点啥了...可是爸爸又说！不管留啥，我们都要像个堂堂大家族！所以必须跟《冰与火之歌》里的家族一样一样的，我们必须有家族箴言！还必须有家徽！zuo 如我爸妈，他们陷入长达两个月的间歇性睡前苦恼，什么样的箴言才能配上我们老陈家？！才能震慑四方？比如说：陈家不可欺，不信问上帝。 比如说：星火燎原，生生不息....什么鬼！至于家徽，科科，身为设计师的妈妈坚持要先有箴言再想家徽，不然她就画颗卤蛋，跟爸爸脑袋一样一样的！ 爸爸坚持先有家徽，才能激发箴言的灵感！最后，他们达成一致，不然想个族谱！族谱！族谱！也是大家族范儿！是像我爸这样爷爷辈就漂洋过海从台湾来这的人，寻根溯源难度略大，妈妈祖上又世代贫农，目不识丁...ut！这些难不倒他们！爸爸决定封自己为陈太祖！妈妈陈太奶！从他们开始记族谱！从此，千年家业，始于他们也....家谱，以后儿孙世世代代取名就得有规律了！一首古诗或者一副对联怎么样？然后我和我儿子和我孙子和我儿子的孙子和我孙子的儿子，孙子的孙子，按顺序取里面的字取名....那啥古诗或者对联好呢？爸爸妈妈诌着诌着就睡着了...我叫陈大桥，我的名字真的不是按：门前大桥下，游过一群鸭....这首儿歌的字词顺序来取的....妈说我可帅了哈喽，世界！我已经和你相处快70天啦，是时候和世人分享下让我爽爽的过得更容易的人生经验了：1. 奶嘴和奶嘴也是不一样的，只有柔软的奶嘴才能安抚柔软的心。2. 喝进肚子里的东西，要新鲜，越接近体温，越绵长舒服。3. 擦不干净的屎，会让你红屁股的。4. 只有爱你的人，才会在你臭屁熏天的时候，一脸坚毅的走向你。5. 嫌你闹腾的人，一定忘了我们在娘胎里一宅十个月的寂寞6. 真•宅男，出趟远门，一定会躺下让人推着走7. 早上8点半-10点，花点时间晒晒屁股哭的时候，甩自己的头，甩出来的泪痕就会比较长，更容易让人觉得你惨。. 每天晚上泡澡. 还是泡澡，如果太舒服想多泡一会儿，就在大人要把你捞起来之前在澡盆里尿尿！最好屙屎！他们就不得不换遍水，再让你泡一遍11.抬得起头，就会有人赞你硬骨头12. 面无表情的发会儿呆，那个在人群里说你有心事的人，就是爱你的人，没跑了13. 哭能解决的事情，决不要废话。要不能解决，那就省着点哭。14. 要哭，就不要吝啬眼泪。我爸让我在床上嚎了十分钟，我嗓子都哑了，他只是瞥瞥我，说我假哭，一滴眼泪都没有....而我妈竖着耳朵听了半天，开心的说我声音真好听，辣种哑哑的摇滚小烟嗓...不说了，我要真心诚意的哭一会儿...皇上！皇上！您听微臣说啊！！皇上！！”我有三条小毛驴，绝不借给阿凡提...爸爸的吻，是胡子扎扎的吻...爸爸这段时间工作好辛苦，要出差要开会要写文档，昨天晚上他又焦虑得睡不着觉了，他说：“都三点了，大桥怎么还不拉屎？”爸爸每次想像我的将来，不知道为啥，都是以他揍我结尾...爸爸深沉的说：男人的第一个敌人来自父亲爸爸说太顺利的人生就会经不起挫折...啊！！只要想到将来的人生里有个时刻想揍我的高大身影！我真的不会过得太容易啊爸爸！！！爸爸说希望我保持理性...妈妈说我睡觉前看起来总是有点奶醉...俺娘说今年收成要不好，俺的尿布能省就省...“我是宁采臣，我还小，不懂缘分。”Wuli大法官今天好忙，没空和你们玩";
    Collection result = kwc.computeArticleTfidf(content, content);
    String str = result.toString().replaceAll("\\d+", "").replaceAll("/.", "").replace("我们,", "").replace(",我们", "");
    System.out.println(str);
  }

  public static void work(int No)
  {
  }

  private static ArrayList<Map<String, String>> getChannelstorylist(String tagid, String action, String channel, String hot, String start, String limit, String tags)
  {
	ArrayList<Map<String, String>> rs = new ArrayList<Map<String, String>>();
    String url = "http://www.xiniugushi.com/function/story.inc.php?action=" + action;
    String param = "";
    if ((!StringUtils.isEmpty(action)) && (action.equals("dailyhotstorylist")))
      param = "channel=" + channel + "&hot=" + hot + "&start=" + start + "&limit=" + limit;
    else if (action.equals("tagstorylist"))
      param = "tagid=" + tagid + "&start=" + start + "&limit=" + limit;
    try
    {
      String httpPost = HttpClientUtil.INSTANCE.httpPost(url, param, null, "x-www-form-urlencoded");
      System.out.println(httpPost);

      JsonObject jo = JsonToMap.parseJson(httpPost);
      String success = jo.get("success")+"";
      if (success.equals("1")) {
        String data = jo.get("data")+"";
        Gson gson = new Gson();
        ArrayList<Map<String, String>> list = gson.fromJson(data, new TypeToken<List<Map<String, String>>>() {
        }.getType());
        for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			String storyid = list.get(i).get("storyid") + "";
			String randcode = list.get(i).get("randcode") + "";
			String title = list.get(i).get("title") + "";

			map.put("storyid", storyid);
			map.put("randcode", randcode);
			map.put("title", title);

			Map<String, String> ContentMap = getSiteContent(randcode);
			map.put("content", ContentMap.get("content") + "");
			rs.add(map);
			System.out.println(list.get(i).get("title"));
		}
        System.out.println("data:" + data);
      }
    } catch (Exception e1) {
      e1.printStackTrace();
    }
    return rs;
  }

  private static Map<String, String> getSiteContent(String randcode) {
		String url = "http://www.xiniugushi.com/function/story.inc.php?action=getstorydetails";
		Map<String, String> rs = new HashMap<String, String>();
		String param = "storyid=" + randcode + "&start=0&limit=1000";
		try {
			String httpPost = HttpClientUtil.INSTANCE.httpPost(url, param, null, "x-www-form-urlencoded");
			System.out.println(httpPost);
			JsonObject jo = JsonToMap.parseJson(httpPost);
			String success = jo.get("success") + "";
			if (success.equals("1")) {
				String data = jo.get("data") + "";
				Gson gson = new Gson();
				ArrayList<Map<String, String>> list = gson.fromJson(data, new TypeToken<List<Map<String, String>>>() {
				}.getType());
				String content = "";
				for (int i = 0; i < list.size(); i++) {
					content = content + list.get(i).get("content") + "";
				}
				System.out.println(content);
				if (content != null && content != "") {
					rs.put("content", content);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return rs;
	}
}