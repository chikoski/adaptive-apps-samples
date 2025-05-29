/*
 * Copyright 2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.jetstream.presentation.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.screens.Screens

class AppState(
    initialTopBarVisibility: Boolean = true,
    initialScreen: Screens = Screens.Home,
) : ViewModel() {
    var isTopBarVisible by mutableStateOf(initialTopBarVisibility)
        private set

    var selectedScreen by mutableStateOf(initialScreen)
        private set

    var isTopBarFocused by mutableStateOf(false)

    var isNavigationVisible by mutableStateOf(true)

    val backStack = mutableStateListOf<Screens>(Screens.Home)

    private var navigationComponentType
        by mutableStateOf(NavigationComponentType.NavigationSuiteScaffold)

    fun updateTopBarVisibility(visibility: Boolean) {
        isTopBarVisible = visibility
    }

    fun showTopBar() {
        updateTopBarVisibility(true)
    }

    fun updateTopBarFocusState(hasFocus: Boolean) {
        isTopBarFocused = hasFocus
    }

    fun navigate(screen: Screens) {
        selectedScreen = screen
        backStack.add(selectedScreen)
        updateNavigationVisibility()
    }

    fun showMovieDetails(movie: Movie) {
        showMovieDetails(movie.id)
    }

    fun showMovieDetails(movieId: String) {
        navigate(Screens.MovieDetails(movieId))
    }

    fun playMovie(movie: Movie) {
        playMovie(movie.id)
    }

    fun playMovie(movieId: String) {
        navigate(Screens.VideoPlayer(movieId))
    }

    fun tryNavigatePreviousScreen() {
        backStack.removeLastOrNull()
        backStack.lastOrNull()?.let {
            selectedScreen = it
        }
        updateNavigationVisibility()
    }

    fun updateNavigationComponentType(type: NavigationComponentType) {
        navigationComponentType = type
        updateNavigationVisibility()
    }

    private fun updateNavigationVisibility() {
        isNavigationVisible = when (navigationComponentType) {
            NavigationComponentType.NavigationSuiteScaffold -> {
                selectedScreen.navigationVisibility.isVisibleInNavigationSuite
            }

            NavigationComponentType.Custom -> {
                selectedScreen.navigationVisibility.isVisibleInCustomNavigation
            }
        }
    }
}
