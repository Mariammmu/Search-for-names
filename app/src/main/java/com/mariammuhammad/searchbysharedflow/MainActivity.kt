package com.mariammuhammad.searchbysharedflow

import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mariammuhammad.searchbysharedflow.ui.theme.SearchBySharedFlowTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                SearchScreen( )
           }
        }
    }

val searchBar = MutableSharedFlow<String>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

val names = listOf("Mariam", "Alyaa", "Engy", "Asmaa", "Zeinab")

fun filter(names: List<String>, query: String): List<String> {
    return names.filter { it.contains(query, ignoreCase = true) }
}

@Composable
fun SearchScreen() {
    val searchQuery = remember { mutableStateOf("") }

    val filteredNames = remember { mutableStateOf(names) }

   val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            searchBar
                .debounce(500)
                .distinctUntilChanged()
                .map { query -> filter(names, query) }
                .collectLatest { results ->
                    filteredNames.value = results
                }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = searchQuery.value,
            onValueChange = { query ->
                searchQuery.value = query
                CoroutineScope(Dispatchers.IO).launch {
                    searchBar.emit(query)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            label = { Text("Search") },
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn {
            items(filteredNames.value) { name ->
                Text(
                    text = name,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}