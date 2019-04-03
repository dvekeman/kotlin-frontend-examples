# Kotlin JS Examples

**Gradle Groovy vs Gradle Kotlin DSL**

**The master branch uses Kotlin DSL for gradle. The Groovy equivalent can be found on the `gradle_groovy` branch (but will not be further updated)**

This repository shows how to interop between JavaScript and KotlinJS using different module systems.
The examples can be opened in a browser and don't require any server runtime (like NodeJs).

The main goal is to show interop between plain JS and JS transpiled from Kotlin.

The first three examples have the same structure. They only differ in the `moduleKind` setting (plain, amd, umd) and of 
course in the way they invoke the kotlin-generated JavaScript.

They all use the `kotlin2js` gradle plugin

```
plugins {
    id("kotlin2js") version "1.3.21"
}
```

The configuration is pretty straighforward

```
tasks {
    "compileKotlin2Js"(Kotlin2JsCompile::class) {
        kotlinOptions {
            outputFile = "${project.buildDir.path}/js/<name>.js"
            moduleKind = "< plain | amd | umd >"
            metaInfo = true
            sourceMap = true
            main = "call"
        }
    }
}
```

**Important remarks for the Kotlin Gradle DSL**

The plugin resolution is defined in the **settings.gradle.kts**:

```
pluginManagement {

    repositories {
        jcenter()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://dl.bintray.com/kotlin/kotlin-eap")
        }

    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlin2js") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }

            if (requested.id.id == "org.jetbrains.kotlin.frontend") {
                useModule("org.jetbrains.kotlin:kotlin-frontend-plugin:${requested.version}")
            }
        }
    }
}
```

The `kotlin2js` part is required for all examples. The `org.jetbrains.kotlin.frontend` case is only needed for example 4.

To run the examples:

```
./gradlew <example>:build
```

Then just open the html file from the example directory (plain.html, amd.html and umd.html) in a browser.

## Example 1: Kotlin JS using `plain` module

This is the most basic example. The `moduleKind` is `plain` and the module name is `ktjs_example01` 
(derived from the _kotlinOptions.outputFile_ setting).

All functions are accessible using `<module_name>.<package>.<function_name>`

So if we have 

**src/main/kotlin/foo.kt**

```
fun bar() : String {
    return "bar (from root package)"
}
```

Then a JS function named `bar` will be present in our `ktjs_example01` module:

```
// A function from the root package
ktjs_example01.bar()
```

We can then use this function in a plain HTML `<script/>` tag

```
<div id="foobar"></div>
<script>
    document.getElementById("foobar").innerText = ktjs_example01.bar();
</script>
```

As a result the `foobar` div element will now show **bar**

If our kotlin file lives inside a package then we'll need to qualify the function name with the package:

**src/main/kotlin/foopkg/foo.kt** 

```
package foopkg

fun bar() : String {
    return "bar (from foopkg)"
}
```

Instead of `ktjs_example01.bar()` we need to include the package name:

```
// A function from the foopkg package
ktjs_example01.foopkg.bar()
```

The third case is a bit special: we invoke a function without specifying the module or package (it lives in the `global` scope).

```
// A function from the global (window) scope
bar();
```

This doesn't happen automatically though. 
The only reason we are able to do this, is because we explicitly registered our `barglobal` function in the global window scope as `bar`.
And the only place to do that is the `main` function:

**src/main/kotlin/foo.kt**

```
fun main(args: Array<String>) {
    println("Hello JavaScript!")

    window.asDynamic().bar = barglobal
}

val barglobal = {
    "bar (from global)"
}
```

We don't need to call the `main` function manually! Whenever our JS is loaded, the main function will be called. 
(which we can also derive from the JavaScript console output where we see the _Hello JavaScript_ message)

Note that if you **don't want main to be called** you can turn this off using the [`KotlinJsOptions`](https://github.com/JetBrains/kotlin/blob/master/libraries/tools/kotlin-gradle-plugin/src/main/kotlin/org/jetbrains/kotlin/gradle/dsl/KotlinJsOptions.kt):

```
tasks {
    "compileKotlin2Js"(Kotlin2JsCompile::class) {
        kotlinOptions {
            ...
            main = "noCall"
        }
    }
}
```

## Example 2: Kotlin JS using `amd` module

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

## Example 3: Kotlin JS using `umd` module

The previous two examples each showed a rather different approach to loading our JavaScript code. UMD tries to 
combine the best of both worlds: use AMD if it's available, fallback to plain.

In this example we'll even combine them!

First we change the Kotlin compilation options to use `umd`:

```
tasks {
    "compileKotlin2Js"(Kotlin2JsCompile::class) {
        kotlinOptions {
            outputFile = "${project.buildDir.path}/js/ktjs_example03.js"
            moduleKind = "umd"
            ...
        }
    }
}
```

Then we need to load the scripts in our HTML. Probably you should use either plain or AMD but here we will load both.

One remark before showing how to load the scripts: The JS generated by Kotlin (our **build/js/ktjs_example03.js** script!) starts by checking if AMD is _available_ 

**build/js/ktjs_example03.js**

```
  if (typeof define === 'function' && define.amd)
```

