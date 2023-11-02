package ofek.yariv.kmovies.view.activities.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import ofek.yariv.kmovies.utils.managers.AnalyticsManager
import ofek.yariv.kmovies.utils.managers.ThemeManager
import ofek.yariv.kmovies.R
import ofek.yariv.kmovies.utils.Constants
import ofek.yariv.kmovies.utils.ReportConstants.CHANGE_THEME
import ofek.yariv.kmovies.utils.ReportConstants.CLICKED
import ofek.yariv.kmovies.utils.ReportConstants.CONTACT_US
import ofek.yariv.kmovies.utils.ReportConstants.SHARE_APP

class MenuItemClickHelper(
    private val context: Context,
    private val activity: AppCompatActivity,
    private val analyticsManager: AnalyticsManager,
    private val themeManager: ThemeManager,
) {

    private fun shareTheApp() {
        analyticsManager.report(SHARE_APP, CLICKED)
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            context.getString(R.string.share_text)
        )
        sendIntent.type = "text/plain"
        activity.startActivity(
            Intent.createChooser(
                sendIntent,
                context.getString(R.string.share_using)
            )
        )
    }

    private fun contactUs() {
        analyticsManager.report(CONTACT_US, CLICKED)
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:${Constants.email}")
        }
        activity.startActivity(
            Intent.createChooser(
                emailIntent,
                context.getString(R.string.contact_us)
            )
        )
    }

    private fun changeTheme() {
        analyticsManager.report(CHANGE_THEME, CLICKED)
        themeManager.showThemeDialog(activity = activity)
    }

    fun menuItemClickById(id: Int): Boolean {
        when (id) {
            R.id.shareItem -> shareTheApp()
            R.id.contactUsItem -> contactUs()
            R.id.changeThemeItem -> changeTheme()
        }
        return true
    }
}