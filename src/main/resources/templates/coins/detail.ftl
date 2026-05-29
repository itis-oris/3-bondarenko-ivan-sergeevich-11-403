<#import "/layout/base.ftl" as layout>
<#import "/layout/macros.ftl" as ui>
<@layout.page title=coin.name active="coins">
    <div class="coin-header" data-coin-id="${coin.id}">
        <#if coin.imageUrl??><img src="${coin.imageUrl}" alt="${coin.symbol}" class="coin-icon-lg"/></#if>
        <div>
            <h1>${coin.name} <span class="muted">${coin.symbol}</span></h1>
            <div class="big-price">
                <@ui.priceCell coin.currentPriceUsd/>
            </div>
            <#list coin.tags as t><span class="badge badge-tag">${t}</span></#list>
        </div>

        <div class="coin-actions">
            <#if isAuthenticated!false>
                <#if inWatchlist>
                <form method="post" action="/watchlist/${coin.id}/delete">
                    <@ui.csrfInput/>
                    <input type="hidden" name="redirect" value="/coins/${coin.coinGeckoId}"/>
                    <button type="submit" class="btn btn-secondary">Убрать из watchlist</button>
                </form>
                <#else>
                <form method="post" action="/watchlist/${coin.id}">
                    <@ui.csrfInput/>
                    <input type="hidden" name="redirect" value="/coins/${coin.coinGeckoId}"/>
                    <button type="submit" class="btn btn-primary">+ В watchlist</button>
                </form>
                </#if>
                <a href="/alerts/new?coinId=${coin.id}" class="btn btn-link">Создать алерт</a>
            </#if>
        </div>
    </div>

    <section class="card">
        <h2>История цен</h2>
        <#if snapshots?size gt 0>
            <table class="table">
                <thead><tr><th>Время</th><th>Цена (USD)</th></tr></thead>
                <tbody>
                <#list snapshots as s>
                    <tr>
                        <td>${s.recordedAt?datetime}</td>
                        <td><@ui.priceCell s.priceUsd/></td>
                    </tr>
                </#list>
                </tbody>
            </table>
        <#else>
            <p class="muted">Пока нет истории — подожди следующий тик опроса.</p>
        </#if>
    </section>

    <script>
        document.addEventListener('DOMContentLoaded', () => {
            window.CryptoTracker?.refreshPrices?.();
            setInterval(() => window.CryptoTracker?.refreshPrices?.(), 15000);
        });
    </script>
</@layout.page>
