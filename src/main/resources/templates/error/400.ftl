<#assign pageTitle="400 — некорректный запрос">
<#include "../main.ftl">

<#macro content>
    <div class="form-wrapper" style="text-align:center;">
        <h2>400</h2>
        <p>${message!"Некорректный запрос"}</p>
        <a href="${contextPath}/" class="btn btn-primary">На главную</a>
    </div>
</#macro>
