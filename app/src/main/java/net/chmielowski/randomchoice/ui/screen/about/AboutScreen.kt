@file:Suppress("FunctionName")

package net.chmielowski.randomchoice.ui.screen.about

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import net.chmielowski.randomchoice.ui.widgets.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import net.chmielowski.randomchoice.BuildConfig
import net.chmielowski.randomchoice.R
import net.chmielowski.randomchoice.ui.destinations.LibrariesScreenDestination
import net.chmielowski.randomchoice.ui.widgets.Scaffold

@Preview
@Destination
@Composable
internal fun AboutScreen(navigator: DestinationsNavigator = EmptyDestinationsNavigator) {
    Scaffold(
        navigateUp = navigator::navigateUp,
        title = stringResource(R.string.label_about)
    ) {
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Text(
                text = stringResource(R.string.label_version),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Text(
                text = BuildConfig.VERSION_NAME,
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            SourceCodeButton()
            LibrariesButton(navigator)
            SendFeedbackButton()
            RateAppButton()
        }
    }
}

@Composable
private fun SourceCodeButton() {
    val context = LocalContext.current
    Button(
        onClick = { context.openWebPage("https://github.com/pchmielowski/RandomChoiceApp") },
        icon = Icons.Outlined.Code,
        label = R.string.label_source,
    )
}

@Composable
private fun LibrariesButton(navigator: DestinationsNavigator) {
    Button(
        onClick = { navigator.navigate(LibrariesScreenDestination) },
        icon = Icons.Outlined.List,
        label = R.string.label_libraries,
    )
}

@Composable
private fun SendFeedbackButton() {
    val context = LocalContext.current
    var noClientInfoVisible by remember { mutableStateOf(false) }
    Button(
        onClick = { sendEmail(context, onNoClient = { noClientInfoVisible = true }) },
        icon = Icons.Outlined.Feedback,
        label = R.string.action_send_feedback,
    )
    if (noClientInfoVisible) {
        NoClientDialog(onDismissRequest = { noClientInfoVisible = false })
    }
}

private fun sendEmail(context: Context, onNoClient: () -> Unit) {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(DEVELOPER_EMAIL))
    intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
    intent.putExtra(
        Intent.EXTRA_TEXT, "Application version: ${BuildConfig.VERSION_NAME}."
    )
    @Suppress("SwallowedException")
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        onNoClient()
    }
}

@Composable
private fun NoClientDialog(onDismissRequest: () -> Unit) {
    val context = LocalContext.current
    AlertDialog(
        title = { Text(stringResource(R.string.error_no_email_client)) },
        text = { Text(stringResource(R.string.label_send_feedback_to, DEVELOPER_EMAIL)) },
        onDismissRequest = onDismissRequest,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.action_close))
            }
        },
        confirmButton = {
            Button(onClick = context::copyEmail) {
                Text(stringResource(R.string.action_copy_email))
            }
        }
    )
}

private fun Context.copyEmail() {
    val manager = getSystemService<ClipboardManager>()!!
    val clip = ClipData.newPlainText("E-mail", DEVELOPER_EMAIL)
    manager.setPrimaryClip(clip)
    Toast
        .makeText(this, getString(R.string.message_email_copied), Toast.LENGTH_LONG)
        .show()
}

private const val DEVELOPER_EMAIL = "random.choice.app@gmail.com"

@Composable
private fun RateAppButton() {
    val context = LocalContext.current
    Button(
        onClick = { context.openWebPage("https://play.google.com/store/apps/details?id=${context.packageName}") },
        icon = Icons.Outlined.StarRate,
        label = R.string.action_rate_app,
    )
}

@Suppress("SwallowedException")
private fun Context.openWebPage(uri: String) = try {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
} catch (e: ActivityNotFoundException) {
    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show()
}

@Composable
private fun Button(
    onClick: () -> Unit,
    icon: ImageVector,
    @StringRes label: Int,
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 8.dp),
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(16.dp))
        Text(stringResource(label))
    }
}
