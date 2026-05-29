<#import "/layout/base.ftl" as layout>
<@layout.page title="404 · Страница не найдена">
    <div class="error-page">
        <h1>404</h1>
        <p>${message!"Страница не найдена."}</p>
        <p class="muted">Путь: ${path!""}</p>
        <a class="btn btn-primary" href="/">На главную</a>
    </div>
</@layout.page>
