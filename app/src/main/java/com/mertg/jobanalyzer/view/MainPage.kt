package com.mertg.jobanalyzer.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mertg.jobanalyzer.util.DropdownField
import com.mertg.jobanalyzer.viewmodel.MainPageViewModel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

@Composable
fun MainPage(innerPadding : PaddingValues) {

    val mainPageViewModel: MainPageViewModel = viewModel()

    val context = LocalContext.current

    var isEmriKodList by remember { mutableStateOf(listOf<String>()) }
    var selectedIsEmriKod by remember { mutableStateOf("") }
    var islemMerkeziKodList by remember { mutableStateOf(listOf<String>()) }
    var selectedIslemMerkeziKod by remember { mutableStateOf("") }
    var yapilanIslemList by remember { mutableStateOf(listOf<String>()) }
    var selectedYapilanIslem by remember { mutableStateOf("") }
    var calisanNoList by remember { mutableStateOf(listOf<String>()) }
    var selectedCalisanNo by remember { mutableStateOf("") }
    var durusNedenleriList by remember { mutableStateOf(listOf<String>()) }
    var selectedDurusNedenleri by remember { mutableStateOf("") }
    var hataNedenleriList by remember { mutableStateOf(listOf<String>()) }
    var selectedHataNedenleri by remember { mutableStateOf("") }
    var fireAmount by remember { mutableStateOf("") }
    var uretilenAmount by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(true) }
    var isDataLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        val jobs = listOf(
            launch {
                mainPageViewModel.getItem(context, "IsEmriKod", "diiDGUUZr4UU5VLwRks3") { names ->
                    isEmriKodList = names ?: listOf()
                }
            },
            launch {
                mainPageViewModel.getItem(context, "IslemMerkeziKod", "4t1NFCNOJwltlkgJnxiy") { names ->
                    islemMerkeziKodList = names ?: listOf()
                }
            },
            launch {
                mainPageViewModel.getItem(context, "YapilanIslem", "4s20cIquhfhJZw0Tw5wo") { names ->
                    yapilanIslemList = names ?: listOf()
                }
            },
            launch {
                mainPageViewModel.getItem(context, "CalisanNo", "IHqXsJsWW9JluauX9Bnm") { names ->
                    calisanNoList = names ?: listOf()
                }
            },
            launch {
                mainPageViewModel.getItem(context, "DurusNedenleri", "HCp0Kd9bGRR4LiDJHdkz") { names ->
                    durusNedenleriList = names ?: listOf()
                }
            },
            launch {
                mainPageViewModel.getItem(context, "HataNedenleri", "jsHBoCeXySHurZnIQTcM") { names ->
                    hataNedenleriList = names ?: listOf()
                }
            }
        )
        jobs.joinAll()
        isLoading = false
        isDataLoaded = true
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Yükleniyor...")
        }
    } else {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(12.dp)
            .padding(bottom = 4.dp)
        ) {
            Row( // Header
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("sa", fontSize = 34.sp)
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
                        selectedValue = selectedIsEmriKod,
                        options = isEmriKodList,
                        onValueChange = { selectedIsEmriKod = "İş Emri Kod: \n$it" }
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
                        selectedValue = selectedIslemMerkeziKod,
                        options = calisanNoList,
                        onValueChange = { selectedIslemMerkeziKod = "İşlem Merkezi Kod: \n$it" }
                    )
                } else {
                    Text("İşlem Merkezi listesi yükleniyor")
                }
            }


            /* Row(
                 modifier = Modifier
                     .fillMaxWidth()
                     .weight(1f),
                 verticalAlignment = Alignment.CenterVertically,
                 horizontalArrangement = Arrangement.Center
             ) {
                 Column( // Is Emri Kod
                     modifier = Modifier.weight(1f),
                     verticalArrangement = Arrangement.Center,
                     horizontalAlignment = Alignment.CenterHorizontally
                 ) {
                     if (isEmriKodList.isNotEmpty()) {
                         DropdownField(
                             label = "İş Emri Kod:",
                             selectedValue = selectedIsEmriKod,
                             options = isEmriKodList,
                             onValueChange = { selectedIsEmriKod = "İş Emri Kod: \n$it" }
                         )
                     } else {
                         Text("İş Emri listesi yükleniyor")
                     }
                 }
                 Column( // Islem Merkezi Kod
                     modifier = Modifier.weight(1f),
                     verticalArrangement = Arrangement.Center,
                     horizontalAlignment = Alignment.CenterHorizontally
                 ) {
                     if (islemMerkeziKodList.isNotEmpty()) {
                         DropdownField(
                             label = "İşlem Merkezi Kod:",
                             selectedValue = selectedIslemMerkeziKod,
                             options = calisanNoList,
                             onValueChange = { selectedIslemMerkeziKod = "İşlem Merkezi Kod: \n$it" }
                         )
                     } else {
                         Text("İşlem Merkezi listesi yükleniyor")
                     }
                 }
             }*/


            Row ( // Yapilan Islem
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                if (yapilanIslemList.isNotEmpty()) {
                    DropdownField(
                        label = "Yapılan İşlem:",
                        selectedValue = selectedYapilanIslem,
                        options = yapilanIslemList,
                        onValueChange = { selectedYapilanIslem = "Yapılan İşlem: $it" }
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
                        selectedValue = selectedCalisanNo,
                        options = calisanNoList,
                        onValueChange = { selectedCalisanNo = "Çalışan No: $it" }
                    )
                } else {
                    Text("Çalışan listesi yükleniyor")
                }
            }

            Row ( // Baslat button
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = "Başlat", fontSize = 16.sp)
                }
            }

            Row ( // Durus Nedeni Row
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column( // Durus Nedenleri Dropdown
                        modifier = Modifier.weight(7f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (durusNedenleriList.isNotEmpty()) {
                            DropdownField(
                                label = "Duruş Nedeni:",
                                selectedValue = selectedDurusNedenleri,
                                options = durusNedenleriList,
                                onValueChange = { selectedDurusNedenleri = "Duruş Nedeni: $it" }
                            )
                        } else {
                            Text("Duruş Nedeni listesi yükleniyor")
                        }
                    }
                    Column( // Durdur Button
                        modifier = Modifier.weight(3f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { /*TODO*/ }
                        ) {
                            Text(text = "Durdur", fontSize = 16.sp)
                        }
                    }
                }
            }

            Row ( // Hata Nedeni Row
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier.weight(7f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if(hataNedenleriList.isNotEmpty()){
                            DropdownField(
                                label = "Hata Nedeni:",
                                selectedValue = selectedHataNedenleri,
                                options = hataNedenleriList,
                                onValueChange = { selectedHataNedenleri = "Hata Nedeni: $it" }
                            )
                        } else {
                            Text("Hata Nedeni listesi yükleniyor")
                        }
                    }


                    Column(
                        modifier = Modifier.weight(3f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier.weight(3f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            OutlinedTextField(
                                value = fireAmount,
                                onValueChange = { fireAmount = it },
                                label = { Text("Fire") },
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                            )
                        }
                    }
                }
            }

            Row ( // Uretilen Miktar
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                            value = uretilenAmount,
                            onValueChange = { uretilenAmount = it },
                            label = { Text("Miktar") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            }

            Row( // Is Emri Sonlandir Button
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /*TODO*/ }) {
                    Text(text = "İş Emri Sonlandır", fontSize = 16.sp)
                }
            }
        }
    }
}
