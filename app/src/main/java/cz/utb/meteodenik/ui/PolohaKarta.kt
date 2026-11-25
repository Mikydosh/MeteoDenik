package cz.utb.meteodenik.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import cz.utb.meteodenik.R

private const val OPRAVNENI_POLOHA = Manifest.permission.ACCESS_COARSE_LOCATION

// Čtyři stavy místo Boolean: "ještě jsem se neptal" a "zamítnuto natrvalo" jsou různé situace.
enum class StavOpravneni { NEZNAMY, ODMITNUTO, ODMITNUTO_NATRVALO, UDELENO }

private fun maOpravneni(context: Context): Boolean =
    ContextCompat.checkSelfPermission(context, OPRAVNENI_POLOHA) ==
            PackageManager.PERMISSION_GRANTED

// Kontrola oprávnění PŘED každým čtením polohy; null = oprávnění chybí nebo poloha není známá.
private fun precistPolohu(context: Context): Location? {
    if (!maOpravneni(context)) return null
    return try {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val sitova = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        val fused = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            lm.getLastKnownLocation(LocationManager.FUSED_PROVIDER)
        } else null
        Log.d("Poloha", "network=$sitova fused=$fused")
        // novější z obou poloh (cache síťového poskytovatele může být zastaralá)
        listOfNotNull(sitova, fused).maxByOrNull { it.elapsedRealtimeNanos }
    } catch (e: SecurityException) {
        null
    }
}

@Composable
fun PolohaKarta(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = LocalActivity.current

    var stav by rememberSaveable {
        mutableStateOf(
            if (maOpravneni(context)) StavOpravneni.UDELENO else StavOpravneni.NEZNAMY
        )
    }
    var zobrazitVysvetleni by rememberSaveable { mutableStateOf(false) }
    var poloha by remember { mutableStateOf<Location?>(null) }

    // Spouštěč vzniká při složení obrazovky, nikdy v onClick.
    val spoustec = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { povoleno ->
        stav = when {
            povoleno -> StavOpravneni.UDELENO
            activity != null && ActivityCompat
                .shouldShowRequestPermissionRationale(activity, OPRAVNENI_POLOHA)
                -> StavOpravneni.ODMITNUTO
            else -> StavOpravneni.ODMITNUTO_NATRVALO
        }
        if (povoleno) poloha = precistPolohu(context)
    }

    // Návrat z Nastavení: callback spouštěče nepřijde, proto kontrola při každém ON_RESUME.
    val lifecycle = (activity as? LifecycleOwner)?.lifecycle
    DisposableEffect(lifecycle) {
        val pozorovatel = LifecycleEventObserver { _, udalost ->
            if (udalost == Lifecycle.Event.ON_RESUME) {
                if (maOpravneni(context)) {
                    stav = StavOpravneni.UDELENO
                    zobrazitVysvetleni = false
                    poloha = precistPolohu(context)
                } else if (stav == StavOpravneni.UDELENO) {
                    stav = StavOpravneni.NEZNAMY
                    poloha = null
                }
            }
        }
        lifecycle?.addObserver(pozorovatel)
        onDispose { lifecycle?.removeObserver(pozorovatel) }
    }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.poloha_titulek),
                style = MaterialTheme.typography.titleMedium
            )
            when (stav) {
                StavOpravneni.UDELENO -> {
                    Text(stringResource(R.string.poloha_povolena))
                    val p = poloha
                    Text(
                        if (p != null) {
                            stringResource(R.string.poloha_souradnice, p.latitude, p.longitude)
                        } else {
                            stringResource(R.string.poloha_neznama)
                        }
                    )
                }
                StavOpravneni.ODMITNUTO_NATRVALO -> {
                    Text(stringResource(R.string.poloha_bez_opravneni))
                    // Tlačítko do Nastavení jen v tomto stavu: je to jediná zbývající cesta.
                    Button(onClick = {
                        context.startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.packageName, null)
                            )
                        )
                    }) { Text(stringResource(R.string.otevrit_nastaveni)) }
                }
                StavOpravneni.NEZNAMY, StavOpravneni.ODMITNUTO ->
                    if (zobrazitVysvetleni) {
                        Text(stringResource(R.string.poloha_vysvetleni))
                        Button(onClick = {
                            zobrazitVysvetleni = false
                            spoustec.launch(OPRAVNENI_POLOHA)
                        }) { Text(stringResource(R.string.poloha_pokracovat)) }
                    } else {
                        Button(onClick = {
                            val potrebaVysvetlit = activity != null && ActivityCompat
                                .shouldShowRequestPermissionRationale(activity, OPRAVNENI_POLOHA)
                            if (potrebaVysvetlit) zobrazitVysvetleni = true
                            else spoustec.launch(OPRAVNENI_POLOHA)
                        }) { Text(stringResource(R.string.poloha_zjistit)) }
                    }
            }
        }
    }
}