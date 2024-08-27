import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

class ToastState {
    var message by mutableStateOf("")
    var isSuccess by mutableStateOf(true)
    var isVisible by mutableStateOf(false)

    fun showToast(message: String, isSuccess: Boolean) {
        this.message = message
        this.isSuccess = isSuccess
        this.isVisible = true
    }
}

class ToastUtils {
    private val toastState = ToastState()

    fun success(message: String) {
        toastState.showToast(message, true)
    }

    fun error(message: String) {
        toastState.showToast(message, false)
    }

    @Composable
    fun ToastMessage() {
        val currentToastState by rememberUpdatedState(toastState)

        LaunchedEffect(currentToastState.isVisible) {
            if (currentToastState.isVisible) {
                currentToastState.isVisible = true
            }
        }

        if (currentToastState.isVisible) {
            Dialog(onDismissRequest = { currentToastState.isVisible = false }) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (currentToastState.isSuccess) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = currentToastState.message,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(onClick = { currentToastState.isVisible = false }) {
                            Text("确定")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun rememberToastUtils(): ToastUtils {
    val toastUtils = remember { ToastUtils() }
    toastUtils.ToastMessage() // 确保 ToastMessage 自动挂载
    return toastUtils
}
