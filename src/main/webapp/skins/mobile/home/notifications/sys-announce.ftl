<#include "macro-notifications.ftl">
<@notifications "sysAnnounce">
<#if sysAnnounceNotifications?size != 0>
<ul class="notification">
    <#list sysAnnounceNotifications as notification>
    <li class="fn-flex<#if notification.hasRead> read</#if>">
        ${notification.description} <span class="ft-gray ft-nowrap">&nbsp; • ${notification.createTime?string('yyyy-MM-dd HH:mm')}</span>
    </li>
    </#list>
</ul>
<#else>
<div class="fn-hr10"></div>
<div class="ft-center">${noMessageLabel}</div>
</#if>

<@pagination url="${servePath}/notifications/sys-announce"/></@notifications>