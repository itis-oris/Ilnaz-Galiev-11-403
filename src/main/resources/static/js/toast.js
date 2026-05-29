
(function (global) {
    const container = () => document.getElementById('toast-container');

    function toast(message, type = 'info', timeoutMs = 3500) {
        const el = document.createElement('div');
        el.className = 'toast toast--' + type;
        el.textContent = message;
        const c = container();
        if (!c) return;
        c.appendChild(el);
        setTimeout(() => el.remove(), timeoutMs);
    }

    function csrfToken() {
        const meta = document.querySelector('meta[name="_csrf"]');
        return meta ? meta.getAttribute('content') : null;
    }

    function csrfHeader() {
        const meta = document.querySelector('meta[name="_csrf_header"]');
        return meta ? meta.getAttribute('content') : 'X-CSRF-TOKEN';
    }

    async function api(url, options = {}) {
        const opts = Object.assign({
            headers: { 'Accept': 'application/json' },
        }, options);
        opts.headers = Object.assign({}, opts.headers || {});
        if (opts.body && !(opts.body instanceof FormData)) {
            opts.headers['Content-Type'] = 'application/json';
        }
        const token = csrfToken();
        if (token && opts.method && opts.method !== 'GET') {
            opts.headers[csrfHeader()] = token;
        }
        const resp = await fetch(url, Object.assign({ redirect: 'manual' }, opts));
        if (resp.type === 'opaqueredirect' || resp.status === 0) {
            const err = new Error('Требуется вход в аккаунт');
            err.status = 401;
            throw err;
        }
        if (!resp.ok) {
            let msg = resp.statusText || ('HTTP ' + resp.status);
            try {
                const j = await resp.json();
                if (j && j.message) msg = j.message;
            } catch (_) {  }
            const err = new Error(msg);
            err.status = resp.status;
            throw err;
        }
        if (resp.status === 204) return null;
        const ct = resp.headers.get('Content-Type') || '';
        if (!ct.includes('application/json')) {
            
            const err = new Error('Сервер вернул не JSON (' + (ct || 'без Content-Type') + ')');
            err.status = resp.status;
            throw err;
        }
        return resp.json();
    }

    global.SkillTrade = { toast, api };
})(window);
