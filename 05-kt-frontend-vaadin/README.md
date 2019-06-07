# Example 5: Using Vaadin Components

## Gradle 

The only difference for the gradle configuration, compared to the previous example [../04-kotlin-frontend-plugin](../04-kotlin-frontend-plugin)
are the extra npm modules

**build.gradle.kts**

```
kotlinFrontend {
    ...

    npm {
        dependency("style-loader")
        dependency("@vaadin/vaadin-upload")
        dependency("@vaadin/vaadin-button")
        dependency("@vaadin/vaadin-grid")
        dependency("@vaadin/vaadin-text-field")
    }

```

## Vaadin Components

To use the [Vaadin Components] as [WebComponents] in the Kotlin JS application

First some trickery

**foo.kt**

```
external fun require(module: String): dynamic

fun main() {

    require("@vaadin/vaadin-button")
    require("@vaadin/vaadin-text-field")
    require("@vaadin/vaadin-grid")
    ...
}   
``` 

`require` is a function, available through webpack, to *explicitly* load a module and thereby apply it's side-effects.
These side-effects are needed to update the DOM and to insert custom elements and styles 
(inspect the `<head>` tag of the HTML page after loading. You'll see a bunch of extra definitions appearing).

Next we add our `initUI` function to `onload` event. There are tons of other ways to achieve the same result. 

**foo.kt**

fun main() {
    ...
    
    document.body?.onload = { initUI() }
    
}

Note that this does impact our HTML page because we now depend on the existence of the body tag.
So we should load the `js` at the end. 

**frontend.html**

```
<!DOCTYPE html>
<html lang="en">
  <head>
    ...
  </head>
  <body>

    ...
    <div id="container"></div>
    <script type="text/javascript" src="build/bundle/ktjs_example05.bundle.js"></script>

  </body>
</html>
```

Alternatively the JS could be loaded in the `head` tag but then `initUI` must be invoked differently (e.g. explititly call it)

## Typed Vaadin Components

The whole purpose of using Kotlin JS is to add some types to our applications.

So when we interact with the Vaadin Components we want to preserve those types. The best way to achieve this in Kotlin is by using a wrapper.
If no such wrapper exists, it's rather easy to create one by hand. We focus only on the API we need so the wrapper will evolve along with the rest of the code.

### vaadin-grid custom elements

WebComponents provide custom tags, e.g. `<vaadin-grid>`.

So similar to 

```
div {
    h1 {
      +"Hello"
    }
}
```

we want to write something like

```
vaadin-grid {
  ...
}
```

Because this tag does not exist we need to define it ourselves.
The following block of code add a custom element to the Kotlin HTML DSL.

**src/main/kotlin/vaadin/vaadin.kt**

```
class VAADIN_GRID(consumer: TagConsumer<*>) :
        HTMLTag("vaadin-grid", consumer, emptyMap(),
                inlineTag = true,
                emptyTag = false), HtmlInlineTag

fun <T> TagConsumer<T>.vaadin_grid(block: VAADIN_GRID.() -> Unit = {}): T {
    return VAADIN_GRID(this).visitAndFinalize(this, block)
}

fun DIV.vaadin_grid(block: VAADIN_GRID.() -> Unit = {}) {
    VAADIN_GRID(consumer).visit(block)
}

// similar for vaadin_grid_column
```

One thing to note is that the function is called `vaadin_grid` instead of `vaadin-grid` so we will also have to refer it like that in our DSL

**src/main/kotlin/foo.kt**

```
@JsName("initUI")
fun initUI() {
    document.getElementById("container")!!.append {
        ...

        vaadin_grid {
            attributes["id"] = "grid"
            vaadin_grid_column {
                attributes["path"] = "firstName"
                attributes["header"] = "First name"
            }
            vaadin_grid_column {
                attributes["path"] = "lastName"
                attributes["header"] = "Last name"
            }
        }

    }
}
```

`vaadin-text-field` and `vaadin-button` are defined in a similar way


**src/main/kotlin/foo.kt**

```

```

### vaadin-grid API

The sections above define the custom element. Now we want to do something with it, like getting values from `vaadin-text-field`

The vaadin grid wrapper defines the API provided by the vaadin-grid component.

The first line refers to the relative path from `build/node_modules` (and without the `.js` extension).

See [Vaadin.GridElement](https://vaadin.com/components/vaadin-grid/html-api/elements/Vaadin.GridElement).

In the properties section we find the `items` property, that's all we need for now:

**src/main/kotlin/vaadin/grid/Imports.kt**

```
@file:JsModule("@vaadin/vaadin-grid/src/vaadin-grid")
@file:JsNonModule

package vaadin.grid

import org.w3c.dom.HTMLElement

abstract external class GridElement<T> : HTMLElement {
    var items: Array<out T> = definedExternally
}
```

## References

- [Vaadin Components](https://vaadin.com/components)
- [Vaadin Grid Component](https://vaadin.com/components/vaadin-grid/)
- [WebComponents](https://www.webcomponents.org/introduction)