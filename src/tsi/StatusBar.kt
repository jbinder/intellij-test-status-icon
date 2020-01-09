package tsi

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetProvider
import com.intellij.util.Consumer
import icons.TsiIcons
import java.awt.event.MouseEvent
import javax.swing.Icon

private class StatusBarIconProvider : StatusBarWidgetProvider {
  override fun getWidget(project: Project): TsiStatusBar? = TsiStatusBar
}

object TsiStatusBar : StatusBarWidget, StatusBarWidget.IconPresentation, SMTRunnerEventsListener, BulkFileListener {

  private var stateToIconMapping: Map<TestState, Icon> = mapOf(
    TestState.Unknown to TsiIcons.ICON_GREY,
    TestState.Success to TsiIcons.ICON_GREEN,
    TestState.Failure to TsiIcons.ICON_RED
  )
  private var excludePaths: List<String> = listOf("/.idea/")
  private var statusBar: StatusBar? = null
  private var testStateService: TestStateService = ServiceManager.getService(TestStateService::class.java)

  override fun ID(): String = "TestStatusIcon-Icon"

  override fun install(statusBar: StatusBar) {
    this.statusBar = statusBar
  }

  override fun dispose() {}

  override fun getTooltipText() = "TestStatusIcon"

  override fun getIcon(): Icon = stateToIconMapping.getValue(testStateService.get())

  override fun getClickConsumer() = Consumer<MouseEvent> {
    // nothing to be done
  }

  override fun getPresentation(type: StatusBarWidget.PlatformType): StatusBarWidget.WidgetPresentation? = this

  override fun onTestingFinished(testsRoot: SMTestProxy.SMRootTestProxy) {
    if (testsRoot.isPassed) {
      testStateService.set(TestState.Success)
    } else {
      testStateService.set(TestState.Failure)
    }
    updateIcon()
  }

  override fun onTestingStarted(testsRoot: SMTestProxy.SMRootTestProxy) {
    testStateService.set(TestState.Unknown)
    updateIcon()
  }

  override fun onTestStarted(test: SMTestProxy) {}
  override fun onTestFinished(test: SMTestProxy) {}
  override fun onTestIgnored(test: SMTestProxy) {}
  override fun onSuiteTreeNodeAdded(testProxy: SMTestProxy?) {}
  override fun onCustomProgressTestFinished() {}
  override fun onSuiteFinished(suite: SMTestProxy) {}
  override fun onTestFailed(test: SMTestProxy) {}
  override fun onCustomProgressTestStarted() {}
  override fun onTestsCountInSuite(count: Int) {}
  override fun onSuiteTreeStarted(suite: SMTestProxy?) {}
  override fun onCustomProgressTestFailed() {}
  override fun onSuiteStarted(suite: SMTestProxy) {}
  override fun onCustomProgressTestsCategory(categoryName: String?, testCount: Int) {}

  override fun after(events: MutableList<out VFileEvent>) {
    super.after(events)
    if (events.filter { x -> excludePaths.any { y -> x.path.contains(y, true) } }.any()) {
      return
    }
    testStateService.set(TestState.Unknown)
    updateIcon()
  }

  private fun updateIcon() {
    statusBar?.updateWidget(ID())
    notify(testStateService.get().name)
  }

  private fun notify(content: String) {
    Notifications.Bus.notify(
      Notification(
        "tsi",
        "Test state changed",
        content,
        NotificationType.INFORMATION
      )
    )
  }

}
