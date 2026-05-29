<#assign pageTitle="Профиль">
<#include "main.ftl">

<#macro content>
    <div class="profile">
        <div class="profile__header">
            <div>
                <h2>${user.username}</h2>
                <#if user.city??><small>${user.city}</small></#if>
            </div>
            <div class="rating">Рейтинг: <strong>${rating}</strong> ★</div>
        </div>

        <p><strong>Email:</strong> ${user.email}</p>
        <#if user.bio??>
            <p><strong>О себе:</strong> ${user.bio}</p>
        </#if>

        <#if currentUser?? && currentUser.id == user.id>
            <p><a href="${contextPath}/profile/edit" class="btn btn-sm btn-primary">Редактировать профиль</a></p>
        </#if>

        <h3>Навыки</h3>
        <div class="skills-list">
            <#if user.skills?? && user.skills?size gt 0>
                <#list user.skills as s>
                    <span class="skill-chip">${s.name}</span>
                </#list>
            <#else>
                <small>Пока нет навыков</small>
            </#if>
        </div>

        <h3>Объявления</h3>
        <#if userOffers?? && userOffers?size gt 0>
            <#list userOffers as o>
                <div class="review-card">
                    <strong>${o.title}</strong>
                    <div>
                        <span class="skill-offer">${o.canTeach.name}</span>
                        →
                        <span class="skill-request">${o.wantToLearn.name}</span>
                        <span class="status-badge status-${o.status}">${o.status}</span>
                    </div>
                    <a href="${contextPath}/listings/${o.id}" class="btn btn-sm">Открыть</a>
                </div>
            </#list>
        <#else>
            <p>Нет объявлений</p>
        </#if>

        <h3>Отзывы</h3>
        <#if reviews?? && reviews?size gt 0>
            <#list reviews as r>
                <div class="review-card">
                    <div class="review-card__meta">
                        от <a href="${contextPath}/profile/${r.author.id}">${r.author.username}</a>
                        · ${r.rating}★
                        <#if r.sentiment??><span class="sentiment sentiment-${r.sentiment}">${r.sentiment}</span></#if>
                        · ${r.createdAt}
                    </div>
                    <p>${r.text}</p>
                </div>
            </#list>
        <#else>
            <p>Пока нет отзывов</p>
        </#if>

        <#if currentUser?? && currentUser.id != user.id>
            <hr style="margin:1.5rem 0;">
            <h3>Оставить отзыв</h3>
            <form action="${contextPath}/reviews" method="post" class="form-inline">
                <input type="hidden" name="targetUserId" value="${user.id}">
                <div class="form-group">
                    <label>Оценка</label>
                    <select name="rating" required>
                        <option value="5">5 — отлично</option>
                        <option value="4">4 — хорошо</option>
                        <option value="3">3 — нормально</option>
                        <option value="2">2 — плохо</option>
                        <option value="1">1 — ужасно</option>
                    </select>
                </div>
                <textarea name="text" required minlength="5" placeholder="Расскажите про обмен..."></textarea>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-primary">Отправить</button>
                <small style="color:#888">Отзыв можно оставить только после завершённого обмена (DONE).</small>
            </form>
        </#if>
    </div>
</#macro>
