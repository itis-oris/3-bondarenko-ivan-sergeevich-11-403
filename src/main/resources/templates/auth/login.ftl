<#import "/layout/base.ftl" as layout>
<#import "/layout/macros.ftl" as ui>
<@layout.page title="Вход">
    <div class="auth-card">
        <h1>Вход</h1>

        <#if hasError!false>
            <div class="alert alert-error">Неверное имя пользователя или пароль.</div>
        </#if>
        <#if hasLogout!false>
            <div class="alert alert-info">Вы вышли из аккаунта.</div>
        </#if>
        <#if hasRegistered!false>
            <div class="alert alert-success">Аккаунт создан. Войдите.</div>
        </#if>

        <form method="post" action="/login" class="form">
            <@ui.csrfInput/>
            <div class="form-group">
                <label for="username">Имя пользователя</label>
                <input type="text" id="username" name="username" required autofocus>
            </div>
            <div class="form-group">
                <label for="password">Пароль</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="form-group form-inline">
                <label><input type="checkbox" name="remember-me"> Запомнить меня</label>
            </div>
            <button type="submit" class="btn btn-primary btn-block">Войти</button>
        </form>

        <p class="auth-footer">Нет аккаунта? <a href="/register">Зарегистрироваться</a></p>
    </div>
</@layout.page>
