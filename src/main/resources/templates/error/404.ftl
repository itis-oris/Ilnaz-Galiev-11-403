<#assign pageTitle="404 — не найдено">
<#include "../main.ftl">

<#macro content>
    <div class="form-wrapper" style="text-align:center;">
        <h2>404</h2>
        <p>${message!"Страница не найдена"}</p>
        <a href="${contextPath}/" class="btn btn-primary">На главную</a>
    </div>
</#macro>
