package ofek.yariv.kmovies.utils.initializable

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import ofek.yariv.kmovies.R

class FirebaseInitializer : InitializableAppComponent {
    override fun init() {
        FirebaseAuth.getInstance().signInAnonymously()
        FirebaseRemoteConfig.getInstance().apply {
            setDefaultsAsync(R.xml.remote_config_defaults)
            fetchAndActivate()
        }
    }
}