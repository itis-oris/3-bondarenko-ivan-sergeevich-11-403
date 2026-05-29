<#import "/layout/base.ftl" as layout>
<#import "/layout/macros.ftl" as ui>
<#import "/spring.ftl" as spring>
<@layout.page title="Регистрация">
    <div class="auth-card">
        <h1>Создание аккаунта</h1>

        <form method="post" action="/register" class="form">
            <@ui.csrfInput/>

            <div class="form-group">
                <label for="username">Имя пользователя</label>
                <@spring.formInput "registerForm.username", 'id="username" required'/>
                <@spring.showErrors "<br>", "form-error"/>
            </div>

            <div class="form-group">
                <label for="email">Email</label>
                <@spring.formInput "registerForm.email", 'id="email" type="email" required'/>
                <@spring.showErrors "<br>", "form-error"/>
            </div>

            <div class="form-group">
                <label for="password">Пароль</label>
                <@spring.formPasswordInput "registerForm.password", 'id="password" required'/>
                <@spring.showErrors "<br>", "form-error"/>
            </div>

            <div class="form-group">
                <label for="passwordConfirm">Повтор пароля</label>
                <@spring.formPasswordInput "registerForm.passwordConfirm", 'id="passwordConfirm" required'/>
                <@spring.showErrors "<br>", "form-error"/>
            </div>

            <div class="form-group form-inline">
                <label>
                    <@spring.formCheckbox "registerForm.emailNotificationsEnabled"/>
                    Также присылать уведомления на email
                </label>
            </div>

            <button type="submit" class="btn btn-primary btn-block">Создать аккаунт</button>
        </form>

        <p class="auth-footer">Уже есть аккаунт? <a href="/login">Войти</a></p>
    </div>
</@layout.page>
