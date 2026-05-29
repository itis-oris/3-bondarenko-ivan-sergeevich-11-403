<#import "/layout/base.ftl" as layout>
<@layout.page title="Ошибка сервера">
    <div class="error-page">
        <h1>${status!500}</h1>
        <p>${message!"Что-то пошло не так на нашей стороне. Попробуй ещё раз через секунду."}</p>
        <p class="muted">Путь: ${path!""}</p>
        <a class="btn btn-primary" href="/">На главную</a>
    </div>
</@layout.page>
