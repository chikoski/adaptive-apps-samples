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

package com.google.jetstream.presentation.screens

import android.os.Parcelable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.jetstream.R
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

sealed interface Screens : Parcelable {
    val isTabItem: Boolean get() = false
    val isMainNavigation: Boolean get() = false
    val tabIcon: ImageVector? get() = null
    val navigationVisibility: NavigationVisibility get() = NavigationVisibility.Visible
    val navIcon: Int get() = 0
    val name: Int get() = 0

    @Parcelize
    @Serializable
    data object Profile : Screens

    @Parcelize
    @Serializable
    data object Home : Screens {
        @IgnoredOnParcel
        override val isTabItem = true

        @IgnoredOnParcel
        override val isMainNavigation = true

        @IgnoredOnParcel
        override val navIcon = R.drawable.ic_home

        @IgnoredOnParcel
        override val name = R.string.home_screen
    }

    @Parcelize
    @Serializable
    data object Categories : Screens {
        @IgnoredOnParcel
        override val isTabItem = true

        @IgnoredOnParcel
        override val isMainNavigation = true

        @IgnoredOnParcel
        override val navIcon = R.drawable.ic_category

        @IgnoredOnParcel
        override val name = R.string.categories_screen
    }

    @Parcelize
    @Serializable
    data object Movies : Screens {
        @IgnoredOnParcel
        override val isTabItem = true

        @IgnoredOnParcel
        override val isMainNavigation = true

        @IgnoredOnParcel
        override val navIcon = R.drawable.ic_movies

        @IgnoredOnParcel
        override val name = R.string.movies_screen
    }

    @Parcelize
    @Serializable
    data object Shows : Screens {
        @IgnoredOnParcel
        override val isTabItem = true

        @IgnoredOnParcel
        override val isMainNavigation = true

        @IgnoredOnParcel
        override val navIcon = R.drawable.ic_shows

        @IgnoredOnParcel
        override val name = R.string.shows_screen
    }

    @Parcelize
    @Serializable
    data object Favourites : Screens {
        @IgnoredOnParcel
        override val isTabItem = true

        @IgnoredOnParcel
        override val isMainNavigation = true

        @IgnoredOnParcel
        override val navIcon = R.drawable.ic_favorites

        @IgnoredOnParcel
        override val name = R.string.favorites_screen
    }

    @Parcelize
    @Serializable
    data object Search : Screens {
        @IgnoredOnParcel
        override val isTabItem = true

        @IgnoredOnParcel
        override val tabIcon = Icons.Default.Search

        @IgnoredOnParcel
        override val navIcon = R.drawable.ic_search

        @IgnoredOnParcel
        override val name = R.string.search_screen
    }

    @Parcelize
    @Serializable
    data class CategoryMovieList(val categoryId: String) : Screens

    @Parcelize
    @Serializable
    data class MovieDetails(val movieId: String) : Screens {
        @IgnoredOnParcel
        override val navigationVisibility: NavigationVisibility =
            NavigationVisibility.VisibleInNavigationSuite
    }

    @Parcelize
    @Serializable
    data class VideoPlayer(val movieId: String) : Screens {
        @IgnoredOnParcel
        override val navigationVisibility: NavigationVisibility = NavigationVisibility.Hidden
    }

    companion object {
        val entries = listOf(
            Home,
            Categories,
            Movies,
            Shows,
            Favourites,
            Search,
            Profile
        )
    }
}

sealed interface NavigationVisibility {
    val isVisibleInNavigationSuite: Boolean
    val isVisibleInCustomNavigation: Boolean

    data object Visible : NavigationVisibility {
        override val isVisibleInNavigationSuite = true
        override val isVisibleInCustomNavigation = true
    }

    data object Hidden : NavigationVisibility {
        override val isVisibleInNavigationSuite = false
        override val isVisibleInCustomNavigation = false
    }

    data object VisibleInNavigationSuite : NavigationVisibility {
        override val isVisibleInNavigationSuite = true
        override val isVisibleInCustomNavigation = false
    }
}
