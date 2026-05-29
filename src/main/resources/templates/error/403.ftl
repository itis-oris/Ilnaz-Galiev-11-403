<#assign pageTitle="403 — доступ запрещён">
<#include "../main.ftl">

<#macro content>
    <div class="form-wrapper" style="text-align:center;">
        <h2>403</h2>
        <p>${message!"У вас нет прав на это действие"}</p>
        <a href="${contextPath}/" class="btn btn-primary">На главную</a>
    </div>
</#macro>
