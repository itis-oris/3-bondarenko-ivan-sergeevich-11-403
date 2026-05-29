<#import "/layout/base.ftl" as layout>
<@layout.page title="Доступ запрещён">
    <div class="error-page">
        <h1>403</h1>
        <p>${message!"У вас нет доступа к этой странице."}</p>
        <a class="btn btn-primary" href="/">На главную</a>
    </div>
</@layout.page>
