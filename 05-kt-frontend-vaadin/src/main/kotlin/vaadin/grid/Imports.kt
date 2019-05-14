@file:JsModule("@vaadin/vaadin-grid/src/vaadin-grid")
@file:JsNonModule

package vaadin.grid

import org.w3c.dom.HTMLElement

abstract external class GridElement<T> : HTMLElement {
    var items: Array<out T> = definedExternally
}