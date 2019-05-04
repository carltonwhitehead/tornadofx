package tornadofx.tests

import org.junit.Assert
import org.junit.Assume
import org.junit.Test
import org.testfx.api.FxRobot
import org.testfx.api.FxToolkit
import tornadofx.*
import tornadofx.testapps.StageSceneBugMainView
import tornadofx.testapps.StageSceneBugApp

class StageSceneBugTest {

    @Test
    fun itShouldNotBugOutWhy() {

        fun cycleApp() {
            val stage = FxToolkit.registerPrimaryStage()
            val app = FxToolkit.setupApplication { StageSceneBugApp() }
            val robot = FxRobot()
            val mainView = find<StageSceneBugMainView>()
            Assume.assumeTrue(mainView.children == 1)
            robot.clickOn("Next")
            Assert.assertTrue(mainView.children == 2)
            FxToolkit.cleanupStages()
            FxToolkit.cleanupApplication(app)
        }
        for (i in 0..10) {
            cycleApp()
        }
    }
}