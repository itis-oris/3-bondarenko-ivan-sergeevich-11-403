(function () {
    const csrf = {
        token: document.querySelector('meta[name="_csrf"]')?.content,
        header: document.querySelector('meta[name="_csrf_header"]')?.content
    };

    function jsonHeaders() {
        const h = {'Accept': 'application/json', 'X-Requested-With': 'XMLHttpRequest'};
        if (csrf.header && csrf.token) h[csrf.header] = csrf.token;
        return h;
    }

    async function fetchJson(url, opts = {}) {
        const res = await fetch(url, {
            credentials: 'same-origin', ...opts,
            headers: {...jsonHeaders(), ...(opts.headers || {})}
        });
        if (!res.ok) {
            const body = await res.json().catch(() => ({message: res.statusText}));
            throw new Error(body.message || ('HTTP ' + res.status));
        }
        return res.json();
    }

    function fmtPrice(v) {
        if (v === null || v === undefined) return '—';
        const n = typeof v === 'number' ? v : Number(v);
        if (Number.isNaN(n)) return v;
        if (n >= 1) return '$' + n.toFixed(2);
        if (n >= 0.01) return '$' + n.toFixed(4);
        return '$' + n.toFixed(8);
    }

    function paintPrices(prices) {
        const byId = new Map(prices.map(p => [String(p.id), p.currentPriceUsd]));
        document.querySelectorAll('[data-coin-id]').forEach(row => {
            const id = row.getAttribute('data-coin-id');
            const target = row.querySelector('.price-cell');
            if (!target) return;
            const price = byId.get(id);
            if (price === undefined || price === null) return;
            const old = target.getAttribute('data-price');
            target.textContent = fmtPrice(price);
            target.setAttribute('data-price', String(price));
            if (old !== null && old !== undefined && Number(old) !== Number(price)) {
                target.classList.remove('price-up', 'price-down');
                target.classList.add(Number(price) > Number(old) ? 'price-up' : 'price-down');
                setTimeout(() => target.classList.remove('price-up', 'price-down'), 1200);
            }
        });
    }

    async function refreshPrices() {
        try {
            const prices = await fetchJson('/api/ui/prices');
            paintPrices(prices);
        } catch (e) {
            console.warn('Failed to refresh prices:', e.message);
        }
    }

    function ensureToastContainer() {
        let c = document.querySelector('.toast-container');
        if (!c) {
            c = document.createElement('div');
            c.className = 'toast-container';
            document.body.appendChild(c);
        }
        return c;
    }

    function showToast(notification) {
        const c = ensureToastContainer();
        const el = document.createElement('div');
        el.className = 'toast';
        el.innerHTML = '<strong>Алерт: ' + (notification.coinSymbol || '') + '</strong><br>'
            + (notification.message || '');
        c.appendChild(el);
        setTimeout(() => el.remove(), 6000);
    }

    function prependNotification(notification) {
        const list = document.getElementById('notification-list');
        if (!list) return;
        const li = document.createElement('li');
        li.className = 'notification-item unread';
        li.dataset.id = notification.id;
        li.innerHTML = '<div class="notification-message">' + (notification.message || '') + '</div>'
            + '<time class="muted">just now</time>';
        if (list.firstChild && list.firstChild.classList?.contains('muted')) {
            list.innerHTML = '';
        }
        list.prepend(li);
    }

    let stompClient = null;
    let connecting = false;

    function connectWebSocket() {
        if (stompClient !== null || connecting) return;
        connecting = true;

        if (typeof SockJS === 'undefined' || typeof Stomp === 'undefined') {
            console.warn('SockJS/STOMP not loaded — WebSocket disabled.');
            connecting = false;
            return;
        }

        const sock = new SockJS('/ws');
        const stomp = Stomp.over(sock);
        stomp.debug = null;

        stomp.connect({}, function () {
            stompClient = stomp;
            stomp.subscribe('/user/queue/notifications', function (frame) {
                try {
                    const n = JSON.parse(frame.body);
                    showToast(n);
                    prependNotification(n);
                } catch (e) {
                    console.error('Bad notification payload', e);
                }
            });
        }, function (err) {
            console.warn('STOMP error:', err);
            stompClient = null;
            connecting = false;
            setTimeout(connectWebSocket, 5000);
        });
    }

    async function markAllRead() {
        try {
            await fetchJson('/api/ui/notifications/mark-read', {method: 'POST'});
            document.querySelectorAll('.notification-item.unread').forEach(el => el.classList.remove('unread'));
            const badge = document.querySelector('.badge-unread');
            if (badge) badge.remove();
        } catch (e) {
            alert('Failed to mark notifications: ' + e.message);
        }
    }


    function initConfirmModals() {
        const overlay = document.getElementById('confirm-modal');
        const okBtn = document.getElementById('confirm-modal-ok');
        const cancelBtn = document.getElementById('confirm-modal-cancel');
        const text = document.getElementById('confirm-modal-text');

        if (!overlay) return;

        let pendingForm = null;

        // перехватываем все формы с data-confirm
        document.addEventListener('submit', function (e) {
            const form = e.target;
            const message = form.getAttribute('data-confirm');
            if (!message) return;
            e.preventDefault();
            pendingForm = form;
            text.textContent = message;
            overlay.style.display = 'flex';
        });

        okBtn.addEventListener('click', function () {
            overlay.style.display = 'none';
            if (pendingForm) {
                pendingForm.removeAttribute('data-confirm');
                pendingForm.submit();
                pendingForm = null;
            }
        });

        cancelBtn.addEventListener('click', function () {
            overlay.style.display = 'none';
            pendingForm = null;
        });


        overlay.addEventListener('click', function (e) {
            if (e.target === overlay) {
                overlay.style.display = 'none';
                pendingForm = null;
            }
        });
    }

    function initDashboard() {
        refreshPrices();
        setInterval(refreshPrices, 15000);
        connectWebSocket();
        initConfirmModals();
        const btn = document.getElementById('mark-read-btn');
        if (btn) btn.addEventListener('click', markAllRead);
    }

    window.CryptoTracker = {initDashboard, refreshPrices, markAllRead, connectWebSocket, initConfirmModals};
})();