package com.example.f1application

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.f1application.core.navigation.DriverDetails
import com.example.f1application.core.navigation.Drivers
import com.example.f1application.core.navigation.EditProfile
import com.example.f1application.core.navigation.Home
import com.example.f1application.core.navigation.Profile
import com.example.f1application.core.navigation.RaceDetails
import com.example.f1application.core.navigation.Races
import com.example.f1application.core.navigation.Route
import com.example.f1application.core.navigation.TopLevelBackStack
import com.example.f1application.features.drivers.view.DriverDetailScreen
import com.example.f1application.features.drivers.view.DriversListScreen
import com.example.f1application.features.home.view.HomeScreen
import com.example.f1application.features.profile.view.EditProfileScreen
import com.example.f1application.features.profile.view.ProfileScreen
import com.example.f1application.features.races.view.RacesListScreen
import com.example.f1application.shared.ui.RaceDetailScreen
import org.koin.java.KoinJavaComponent.inject

@Composable
fun MainScreen() {
    val topLevelBackStack by inject<TopLevelBackStack<Route>>(clazz = TopLevelBackStack::class.java)

    Scaffold(bottomBar = {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                        )
                    )
                )
        ) {
            listOf(Home, Races, Drivers, Profile).forEach { route ->
                NavigationBarItem(icon = {
                    Icon(
                        route.icon, contentDescription = null, modifier = Modifier.size(38.dp)
                    )
                }, selected = topLevelBackStack.topLevelKey == route, onClick = {
                    topLevelBackStack.addTopLevel(route)
                })
            }
        }
    }) { padding ->
        NavDisplay(
            backStack = topLevelBackStack.backStack,
            onBack = { topLevelBackStack.removeLast() },
            modifier = Modifier.padding(padding),
            entryDecorators = listOf(
                rememberSavedStateNavEntryDecorator(), rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<Home> {
                    HomeScreen()
                }
                entry<Races> {
                    RacesListScreen()
                }
                entry<Drivers> {
                    DriversListScreen()
                }
                entry<Profile> {
                    ProfileScreen()
                }
                entry<RaceDetails> { route ->
                    RaceDetailScreen(race = route.race)
                }
                entry<DriverDetails> { route ->
                    DriverDetailScreen(driverId = route.driverId)
                }
                entry<EditProfile> {
                    EditProfileScreen()
                }
            })
    }
}
