<#import "/layout/base.ftl" as layout>
<#import "/layout/macros.ftl" as ui>
<@layout.page title="Дашборд" active="dashboard">
    <h1>С возвращением, ${currentUser.username}</h1>

    <section class="card">
        <div class="card-header">
            <h2>Watchlist</h2>
            <a href="/coins" class="btn btn-link">+ Добавить монеты</a>
        </div>
        <#if watchlist?? && watchlist?size gt 0>
            <table class="table" id="watchlist-table">
                <thead>
                <tr><th>Монета</th><th>Цена (USD)</th><th></th></tr>
                </thead>
                <tbody>
                <#list watchlist as item>
                    <tr data-coin-id="${item.coinId}">
                        <td>
                            <a href="/coins/${item.coinGeckoId}">${item.coinSymbol} · ${item.coinName}</a>
                        </td>
                        <td><@ui.priceCell item.currentPriceUsd/></td>
                        <td>
                            <form method="post" action="/watchlist/${item.coinId}/delete">
                                <@ui.csrfInput/>
                                <input type="hidden" name="redirect" value="/dashboard"/>
                                <button type="submit" class="btn btn-link btn-danger">Удалить</button>
                            </form>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </table>
        <#else>
            <p class="muted">Watchlist пустой. <a href="/coins">Перейти к каталогу</a> и что-нибудь добавить.</p>
        </#if>
    </section>

    <section class="card">
        <div class="card-header">
            <h2>Активные алерты</h2>
            <a href="/alerts/new" class="btn btn-primary">+ Новый алерт</a>
        </div>
        <#if alerts?? && alerts?size gt 0>
            <table class="table">
                <thead>
                <tr><th>Монета</th><th>Направление</th><th>Цель</th><th>Статус</th><th>Канал</th></tr>
                </thead>
                <tbody>
                <#list alerts as a>
                    <tr>
                        <td>${a.coinSymbol}</td>
                        <td><@ui.directionBadge a.direction/></td>
                        <td><@ui.priceCell a.targetPrice/></td>
                        <td><@ui.alertBadge a.status/></td>
                        <td><@ui.channelLabel a.channel/></td>
                    </tr>
                </#list>
                </tbody>
            </table>
        <#else>
            <p class="muted">Алертов пока нет.</p>
        </#if>
    </section>

    <section class="card">
        <div class="card-header">
            <h2>Последние уведомления <#if unread gt 0><span class="badge badge-unread">${unread}</span></#if></h2>
            <button id="mark-read-btn" class="btn btn-link">Отметить все прочитанными</button>
        </div>
        <ul class="notification-list" id="notification-list">
            <#list notifications as n>
                <li class="notification-item <#if !n.read>unread</#if>" data-id="${n.id}">
                    <div class="notification-message">${n.message}</div>
                    <time class="muted">${n.createdAt?datetime}</time>
                </li>
            <#else>
                <li class="muted">Уведомлений пока нет — здесь будут срабатывания твоих алертов.</li>
            </#list>
        </ul>
    </section>

    <#if frequentAlerts?? && frequentAlerts?size gt 0>
        <section class="card">
            <h2>Часто срабатывающие алерты (за неделю)</h2>
            <p class="muted">алерты с более 3 срабатываниями за последние 7 дней.</p>
            <table class="table">
                <thead><tr><th>Монета</th><th>Направление</th><th>Цель</th><th>Статус</th></tr></thead>
                <tbody>
                <#list frequentAlerts as a>
                    <tr>
                        <td>${a.coinSymbol}</td>
                        <td><@ui.directionBadge a.direction/></td>
                        <td><@ui.priceCell a.targetPrice/></td>
                        <td><@ui.alertBadge a.status/></td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </section>
    </#if>

    <script>
        document.addEventListener('DOMContentLoaded', () => window.CryptoTracker?.initDashboard?.());
    </script>
</@layout.page>
