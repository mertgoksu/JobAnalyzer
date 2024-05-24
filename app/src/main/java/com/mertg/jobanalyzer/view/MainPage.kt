package com.mertg.jobanalyzer.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mertg.jobanalyzer.util.DropdownField
import com.mertg.jobanalyzer.viewmodel.MainPageViewModel

@Composable
fun MainPage(innerPadding : PaddingValues) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(16.dp)) {
        val mainPageViewModel: MainPageViewModel = viewModel()

        val context = LocalContext.current

        var calisanNoList: List<String> by remember { mutableStateOf(listOf()) }
        var selectedCalisanNo by remember { mutableStateOf("") }
        mainPageViewModel.getItem(context, "CalisanNo", "IHqXsJsWW9JluauX9Bnm") { names ->
            if (names != null) {
                calisanNoList = names
            }
        }

        var isEmriKodList : List<String> by remember { mutableStateOf(listOf()) }
        var selectedIsEmriKod by remember { mutableStateOf("") }
        mainPageViewModel.getItem(context, "IsEmriKod", "diiDGUUZr4UU5VLwRks3") { names ->
            if (names != null) {
                isEmriKodList = names
            }
        }

        var islemMerkeziKodList : List<String> by remember { mutableStateOf(listOf()) }
        var selectedIslemMerkeziKod by remember { mutableStateOf("") }
        mainPageViewModel.getItem(context, "IslemMerkeziKod", "4t1NFCNOJwltlkgJnxiy"){ names ->
            if (names != null) {
                islemMerkeziKodList = names
            }
        }


        /*LaunchedEffect(Unit) {
            mainPageViewModel.getItem(context, "CalisanNo", "IHqXsJsWW9JluauX9Bnm") { names ->
                calisanNoList = names ?: listOf()  // Eğer null ise boş liste atayarak güvenli bir şekilde devam edilir
            }
        }*/

        Column {
            Row( // Header
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("sa", fontSize = 34.sp)
            }

            Row ( // Is Emri Kod
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                if (isEmriKodList.isNotEmpty()) {
                    DropdownField(
                        label = "İş Emri Kod:",
                        selectedValue = selectedIsEmriKod,
                        options = isEmriKodList,
                        onValueChange = { selectedIsEmriKod = "İş Emri Kod: $it" }
                    )
                } else {
                    Text("İş Emri listesi yükleniyor veya boş...")
                }
            }

            Row ( // Islem Merkezi Kod
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                if (islemMerkeziKodList.isNotEmpty()) {
                    DropdownField(
                        label = "İşlem Merkezi Kod:",
                        selectedValue = selectedIslemMerkeziKod,
                        options = calisanNoList,
                        onValueChange = { selectedIslemMerkeziKod = "İşlem Merkezi Kod: $it" }
                    )
                } else {
                    Text("İşlem Merkezi listesi yükleniyor veya boş...")
                }
            }

            Row ( // Yapilan Islem
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){

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
                    Text("Çalışan listesi yükleniyor veya boş...")
                }
            }
            
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){

            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){

            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){

            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){

            }


        }
    }
}

@Preview
@Composable
private fun MainPagePrev() {
    MainPage(PaddingValues(1.dp))
}