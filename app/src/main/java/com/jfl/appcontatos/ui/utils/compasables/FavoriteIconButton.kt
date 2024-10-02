package com.jfl.appcontatos.ui.utils.compasables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jfl.appcontatos.R
import com.jfl.appcontatos.ui.theme.AppContatosTheme


@Composable
fun FavoriteIconButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onPressed: () -> Unit
) {

    IconButton(
        modifier = modifier,
        onClick = onPressed
    ) {
        Icon(
            imageVector = if (isFavorite) {
                Icons.Filled.Favorite
            } else {
                Icons.Filled.FavoriteBorder
            },
            contentDescription = stringResource(R.string.favorite),
            tint = if (isFavorite) {
                Color.Red
            } else {
                LocalContentColor.current
            }
        )
    }
}

@Preview
@Composable
fun FavoriteIconButtonPreview() {
    AppContatosTheme {
        FavoriteIconButton(
            isFavorite = true,
            onPressed = {})
    }
}

@Preview
@Composable
fun FavoriteIconButtonPreview2() {
    AppContatosTheme {
        FavoriteIconButton(
            isFavorite = false,
            onPressed = {})
    }
}
