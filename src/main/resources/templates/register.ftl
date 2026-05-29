<#assign pageTitle="Регистрация">
<#include "main.ftl">

<#macro content>
    <div class="form-wrapper">
        <form action="${contextPath}/register" method="post" class="form">
            <h2>Создать аккаунт</h2>
            <#if error??><p class="error">${error}</p></#if>

            <div class="form-group">
                <label>Логин</label>
                <input type="text" name="username" required minlength="3" value="${(form.username)!''}">
                <#if (binding?? && binding.hasFieldErrors('username'))>
                    <small class="field-error">${binding.getFieldError('username').defaultMessage}</small>
                </#if>
            </div>
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" required value="${(form.email)!''}">
            </div>
            <div class="form-group">
                <label>Пароль</label>
                <input type="password" name="password" required minlength="6">
            </div>
            <div class="form-group">
                <label>Город</label>
                <input type="text" name="city" value="${(form.city)!''}">
            </div>
            <div class="form-group">
                <label>О себе (необязательно)</label>
                <textarea name="bio">${(form.bio)!''}</textarea>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-primary">Зарегистрироваться</button>
        </form>
    </div>
</#macro>
