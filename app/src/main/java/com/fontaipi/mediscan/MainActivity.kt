package com.fontaipi.mediscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.rememberCoroutineScope
import com.fontaipi.mediscan.sync.SyncFromNetwork
import com.fontaipi.mediscan.ui.MediScanApp
import com.fontaipi.mediscan.ui.theme.MediScanTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var syncFromNetwork: SyncFromNetwork

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val coroutine = rememberCoroutineScope()
            MediScanTheme {
//                Button(onClick = { coroutine.launch { syncFromNetwork.sync() } }) {
//                    Text(text = "fdfs")
//                }
                MediScanApp(syncFromNetwork = { coroutine.launch { syncFromNetwork.sync() } })
            }
        }
    }
}
