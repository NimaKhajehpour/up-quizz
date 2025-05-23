package com.nima.upquizz.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.nima.upquizz.R

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    query: String,
    label: String,
    onQueryChanged: (String) -> Unit,
    enabled: Boolean,
    onSearch: () -> Unit,
    ) {
    TextField(
        modifier = modifier.fillMaxWidth()
            .padding(bottom = 32.dp, start = 32.dp, end = 32.dp),
        value = query,
        onValueChange = onQueryChanged,
        singleLine = true,
        enabled = enabled,
        placeholder = {
            Text(label)
        },
        trailingIcon = {
            IconButton(
                onSearch
            ) {
                Icon(painterResource(R.drawable.search), null, Modifier.size(24.dp))
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onSearch()
        }),
        shape = RoundedCornerShape(100),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            focusedIndicatorColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledIndicatorColor = Color.Transparent
        )
    )
}