<#import "/layout/base.ftl" as layout>
<@layout.page title="Главная">
    <section class="hero">
        <h1>Отслеживай криптовалюты и получай алерты в реальном времени.</h1>
        <p>Задай целевую цену, выбери канал — пуш в браузер, email или оба.
           CryptoTracker следит за ценами на CoinGecko и сигналит ровно в тот момент, когда твой уровень пробит.</p>
        <div class="hero-actions">
            <a class="btn btn-primary" href="/register">Регистрация</a>
            <a class="btn btn-secondary" href="/login">Войти</a>
        </div>
    </section>

    <section class="grid grid-3">
        <div class="card">
            <h3>Живые цены</h3>
            <p>Фоновый поллер тянет цены с CoinGecko каждые 30 секунд и кэширует их в Redis.</p>
        </div>
        <div class="card">
            <h3>Одноразовые алерты</h3>
            <p>Алерт срабатывает один раз и ждёт, пока ты сам его не активируешь снова — никакого спама.</p>
        </div>
        <div class="card">
            <h3>Браузер и почта</h3>
            <p>Сам выбираешь, как получать уведомления — всплывающее окно в браузере, письмо или сразу оба способа.</p>
        </div>
    </section>
</@layout.page>
