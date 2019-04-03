requirejs.config({
    paths: {
        kotlin: './kotlin',
        ktjs_example03_umd: '../build/js/ktjs_example03'
    }
});

requirejs(["ktjs_example03_umd"], function (example03) {
    document.getElementById("amdfoobar").innerText = example03.bar();
    document.getElementById("amdfoobar2").innerText = example03.foopkg.bar();
    document.getElementById("amdfoobar3").innerText = bar();
});