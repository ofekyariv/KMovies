package ofek.yariv.kmovies.utils.initializable

class InitializeAppComponentsManager(private val initializableAppComponent: List<InitializableAppComponent>) :
    InitializableAppComponent {
    override fun init() {
        initializableAppComponent.forEach { it.init() }
    }
}