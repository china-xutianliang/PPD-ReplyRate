import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

/**
 * 窗口名称定义
 */
enum class WindowConfig(val title: String) {

    MAIN("易电商"),
    SYSTEM("设置");

    // 获取 Painter 资源用于设置图标
    @Composable
    fun getIconPainter(): Painter {
        return painterResource("icons/main.ico")
    }
}
