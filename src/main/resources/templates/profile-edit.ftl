<#assign pageTitle="Редактировать профиль">
<#include "main.ftl">

<#macro content>
    <div class="form-wrapper">
        <form action="${contextPath}/profile/edit" method="post" class="form">
            <h2>Редактировать профиль</h2>

            <div class="form-group">
                <label>Город</label>
                <input type="text" name="city" value="${form.city!''}">
            </div>
            <div class="form-group">
                <label>О себе</label>
                <textarea name="bio">${form.bio!''}</textarea>
            </div>

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-primary">Сохранить</button>
        </form>
    </div>

    <div class="form-wrapper" style="margin-top:1.5rem;">
        <h3>Мои навыки</h3>
        <div id="my-skills" class="skills-list">
            <#list user.skills as s>
                <span class="skill-chip">
                    ${s.name}
                    <button type="button" class="btn btn-sm btn-danger" data-remove-skill="${s.id}">×</button>
                </span>
            </#list>
        </div>

        <div class="form-group" style="margin-top:1rem;">
            <label>Добавить существующий</label>
            <select id="add-skill-select">
                <#list allSkills as s>
                    <option value="${s.id}">${s.name}</option>
                </#list>
            </select>
            <button type="button" class="btn btn-success btn-sm" id="add-skill-btn" style="margin-top:0.4rem;">+ Добавить</button>
        </div>

        <hr style="margin:1rem 0;">

        <div class="form-group">
            <label>Создать новый навык</label>
            <input type="text" id="new-skill-name" placeholder="Например: Каллиграфия" maxlength="100">
            <small style="color:#888">Появится в общем каталоге и сразу добавится к вашему профилю.</small>
            <button type="button" class="btn btn-primary btn-sm" id="new-skill-btn" style="margin-top:0.4rem;">+ Создать</button>
        </div>
    </div>

    <script src="${contextPath}/static/js/add-skill.js"></script>
</#macro>
