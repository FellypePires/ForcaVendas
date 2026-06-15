package com.faculdade.forcavendas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.faculdade.forcavendas.data.AppDatabase
import com.faculdade.forcavendas.data.Repository
import com.faculdade.forcavendas.ui.navigation.AppNavigation
import com.faculdade.forcavendas.ui.theme.ForcaVendasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = Repository(AppDatabase.getInstance(applicationContext))

        setContent {
            ForcaVendasTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(repository)
                }
            }
        }
    }
}
