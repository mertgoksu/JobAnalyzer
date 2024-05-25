package com.mertg.jobanalyzer.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mertg.jobanalyzer.util.DropdownField
import com.mertg.jobanalyzer.viewmodel.MainPageViewModel

@Composable
fun MainPage(innerPadding: PaddingValues) {
    val mainPageViewModel: MainPageViewModel = viewModel()
    val context = LocalContext.current

    var isEmriKodList by remember { mutableStateOf(listOf<String>()) }
    var islemMerkeziKodList by remember { mutableStateOf(listOf<String>()) }
    var yapilanIslemList by remember { mutableStateOf(listOf<String>()) }
    var calisanNoList by remember { mutableStateOf(listOf<String>()) }
    var durusNedenleriList by remember { mutableStateOf(listOf<String>()) }
    var hataNedenleriList by remember { mutableStateOf(listOf<String>()) }

    var isLoading by remember { mutableStateOf(true) }
    var isDataLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val isEmriKodListDeferred = mainPageViewModel.getItemAsync(context, "IsEmriKod", "diiDGUUZr4UU5VLwRks3")
            val islemMerkeziKodListDeferred = mainPageViewModel.getItemAsync(context, "IslemMerkeziKod", "4t1NFCNOJwltlkgJnxiy")
            val yapilanIslemListDeferred = mainPageViewModel.getItemAsync(context, "YapilanIslem", "4s20cIquhfhJZw0Tw5wo")
            val calisanNoListDeferred = mainPageViewModel.getItemAsync(context, "CalisanNo", "IHqXsJsWW9JluauX9Bnm")
            val durusNedenleriListDeferred = mainPageViewModel.getItemAsync(context, "DurusNedenleri", "HCp0Kd9bGRR4LiDJHdkz")
            val hataNedenleriListDeferred = mainPageViewModel.getItemAsync(context, "HataNedenleri", "jsHBoCeXySHurZnIQTcM")

            isEmriKodList = isEmriKodListDeferred.await() ?: listOf()
            islemMerkeziKodList = islemMerkeziKodListDeferred.await() ?: listOf()
            yapilanIslemList = yapilanIslemListDeferred.await() ?: listOf()
            calisanNoList = calisanNoListDeferred.await() ?: listOf()
            durusNedenleriList = durusNedenleriListDeferred.await() ?: listOf()
            hataNedenleriList = hataNedenleriListDeferred.await() ?: listOf()

            isLoading = false
            isDataLoaded = true
        } catch (e: Exception) {
            isLoading = false
            Toast.makeText(context, "Veriler Alınamadı...", Toast.LENGTH_SHORT).show()
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Yükleniyor...")
        }
    } else {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(12.dp)) {

            Row( // Header
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("İş Analizi", fontSize = 34.sp)
            }

            Row( // Is Emri Kod
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isEmriKodList.isNotEmpty()) {
                    DropdownField(
                        label = "İş Emri Kod:",
                        selectedValue = mainPageViewModel.selectedIsEmriKod,
                        options = isEmriKodList,
                        onValueChange = { mainPageViewModel.selectedIsEmriKod = "İş Emri Kod: $it" }
                    )
                } else {
                    Text("İş Emri listesi yükleniyor")
                }
            }

            Row( // Islem Merkezi Kod
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (islemMerkeziKodList.isNotEmpty()) {
                    DropdownField(
                        label = "İşlem Merkezi Kod:",
                        selectedValue = mainPageViewModel.selectedIslemMerkeziKod,
                        options = islemMerkeziKodList,
                        onValueChange = { mainPageViewModel.selectedIslemMerkeziKod ="İşlem Merkezi Kod: $it" }
                    )
                } else {
                    Text("İşlem Merkezi listesi yükleniyor")
                }
            }

            Row( // Yapilan Islem
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (yapilanIslemList.isNotEmpty()) {
                    DropdownField(
                        label = "Yapılan İşlem:",
                        selectedValue = mainPageViewModel.selectedYapilanIslem,
                        options = yapilanIslemList,
                        onValueChange = { mainPageViewModel.selectedYapilanIslem = "Yapılan İşlem: $it" }
                    )
                } else {
                    Text("Yapılan İşlem listesi yükleniyor")
                }
            }

            Row( // Calisan No
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (calisanNoList.isNotEmpty()) {
                    DropdownField(
                        label = "Çalışan No:",
                        selectedValue = mainPageViewModel.selectedCalisanNo,
                        options = calisanNoList,
                        onValueChange = { mainPageViewModel.selectedCalisanNo = "Çalışan No: $it" }
                    )
                } else {
                    Text("Çalışan listesi yükleniyor")
                }
            }

            Row( // Baslat Button
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        mainPageViewModel.onStartClicked(context)
                    },
                    enabled = mainPageViewModel.isStartEnabled
                            && mainPageViewModel.selectedIsEmriKod.isNotEmpty()
                            && mainPageViewModel.selectedIslemMerkeziKod.isNotEmpty()
                            && mainPageViewModel.selectedYapilanIslem.isNotEmpty()
                            && mainPageViewModel.selectedCalisanNo.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Başlat", fontSize = 16.sp)
                }
            }

            Row( // Durus Nedeni
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column( // Durus Nedeni Dropdown
                    modifier = Modifier.weight(7f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (durusNedenleriList.isNotEmpty()) {
                        DropdownField(
                            label = "Duruş Nedeni:",
                            selectedValue = mainPageViewModel.selectedStopReason,
                            options = durusNedenleriList,
                            onValueChange = {
                                mainPageViewModel.onStopReasonSelected("Duruş Nedeni: $it")
                            }
                        )
                    } else {
                        Text("Duruş Nedeni listesi yükleniyor")
                    }
                }
                Column( // Durus Nedeni DurdurButton
                    modifier = Modifier.weight(3f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            mainPageViewModel.onStopClicked()
                        },
                        enabled = mainPageViewModel.isStopEnabled,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = mainPageViewModel.stopButtonText, fontSize = 16.sp)
                    }
                }
            }

            Row( // Hata Nedeni
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column( // Hata Nedeni Dropdown
                    modifier = Modifier.weight(7f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (hataNedenleriList.isNotEmpty()) {
                        DropdownField(
                            label = "Hata Nedeni:",
                            selectedValue = mainPageViewModel.selectedHataNedenleri,
                            options = hataNedenleriList,
                            onValueChange = { mainPageViewModel.onHataNedeniSelected("Hata Nedeni: $it") }
                        )
                    } else {
                        Text("Hata Nedeni listesi yükleniyor")
                    }
                }
                Column( // Hata Nedeni Fire textfield
                    modifier = Modifier.weight(3f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = mainPageViewModel.fireAmount,
                        onValueChange = { mainPageViewModel.onFireAmountChanged(it) },
                        label = { Text("Fire") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                }
            }

            Row( // Uretilen Miktar
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.weight(7f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Üretilen Miktar")
                }
                Column(
                    modifier = Modifier.weight(3f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = mainPageViewModel.uretilenAmount,
                        onValueChange = { mainPageViewModel.onUretilenAmountChanged(it) },
                        label = { Text("Miktar") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        mainPageViewModel.onTerminateClicked(context)
                    },
                    enabled = !mainPageViewModel.isStartEnabled
                            && mainPageViewModel.isTerminateEnabled,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "İş Emri Sonlandır", fontSize = 16.sp)
                }
            }
        }
    }
}
