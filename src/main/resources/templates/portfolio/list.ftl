<#import "/layout/base.ftl" as layout>
<#import "/layout/macros.ftl" as ui>
<@layout.page title="Портфель" active="portfolio">
    <h1>Портфель</h1>

    <section class="card">
        <h2>Добавить позицию</h2>
        <form method="post" action="/portfolio" class="form-inline-row">
            <@ui.csrfInput/>
            <select name="coinId" required>
                <option value="">— монета —</option>
                <#list coins as c>
                    <option value="${c.id}">${c.symbol} · ${c.name}</option>
                </#list>
            </select>
            <input type="number" step="0.00000001" min="0" name="amount" placeholder="Кол-во" required>
            <input type="number" step="0.00000001" min="0" name="buyPriceUsd" placeholder="Цена покупки (USD)" required>
            <input type="text" name="comment" placeholder="Комментарий" maxlength="256">
            <button type="submit" class="btn btn-primary">Добавить</button>
        </form>
    </section>

    <section class="card">
        <h2>Позиции</h2>
        <table class="table">
            <thead>
            <tr>
                <th>Монета</th><th>Кол-во</th><th>Покупка</th><th>Текущая</th>
                <th>Стоимость</th><th>P/L</th><th>P/L %</th><th>Куплено</th><th>Комментарий</th><th></th>
            </tr>
            </thead>
            <tbody>
            <#list entries as e>
                <tr data-coin-id="${e.coinId}">
                    <td>${e.coinSymbol} · ${e.coinName}</td>
                    <td>${e.amount}</td>
                    <td><@ui.priceCell e.buyPriceUsd/></td>
                    <td class="price-cell" data-price="${e.currentPriceUsd!0}"><@ui.priceCell e.currentPriceUsd/></td>
                    <td><@ui.priceCell e.currentValueUsd/></td>
                    <td>
                        <#if e.pnlUsd??>
                            <span class="pnl <#if e.pnlUsd lt 0>negative<#else>positive</#if>">
                                <@ui.priceCell e.pnlUsd/>
                            </span>
                        <#else><span class="muted">—</span></#if>
                    </td>
                    <td>
                        <#if e.pnlPercent??>${e.pnlPercent?string("0.00")}%<#else><span class="muted">—</span></#if>
                    </td>
                    <td>${e.boughtAt?datetime}</td>
                    <td style="max-width: 160px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                        <#if e.comment?? && e.comment?has_content>
                            <span title="${e.comment}">${e.comment}</span>
                        <#else>
                            <span class="muted">—</span>
                        </#if>
                    </td>
                    <td>
                        <form method="post" action="/portfolio/${e.id}/delete" data-confirm="Удалить позицию?">
                            <@ui.csrfInput/>
                            <button class="btn btn-link btn-danger">Удалить</button>
                        </form>
                    </td>
                </tr>
            <#else>
                <tr><td colspan="10" class="muted">Позиций пока нет.</td></tr>
            </#list>
            </tbody>
        </table>
    </section>

    <script>
        document.addEventListener('DOMContentLoaded', () => {
            window.CryptoTracker?.refreshPrices?.();
            setInterval(() => window.CryptoTracker?.refreshPrices?.(), 15000);
        });
    </script>
</@layout.page>
