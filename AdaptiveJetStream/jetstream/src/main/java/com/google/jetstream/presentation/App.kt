/*
 * Copyright 2023 Google LLC
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

package com.google.jetstream.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.jetstream.presentation.app.AppState
import com.google.jetstream.presentation.app.NavigationComponentType
import com.google.jetstream.presentation.app.rememberNavigationComponentType
import com.google.jetstream.presentation.app.withNavigationSuiteScaffold.AppWithNavigationSuiteScaffold
import com.google.jetstream.presentation.app.withNavigationSuiteScaffold.EnableProminentMovieListOverride
import com.google.jetstream.presentation.app.withTopBarNavigation.AppWithTopBarNavigation
import com.google.jetstream.presentation.components.KeyboardShortcut
import com.google.jetstream.presentation.components.ModifierKey
import com.google.jetstream.presentation.components.handleKeyboardShortcuts
import com.google.jetstream.presentation.screens.Screens

@Composable
fun App(
    onActivityBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    appState: AppState = hiltViewModel()
) {
    val navigationComponentType = rememberNavigationComponentType()

    val keyboardShortcuts = remember {
        listOf(
            KeyboardShortcut(
                key = Key.Comma,
                modifierKeys = setOf(ModifierKey.Ctrl),
                action = {
                    if (appState.selectedScreen != Screens.Profile) {
                        appState.navigate(Screens.Profile)
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.P,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Profile) {
                        appState.navigate(Screens.Profile)
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.H,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Home) {
                        appState.navigate(Screens.Home)
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.C,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Categories) {
                        appState.navigate(Screens.Categories)
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.M,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Movies) {
                        appState.navigate(Screens.Movies)
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.T,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Shows) {
                        appState.navigate(Screens.Shows)
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.F,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Favourites) {
                        appState.navigate(Screens.Shows)
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.Slash,
                action = {
                    if (appState.selectedScreen != Screens.Search) {
                        appState.navigate(Screens.Search)
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.S,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Search) {
                        appState.navigate(Screens.Search)
                    }
                }
            ),
        )
    }

    LaunchedEffect(navigationComponentType) {
        appState.updateNavigationComponentType(navigationComponentType)
    }

    when (navigationComponentType) {
        NavigationComponentType.NavigationSuiteScaffold -> {
            EnableProminentMovieListOverride {
                AppWithNavigationSuiteScaffold(
                    appState = appState,
                    modifier = modifier.handleKeyboardShortcuts(keyboardShortcuts),
                )
            }
        }

        NavigationComponentType.Custom -> {
            AppWithTopBarNavigation(
                appState = appState,
                onActivityBackPressed = onActivityBackPressed,
                modifier = modifier.handleKeyboardShortcuts(keyboardShortcuts),
            )
        }
    }
}
