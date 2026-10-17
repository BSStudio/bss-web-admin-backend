// Unsaved changes: forms with data-unsaved="track" get data-dirty while their values differ from the
// loaded ones (CSS then shows the "Unsaved changes" marker), and the browser asks before leaving the page.
// A form shown again after a failed save has data-unsaved="pending" and counts as unsaved from the start.
(() => {
    const loaded = new Map();
    const valuesOf = (form) => new URLSearchParams(new FormData(form)).toString();
    const refresh = (form) =>
        form.toggleAttribute("data-dirty", form.dataset.unsaved === "pending" || valuesOf(form) !== loaded.get(form));

    for (const form of document.querySelectorAll("form[data-unsaved]")) {
        loaded.set(form, valuesOf(form));
        refresh(form);
    }

    document.addEventListener("input", (event) => {
        const form = event.target.form;
        if (loaded.has(form)) refresh(form);
    });

    document.addEventListener("submit", (event) => {
        loaded.delete(event.target);
        delete event.target.dataset.dirty;
    });

    // Forms with data-confirm ask before they submit, e.g. delete buttons in table rows.
    document.addEventListener("submit", (event) => {
        const question = event.target.dataset.confirm;
        if (question && !window.confirm(question)) event.preventDefault();
    });

    window.addEventListener("beforeunload", (event) => {
        if (document.querySelector("form[data-dirty]")) event.preventDefault();
    });
})();
