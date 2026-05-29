(function () {
    const btn = document.getElementById('translate-btn');
    const desc = document.getElementById('listing-description');
    if (!btn || !desc) return;

    function escapeHtml(s) {
        if (s == null) return '';
        return String(s)
            .replaceAll('&', '&amp;')
            .replaceAll('<', '&lt;')
            .replaceAll('>', '&gt;')
            .replaceAll('"', '&quot;');
    }

    btn.addEventListener('click', async () => {
        const text = (btn.getAttribute('data-text') || '').trim();
        if (!text) {
            SkillTrade.toast('Нечего переводить', 'info');
            return;
        }
        btn.disabled = true;
        btn.textContent = 'Перевожу...';
        try {
            const data = await SkillTrade.api('/api/v1/translate', {
                method: 'POST',
                body: JSON.stringify({ text, sourceLang: 'auto', targetLang: 'ru' })
            });
            console.log('translate response:', data);

                const translated = (data && data.text) || '';
            if (!translated) {
                SkillTrade.toast('Перевод пустой — LibreTranslate не вернул текст', 'error');
                btn.disabled = false;
                btn.textContent = 'Перевести на русский';
                return;
            }

            if (translated.trim() === text.trim()) {
                SkillTrade.toast(
                    'Текст не изменился — возможно, LibreTranslate ещё грузит модель или не понял язык. ' +
                    'Проверь логи: docker compose logs libretranslate',
                    'info',
                    6000
                );
                btn.disabled = false;
                btn.textContent = 'Попробовать ещё раз';
                return;
            }

            desc.innerHTML = '<strong>Описание (перевод):</strong> ' + escapeHtml(translated);
            btn.textContent = 'Переведено';
        } catch (e) {
            console.error('translate error:', e);
            SkillTrade.toast('Ошибка перевода: ' + e.message, 'error');
            btn.disabled = false;
            btn.textContent = 'Перевести на русский';
        }
    });
})();
