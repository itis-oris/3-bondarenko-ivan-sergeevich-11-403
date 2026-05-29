<#import "/layout/base.ftl" as layout>
<#import "/layout/macros.ftl" as ui>
<@layout.page title="Админка · Монеты" active="admin">
    <h1>Админка · Монеты</h1>

    <section class="card">
        <h2>Добавить монету из CoinGecko</h2>
        <form method="post" action="/admin/coins" class="form-inline-row">
            <@ui.csrfInput/>
            <input type="text" name="coinGeckoId" placeholder="ID на CoinGecko (например, cardano)" required>
            <select name="tagIds" multiple size="4">
                <#list tags as t><option value="${t.id}">${t.name}</option></#list>
            </select>
            <button type="submit" class="btn btn-primary">Добавить</button>
        </form>
    </section>

    <section class="card">
        <h2>Добавить тег</h2>
        <form method="post" action="/admin/coins/tags" class="form-inline-row">
            <@ui.csrfInput/>
            <input type="text" name="name" placeholder="название-тега" required maxlength="32">
            <button type="submit" class="btn btn-secondary">Добавить тег</button>
        </form>
    </section>

    <section class="card">
        <h2>Каталог</h2>
        <table class="table">
            <thead><tr><th></th><th>ID</th><th>Тикер</th><th>Имя</th><th></th></tr></thead>
            <tbody>
            <#list coins as c>
                <tr>
                    <td><#if c.imageUrl??><img src="${c.imageUrl}" class="coin-icon"/></#if></td>
                    <td>${c.coinGeckoId}</td>
                    <td>${c.symbol}</td>
                    <td>${c.name}</td>
                    <td>
                        <form method="post" action="/admin/coins/${c.id}/delete" data-confirm="Удалить?">
                            <@ui.csrfInput/>
                            <button class="btn btn-link btn-danger">Удалить</button>
                        </form>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </section>
</@layout.page>
