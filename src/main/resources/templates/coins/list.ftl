<#import "/layout/base.ftl" as layout>
<#import "/layout/macros.ftl" as ui>
<@layout.page title="Монеты" active="coins">
    <h1>Каталог монет</h1>

    <form method="get" action="/coins" class="filter-bar">
        <input type="text" name="q" value="${query!''}" placeholder="Поиск по имени, тикеру или ID">
        <button type="submit" class="btn btn-primary">Найти</button>
        <a href="/coins" class="btn btn-link">Сброс</a>
    </form>

    <table class="table">
        <thead>
            <tr><th></th><th>Монета</th><th>Тикер</th><th>Теги</th><th>Цена (USD)</th><th></th></tr>
        </thead>
        <tbody>
        <#list coins as c>
            <tr data-coin-id="${c.id}">
                <td>
                    <#if c.imageUrl??><img src="${c.imageUrl}" alt="${c.symbol}" class="coin-icon"/></#if>
                </td>
                <td><a href="/coins/${c.coinGeckoId}">${c.name}</a></td>
                <td>${c.symbol}</td>
                <td>
                    <#list c.tags as t><span class="badge badge-tag">${t}</span></#list>
                </td>
                <td><@ui.priceCell c.currentPriceUsd/></td>
                <td>
                    <#if isAuthenticated!false>
                        <#assign inWl = (watchlistCoinIds?? && watchlistCoinIds?seq_contains(c.id))>
                        <#if inWl>
                        <form method="post" action="/watchlist/${c.id}/delete">
                            <@ui.csrfInput/>
                            <input type="hidden" name="redirect" value="/coins"/>
                            <button type="submit" class="btn btn-secondary">— Убрать</button>
                        </form>
                        <#else>
                        <form method="post" action="/watchlist/${c.id}">
                            <@ui.csrfInput/>
                            <input type="hidden" name="redirect" value="/coins"/>
                            <button type="submit" class="btn btn-link">+ В watchlist</button>
                        </form>
                        </#if>
                    </#if>
                </td>
            </tr>
        <#else>
            <tr><td colspan="6" class="muted">Ничего не найдено.</td></tr>
        </#list>
        </tbody>
    </table>

    <script>
        document.addEventListener('DOMContentLoaded', () => {
            window.CryptoTracker?.refreshPrices?.();
            setInterval(() => window.CryptoTracker?.refreshPrices?.(), 15000);
        });
    </script>
</@layout.page>
