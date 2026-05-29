<#import "/layout/base.ftl" as layout>
<#import "/layout/macros.ftl" as ui>
<@layout.page title="Мои алерты" active="alerts">
    <div class="page-header">
        <h1>Мои алерты</h1>
        <a href="/alerts/new" class="btn btn-primary">+ Новый алерт</a>
    </div>

    <form method="get" action="/alerts" class="filter-bar">
        <select name="status">
            <option value="">Все статусы</option>
            <option value="ACTIVE" <#if statusFilter?? && statusFilter?string == "ACTIVE">selected</#if>>Активные</option>
            <option value="TRIGGERED" <#if statusFilter?? && statusFilter?string == "TRIGGERED">selected</#if>>Сработавшие</option>
            <option value="DISABLED" <#if statusFilter?? && statusFilter?string == "DISABLED">selected</#if>>Отключённые</option>
        </select>
        <select name="direction">
            <option value="">Любое направление</option>
            <option value="ABOVE" <#if directionFilter?? && directionFilter?string == "ABOVE">selected</#if>>Выше</option>
            <option value="BELOW" <#if directionFilter?? && directionFilter?string == "BELOW">selected</#if>>Ниже</option>
        </select>
        <input type="text" name="q" value="${query!''}" placeholder="Имя или тикер монеты">
        <button type="submit" class="btn btn-primary">Применить</button>
        <a href="/alerts" class="btn btn-link">Сброс</a>
    </form>

    <table class="table">
        <thead>
            <tr>
                <th>Монета</th><th>Направление</th><th>Цель</th><th>Статус</th>
                <th>Канал</th><th>Создан</th><th>Сработал</th><th></th>
            </tr>
        </thead>
        <tbody>
        <#list alerts as a>
            <tr>
                <td>${a.coinSymbol} · ${a.coinName}</td>
                <td><@ui.directionBadge a.direction/></td>
                <td><@ui.priceCell a.targetPrice/></td>
                <td><@ui.alertBadge a.status/></td>
                <td><@ui.channelLabel a.channel/></td>
                <td>${a.createdAt?datetime}</td>
                <td><#if a.triggeredAt??>${a.triggeredAt?datetime}<#else><span class="muted">—</span></#if></td>
                <td class="actions">
                    <a href="/alerts/${a.id}/edit" class="btn btn-link">Изменить</a>
                    <#if a.status?string == "TRIGGERED" || a.status?string == "DISABLED">
                        <form method="post" action="/alerts/${a.id}/reactivate" class="inline-form">
                            <@ui.csrfInput/>
                            <button type="submit" class="btn btn-link">Активировать</button>
                        </form>
                    <#else>
                        <form method="post" action="/alerts/${a.id}/disable" class="inline-form">
                            <@ui.csrfInput/>
                            <button type="submit" class="btn btn-link">Отключить</button>
                        </form>
                    </#if>
                    <form method="post" action="/alerts/${a.id}/delete" class="inline-form" data-confirm="Удалить алерт?">
                        <@ui.csrfInput/>
                        <button type="submit" class="btn btn-link btn-danger">Удалить</button>
                    </form>
                </td>
            </tr>
        <#else>
            <tr><td colspan="8" class="muted">Алертов нет.</td></tr>
        </#list>
        </tbody>
    </table>
</@layout.page>
