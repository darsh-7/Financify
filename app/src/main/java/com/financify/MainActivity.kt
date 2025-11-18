package com.financify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import com.financify.data.DataStoreManager
import com.financify.presentation.navigation.AppNavHost
import com.financify.presentation.theme.FinancifyTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : FragmentActivity() {

    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        dataStoreManager = DataStoreManager(this)

        val biometricEnabled = runBlocking { dataStoreManager.biometricEnabledFlow.first() }

        if (biometricEnabled == true) {
            showBiometricPrompt()
        } else {
            setContent {
                FinancifyTheme {
                    AppNavHost()
                }
            }
        }
    }

    private fun showBiometricPrompt() {
        val biometricManager = BiometricManager.from(this)
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for Financify")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build()

            val biometricPrompt = BiometricPrompt(this,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        setContent {
                            FinancifyTheme {
                                AppNavHost()
                            }
                        }
                    }
                })

            biometricPrompt.authenticate(promptInfo)
        } else {
            setContent {
                FinancifyTheme {
                    AppNavHost()
                }
            }
        }
    }
}
