requirejs.config({
    paths: {
        ktjs_example04: '../build/bundle/ktjs_example04.bundle'
    }
});

requirejs(["ktjs_example04"], function (ktjs_example04) {
    document.getElementById("amdfoobar").innerText = ktjs_example04.bar();
    document.getElementById("amdfoobar2").innerText = ktjs_example04.foopkg.bar();
    document.getElementById("amdfoobar3").innerText = bar();
});