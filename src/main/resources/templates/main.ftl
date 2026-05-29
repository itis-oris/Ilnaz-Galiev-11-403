<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle!"SkillTrade"}</title>
    <#if _csrf??>
        <meta name="_csrf" content="${_csrf.token}"/>
        <meta name="_csrf_header" content="${_csrf.headerName}"/>
    </#if>
    <link rel="stylesheet" href="${contextPath!''}/static/css/main.css">
    <link rel="stylesheet" href="${contextPath!''}/static/css/forms.css">
</head>
<body>
<#include "header.ftl">
<#include "nav.ftl">

<main class="container">
    <#if pageTitle??><h1>${pageTitle}</h1></#if>
    <@content />
</main>

<div id="toast-container"></div>

<#include "footer.ftl">
<script src="${contextPath!''}/static/js/toast.js"></script>
</body>
</html>
