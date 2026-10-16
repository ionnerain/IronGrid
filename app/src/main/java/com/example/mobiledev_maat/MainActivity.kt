package com.example.mobiledev_maat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mobiledev_maat.navigation.AppRouting
import com.example.mobiledev_maat.ui.theme.IronGridTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IronGridTheme {
                AppRouting()
            }
        }
    }
}