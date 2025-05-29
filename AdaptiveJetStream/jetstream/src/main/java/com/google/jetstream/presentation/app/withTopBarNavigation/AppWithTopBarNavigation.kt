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

package com.google.jetstream.presentation.app.withTopBarNavigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import com.google.jetstream.presentation.app.AppState
import com.google.jetstream.presentation.app.NavigationTree
import com.google.jetstream.presentation.components.onBackButtonPressed
import com.google.jetstream.presentation.components.shim.tryRequestFocus
import com.google.jetstream.presentation.screens.Screens

@Composable
fun AppWithTopBarNavigation(
    appState: AppState,
    onActivityBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = remember { Screens.entries.filter { it.isTabItem } }
    val topBar = remember { FocusRequester() }

    Column(
        modifier = modifier.onBackButtonPressed {
            when {
                !appState.isNavigationVisible -> {
                    onActivityBackPressed()
                }

                !appState.isTopBarVisible -> {
                    appState.showTopBar()
                    topBar.tryRequestFocus()
                }

                !appState.isTopBarFocused -> {
                    topBar.tryRequestFocus()
                }

                appState.selectedScreen != Screens.Home -> {
                    appState.navigate(Screens.Home)
                }

                else -> {
                    onActivityBackPressed()
                }
            }
        }
    ) {
        AnimatedVisibility(
            appState.isNavigationVisible &&
                appState.isTopBarVisible
        ) {
            TopBar(
                items = items,
                selectedScreen = appState.selectedScreen,
                showScreen = {
                    if (it != appState.selectedScreen) {
                        appState.navigate(it)
                    }
                },
                modifier = Modifier
                    .padding(
                        vertical = 16.dp,
                        horizontal = 74.dp,
                    )
                    .focusRequester(topBar)
                    .onFocusChanged {
                        appState.updateTopBarFocusState(it.hasFocus)
                    }
            )
        }
        NavigationTree(
            appState = appState,
            isTopBarVisible = appState.isTopBarVisible,
            onScroll = appState::updateTopBarVisibility
        )
    }
}
