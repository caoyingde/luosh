<#include "macro-admin.ftl">
<@admin "misc">
<div class="content">
    <div class="module">
        <div class="module-header">
            <h2>${modifiableLabel}</h2>
        </div>

        <div class="module-panel form fn-clear">
            <form action="${servePath}/admin/misc" method="POST">
                <#list options as item>
                    <#if (permissions["miscAllowAddArticle"].permissionGrant && item.oId == 'miscAllowAddArticle')
                         || (permissions["miscAllowAddComment"].permissionGrant && item.oId == 'miscAllowAddComment')
                         || (permissions["miscAllowAnonymousView"].permissionGrant && item.oId == 'miscAllowAnonymousView')
                         || (permissions["miscLanguage"].permissionGrant && item.oId == 'miscLanguage')
                         || (permissions["miscRegisterMethod"].permissionGrant && item.oId == 'miscAllowRegister')
                    >
                        <label>${item.label}</label>
                        <select id="${item.oId}" name="${item.oId}">
                            <#if "miscAllowRegister" == item.oId || "miscAllowAnonymousView" == item.oId ||
                            "miscAllowAddArticle" == item.oId || "miscAllowAddComment" == item.oId>
                            <option value="0"<#if "0" == item.optionValue> selected</#if>>${yesLabel}</option>
                            <option value="1"<#if "1" == item.optionValue> selected</#if>>${noLabel}</option>
                            <#if "miscAllowRegister" == item.oId>
                            <option value="2"<#if "2" == item.optionValue> selected</#if>>${invitecodeLabel}</option>
                            </#if>
                            </#if>
                            <#if "miscLanguage" == item.oId>
                            <option value="0"<#if "0" == item.optionValue> selected</#if>>${selectByBrowserLabel}</option>
                            <option value="zh_CN"<#if "zh_CN" == item.optionValue> selected</#if>>zh_CN</option>
                            <option value="en_US"<#if "en_US" == item.optionValue> selected</#if>>en_US</option>
                            </#if>
                        </select>
                    </#if>
                </#list>

                <br/><br/>
                <button type="submit" class="green fn-right" >${submitLabel}</button>
            </form>
        </div>
        
        <div class="module-panel form fn-clear">
            <form action="/initArticle" method="POST">
            	<label for="action">action:  (channelstorylist：通道类型   tagstorylist：标签)</label>
                <input type="text" id="action" name="action" value="" />
            	<label for="tagid">tagid  
            	(0: {tagid: "1", title: "爱情"}
				1: {tagid: "12", title: "同性"}
				2: {tagid: "2", title: "青春"}
				3: {tagid: "29", title: "搞笑"}
				4: {tagid: "33", title: "励志"}
				5: {tagid: "60", title: "LivePhoto"}
				6: {tagid: "32", title: "泪点"}
				7: {tagid: "3", title: "暗恋"}
				8: {tagid: "41", title: "古风"}
				9: {tagid: "15", title: "治愈"}
				10: {tagid: "59", title: "我有一个朋友"}
				11: {tagid: "13", title: "悬疑"}
				)</label>
                <input type="text" id="tagid" name="tagid" value="" />
            	<label for="channel">channel  (1:真事  2:创作  3:游记  4:秘密)</label>
                <input type="text" id="channel" name="channel" value="" />
            	<label for="hot">hot (0:最新  1:最热)</label>
                <input type="text" id="hot" name="hot" value="" />
            	<label for="start">start (20:起始)</label>
                <input type="text" id="start" name="start" value="" />
            	<label for="limit">limit (40:线束)</label>
                <input type="text" id="limit" name="limit" value="" />
            	<label for="tags">tags (标签：真事，创作，游记，秘密)</label>
                <input type="text" id="tags" name="tags" value="" />
                <br/><br/>
                <button type="submit" class="green fn-right" >爬取</button>
            </form>
        </div>
    </div>
</div>
</@admin>
