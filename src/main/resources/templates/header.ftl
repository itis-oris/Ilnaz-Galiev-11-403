<header class="header">
    <div class="container header__inner">
        <a href="${contextPath!''}/" class="logo">SkillTrade</a>
        <div class="user-info">
            <#if currentUser??>
                <span>Привет, ${currentUser.username}!</span>
                <a href="${contextPath!''}/profile" class="btn btn-sm btn-primary">Профиль</a>
                <form action="${contextPath!''}/logout" method="post" style="display:inline">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit" class="btn btn-sm btn-danger">Выйти</button>
                </form>
            <#else>
                <a href="${contextPath!''}/login" class="btn btn-sm btn-primary">Войти</a>
                <a href="${contextPath!''}/register" class="btn btn-sm btn-primary">Регистрация</a>
            </#if>
        </div>
    </div>
</header>
