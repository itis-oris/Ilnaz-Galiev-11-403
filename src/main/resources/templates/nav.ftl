<nav class="nav">
    <div class="container">
        <ul class="nav__list">
            <li><a href="${contextPath!''}/">Главная</a></li>
            <li><a href="${contextPath!''}/listings">Объявления</a></li>
            <#if currentUser??>
                <li><a href="${contextPath!''}/listings/create">Создать</a></li>
                <li><a href="${contextPath!''}/offers/my">Мои отклики</a></li>
                <li><a href="${contextPath!''}/offers/incoming">Входящие</a></li>
            </#if>
        </ul>
    </div>
</nav>
