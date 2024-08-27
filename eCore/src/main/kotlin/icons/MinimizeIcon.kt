package icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Filled.Minimize: ImageVector
    get() {
        if (_minimize != null) {
            return _minimize!!
        }
        _minimize = materialIcon(name = "Filled.Minimize") {
            materialPath {
                moveTo(5.0f, 12.0f) // 起点
                lineTo(19.0f, 12.0f) // 终点
                verticalLineToRelative(2.0f)
                lineTo(5.0f, 14.0f)
                verticalLineToRelative(-2.0f)
                close()
            }
        }
        return _minimize!!
    }

private var _minimize: ImageVector? = null
