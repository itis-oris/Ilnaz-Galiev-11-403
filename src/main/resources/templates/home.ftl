<#assign pageTitle="Главная">
<#include "main.ftl">

<#macro content>
    <section class="hero">
        <h2>Обменивайтесь навыками — бесплатно</h2>
        <p class="lead">
            SkillTrade — платформа, где вы можете предложить свои умения и получить нужные вам в обмен.
            Никаких денег, только взаимопомощь.
        </p>
        <a href="${contextPath}/listings" class="btn btn-primary">Смотреть предложения</a>
    </section>

    <section class="stats" id="stats">
        <div class="stat">
            <span class="stat__number" data-target="${countOfActiveOffers}">0</span>
            <span class="stat__label">активных предложений</span>
        </div>
        <div class="stat">
            <span class="stat__number" data-target="${countOfDidOffers}">0</span>
            <span class="stat__label">обменов завершено</span>
        </div>
        <div class="stat">
            <span class="stat__number" data-target="${userCount}">0</span>
            <span class="stat__label">участников</span>
        </div>
    </section>

    <section class="how-it-works">
        <h3>Как это работает?</h3>
        <ol>
            <li>Создайте объявление: что вы умеете и что хотите получить.</li>
            <li>Другие пользователи откликаются на ваше объявление.</li>
            <li>Договоритесь о деталях и обменяйтесь навыками.</li>
        </ol>
    </section>

    <script src="${contextPath}/static/js/stats-counter.js"></script>
</#macro>
