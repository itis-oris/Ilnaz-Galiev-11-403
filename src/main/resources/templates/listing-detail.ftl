<#assign pageTitle="Объявление">
<#include "main.ftl">

<#macro content>
    <div class="listing-detail">
        <h2>
            <span class="skill-offer">${listing.canTeach.name}</span>
            <span class="arrow">↔</span>
            <span class="skill-request">${listing.wantToLearn.name}</span>
        </h2>
        <h3>${listing.title}</h3>

        <p><strong>Статус:</strong>
            <span class="status-badge status-${listing.status}">${listing.status}</span>
        </p>
        <p><strong>Автор:</strong>
            <a href="${contextPath}/profile/${listing.author.id}">${listing.author.username}</a>
            <#if listing.author.city??>· ${listing.author.city}</#if>
        </p>

        <p id="listing-description"><strong>Описание:</strong> ${listing.description}</p>
        <button type="button" class="btn btn-sm" id="translate-btn"
                data-text="${listing.description}">Перевести на русский</button>

        <#if currentUser?? && listing.status == "ACTIVE" && currentUser.id != listing.author.id>
            <hr style="margin:1.5rem 0;">
            <h3>Отправить оффер</h3>
            <form id="offer-form" class="form-inline" data-listing-id="${listing.id}">
                <textarea name="message" placeholder="Ваше сообщение..." required minlength="5"></textarea>
                <button type="submit" class="btn btn-primary">Откликнуться</button>
            </form>
        </#if>

        <#if currentUser?? && currentUser.id == listing.author.id>
            <hr style="margin:1.5rem 0;">
            <h3>Управление</h3>
            <div style="display:flex; gap:0.5rem; flex-wrap:wrap;">
                <a href="${contextPath}/listings/${listing.id}/edit" class="btn btn-warning btn-sm">Редактировать</a>

                <form action="${contextPath}/listings/${listing.id}/status" method="post" style="display:inline">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <select name="status">
                        <option value="ACTIVE"  <#if listing.status == "ACTIVE">selected</#if>>ACTIVE</option>
                        <option value="PAUSED"  <#if listing.status == "PAUSED">selected</#if>>PAUSED</option>
                        <option value="CLOSED"  <#if listing.status == "CLOSED">selected</#if>>CLOSED</option>
                    </select>
                    <button type="submit" class="btn btn-sm">Сменить статус</button>
                </form>

                <form action="${contextPath}/listings/${listing.id}/delete" method="post" style="display:inline"
                      onsubmit="return confirm('Точно удалить?');">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit" class="btn btn-danger btn-sm">Удалить</button>
                </form>
            </div>

            <#if incomingOffers?? && incomingOffers?size gt 0>
                <h3 style="margin-top:1.5rem;">Входящие отклики (${incomingOffers?size})</h3>
                <#list incomingOffers as o>
                    <div class="review-card">
                        <div class="review-card__meta">
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
                            <form action="${contextPath}/offers/${o.id}/done" method="post" style="display:inline; margin-top:0.5rem;">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button class="btn btn-sm btn-success" type="submit">Завершить обмен</button>
                            </form>
                        </#if>
                    </div>
                </#list>
            </#if>
        </#if>
    </div>

    <script src="${contextPath}/static/js/translate.js"></script>
    <#if currentUser?? && listing.status == "ACTIVE" && currentUser.id != listing.author.id>
        <script src="${contextPath}/static/js/send-offer.js"></script>
    </#if>
</#macro>
