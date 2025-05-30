package com.nima.upquizz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nima.upquizz.datastore.AppDatastore
import com.nima.upquizz.navigation.main.MainNavigation
import com.nima.upquizz.ui.theme.UpQuizzTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val datastore = AppDatastore(context)
            val theme = datastore.getTheme.collectAsState(true).value
            UpQuizzTheme(
                darkTheme = theme
            ) {
                Surface (
                ) {
                    MainNavigation()
                }
            }
        }
    }
}
