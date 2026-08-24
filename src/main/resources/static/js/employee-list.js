document.addEventListener("DOMContentLoaded", function () {

    const table = document.querySelector("table");

    if (!table) {
        return;
    }

    const rows = table.querySelectorAll("tbody tr");

    rows.forEach(function (row) {

        row.addEventListener("mouseenter", function () {
            row.style.backgroundColor = "#f0f8ff";
        });

        row.addEventListener("mouseleave", function () {
            row.style.backgroundColor = "";
        });

    });

});
