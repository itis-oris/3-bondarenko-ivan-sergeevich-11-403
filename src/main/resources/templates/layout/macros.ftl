
<#macro alertBadge status>
    <#assign cls = "badge badge-default">
    <#assign label = status>
    <#switch status>
        <#case "ACTIVE"><#assign cls = "badge badge-active"><#assign label = "Активен"><#break>
        <#case "TRIGGERED"><#assign cls = "badge badge-triggered"><#assign label = "Сработал"><#break>
        <#case "DISABLED"><#assign cls = "badge badge-disabled"><#assign label = "Отключён"><#break>
    </#switch>
    <span class="${cls}">${label}</span>
</#macro>

<#macro directionBadge direction>
    <#if direction == "ABOVE">
        <span class="badge badge-up">▲ ВЫШЕ</span>
    <#else>
        <span class="badge badge-down">▼ НИЖЕ</span>
    </#if>
</#macro>

<#macro priceCell price>
    <#if price??>
        <span class="price-cell" data-price="${price}">$${price?string("0.########")}</span>
    <#else>
        <span class="muted">—</span>
    </#if>
</#macro>

<#macro csrfInput>
    <#if _csrf??>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </#if>
</#macro>

<#macro channelLabel channel>
    <#switch channel>
        <#case "WEBSOCKET">Веб-пуш<#break>
        <#case "EMAIL">Email<#break>
        <#case "BOTH">Веб-пуш + Email<#break>
        <#default>${channel}
    </#switch>
</#macro>
