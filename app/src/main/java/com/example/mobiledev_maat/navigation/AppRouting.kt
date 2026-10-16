package com.example.mobiledev_maat.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mobiledev_maat.models.DayLogFactory
import com.example.mobiledev_maat.models.DayLogViewModel
import com.example.mobiledev_maat.models.FirebaseRepository
import com.example.mobiledev_maat.models.IronGridApplication
import com.example.mobiledev_maat.screen.GridScreen
import com.example.mobiledev_maat.screen.LogScreen
import com.example.mobiledev_maat.screen.RecoveryScreen
import com.example.mobiledev_maat.screen.WelcomeScreen

object Routes {
    const val WELCOME = "welcome"
    const val GRID = "grid"
    const val RECOVERY = "recovery"
    const val LOG = "log"
}

@Composable
fun AppRouting() {
    val navController = rememberNavController()

    val context = LocalContext.current
    val app = context.applicationContext as IronGridApplication
    val factory = DayLogFactory(app.repository)
    val sharedViewModel: DayLogViewModel = viewModel(factory = factory)

    NavHost(
        navController = navController,
        startDestination = Routes.WELCOME
    ) {

        composable(Routes.WELCOME) {
            WelcomeScreen(
                onStart = {
                    FirebaseRepository().addWorkout()
                    navController.navigate(Routes.GRID) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.GRID) {
            GridScreen(
                viewModel = sharedViewModel,
                onDayClick = { date -> navController.navigate("${Routes.LOG}/$date") },
                onRecoveryClick = { navController.navigate(Routes.RECOVERY) }
            )
        }

        composable(Routes.RECOVERY) {
            RecoveryScreen(
                viewModel = sharedViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Routes.LOG}/{date}",
            arguments = listOf(navArgument("date") { type = NavType.StringType })
        ) { back ->
            val date = back.arguments?.getString("date") ?: return@composable
            LogScreen(
                viewModel = sharedViewModel,
                date = date,
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}