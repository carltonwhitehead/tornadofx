package tornadofx.testapps

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import tornadofx.*

class StageSceneBugApp : App(StageSceneBugMainView::class) {

    override fun start(stage: Stage) {
        super.start(stage)
        // when using only minWidth and minHeight, test glitches out
        stage.width = 1024.0
        stage.height = 720.0
    }
}

class StageSceneBugMainView : View() {

    private lateinit var stackpane: StackPane
    var children = 0
        private set

    override val root = vbox {
        id = "main"
        stackpane {
            stackpane = this
        }
        button("Next") {
            action { fire(NextChildEvent()) }
        }
    }

    override fun onDock() {
        super.onDock()
        subscribe(action = onNextChildEvent)
        fire(NextChildEvent())
    }

    override fun onUndock() {
        super.onUndock()
        stackpane.children.clear()
        children = 0
        unsubscribe(onNextChildEvent)
    }

    private fun addNextChild() {
        val nextChild = find<StageSceneBugChildFragment>(StageSceneBugChildFragment::idParam to ++children)
        stackpane.add(nextChild)
    }

    private val onNextChildEvent: EventContext.(NextChildEvent) -> Unit = {
        addNextChild()
    }

    class NextChildEvent : FXEvent()
}

class StageSceneBugChildFragment : Fragment() {

    val idParam: Int by param()
    val idProperty = SimpleIntegerProperty()
    var id by idProperty

    override val root = form {
        fieldset("Child") {
            field("ID") {
                textfield(idProperty) {
                    isEditable = false
                }
            }
        }
    }

    override fun onDock() {
        super.onDock()
        id = idParam
        root.id = "child-$id"
    }
}