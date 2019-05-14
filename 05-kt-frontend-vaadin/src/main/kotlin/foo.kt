import vaadin.vaadin_button
import vaadin.vaadin_grid
import vaadin.vaadin_grid_column
import vaadin.vaadin_text_field
import kotlinx.html.div
import kotlinx.html.dom.append
import org.w3c.dom.HTMLElement
import vaadin.button.ButtonElement
import vaadin.grid.GridElement
import vaadin.text.field.TextFieldElement
import kotlin.browser.document

external fun require(module: String): dynamic

fun main() {

    require("@vaadin/vaadin-button")
    require("@vaadin/vaadin-text-field")
    require("@vaadin/vaadin-grid")

    // See body onload
    // initUI()
    document.body?.onload = { initUI() }

}

data class Person(val firstName: String? = null, val lastName: String? = null)

@JsName("initUI")
fun initUI() {
    document.getElementById("container")!!.append {
        div(classes = "form") {
            vaadin_text_field {
                attributes["id"] = "firstName"
                attributes["label"] = "First Name"
            }
            vaadin_text_field {
                attributes["id"] = "lastName"
                attributes["label"] = "Last Name"
            }
            vaadin_button {
                attributes["id"] = "addButton"
                +"Add"
            }
        }

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

    val firstNameField = document.querySelector("#firstName") as TextFieldElement?
    val lastNameField = document.querySelector("#lastName") as TextFieldElement?
    val addButton = document.querySelector("#addButton") as ButtonElement?
    val grid = document.querySelector("#grid") as GridElement<Person>?

    val initialPeople: Array<out Person> = emptyArray()
    grid?.items = initialPeople

    addButton?.addEventListener("click", {
        // Read the new person's data
        val person = Person(firstNameField?.value, lastNameField?.value)

        // Add it to the items
        if(grid != null){
            val people = grid.items
            grid.items = people.plus(person)
        }

        // Reset the form fields
        firstNameField?.value = ""
        lastNameField?.value = ""
    })
}