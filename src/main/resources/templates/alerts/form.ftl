<#import "/layout/base.ftl" as layout>
<#import "/layout/macros.ftl" as ui>
<#import "/spring.ftl" as spring>
<@layout.page title=(editing?then('Изменить алерт', 'Новый алерт')) active="alerts">
    <h1><#if editing>Изменить алерт<#else>Новый алерт</#if></h1>

    <form method="post"
          action="<#if editing>/alerts/${alertForm.id}<#else>/alerts</#if>"
          class="form form-narrow">
        <@ui.csrfInput/>

        <div class="form-group">
            <label for="coinId">Монета</label>
            <@spring.bind "alertForm.coinId"/>

            <select name="coinId" id="coinId">
                <option value="">— выбери монету —</option>
                <#list coins as c>
                    <option value="${c.id}" <#if alertForm.coinId?? && alertForm.coinId == c.id>selected</#if>>
                        ${c.symbol} · ${c.name}<#if c.currentPriceUsd??> ($${c.currentPriceUsd?string("0.##")})</#if>
                    </option>
                </#list>
            </select>
            <@spring.showErrors "<br>", "form-error"/>
        </div>

        <div class="form-group">
            <label for="direction">Направление</label>
            <select name="direction" id="direction">
                <option value="">—</option>
                <option value="ABOVE" <#if alertForm.direction?? && alertForm.direction?string == "ABOVE">selected</#if>>Выше — сработает, если цена ≥ цели</option>
                <option value="BELOW" <#if alertForm.direction?? && alertForm.direction?string == "BELOW">selected</#if>>Ниже — сработает, если цена ≤ цели</option>
            </select>
            <@spring.bind "alertForm.direction"/>
            <#if spring.status.error><div class="form-error">${spring.status.errorMessage}</div></#if>
        </div>

        <div class="form-group">
            <label for="targetPrice">Целевая цена (USD)</label>
            <@spring.formInput "alertForm.targetPrice", "id=\"targetPrice\" type=\"number\" step=\"0.00000001\" min=\"0\""/>
            <@spring.showErrors "<br>", "form-error"/>
        </div>

        <div class="form-group">
            <label for="channel">Канал уведомления</label>
            <select name="channel" id="channel">
                <option value="WEBSOCKET" <#if alertForm.channel?? && alertForm.channel?string == "WEBSOCKET">selected</#if>>Веб-пуш</option>
                <option value="EMAIL" <#if alertForm.channel?? && alertForm.channel?string == "EMAIL">selected</#if>>Email</option>
                <option value="BOTH" <#if alertForm.channel?? && alertForm.channel?string == "BOTH">selected</#if>>Веб-пуш + Email</option>
            </select>
        </div>

        <div class="form-group">
            <label for="comment">Комментарий (необязательно)</label>
            <@spring.formInput "alertForm.comment", "id=\"comment\" maxlength=\"256\""/>
        </div>

        <button type="submit" class="btn btn-primary"><#if editing>Сохранить<#else>Создать</#if></button>
        <a href="/alerts" class="btn btn-link">Отмена</a>
    </form>
</@layout.page>
