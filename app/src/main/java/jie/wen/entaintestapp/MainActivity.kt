package jie.wen.entaintestapp

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import jie.wen.entaintestapp.data.Constants.Companion.NO_INTERNET_MESSAGE
import jie.wen.entaintestapp.ui.fragment.RaceListFragment
import jie.wen.entaintestapp.ui.fragment.RaceListFragmentComponent
import jie.wen.entaintestapp.ui.theme.EntainTestAppTheme

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var connectivityManager: ConnectivityManager

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EntainTestAppTheme {
                Scaffold (
                    modifier = Modifier
                        .fillMaxSize(),
                    content = {
                        RaceListFragmentComponent(
                            modifier = Modifier.fillMaxSize(),
                            fragmentManager = supportFragmentManager,
                            fragmentId = 1,
                            fragment = RaceListFragment.newInstance()
                        )
                    }
                )
            }
        }

        // listen to network change
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // take action when network connection is gained
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                // take action when network connection lost
                showNoInternetConnectionError()
            }
        }
    }

    override fun onDestroy() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
        super.onDestroy()
    }

    private fun showNoInternetConnectionError() {
        Snackbar.make(window.decorView, NO_INTERNET_MESSAGE, Snackbar.LENGTH_SHORT).show()
    }
}