So it's **important to first load the plain scripts** (when AMD is not yet available) and only afterwards load our module as an AMD module using RequireJS:  

**umd.html**

```
<!DOCTYPE html>
<html>
<head>
    <!-- Important: First the plain scripts -->
    <script type="text/javascript" src="./scripts/kotlin.js"></script>
    <script type="text/javascript" src="build/js/ktjs_example03.js"></script>

    <!-- Important: load this after the plain js (scripts/kotlin.js and build/js/ktsj_example03.js -->
    <script data-main="./scripts/main.js"  src="./scripts/require.js"></script>
</head>
<body>
...
</body>
</html>
```

**Obviously** this is a source of conflicts which I ignore here because it's just a simple example.

The rest is basically just a combination of example 1 and example 2.

For the HTML `<script/>` tags we use the plain JS: 

**umd.html**

```
<div><span>Plain: Access a function in the root: </span><div id="plainfoobar"></div></div>
<script>
    document.getElementById("plainfoobar").innerText = ktjs_example03.bar();
</script>
```

And for the AMD module we use requirejs: 

**umd.html**

```
<div><span>AMD: Access a function in the root: </span><div id="amdfoobar"></div></div>
<script>
    // see scripts/main.js
</script>
```

**scripts/main.js**

```
requirejs.config({
    paths: {
        kotlin: './kotlin',
        ktjs_example03_umd: '../build/js/ktjs_example03'
    }
});

requirejs(["ktjs_example03_umd"], function (example03) {
    document.getElementById("amdfoobar").innerText = example03.bar();
    ...
});
```

## Example 4: Kotlin JS using the kotlin-frontend-plugin

The previous examples showed the basics of the `kotlin2js` plugin using different moduleKinds.
The Kotlin community also has an additional plugin for gradle called `kotlin-frontend-plugin`.

First the plugin needs to be applied (note that the `kotlin2js` plugin is also still applied **before** the frontend plugin) 
and it adds a new configuration called `kotlinFrontend`.

**build.gradle.kts**

```
...

plugins {
    id("kotlin2js") version "1.3.21"
    // id("kotlin-dce-js") version "1.3.21" // Dead code elimination. Advised but turned off for this example
    id("org.jetbrains.kotlin.frontend") version "0.0.45"
}
...

tasks {
    "compileKotlin2Js"(Kotlin2JsCompile::class) {
        kotlinOptions {
            ...
        }
    }
}

kotlinFrontend {
    downloadNodeJsVersion = "latest"

    npm {
        dependency("style-loader")
    }

    bundle<WebPackExtension>("webpack") {
        this as WebPackExtension
        bundleName = "ktjs_example04"
        mode = "development"
    }
}

```

The gradle plugin also requires a (potentially empty) directory called `webpack.config.d`. 
We'll now get a bundle as output: **build/bundle/ktjs_example04.bundle.js**. To load it:

**frontend.html**

```
<head>
    <script type="text/javascript" src="build/bundle/ktjs_example04.bundle.js"></script>

    <!-- Important: load this after the plain js (scripts/kotlin.js and build/js/ktsj_example03.js -->
    <script data-main="./scripts/main.js"  src="./scripts/require.js"></script>
</head>
```

Note that we no longer need to load the `kotlin.js` dependency separately!

**scripts/main.js**

```
requirejs.config({
    paths: {
        kotlin: './kotlin',
        ktjs_example04: '../build/bundle/ktjs_example04.bundle'
    }
});
```

Basically these are (almost) the only changes compared to our Example 3. 

If we would run it just like this, it would fail though.
Kotlin does not allow anything equivalent to the JS `export default ...` inside the Kotlin code so when building the module
using the default settings nothing gets exposed.

Fortunately the frontend plugin lets us hook into the webpack bundling using scripts in the aforementioned `webpack.config.d`.

To solve our problem and to achieve the same result as Example 3 (UMD) we need to add the following script in that directory

**webpack.config.d/custom.js**

```
config.output.library = 'ktjs_example04_lib';
config.output.libraryTarget = 'umd';
```

The resuling webpack configuration can be found in **build/webpack.config.js** for further inspection and tweaking.

It does look a bit awkward though as it starts with a basic declarative configuration which is then altered programatically

```
'use strict';

var webpack = require('webpack');

var config = {
    "mode": "development",
    ...
};

module.exports = config;

// from file /src/kotlin/kotlin-frontend-examples/04-kt-frontend/webpack.config.d/custom.js
config.output.library = 'ktjs_example04_lib';
```

More information on this (for example about the different library targets) can be found in the webpack documentation: 
[author-libraries](https://webpack.js.org/guides/author-libraries/).

# References

- [Kotlin JavaScript](https://kotlinlang.org/docs/reference/js-overview.html)
- [Kotlin Gradle Compile Options](https://github.com/JetBrains/kotlin/blob/master/ant/src/org/jetbrains/kotlin/ant/KotlinCompilerBaseTask.kt)
- [Kotlin Frontend plugin](https://github.com/Kotlin/kotlin-frontend-plugin)
- [RequireJS](https://requirejs.org/)
- [Webpack library and libraryTarget](https://webpack.js.org/guides/author-libraries/)