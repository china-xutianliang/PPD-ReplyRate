package androidx.compose.material.icons.filled

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Filled.QuestionBank: ImageVector
    get() {
        if (_questionBank != null) {
            return _questionBank!!
        }
        _questionBank = materialIcon(name = "Filled.QuestionBank") {
            materialPath {
                moveTo(21.0f, 3.0f)
                horizontalLineTo(7.0f)
                curveTo(5.9f, 3.0f, 5.0f, 3.9f, 5.0f, 5.0f)
                verticalLineTo(21.0f)
                curveTo(5.0f, 22.1f, 5.9f, 23.0f, 7.0f, 23.0f)
                horizontalLineTo(21.0f)
                curveTo(22.1f, 23.0f, 23.0f, 22.1f, 23.0f, 21.0f)
                verticalLineTo(5.0f)
                curveTo(23.0f, 3.9f, 22.1f, 3.0f, 21.0f, 3.0f)
                close()
                moveTo(7.0f, 19.0f)
                horizontalLineTo(19.0f)
                verticalLineTo(5.0f)
                horizontalLineTo(7.0f)
                verticalLineTo(19.0f)
                close()
                moveTo(14.0f, 11.0f)
                curveTo(14.0f, 12.1f, 13.1f, 13.0f, 12.0f, 13.0f)
                curveTo(10.9f, 13.0f, 10.0f, 12.1f, 10.0f, 11.0f)
                curveTo(10.0f, 9.9f, 10.9f, 9.0f, 12.0f, 9.0f)
                curveTo(13.1f, 9.0f, 14.0f, 9.9f, 14.0f, 11.0f)
                close()
                moveTo(11.0f, 15.0f)
                horizontalLineTo(13.0f)
                verticalLineTo(17.0f)
                horizontalLineTo(11.0f)
                verticalLineTo(15.0f)
                close()
            }
        }
        return _questionBank!!
    }

private var _questionBank: ImageVector? = null
