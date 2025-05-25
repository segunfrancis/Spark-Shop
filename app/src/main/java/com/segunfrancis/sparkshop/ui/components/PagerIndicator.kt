package com.segunfrancis.sparkshop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PagerIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2F),
    indicatorHeight: Int = 4,
    indicatorSpacing: Int = 8
) {
    Row(
        modifier = modifier.height(indicatorHeight.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(indicatorHeight.dp)
                    .padding(horizontal = (indicatorSpacing / 2).dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (index == currentPage) activeColor else inactiveColor)
            )
        }
    }
}
