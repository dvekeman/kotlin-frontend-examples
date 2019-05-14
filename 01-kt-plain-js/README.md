# Example 1: Kotlin JS using `plain` module

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
