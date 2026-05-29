<div class="offer-card">
    <div class="offer-card__skills">
        <span class="skill-offer">${l.canTeach.name}</span>
        <span class="arrow">↔</span>
        <span class="skill-request">${l.wantToLearn.name}</span>
    </div>
    <div class="offer-card__title">${l.title}</div>
    <p class="offer-card__desc">${l.description}</p>
    <div class="offer-card__footer">
        <small>
            от <a href="${contextPath}/profile/${l.author.id}">${l.author.username}</a>
            <#if l.author.city??>· ${l.author.city}</#if>
        </small>
        <a href="${contextPath}/listings/${l.id}" class="btn btn-sm btn-primary">Подробнее</a>
    </div>
</div>
