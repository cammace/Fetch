package com.cammace.fetch.ui.hiring

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cammace.fetch.R
import com.cammace.fetch.data.model.HiringItem

@Composable
fun HiringListScreen(
    modifier: Modifier = Modifier,
    viewModel: HiringViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HiringListScreen(
        modifier = modifier,
        uiState = uiState,
        onRetry = { viewModel.retry() }
    )
}

@Composable
private fun HiringListScreen(
    modifier: Modifier = Modifier,
    uiState: HiringUiState,
    onRetry: () -> Unit = {},
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        when (uiState) {
            is HiringUiState.Loading -> CircularProgressIndicator()
            is HiringUiState.Success -> {
                val items = uiState.items
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(items.size) { index ->
                        ItemRow(items[index])
                    }
                }
            }

            is HiringUiState.Empty -> {
                Text(
                    text = stringResource(R.string.empty_list_message),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

            is HiringUiState.LoadFailed -> {
                val exception = uiState.exception
                LoadFailed(exception, onRetry)
            }
        }
    }
}

@Composable
fun ItemRow(
    item: HiringItem,
) {
    ListItem(
        headlineContent = {
            Text(
                text = item.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Text(
                text = stringResource(R.string.list_id_preface, item.listId),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
    HorizontalDivider(thickness = 0.5.dp)
}

@Composable
fun LoadFailed(
    exception: Throwable,
    onRetry: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = { onRetry }) {
            Text(text = stringResource(R.string.error_button_retry))
        }
        Text(
            text = "An error occurred: ${exception.localizedMessage}",
            color = Color.Red,
            modifier = Modifier.padding(16.dp)
        )
    }
}

//
// PREVIEWS
//

@Preview(showBackground = true)
@Composable
fun PreviewItemRow() {
    val sampleItem = HiringItem(id = 1, listId = 1, name = "Sample Item")
    ItemRow(item = sampleItem)
}

@Preview(showBackground = true)
@Composable
fun PreviewLoadingState() {
    HiringListScreen(uiState = HiringUiState.Loading)
}

@Preview(showBackground = true)
@Composable
fun PreviewSuccessState() {
    val items = listOf(
        HiringItem(id = 1, listId = 1, name = "Item 1"),
        HiringItem(id = 2, listId = 1, name = "Item 2"),
        HiringItem(id = 3, listId = 2, name = "Item 3"),
        HiringItem(id = 4, listId = 2, name = "Item 4")
    )
    HiringListScreen(uiState = HiringUiState.Success(items = items))
}

@Preview(showBackground = true)
@Composable
fun PreviewEmptyState() {
    HiringListScreen(uiState = HiringUiState.Empty)
}

@Preview(showBackground = true)
@Composable
fun PreviewErrorState() {
    HiringListScreen(
        uiState = HiringUiState.LoadFailed(exception = Exception("Sample error message")),
        onRetry = {} // No action needed for preview
    )
}
