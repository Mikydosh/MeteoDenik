package cz.utb.meteodenik.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
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
import cz.utb.meteodenik.ui.popis
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private val FORMAT_CASU: DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)

@Composable
fun DetailObrazovka(
    onZpet: () -> Unit,
    viewModel: DetailViewModel = viewModel()
) {
    val stav by viewModel.uiState.collectAsStateWithLifecycle()
    val mesto = stav.mesto
    val pocasi = stav.pocasi

    Scaffold { insety ->
        Column(modifier = Modifier.padding(insety).padding(16.dp)) {
            if (stav.nacitaSe) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            Text(
                text = mesto?.nazev ?: stringResource(R.string.mesto_nezname),
                style = MaterialTheme.typography.headlineMedium
            )
            if (pocasi != null) {
                Text(
                    text = stringResource(
                        R.string.teplota_a_popis, pocasi.teplotaC, stringResource(pocasi.stav.popis())
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(stringResource(R.string.detail_vlhkost, pocasi.vlhkostProcent))
                Text(stringResource(R.string.detail_vitr, pocasi.rychlostVetruKmH))
                Text(stringResource(R.string.detail_cas, pocasi.cas.format(FORMAT_CASU)))
            } else {
                Text(
                    text = stringResource(R.string.teplota_chybi),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Button(onClick = onZpet, modifier = Modifier.padding(top = 24.dp)) {
                Text(stringResource(R.string.zpet))
            }
        }
    }
}