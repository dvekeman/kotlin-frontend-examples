package vaadin

import kotlinx.html.*


// template >>
class TEMPLATE(consumer: TagConsumer<*>) :
        HTMLTag("template", consumer, emptyMap(),
                inlineTag = true,
                emptyTag = false), HtmlInlineTag

fun <T> TagConsumer<T>.template(block: TEMPLATE.() -> Unit = {}): T {
    return TEMPLATE(this).visitAndFinalize(this, block)
}

fun DIV.template(block: TEMPLATE.() -> Unit = {}) {
    TEMPLATE(consumer).visit(block)
}
// << template


// vaadin-demo-snippet >>
class VAADIN_DEMO_SNIPPET(consumer: TagConsumer<*>) :
        HTMLTag("vaadin-demo-snippet", consumer, emptyMap(),
                inlineTag = true,
                emptyTag = false), HtmlInlineTag

fun <T> TagConsumer<T>.vaadin_demo_snippet(block: VAADIN_DEMO_SNIPPET.() -> Unit = {}): T {
    return VAADIN_DEMO_SNIPPET(this).visitAndFinalize(this, block)
}

fun DIV.vaadin_demo_snippet(block: VAADIN_DEMO_SNIPPET.() -> Unit = {}) {
    VAADIN_DEMO_SNIPPET(consumer).visit(block)
}
// << vaadin-demo-snippet

// vaadin-upload >>
class VAADIN_UPLOAD(consumer: TagConsumer<*>) :
        HTMLTag("vaadin-upload", consumer, emptyMap(),
                inlineTag = true,
                emptyTag = false), HtmlInlineTag

fun <T> TagConsumer<T>.vaadin_upload(block: VAADIN_UPLOAD.() -> Unit = {}): T {
    return VAADIN_UPLOAD(this).visitAndFinalize(this, block)
}

fun DIV.vaadin_upload(block: VAADIN_UPLOAD.() -> Unit = {}) {
    VAADIN_UPLOAD(consumer).visit(block)
}
// << vaadin-upload

// vaadin-text-field >>
class VAADIN_TEXT_FIELD(consumer: TagConsumer<*>) :
        HTMLTag("vaadin-text-field", consumer, emptyMap(),
                inlineTag = true,
                emptyTag = false), HtmlInlineTag

fun <T> TagConsumer<T>.vaadin_text_field(block: VAADIN_TEXT_FIELD.() -> Unit = {}): T {
    return VAADIN_TEXT_FIELD(this).visitAndFinalize(this, block)
}

fun DIV.vaadin_text_field(block: VAADIN_TEXT_FIELD.() -> Unit = {}) {
    VAADIN_TEXT_FIELD(consumer).visit(block)
}
// << vaadin-text-field

// vaadin-button >>
class VAADIN_BUTTON(consumer: TagConsumer<*>) :
        HTMLTag("vaadin-button", consumer, emptyMap(),
                inlineTag = true,
                emptyTag = false), HtmlInlineTag

fun <T> TagConsumer<T>.vaadin_button(block: VAADIN_BUTTON.() -> Unit = {}): T {
    return VAADIN_BUTTON(this).visitAndFinalize(this, block)
}

fun DIV.vaadin_button(block: VAADIN_BUTTON.() -> Unit = {}) {
    VAADIN_BUTTON(consumer).visit(block)
}
// << vaadin-button

// vaadin-grid >>
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
// << vaadin-grid

// vaadin-grid-column >>
class VAADIN_GRID_COLUMN(consumer: TagConsumer<*>) :
        HTMLTag("vaadin-grid-column", consumer, emptyMap(),
                inlineTag = true,
                emptyTag = false), HtmlInlineTag

fun <T> TagConsumer<T>.vaadin_grid_column(block: VAADIN_GRID_COLUMN.() -> Unit = {}): T {
    return VAADIN_GRID_COLUMN(this).visitAndFinalize(this, block)
}

fun DIV.vaadin_grid_column(block: VAADIN_GRID_COLUMN.() -> Unit = {}) {
    VAADIN_GRID_COLUMN(consumer).visit(block)
}
// << vaadin-grid-column


