<#assign pageTitle="Мои отклики">
<#include "main.ftl">

<#macro content>
    <div class="my-responses">
        <h2>Объявления, на которые вы откликнулись</h2>

        <#if offers?? && offers?size gt 0>
            <div class="offers-grid">
                <#list offers as o>
                    <div class="offer-card">
                        <div class="offer-card__title">${o.listing.title}</div>
                        <div class="offer-card__skills">
                            <span class="skill-offer">${o.listing.canTeach.name}</span>
                            <span class="arrow">↔</span>
                            <span class="skill-request">${o.listing.wantToLearn.name}</span>
                        </div>
                        <p class="offer-card__desc">${o.message}</p>
                        <div class="offer-card__footer">
                            <small>
                                <span class="status-badge status-${o.status}">${o.status}</span>
                                · ${o.createdAt}
                            </small>
                            <a href="${contextPath}/listings/${o.listing.id}" class="btn btn-sm btn-primary">К объявлению</a>
                        </div>
                    </div>
                </#list>
            </div>
        <#else>
            <p class="empty">Вы пока ни на что не откликнулись.</p>
        </#if>
    </div>
</#macro>
