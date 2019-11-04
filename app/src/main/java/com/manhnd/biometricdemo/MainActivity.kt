package com.manhnd.biometricdemo

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    lateinit var biometricPrompt: BiometricPrompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        biometricPrompt = instanceOfBiometricPrompt()
        findViewById<Button>(R.id.btnClickBiometric).setOnClickListener {
            if (isDeviceSupportBiometric()) {
                biometricPrompt.authenticate(getPromptInfo())
            }
        }

    }

    private fun isDeviceSupportBiometric(): Boolean {
        val biometricManager = BiometricManager.from(this@MainActivity)
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
    }


    private fun instanceOfBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(this@MainActivity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                showMessage("$errorCode :: $errString")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                showMessage("Authentication failed for an unknown reason")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                showMessage("Authentication was successful")
            }
        }

        return BiometricPrompt(this@MainActivity, executor, callback)
    }

    private fun getPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("My App's Authentication")
            .setSubtitle("Please login to get access")
            .setDescription("My App is using Android biometric authentication")
            .setDeviceCredentialAllowed(true)
            .setConfirmationRequired(true)
            .build()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}
