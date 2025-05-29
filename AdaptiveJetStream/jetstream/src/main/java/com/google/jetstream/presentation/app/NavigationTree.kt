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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.google.jetstream.presentation.screens.Screens
import com.google.jetstream.presentation.screens.categories.CategoriesScreen
import com.google.jetstream.presentation.screens.categories.CategoryMovieListScreen
import com.google.jetstream.presentation.screens.favourites.FavouritesScreen
import com.google.jetstream.presentation.screens.home.HomeScreen
import com.google.jetstream.presentation.screens.moviedetails.MovieDetailsScreen
import com.google.jetstream.presentation.screens.movies.MoviesScreen
import com.google.jetstream.presentation.screens.profile.ProfileScreen
import com.google.jetstream.presentation.screens.shows.ShowsScreen
import com.google.jetstream.presentation.screens.videoPlayer.VideoPlayerScreen

@Composable
fun NavigationTree(
    appState: AppState,
    modifier: Modifier = Modifier,
    isTopBarVisible: Boolean = true,
    onScroll: (Boolean) -> Unit = {}
) {
    NavDisplay(
        backStack = appState.backStack,
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        modifier = modifier,
    ) { screen ->
        when (screen) {
            Screens.Categories -> NavEntry(screen) {
                CategoriesScreen(
                    onCategoryClick = {
                        appState.navigate(Screens.CategoryMovieList(it))
                    },
                    onScroll = onScroll,
                )
            }

            is Screens.CategoryMovieList -> NavEntry(screen) {
                CategoryMovieListScreen(
                    categoryId = screen.categoryId,
                    onBackPressed = appState::tryNavigatePreviousScreen,
                    onMovieSelected = appState::playMovie,
                )
            }

            Screens.Movies -> NavEntry(screen) {
                MoviesScreen(
                    showMovieDetails = appState::showMovieDetails,
                    playMovie = appState::playMovie,
                    onScroll = onScroll,
                    isTopBarVisible = isTopBarVisible
                )
            }

            Screens.Shows -> NavEntry(screen) {
                ShowsScreen(
                    showTvShowDetails = appState::showMovieDetails,
                    playTvShow = appState::playMovie,
                    onScroll = onScroll,
                    isTopBarVisible = isTopBarVisible
                )
            }

            Screens.Favourites -> NavEntry(screen) {
                FavouritesScreen(
                    onMovieClick = appState::showMovieDetails,
                    onScroll = onScroll,
                    isTopBarVisible = isTopBarVisible
                )
            }

            is Screens.MovieDetails -> NavEntry(screen) {
                MovieDetailsScreen(
                    movieId = screen.movieId,
                    goToMoviePlayer = {
                        appState.playMovie(screen.movieId)
                    },
                    onBackPressed = appState::tryNavigatePreviousScreen,
                    refreshScreenWithNewMovie = appState::showMovieDetails,
                )
            }

            Screens.Profile -> NavEntry(screen) {
                ProfileScreen()
            }

            is Screens.VideoPlayer -> NavEntry(screen) {
                VideoPlayerScreen(
                    movieId = screen.movieId,
                    onBackPressed = appState::tryNavigatePreviousScreen,
                )
            }

            else -> NavEntry(screen) {
                HomeScreen(
                    onMovieClick = appState::showMovieDetails,
                    goToVideoPlayer = appState::playMovie,
                    onScroll = onScroll,
                    isTopBarVisible = isTopBarVisible
                )
            }
        }
    }
}
