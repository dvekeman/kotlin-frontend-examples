requirejs.config({
    paths: {
        kotlin: './kotlin',
        ktjs_example02_amd: '../build/js/ktjs_example02'
    }
});

requirejs(["ktjs_example02_amd"], function (example02) {
    document.getElementById("foobar").innerText = example02.bar();
    document.getElementById("foobar2").innerText = example02.foopkg.bar();
    document.getElementById("foobar3").innerText = bar();
});