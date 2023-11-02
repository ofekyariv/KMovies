package ofek.yariv.kmovies.view.activities.main

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import ofek.yariv.kmovies.R
import ofek.yariv.kmovies.databinding.ActivityMainBinding
import ofek.yariv.kmovies.view.activities.main.listeners.ScrollListener
import ofek.yariv.kmovies.view.activities.main.listeners.TopBarTextChangeListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val SPLASH_TIME_OUT = 3000L

class MainActivity : AppCompatActivity(), ScrollListener, TopBarTextChangeListener {
    private lateinit var binding: ActivityMainBinding
    private val mainActivityViewModel: MainActivityViewModel by viewModel()
    private val menuItemClickHelper: MenuItemClickHelper by inject { parametersOf(this) }

    override fun onScrolledUp() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.btnMenu.startAnimation(fadeIn)
        binding.tvAppName.startAnimation(fadeIn)
        binding.btnMenu.visibility = View.VISIBLE
        binding.tvAppName.visibility = View.VISIBLE
    }

    override fun onScrolledDown() {
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                binding.btnMenu.visibility = View.GONE
                binding.tvAppName.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        binding.btnMenu.startAnimation(fadeOut)
        binding.tvAppName.startAnimation(fadeOut)
    }

    override fun ChangeTopBarText(text: String) {
        binding.tvAppName.text = text
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateSplashScreen()
        initiateLayout()
        setupOnBackPressedHandle()
        mainActivityViewModel.reportMainActivityShown()
    }

    private fun setupOnBackPressedHandle() {
        onBackPressedDispatcher.addCallback(this) {
            if (binding.drawerLayout.isOpen) {
                binding.drawerLayout.close()
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
                isEnabled = true
            }
        }
    }

    private fun initiateSplashScreen() {
        var loading = true

        lifecycleScope.launch {
            withTimeoutOrNull(SPLASH_TIME_OUT) {
                mainActivityViewModel.trendingMovies.collect {
                    loading = false
                }
            }
        }

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                loading
            }
        }
    }


    private fun initiateLayout() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigationMenu()
        binding.btnMenu.setOnClickListener { binding.drawerLayout.open() }
        binding.sideNavigationView.setNavigationItemSelectedListener {
            menuItemClickHelper.menuItemClickById(
                it.itemId
            )
        }
    }

    private fun setupBottomNavigationMenu() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.moviesNavHostFragment) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.moviesFragment -> {
                    navigateToTopLevelDestination(R.id.moviesFragment)
                    true
                }

                R.id.savedMoviesFragment -> {
                    navigateToTopLevelDestination(R.id.savedMoviesFragment)
                    true
                }

                R.id.searchMoviesFragment -> {
                    navigateToTopLevelDestination(R.id.searchMoviesFragment)
                    true
                }

                else -> false
            }
        }
    }

    private fun navigateToTopLevelDestination(destinationId: Int) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.moviesNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.popBackStack(destinationId, inclusive = false)
        if (navController.currentDestination?.id != destinationId) {
            navController.navigate(destinationId)
        }
    }
}