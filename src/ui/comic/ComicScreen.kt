package com.shortcut.myapplication.ui.comic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.shortcut.myapplication.domain.Comic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComicScreen(
    onNavigateToFavorites: () -> Unit,
    viewModel: ComicViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSearchDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("xkcd #${uiState.comic?.num ?: ""}") },
                actions = {
                    IconButton(onClick = { showSearchDialog = true }) {
                        Icon(Icons.Default.Search, "Search")
                    }
                    IconButton(onClick = onNavigateToFavorites) {
                        Icon(Icons.Default.Favorite, "Favorites")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                onPrevious = { viewModel.loadPreviousComic() },
                onNext = { viewModel.loadNextComic() },
                onRandom = { viewModel.loadRandomComic() },
                onLatest = { viewModel.loadLatestComic() }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null -> {
                    ErrorMessage(
                        message = uiState.error!!,
                        onRetry = { viewModel.loadLatestComic() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.comic != null -> {
                    ComicContent(
                        comic = uiState.comic!!,
                        onToggleFavorite = { viewModel.toggleFavorite() }
                    )
                }
            }
        }
    }

    if (showSearchDialog) {
        SearchDialog(
            onDismiss = { showSearchDialog = false },
            onSearch = { num ->
                viewModel.searchComic(num)
                showSearchDialog = false
            }
        )
    }
}

@Composable
fun ComicContent(
    comic: Comic,
    onToggleFavorite: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = comic.title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = comic.date,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            AsyncImage(
                model = comic.img,
                contentDescription = comic.alt,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (comic.isFavorite) Icons.Default.Favorite
                    else Icons.Default.FavoriteBorder,
                    contentDescription = "Toggle Favorite",
                    tint = if (comic.isFavorite) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Alt Text:",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = comic.alt,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun NavigationBar(
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onRandom: () -> Unit,
    onLatest: () -> Unit
) {
    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = onPrevious) {
                Icon(Icons.Default.ArrowBack, "Previous")
            }
            IconButton(onClick = onRandom) {
                Icon(Icons.Default.Refresh, "Random")
            }
            IconButton(onClick = onLatest) {
                Icon(Icons.Default.Home, "Last")
            }
            IconButton(onClick = onNext) {
                Icon(Icons.Default.ArrowForward, "Next")
            }
        }
    }
}

@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Error: $message",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
fun SearchDialog(
    onDismiss: () -> Unit,
    onSearch: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Search Comic") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Comic Number") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { onSearch(text) }) {
                Text("Search")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}