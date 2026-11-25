package cz.utb.meteodenik.ui.mesta

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.utb.meteodenik.R
import cz.utb.meteodenik.data.DuvodSelhani
import cz.utb.meteodenik.model.Mesto
import cz.utb.meteodenik.model.MeteoZaznam
import cz.utb.meteodenik.model.StavPocasi
import cz.utb.meteodenik.ui.theme.MeteoDenikTheme
import java.time.LocalDateTime

@Composable
fun SeznamMestObsah(
    stav: MestaUiState,
    onZkusitZnovu: () -> Unit,
    onMestoKliknuto: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        if (stav.nacitaSe) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        val duvod = stav.chyba
        if (duvod != null) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(duvod.textProUzivatele()),
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = onZkusitZnovu) {
                    Text(stringResource(R.string.akce_zkusit_znovu))
                }
            }
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(stav.mesta, key = { it.mesto.id }) { radek ->
                MestoKarta(
                    radek = radek,
                    vybrane = radek.mesto.id == stav.vybraneMestoId,
                    modifier = Modifier.clickable { onMestoKliknuto(radek.mesto.id) }
                )
            }
        }
    }
}

private val vzorek = listOf(
    MestoSPocasim(
        mesto = Mesto(id = 1, nazev = "Zlín", sirka = 49.2265, delka = 17.6683),
        pocasi = MeteoZaznam(
            cas = LocalDateTime.of(2026, 11, 14, 12, 0),
            teplotaC = 8.4,
            vlhkostProcent = 71,
            rychlostVetruKmH = 12.5,
            stav = StavPocasi.POLOJASNO
        )
    ),
    MestoSPocasim(mesto = Mesto(id = 2, nazev = "Brno", sirka = 49.1951, delka = 16.6068))
)

@Preview(name = "Načítání", showBackground = true)
@Composable
private fun NacitaniPreview() {
    MeteoDenikTheme {
        SeznamMestObsah(
            stav = MestaUiState(mesta = vzorek, nacitaSe = true),
            onZkusitZnovu = {},
            onMestoKliknuto = {}
        )
    }
}

@Preview(name = "Hotovo", showBackground = true)
@Composable
private fun HotovoPreview() {
    MeteoDenikTheme {
        SeznamMestObsah(
            stav = MestaUiState(mesta = vzorek),
            onZkusitZnovu = {},
            onMestoKliknuto = {}
        )
    }
}

@Preview(name = "Chyba", showBackground = true)
@Composable
private fun ChybaPreview() {
    MeteoDenikTheme {
        SeznamMestObsah(
            stav = MestaUiState(mesta = vzorek, chyba = DuvodSelhani.BEZ_PRIPOJENI),
            onZkusitZnovu = {},
            onMestoKliknuto = {}
        )
    }
}