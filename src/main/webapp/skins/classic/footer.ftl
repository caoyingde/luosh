<div class="footer">
    <div class="wrapper">
        <div class="slogan">
            ${indexIntroLabel} &nbsp; &nbsp;
            <a target="_blank" class="tooltipped tooltipped-n" aria-label="${joinQQGroupLabel}"
               href="http://shang.qq.com/wpa/qunwpa?idkey=679be421edb579d2bc454b510f941d1d28c2c5daf9cc63bb200d804cd73c1184">
                <svg class="icon-qq"><use xlink:href="#qq"></use></svg></a>
        </div>
        <div class="fn-flex-1">
            <div class="footer-nav fn-clear">
                <a rel="help" href="${servePath}/about">${aboutLabel}</a>
                <a href="${servePath}/tag/announcement">${symAnnouncementLabel}</a>
                <a href="${servePath}/domains">${domainLabel}</a>
                <a href="${servePath}/tags">${tagLabel}</a>
                <a href="${servePath}/forge/link">${forgeLabel}</a>
                <a href="${servePath}/statistic">${dataStatLabel}</a>
                <a href="${servePath}/article/1507370540915">${adDeliveryLabel}</a>
				<a href="${servePath}/article/1507522896309">友链</a>
                <div class="fn-right">&COPY; ${year}
                    <a rel="copyright" href="http://luosh.com" target="_blank">落事网</a>
                    ${visionLabel}</div>
            </div>
            <div class="fn-clear ft-smaller">
                       ${sloganLabel}
                    <div class="fn-right">
                       Powered by <a href="http://b3log.org" target="_blank">B3log 开源</a> •
                            <a href="https://github.com/b3log/symphony" target="_blank">Sym</a>
                            ${version} • ${elapsed?c}ms
                    </div>
                </div>
        </div>
    </div>
</div>

<script src="${staticServePath}/js/symbol-defs.js?${staticResourceVersion}"></script>
<script src="${staticServePath}/js/lib/compress/libs.min.js?${staticResourceVersion}"></script>
<script src="${staticServePath}/js/common${miniPostfix}.js?${staticResourceVersion}"></script>
<script>
    var Label = {
        invalidPasswordLabel: "${invalidPasswordLabel}",
        loginNameErrorLabel: "${loginNameErrorLabel}",
        followLabel: "${followLabel}",
        unfollowLabel: "${unfollowLabel}",
        symphonyLabel: "${symphonyLabel}",
        visionLabel: "${visionLabel}",
        cmtLabel: "${cmtLabel}",
        collectLabel: "${collectLabel}",
        uncollectLabel: "${uncollectLabel}",
        desktopNotificationTemplateLabel: "${desktopNotificationTemplateLabel}",
        servePath: "${servePath}",
        staticServePath: "${staticServePath}",
        isLoggedIn: ${isLoggedIn?c},
        funNeedLoginLabel: '${funNeedLoginLabel}',
		notificationCommentedLabel: '${notificationCommentedLabel}',
		notificationReplyLabel: '${notificationReplyLabel}',
		notificationAtLabel: '${notificationAtLabel}',
		notificationFollowingLabel: '${notificationFollowingLabel}',
		pointLabel: '${pointLabel}',
		sameCityLabel: '${sameCityLabel}',
		systemLabel: '${systemLabel}',
		newFollowerLabel: '${newFollowerLabel}',
        makeAsReadLabel: '${makeAsReadLabel}'
        <#if isLoggedIn>,
            currentUserName: '${currentUser.userName}'
        </#if>
    };

    <#if isLoggedIn>
    Label.userKeyboardShortcutsStatus = '${currentUser.userKeyboardShortcutsStatus}';
    </#if>

    Util.init(${isLoggedIn?c});

    <#if isLoggedIn>
    // Init [User] channel
    Util.initUserChannel("${wsScheme}://${serverHost}:${serverPort}${contextPath}/user-channel");
    </#if>

    <#if mouseEffects>
    Util.mouseClickEffects();
    </#if>
</script>
<#if algoliaEnabled>
<script src="${staticServePath}/js/lib/algolia/algolia.min.js"></script>
<script>
    Util.initSearch('${algoliaAppId}', '${algoliaSearchKey}', '${algoliaIndex}');
</script>
</#if>
