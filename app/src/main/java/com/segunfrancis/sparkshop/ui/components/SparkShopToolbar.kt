package com.segunfrancis.sparkshop.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@PreviewLightDark
fun SparkShopToolbar(
    title: String = "Title",
    navIcon: Int? = null,
    onNavIconClick: () -> Unit = {},
    actionIcon: Int? = null,
    onActionIconClick: () -> Unit = {},
    cartItemCount: Int? = null
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = Modifier.fillMaxWidth(),
        expandedHeight = 56.dp,
        windowInsets = WindowInsets(top = 0.dp),
        navigationIcon = {
            navIcon?.let {
                Image(
                    painter = painterResource(navIcon),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(36.dp)
                        .clip(
                            CircleShape
                        )
                        .clickable {
                            onNavIconClick()
                        },
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
                )
            }
        },
        actions = {
            actionIcon?.let {
                Box(modifier = Modifier.padding(end = 8.dp)) {
                    Image(
                        painter = painterResource(actionIcon),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable {
                                onActionIconClick()
                            },
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
                    )
                    cartItemCount?.let {
                        if (it > 0) {
                            Text(
                                text = cartItemCount.toString(),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.error,
                                        shape = CircleShape
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                    .align(Alignment.TopEnd),
                                color = MaterialTheme.colorScheme.onError
                            )
                        }
                    }
                }
            }
        }
    )
}
