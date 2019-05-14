# Example 4: Kotlin JS using the kotlin-frontend-plugin

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
