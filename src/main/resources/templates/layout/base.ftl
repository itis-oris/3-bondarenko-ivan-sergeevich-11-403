<#macro page title="CryptoTracker" active="">
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <#if _csrf??>
            <meta name="_csrf" content="${_csrf.token}">
            <meta name="_csrf_header" content="${_csrf.headerName}">
        </#if>
        <title>${title} · CryptoTracker</title>
        <link rel="stylesheet" href="/css/style.css">
    </head>
    <body>
    <header class="topbar">
        <div class="container topbar-inner">
            <a class="logo" href="/">CryptoTracker</a>
            <#if isAuthenticated!false>
                <nav class="nav">
                    <a href="/dashboard" class="<#if active=='dashboard'>active</#if>">Дашборд</a>
                    <a href="/coins" class="<#if active=='coins'>active</#if>">Монеты</a>
                    <a href="/alerts" class="<#if active=='alerts'>active</#if>">Алерты</a>
                    <a href="/portfolio" class="<#if active=='portfolio'>active</#if>">Портфель</a>
                    <#if isAdmin!false>
                        <a href="/admin/coins" class="<#if active=='admin'>active</#if>">Админка</a>
                    </#if>
                </nav>
                <form class="logout-form" action="/logout" method="post">
                    <#if _csrf??>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    </#if>
                    <span class="user-pill">${currentUser.username}</span>
                    <button type="submit" class="btn btn-link">Выйти</button>
                </form>
            <#else>
                <nav class="nav">
                    <a href="/login">Войти</a>
                    <a href="/register">Регистрация</a>
                </nav>
            </#if>
        </div>
    </header>

    <main class="container">
        <#nested>
    </main>

    <footer class="footer">
        <div class="container">CryptoTracker · семестровая работа · данные с CoinGecko</div>
    </footer>

    <div id="confirm-modal" class="modal-overlay" style="display:none;">
        <div class="modal-box">
            <p id="confirm-modal-text">Вы уверены?</p>
            <div class="modal-actions">
                <button id="confirm-modal-ok" class="btn btn-danger">Удалить</button>
                <button id="confirm-modal-cancel" class="btn btn-link">Отмена</button>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1.6.1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stomp-websocket/lib/stomp.min.js"></script>
    <script src="/js/app.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            window.CryptoTracker?.initConfirmModals?.();
            <#if isAuthenticated!false>
            window.CryptoTracker?.connectWebSocket?.();
            </#if>
        });
    </script>
    </body>
    </html>
</#macro>