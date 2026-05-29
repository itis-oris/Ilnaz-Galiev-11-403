
(function () {
    const applyBtn = document.getElementById('filter-apply');
    const resetBtn = document.getElementById('filter-reset');
    const grid = document.getElementById('listings-grid');
    if (!applyBtn || !grid) return;

    function escapeHtml(s) {
        if (s == null) return '';
        return String(s)
            .replaceAll('&', '&amp;')
            .replaceAll('<', '&lt;')
            .replaceAll('>', '&gt;')
            .replaceAll('"', '&quot;');
    }

    function render(listings) {
        if (!listings.length) {
            grid.innerHTML = '<p class="empty">По вашему запросу ничего не найдено.</p>';
            return;
        }
        grid.innerHTML = listings.map(l => `
            <div class="offer-card">
                <div class="offer-card__skills">
                    <span class="skill-offer">${escapeHtml(l.canTeach && l.canTeach.name)}</span>
                    <span class="arrow">↔</span>
                    <span class="skill-request">${escapeHtml(l.wantToLearn && l.wantToLearn.name)}</span>
                </div>
                <div class="offer-card__title">${escapeHtml(l.title)}</div>
                <p class="offer-card__desc">${escapeHtml(l.description)}</p>
                <div class="offer-card__footer">
                    <small>от <a href="/profile/${l.author.id}">${escapeHtml(l.author.username)}</a>
                        ${l.author.city ? '· ' + escapeHtml(l.author.city) : ''}</small>
                    <a href="/listings/${l.id}" class="btn btn-sm btn-primary">Подробнее</a>
                </div>
            </div>
        `).join('');
    }

    async function applyFilters() {
        const params = new URLSearchParams();
        const cat = document.getElementById('filter-category').value;
        const city = document.getElementById('filter-city').value.trim();
        const minRating = document.getElementById('filter-min-rating').value;
        if (cat) params.set('categoryId', cat);
        if (city) params.set('city', city);
        if (minRating) params.set('minRating', minRating);
        params.set('status', 'ACTIVE');

        try {
            const data = await SkillTrade.api('/api/v1/listings?' + params.toString());
            render(data);
        } catch (e) {
            SkillTrade.toast('Не удалось загрузить: ' + e.message, 'error');
        }
    }

    applyBtn.addEventListener('click', applyFilters);
    resetBtn.addEventListener('click', () => {
        document.getElementById('filter-category').value = '';
        document.getElementById('filter-city').value = '';
        document.getElementById('filter-min-rating').value = '';
        applyFilters();
    });
})();
