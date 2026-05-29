<#assign pageTitle="Объявления">
<#include "main.ftl">

<#macro content>
    <div class="offers">
        <div class="offers__header" style="display:flex; justify-content:space-between; align-items:center;">
            <h2>Активные объявления</h2>
            <#if currentUser??>
                <a href="${contextPath}/listings/create" class="btn btn-success">+ Создать</a>
            </#if>
        </div>

        <div class="filters">
            <div class="form-group">
                <label>Категория</label>
                <select id="filter-category">
                    <option value="">Все</option>
                    <#list categories as cat>
                        <option value="${cat.id}">${cat.name}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group">
                <label>Город</label>
                <input type="text" id="filter-city" placeholder="Например: Москва">
            </div>
            <div class="form-group">
                <label>Мин. рейтинг автора</label>
                <input type="number" id="filter-min-rating" min="0" max="5" step="0.5">
            </div>
            <button type="button" class="btn btn-primary" id="filter-apply">Применить</button>
            <button type="button" class="btn" id="filter-reset">Сброс</button>
        </div>

        <div id="listings-grid" class="offers-grid">
            <#list listings as l>
                <#include "fragments/listing-card.ftl">
            </#list>
            <#if listings?size == 0>
                <p class="empty">Пока нет объявлений. Будьте первым!</p>
            </#if>
        </div>
    </div>

    <script src="${contextPath}/static/js/listings-filter.js"></script>
</#macro>
