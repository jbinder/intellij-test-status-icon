package tsi

import com.intellij.openapi.project.Project
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

object TsiStatusBar : StatusBarWidget, StatusBarWidget.IconPresentation {

  private var statusBar: StatusBar? = null

  override fun ID(): String = "TestStatusIcon-Icon"

  override fun install(statusBar: StatusBar) {
    this.statusBar = statusBar
  }

  override fun dispose() {}

  override fun getTooltipText() = "TestStatusIcon"

  override fun getIcon(): Icon = TsiIcons.ICON_GREY

  override fun getClickConsumer() = Consumer<MouseEvent> {
    // nothing to be done
  }

  override fun getPresentation(type: StatusBarWidget.PlatformType): StatusBarWidget.WidgetPresentation? = this
}
