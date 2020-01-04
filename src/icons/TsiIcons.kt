package icons

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

interface TsiIcons {
  companion object {
    val ICON_GREY: Icon
      get() = IconLoader.getIcon("/icons/grey.svg")
    val ICON_RED: Icon
      get() = IconLoader.getIcon("/icons/red.svg")
    val ICON_GREEN: Icon
      get() = IconLoader.getIcon("/icons/green.svg")
  }
}