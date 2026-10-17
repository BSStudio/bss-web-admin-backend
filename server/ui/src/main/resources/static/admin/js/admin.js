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

    // Images the file API has not made yet (nothing uploaded so far) show their placeholder text instead.
    const showPlaceholder = (image) => image.closest(".media-preview")?.classList.add("missing");
    document.addEventListener(
        "error",
        (event) => {
            if (event.target.matches?.("img[data-media]")) showPlaceholder(event.target);
        },
        true,
    );
    for (const image of document.querySelectorAll("img[data-media]")) {
        if (image.complete && image.naturalWidth === 0) showPlaceholder(image);
    }

    // Select all: the header checkbox (data-select-all names the row checkboxes) ticks every row on the page and
    // shows a dash while only some are ticked. The selection bar counts the ticked rows.
    const syncSelection = () => {
        const all = document.querySelector("input[data-select-all]");
        if (!all) return;
        const rows = [...document.querySelectorAll(`input[name="${all.dataset.selectAll}"]`)];
        const ticked = rows.filter((row) => row.checked).length;
        all.checked = ticked > 0 && ticked === rows.length;
        all.indeterminate = ticked > 0 && ticked < rows.length;
        const count = document.querySelector(".selected-count[data-template]");
        if (count) count.textContent = count.dataset.template.replace("{0}", ticked);
    };
    document.addEventListener("change", (event) => {
        const name = event.target.dataset?.selectAll;
        if (name) for (const row of document.querySelectorAll(`input[name="${name}"]`)) row.checked = event.target.checked;
        syncSelection();
    });
    // the selection bar's Cancel unticks the rows only after its reset event; a new page brings new rows
    document.addEventListener("reset", () => setTimeout(syncSelection));
    document.addEventListener("htmx:afterSettle", syncSelection);

    // A select with data-other="<id>" shows that text field, required and focused, while its option marked
    // data-other is chosen, e.g. "New position…" in a video's crew.
    document.addEventListener("change", (event) => {
        const field = document.getElementById(event.target.dataset?.other ?? "");
        if (!field) return;
        const other = event.target.selectedOptions[0]?.hasAttribute("data-other") ?? false;
        field.closest("label").hidden = !other;
        field.required = other;
        // after the list has closed, which puts the focus back on the select
        if (other) setTimeout(() => field.focus());
    });

    window.addEventListener("beforeunload", (event) => {
        if (document.querySelector("form[data-dirty]")) event.preventDefault();
    });
})();
