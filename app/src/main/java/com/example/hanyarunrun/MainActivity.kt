package com.example.hanyarunrun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hanyarunrun.ui.navigation.AppNavHost
import com.example.hanyarunrun.ui.theme.HanyarunrunTheme
import com.example.hanyarunrun.ui.theme.HanyarunrunTheme
import com.example.hanyarunrun.viewmodel.DataViewModel
import com.example.hanyarunrun.viewmodel.ProfileViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: DataViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HanyarunrunTheme {
                Main(viewModel = viewModel,
                    profileViewModel = profileViewModel
                )
            }
        }
    }
}
