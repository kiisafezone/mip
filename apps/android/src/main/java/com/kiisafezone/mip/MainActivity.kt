package com.kiisafezone.mip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kiisafezone.mip.data.local.MatchDatabaseFactory
import com.kiisafezone.mip.data.local.export.MatchExportService
import com.kiisafezone.mip.presentation.match.MatchApp
import com.kiisafezone.mip.presentation.match.MatchSessionViewModel

class MainActivity : ComponentActivity() {
    private lateinit var matchViewModel: MatchSessionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = MatchDatabaseFactory.create(applicationContext)
        val exporter = MatchExportService(repository, repository)
        val factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MatchSessionViewModel::class.java)) {
                    return MatchSessionViewModel(repository, repository, exporter) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
        matchViewModel = ViewModelProvider(this, factory)[MatchSessionViewModel::class.java]
        setContent {
            androidx.compose.material3.MaterialTheme {
                MatchApp(matchViewModel)
            }
        }
    }
}
