package com.example.f1application

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.f1application.core.navigation.Drivers
import com.example.f1application.core.navigation.Home
import com.example.f1application.core.navigation.Profile
import com.example.f1application.core.navigation.RaceDetails
import com.example.f1application.core.navigation.Races
import com.example.f1application.core.navigation.Route
import com.example.f1application.core.navigation.TopLevelBackStack
import com.example.f1application.features.home.view.HomeScreen
import com.example.f1application.features.races.view.RacesListScreen
import com.example.f1application.shared.ui.RaceDetailScreen
import org.koin.java.KoinJavaComponent.inject


@Composable
fun MainScreen() {
    val topLevelBackStack by inject<TopLevelBackStack<Route>>(clazz = TopLevelBackStack::class.java)

    Scaffold(bottomBar = {
        NavigationBar {
            listOf(Home, Races, Drivers, Profile).forEach { route ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            route.icon,
                            contentDescription = null,
                            modifier = Modifier.size(38.dp)
                        )
                    },
                    selected = topLevelBackStack.topLevelKey == route,
                    onClick = {
                        topLevelBackStack.addTopLevel(route)
                    }
                )
            }
        }
    }) { padding ->
        NavDisplay(
            backStack = topLevelBackStack.backStack,
            onBack = { topLevelBackStack.removeLast() },
            modifier = Modifier.padding(padding),
            entryDecorators = listOf(
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<Home> {
                    HomeScreen()
                }
                entry<Races> {
                    RacesListScreen()
                }
                entry<Drivers> {
//                    DriversListView()
                }
                entry<Profile> {
//                    ProfileView()
                }

                entry<RaceDetails> { route ->
                    RaceDetailScreen(race = route.race)
                }
//                entry<EditProfile> {
//                    EditProfileScreen(
//                        onBackClick = { topLevelBackStack.removeLast() }
//                    )
//                }
//                entry<DriverDetails> { route ->
//                    DriversDetailsView(
//                        driverId = route.driverId,
//                        initialPoints = route.initialPoints,
//                        initialPosition = route.initialPosition,
//                        initialWins = route.initialWins
//                    )
//                }
            }
        )
    }
}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    PracticesAndroidTheme {
//        MainScreen()
//    }
//}