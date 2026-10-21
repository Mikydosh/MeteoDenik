package cz.utb.meteodenik

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@Composable
fun BmiObrazovka(modifier: Modifier = Modifier) {
    var vyska by remember { mutableStateOf("") }
    var vaha by remember { mutableStateOf("") }
    var bmi by remember { mutableStateOf<Double?>(null) }
    var chyba by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = vyska,
            onValueChange = { vyska = it },
            label = { Text(stringResource(R.string.bmi_vyska)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )
        OutlinedTextField(
            value = vaha,
            onValueChange = { vaha = it },
            label = { Text(stringResource(R.string.bmi_vaha)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )
        Button(onClick = {
            val vyska_edit = vyska.replace(',', '.').toDoubleOrNull()
            val vaha_edit = vaha.replace(',', '.').toDoubleOrNull()
            if (vyska_edit == null || vaha_edit == null || vyska_edit <= 0 || vaha_edit <= 0) {
                chyba = true
                bmi = null
            } else {
                chyba = false
                val vyska_edit_m = vyska_edit / 100
                bmi = vaha_edit / (vyska_edit_m * vyska_edit_m) // vaha(kg)/vyska(m)^2
            }
        }) {
            Text(stringResource(R.string.bmi_vypocitat))
        }
        if (chyba) {
            Text(
                text = stringResource(R.string.bmi_error),
                color = MaterialTheme.colorScheme.error
            )
        }
        bmi?.let { hodnota ->
            Text(stringResource(R.string.bmi_vysledek, hodnota))
        }
    }

}