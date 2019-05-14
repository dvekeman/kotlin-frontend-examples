@file:JsModule("@vaadin/vaadin-text-field/src/vaadin-text-field")
@file:JsNonModule

package vaadin.text.field

import org.w3c.dom.HTMLElement

abstract external class TextFieldElement : HTMLElement {
    var value: String = definedExternally
}
