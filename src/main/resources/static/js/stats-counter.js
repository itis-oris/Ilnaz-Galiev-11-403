document.addEventListener('DOMContentLoaded', () => {
    const counters = document.querySelectorAll('.stat__number');
    counters.forEach(counter => {
        const target = +counter.getAttribute('data-target') || 0;
        if (target === 0) { counter.textContent = '0'; return; }
        const increment = Math.max(1, target / 60);
        let current = 0;
        const timer = setInterval(() => {
            current += increment;
            if (current >= target) {
                counter.textContent = target;
                clearInterval(timer);
            } else {
                counter.textContent = Math.ceil(current);
            }
        }, 25);
    });
});
