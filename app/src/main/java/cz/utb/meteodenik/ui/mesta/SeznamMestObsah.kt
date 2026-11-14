package cz.utb.meteodenik.ui.mesta

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SeznamMestObsah(
    stav: MestaUiState,
    onMestoKliknuto: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
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