package com.pratikbendre.randomstringgenerator.ui.randomString

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pratikbendre.randomstringgenerator.model.RandomText
import com.pratikbendre.randomstringgenerator.utils.TitleText
import com.pratikbendre.randomstringgenerator.utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomStringScreen(
    viewModel: RandomStringViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        TopAppBar(colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary, titleContentColor = Color.White
        ), title = { Text(text = "Random String Generator") })
    }, content = { padding ->
        Column(modifier = Modifier.padding(padding)) {
            RandomStringUi(viewModel)
        }
    })
}

@Composable
fun RandomStringUi(viewModel: RandomStringViewModel) {
    var dataList by remember { mutableStateOf<List<RandomText>>(emptyList()) }
    val randomStringUiState by viewModel.randomString.collectAsStateWithLifecycle()
    var context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IntInputScreen(
            viewModel, onDeleteAll = { dataList = emptyList() },
            hasData = dataList.isNotEmpty()
        )
        Spacer(modifier = Modifier.height(16.dp))

        LaunchedEffect(randomStringUiState) {
            when (randomStringUiState) {
                is UiState.Success -> {
                    val data = (randomStringUiState as UiState.Success<RandomText>).data
                    if (data.value.isNotEmpty()) {
                        dataList = dataList + listOf(data)
                    }
                }

                is UiState.Error -> {
                    Toast.makeText(context, "Please enter a valid integer", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        StringList(dataList) { itemToDelete ->
            dataList = dataList.filter { it.created != itemToDelete.created }
        }
    }
}

@Composable
fun IntInputScreen(viewModel: RandomStringViewModel, onDeleteAll: () -> Unit, hasData: Boolean) {
    var inputText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = inputText,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) inputText = newValue
            },
            label = { Text("Enter an Integer") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            val intValue = inputText.toIntOrNull()
            if (intValue == null || intValue == 0) {
                Toast.makeText(context, "Please enter a valid input", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.fetchData(intValue)
                inputText = "" // Clear input after submission
                keyboardController?.hide()
            }
        }) {
            Text("Submit")
        }

        if (hasData) {
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onDeleteAll() },
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Text("Delete All")
            }
        }
    }
}

@Composable
private fun StringList(textList: List<RandomText>, onDelete: (RandomText) -> Unit) {
    LazyColumn {
        items(textList, key = { text -> text.created }) { text ->
            ShowData(text, onDelete)
        }
    }
}

@Composable
fun ShowData(randomText: RandomText, onDelete: (RandomText) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                TitleText("Text: ${randomText.value}")
                TitleText("Length: ${randomText.length}")
                TitleText("Created: ${randomText.created}")
            }

            IconButton(onClick = { onDelete(randomText) }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}
