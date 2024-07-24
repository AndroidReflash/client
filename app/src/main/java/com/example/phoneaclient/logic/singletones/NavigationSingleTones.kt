package com.example.phoneaclient.logic.singletones

sealed class NavigationSingleTones(val route: String) {
    object MainScreen: NavigationSingleTones(route = "main_screen")
    object ConfigScreen: NavigationSingleTones(route = "config_screen")
    object ScanListScreen: NavigationSingleTones(route = "scan_list_screen")
}