package ofek.yariv.kmovies.view.fragments

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import ofek.yariv.kmovies.utils.ReportConstants
import ofek.yariv.kmovies.utils.managers.AnalyticsManager
import ofek.yariv.kmovies.view.activities.main.MainActivity
import org.koin.android.ext.android.inject

abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {
    private val analyticsManager: AnalyticsManager by inject()

    override fun onResume() {
        super.onResume()
        reportScreenShown()
    }

    abstract fun getFragmentName(): String

    abstract fun getFragmentItemId(): Int

    private fun reportScreenShown() {
        analyticsManager.report(
            "${getFragmentName()} ${ReportConstants.FRAGMENT_SHOWN}",
            ReportConstants.SHOWN
        )
    }
}