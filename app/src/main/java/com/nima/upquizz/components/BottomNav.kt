package com.nima.upquizz.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.nima.upquizz.R
import com.nima.upquizz.navigation.pages.PagesScreens

@Composable
fun BottomNav(navController: NavHostController, destination: String, list: List<String>) {
    if (destination in list){
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surfaceBright,
                tonalElevation = 5.dp
            ) {
                NavigationBarItem(
                    selected = destination == PagesScreens.HomeScreen.name,
                    onClick = {
                        if (destination != PagesScreens.HomeScreen.name) {
                            navController.navigate(PagesScreens.HomeScreen.name) {
                                popUpTo(destination!!) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = {
                        Icon(painterResource(R.drawable.home), null, modifier = Modifier.size(24.dp))
                    },
                    label = {
                        Text("Home")
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onSecondary,
                        selectedTextColor = MaterialTheme.colorScheme.secondary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                        indicatorColor = MaterialTheme.colorScheme.secondary
                    )
                )
                NavigationBarItem(
                    selected = destination == PagesScreens.CategoriesScreen.name,
                    onClick = {
                        if (destination != PagesScreens.CategoriesScreen.name) {
                            navController.navigate(PagesScreens.CategoriesScreen.name) {
                                popUpTo(destination!!) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = {
                        Icon(painterResource(R.drawable.category), null, modifier = Modifier.size(24.dp))
                    },
                    label = {
                        Text("Categories")
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onSecondary,
                        selectedTextColor = MaterialTheme.colorScheme.secondary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                        indicatorColor = MaterialTheme.colorScheme.secondary
                    )
                )
                NavigationBarItem(
                    selected = destination == PagesScreens.UsersScreen.name,
                    onClick = {
                        if (destination != PagesScreens.UsersScreen.name) {
                            navController.navigate(PagesScreens.UsersScreen.name) {
                                popUpTo(destination!!) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = {
                        Icon(painterResource(R.drawable.users), null, modifier = Modifier.size(24.dp))
                    },
                    label = {
                        Text("Users")
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onSecondary,
                        selectedTextColor = MaterialTheme.colorScheme.secondary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                        indicatorColor = MaterialTheme.colorScheme.secondary
                    )
                )
                NavigationBarItem(
                    selected = destination == PagesScreens.SettingsScreen.name,
                    onClick = {
                        if (destination != PagesScreens.SettingsScreen.name) {
                            navController.navigate(PagesScreens.SettingsScreen.name) {
                                popUpTo(destination!!) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = {
                        Icon(painterResource(R.drawable.settings), null, modifier = Modifier.size(24.dp))
                    },
                    label = {
                        Text("Settings")
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onSecondary,
                        selectedTextColor = MaterialTheme.colorScheme.secondary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                        indicatorColor = MaterialTheme.colorScheme.secondary
                    )
                )
            }
    }
}