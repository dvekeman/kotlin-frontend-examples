# Example 2: Kotlin JS using `amd` module

When using `moduleKind = "plain"` we could access all functions immediately from any JS script as well as html `<script/>` tags. 

Switching to `amd` alters this behaviour and the setup becomes a bit more involved. 

In this example the module name is `ktjs_example02`.

First we load the scripts a bit differently. We use [RequireJS](https://requirejs.org/) as the module loader:

**amd.html**

```
<html>
<head>
  ...
  <script data-main="./scripts/main.js"  src="./scripts/require.js"></script>
  ...
</head>
...
</html>
```

`require.js` is downloaded from the RequireJS website and the `data-main` tells us where our custom main script is located.

Our kotlin JS code cannot be accessed directly from html `<script/>` tags. Instead we've moved this to the `main.js` script where 
we have an AMD context.

```
<div><span>Access a function in the root: </span><div id="foobar"></div></div>
<script>
    // see scripts/main.js
</script>
``` 

The most important part is thus in the `main.js`:

**scripts/main.js** (part 1)

```
requirejs.config({
    paths: {
        kotlin: './kotlin',
        ktjs_example02_amd: '../build/js/ktjs_example02'
    }
});
```

The first part - shown above - loads our `ktjs_example02.js` which is the output of our Kotlin JS code, as an AMD module as well as its dependencies (in this case: kotlin.js).
Note that the `.js` suffixes are left out and the paths are relative to the `main.js` file itself (which is in the `scripts` directory of our project).

The second part below is the actual application code

**main.js** (part 2)

```
requirejs(["ktjs_example02_amd"], function (example02) {
    document.getElementById("foobar").innerText = example02.bar();
    document.getElementById("foobar2").innerText = example02.foopkg.bar();
    document.getElementById("foobar3").innerText = bar();
});
```

Besides the root structure `requirejs([...], function(...) {...});` the code is pretty self explanatory.

I've played around a bit with the variable names to show how these all map to each other:

- In the `paths` we load `../build/js/ktjs_example02.js` (without the .js ext) as `ktjs_example02_amd`
- `requirejs(["ktjs_example02_amd"])` takes this path as input and generates a function with a module named `example02`
