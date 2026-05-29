<#assign pageTitle="Редактировать объявление">
<#include "main.ftl">

<#macro content>
    <div class="form-wrapper">
        <form action="${contextPath}/listings/${listing.id}/edit" method="post" class="form">
            <h2>Редактировать</h2>

            <div class="form-group">
                <label>Заголовок</label>
                <input type="text" name="title" required value="${form.title!''}">
            </div>

            <div class="form-group">
                <label>Предлагаю навык</label>
                <input type="text" name="canTeach" list="skills-list" required
                       value="${form.canTeach!''}">
                <small style="color:#888">Если такого навыка ещё нет — создастся.</small>
            </div>

            <div class="form-group">
                <label>Хочу изучить</label>
                <input type="text" name="wantToLearn" list="skills-list" required
                       value="${form.wantToLearn!''}">
            </div>

            <datalist id="skills-list">
                <#list skills as s>
                    <option value="${s.name}">
                </#list>
            </datalist>

            <div class="form-group">
                <label>Категория</label>
                <select name="categoryId">
                    <option value="">—</option>
                    <#list categories as c>
                        <option value="${c.id}" <#if form.categoryId?? && form.categoryId == c.id>selected</#if>>${c.name}</option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <label>Описание</label>
                <textarea name="description" required minlength="10">${form.description!''}</textarea>
            </div>

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-primary">Сохранить</button>
            <a href="${contextPath}/listings/${listing.id}" class="btn">Отмена</a>
        </form>
    </div>
</#macro>
