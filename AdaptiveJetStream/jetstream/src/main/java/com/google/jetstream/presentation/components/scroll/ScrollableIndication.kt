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

package com.google.jetstream.presentation.components.scroll

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun Modifier.horizontalScrollIndication(
    lazyListState: LazyListState,
    color: Color = MaterialTheme.colorScheme.background,
): Modifier {
    val interactionSource = remember(lazyListState) { MutableInteractionSource() }
    return scrollIndication(
        lazyListState = lazyListState,
        scrollDirection = ScrollDirection.Horizontal,
        interactionSource = interactionSource,
        color = color
    )
}

@Composable
fun Modifier.verticalScrollIndication(
    lazyListState: LazyListState,
    color: Color = MaterialTheme.colorScheme.background
): Modifier {
    val interactionSource = remember(lazyListState) { MutableInteractionSource() }
    return scrollIndication(
        lazyListState = lazyListState,
        scrollDirection = ScrollDirection.Vertical,
        interactionSource = interactionSource,
        color = color
    )
}

fun Modifier.scrollIndication(
    lazyListState: LazyListState,
    scrollDirection: ScrollDirection,
    interactionSource: MutableInteractionSource,
    color: Color
): Modifier {
    return indication(
        interactionSource = interactionSource,
        indication = ScrollIndication(
            lazyListState = lazyListState,
            scrollDirection = scrollDirection,
            color = color,
        )
    ).onFocusChanged {
        val interaction = if (it.hasFocus) {
            ScrollableContainerInteraction.Enter
        } else {
            ScrollableContainerInteraction.Exit
        }
        interactionSource.tryEmit(interaction)
    }
}

enum class ScrollableContainerInteraction : Interaction {
    Enter,
    Exit,
}

class ScrollIndication(
    private val lazyListState: LazyListState,
    private val scrollDirection: ScrollDirection,
    private val color: Color,
) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return ScrollIndicationNode(
            lazyListState = lazyListState,
            scrollDirection = scrollDirection,
            color = color,
            interactionSource = interactionSource,
        )
    }

    override fun equals(other: Any?): Boolean {
        return other is ScrollIndication &&
            other.lazyListState == lazyListState &&
            other.scrollDirection == scrollDirection &&
            other.color == color
    }

    override fun hashCode(): Int {
        return lazyListState.hashCode() * 31
        +scrollDirection.hashCode() * 31
        +color.hashCode()
    }
}

class ScrollIndicationNode(
    private val lazyListState: LazyListState,
    private val scrollDirection: ScrollDirection,
    private val color: Color,
    private val interactionSource: InteractionSource,
    private val scrimWidth: Dp = 180.dp
) : DrawModifierNode, Modifier.Node() {

    private var isFocused: Boolean = false

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collect {
                when (it) {
                    ScrollableContainerInteraction.Enter -> isFocused = true
                    ScrollableContainerInteraction.Exit -> isFocused = false
                }
                invalidateDraw()
            }
        }
    }

    override fun ContentDrawScope.draw() {
        drawContent()
        if (isFocused) {
            drawScrim()
        }
    }

    private fun ContentDrawScope.drawScrim() {
        if (scrollDirection == ScrollDirection.Horizontal) {
            if (lazyListState.canScrollForward) {
                drawScrimEnd()
            }
            if (lazyListState.canScrollBackward) {
                drawScrimStart()
            }
        } else {
            if (lazyListState.canScrollForward) {
                drawScrimBottom()
            }
            if (lazyListState.canScrollBackward) {
                drawScrimTop()
            }
        }
    }

    private fun ContentDrawScope.drawScrimStart() {
        val widthPx = scrimWidth.toPx()
        val brush = Brush.horizontalGradient(
            listOf(color, Color.Transparent),
            startX = 0f,
            endX = widthPx,
        )
        drawRect(
            brush = brush,
            size = size.copy(width = scrimWidth.toPx())
        )
    }

    private fun ContentDrawScope.drawScrimEnd() {
        val widthPx = scrimWidth.toPx()
        val brush = Brush.horizontalGradient(
            listOf(Color.Transparent, color),
            startX = size.width - widthPx,
            endX = size.width,
        )
        drawRect(
            brush = brush,
            size = size.copy(width = widthPx),
            topLeft = Offset(size.width - widthPx, 0f)
        )
    }

    private fun ContentDrawScope.drawScrimTop() {
        val heightPx = scrimWidth.toPx()
        val brush = Brush.verticalGradient(
            listOf(color, Color.Transparent),
            startY = 0f,
            endY = heightPx
        )
        drawRect(
            brush = brush,
            size = size.copy(height = heightPx),
        )
    }

    private fun ContentDrawScope.drawScrimBottom() {
        val heightPx = scrimWidth.toPx()
        val offset = Offset(0f, size.height - heightPx)
        val brush = Brush.verticalGradient(
            listOf(Color.Transparent, color),
            startY = offset.y,
            endY = size.height
        )
        drawRect(
            brush = brush,
            size = size.copy(height = heightPx),
            topLeft = offset
        )
    }
}

enum class ScrollDirection {
    Horizontal,
    Vertical
}
