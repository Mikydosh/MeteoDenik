package cz.utb.meteodenik.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.utb.meteodenik.R

@Composable
fun DetailObrazovka(
    onZpet: () -> Unit,
    viewModel: DetailViewModel = viewModel()
) {
    val stav by viewModel.uiState.collectAsStateWithLifecycle()
    val mesto = stav.mesto

    Scaffold { insety ->
        Column(modifier = Modifier.padding(insety).padding(16.dp)) {
            Text(
                text = mesto?.nazev ?: stringResource(R.string.mesto_nezname),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = mesto?.teplotaC
                    ?.let { stringResource(R.string.teplota_format, it) }
                    ?: stringResource(R.string.teplota_chybi),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
            Button(onClick = onZpet, modifier = Modifier.padding(top = 24.dp)) {
                Text(stringResource(R.string.zpet))
            }
        }
    }
}