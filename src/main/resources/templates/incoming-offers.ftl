<#assign pageTitle="Входящие отклики">
<#include "main.ftl">

<#macro content>
    <div class="incoming-offers">
        <#if offers?? && offers?size gt 0>
            <#list offers as o>
                <div class="review-card">
                    <div class="review-card__meta">
                        На объявление <a href="${contextPath}/listings/${o.listing.id}">${o.listing.title}</a>
                        от <a href="${contextPath}/profile/${o.sender.id}">${o.sender.username}</a>
                        · <span class="status-badge status-${o.status}">${o.status}</span>
                        · ${o.createdAt}
                    </div>
                    <p>${o.message}</p>
                    <#if o.status == "PENDING">
                        <div style="display:flex; gap:0.4rem; margin-top:0.5rem;">
                            <form action="${contextPath}/offers/${o.id}/accept" method="post" style="display:inline">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button class="btn btn-sm btn-success" type="submit">Принять</button>
                            </form>
                            <form action="${contextPath}/offers/${o.id}/decline" method="post" style="display:inline">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button class="btn btn-sm btn-danger" type="submit">Отклонить</button>
                            </form>
                        </div>
                    <#elseif o.status == "ACCEPTED">
                        <form action="${contextPath}/offers/${o.id}/done" method="post" style="display:inline">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button class="btn btn-sm btn-success" type="submit">Завершить обмен</button>
                        </form>
                    </#if>
                </div>
            </#list>
        <#else>
            <p class="empty">Пока никто не откликнулся на ваши объявления.</p>
        </#if>
    </div>
</#macro>
