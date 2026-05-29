(function () {
    const addBtn = document.getElementById('add-skill-btn');
    const select = document.getElementById('add-skill-select');
    const newBtn = document.getElementById('new-skill-btn');
    const newInput = document.getElementById('new-skill-name');
    const list = document.getElementById('my-skills');
    if (!list) return;

    function renderChip(name, skillId) {
        const chip = document.createElement('span');
        chip.className = 'skill-chip';
        chip.innerHTML =
            `${name} <button type="button" class="btn btn-sm btn-danger" data-remove-skill="${skillId}">×</button>`;
        list.appendChild(chip);
    }

    if (addBtn && select) {
        addBtn.addEventListener('click', async () => {
            const skillId = +select.value;
            const skillName = select.options[select.selectedIndex].textContent;
            if (!skillId) return;
            try {
                await SkillTrade.api('/api/v1/skills/me/' + skillId, { method: 'POST' });
                renderChip(skillName, skillId);
                SkillTrade.toast('Навык добавлен', 'success');
            } catch (e) {
                SkillTrade.toast(e.message, 'error');
            }
        });
    }

    if (newBtn && newInput) {
        newBtn.addEventListener('click', async () => {
            const name = newInput.value.trim();
            if (name.length < 2) {
                SkillTrade.toast('Имя навыка слишком короткое', 'error');
                return;
            }
            newBtn.disabled = true;
            try {
                const skill = await SkillTrade.api('/api/v1/skills/me/new', {
                    method: 'POST',
                    body: JSON.stringify({ name })
                });
                renderChip(skill.name, skill.id);
                if (select) {
                    const opt = document.createElement('option');
                    opt.value = skill.id;
                    opt.textContent = skill.name;
                    select.appendChild(opt);
                }
                newInput.value = '';
                SkillTrade.toast('Навык "' + skill.name + '" создан и добавлен', 'success');
            } catch (e) {
                SkillTrade.toast(e.message, 'error');
            } finally {
                newBtn.disabled = false;
            }
        });
    }

    list.addEventListener('click', async (e) => {
        const btn = e.target.closest('[data-remove-skill]');
        if (!btn) return;
        const skillId = +btn.getAttribute('data-remove-skill');
        try {
            await SkillTrade.api('/api/v1/skills/me/' + skillId, { method: 'DELETE' });
            btn.closest('.skill-chip').remove();
        } catch (err) {
            SkillTrade.toast(err.message, 'error');
        }
    });
})();
