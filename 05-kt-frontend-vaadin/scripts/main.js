requirejs.config({
    paths: {
        ktjs_example05: '../build/bundle/ktjs_example05.bundle'
    }
});

requirejs(["ktjs_example05"], function (ktjs_example05) {
});