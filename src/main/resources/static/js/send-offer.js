
(function () {
    const form = document.getElementById('offer-form');
    if (!form) return;

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const btn = form.querySelector('button[type="submit"]');
        const ta = form.querySelector('textarea[name="message"]');
        const listingId = +form.dataset.listingId;
        const message = ta.value.trim();
        if (message.length < 5) {
            SkillTrade.toast('Сообщение слишком короткое', 'error');
            return;
        }
        btn.disabled = true;
        try {
            await SkillTrade.api('/api/v1/offers', {
                method: 'POST',
                body: JSON.stringify({ listingId, message })
            });
            btn.textContent = 'Оффер отправлен';
            ta.disabled = true;
            SkillTrade.toast('Оффер отправлен!', 'success');
        } catch (err) {
            SkillTrade.toast(err.message, 'error');
            btn.disabled = false;
        }
    });
})();
