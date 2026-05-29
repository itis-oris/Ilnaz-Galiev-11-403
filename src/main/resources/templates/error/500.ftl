<#assign pageTitle="500 — внутренняя ошибка">
<#include "../main.ftl">

<#macro content>
    <div class="form-wrapper" style="text-align:center;">
        <h2>500</h2>
        <p>${message!"Что-то пошло не так. Мы уже разбираемся."}</p>
        <a href="${contextPath}/" class="btn btn-primary">На главную</a>
    </div>
</#macro>
