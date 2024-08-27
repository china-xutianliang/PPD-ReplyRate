import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

object LoadingUtils {
    private val loadingState = mutableStateOf(LoadingState())

    class LoadingState {
        var message by mutableStateOf("")
        var isVisible by mutableStateOf(false)
    }

    fun show(message: String) {
        loadingState.value.message = message
        loadingState.value.isVisible = true
    }

    fun dismiss() {
        loadingState.value.isVisible = false
    }

    @Composable
    fun LoadingDialog() {
        val state = loadingState.value
        if (state.isVisible) {
            Dialog(onDismissRequest = {}) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }
            }
        }
    }
}
