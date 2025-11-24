package com.shortcut.myapplication.ui.favorites;

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.shortcut.myapplication.domain.Comic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
        onNavigateBack: () -> Unit,
onComicClick: (Int) -> Unit,
viewModel: FavoritesViewModel = hiltViewModel()
) {
val favorites by viewModel.favorites.collectAsState()

Scaffold(
        topBar = {
    TopAppBar(
            title = { Text("Favorites") },
            navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, "Back")
            }
            }
    )
}
    ) { padding ->
        if (favorites.isEmpty()) {
Box(
        modifier = Modifier
                .fillMaxSize()
                    .padding(padding),
contentAlignment = Alignment.Center
            ) {
Text(
        text = "No favorites yet!",
        style = MaterialTheme.typography.bodyLarge
)
            }
                    } else {
LazyColumn(
        modifier = Modifier
                .fillMaxSize()
                    .padding(padding),
contentPadding = PaddingValues(16.dp),
verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
items(favorites) { comic ->
        FavoriteComicCard(
                comic = comic,
                onClick = { onComicClick(comic.num) }
        )
}
            }
                    }
                    }
                    }

@Composable
fun FavoriteComicCard(
        comic: Comic,
        onClick: () -> Unit
) {
Card(
        modifier = Modifier
                .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
Row(
        modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
verticalAlignment = Alignment.CenterVertically
        ) {
AsyncImage(
        model = comic.img,
        contentDescription = comic.title,
        modifier = Modifier.size(80.dp),
contentScale = ContentScale.Fit
            )

Spacer(modifier = Modifier.width(12.dp))

Column {
    Text(
            text = "#${comic.num}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
    )
    Text(
            text = comic.title,
            style = MaterialTheme.typography.titleMedium
    )
    Text(
            text = comic.date,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
        }
                }
                }