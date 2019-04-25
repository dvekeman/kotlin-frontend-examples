import com.agilent.vaadin.vaadin_button
import com.agilent.vaadin.vaadin_grid
import com.agilent.vaadin.vaadin_grid_column
import com.agilent.vaadin.vaadin_text_field
import kotlinx.html.div
import kotlinx.html.dom.append
import vaadin.button.ButtonElement
import vaadin.grid.GridElement
import vaadin.text.field.TextFieldElement
import kotlin.browser.document

fun main() {

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

    initUI()

}

data class Person(val firstName: String, val lastName: String)

fun initUI() {
    // Force the side-effects of the vaadin modules. Is there a better way?
    console.log(jsTypeOf(TextFieldElement))
    console.log(jsTypeOf(ButtonElement))
    console.log(jsTypeOf(GridElement))

    val firstNameField = document.querySelector("#firstName")
    val lastNameField = document.querySelector("#lastName")
    val addButton = document.querySelector("#addButton")
    val grid = document.querySelector("#grid")

    val initialPeople: Array<Person> = emptyArray()
    grid.asDynamic().items = initialPeople

    addButton?.addEventListener("click", {
        // Read the new person's data
        val person = Person(firstNameField.asDynamic().value as String, lastNameField.asDynamic().value as String)

        // Add it to the items
        val people: Array<Person> = grid.asDynamic().items as Array<Person>
        grid.asDynamic().items = people.plus(person)

        // Reset the form fields
        firstNameField.asDynamic().value = ""
        lastNameField.asDynamic().value = ""
    })
}