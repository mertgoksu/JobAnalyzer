package com.mertg.jobanalyzer.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mertg.jobanalyzer.model.JobPackage
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date

class MainPageViewModel : ViewModel() {
    private val db = Firebase.firestore

    var isStartEnabled by mutableStateOf(true)
    var isStopEnabled by mutableStateOf(false)
    var isTerminateEnabled by mutableStateOf(false)
    var selectedStopReason by mutableStateOf("")

    var selectedIsEmriKod by mutableStateOf("")
    var selectedIslemMerkeziKod by mutableStateOf("")
    var selectedYapilanIslem by mutableStateOf("")
    var selectedCalisanNo by mutableStateOf("")

    var selectedHataNedenleri by mutableStateOf("")

    var fireAmount by mutableStateOf("")
    var uretilenAmount by mutableStateOf("")

    var jobPackage: JobPackage? by mutableStateOf(null)

    var isStopButtonClicked by mutableStateOf(false)

    fun onStartClicked() {
        isStartEnabled = false
        updateStopEnabledState()
        isTerminateEnabled = false
        isStopButtonClicked = false
        createJobPackage()
    }

    private fun createJobPackage() {
        jobPackage = JobPackage(
            isEmriKod = selectedIsEmriKod,
            islemMerkeziKod = selectedIslemMerkeziKod,
            yapilanIslem = selectedYapilanIslem,
            calisanNo = selectedCalisanNo,
            startTime = Date() // Şu anki tarih ve saat
        )
    }

    fun onStopReasonSelected(reason: String) {
        selectedStopReason = reason
        updateStopEnabledState()
    }

    fun onStopClicked() {
        isStopEnabled = false
        isTerminateEnabled = true
        isStopButtonClicked = true
    }

    fun onTerminateClicked() {
        isStartEnabled = true
        isStopEnabled = false
        isTerminateEnabled = false
        selectedStopReason = ""

        selectedIsEmriKod = ""
        selectedIslemMerkeziKod = ""
        selectedYapilanIslem = ""
        selectedCalisanNo = ""
        fireAmount = ""
        uretilenAmount = ""
    }

    private fun updateStopEnabledState() {
        isStopEnabled = selectedStopReason.isNotEmpty() && !isStartEnabled
    }

    fun onHataNedeniSelected(reason: String) {
        selectedHataNedenleri = reason
        updateTerminateEnabledState()
    }

    fun onFireAmountChanged(amount: String) {
        fireAmount = amount
        updateTerminateEnabledState()
    }

    fun onUretilenAmountChanged(amount: String) {
        uretilenAmount = amount
        updateTerminateEnabledState()
    }

    private fun updateTerminateEnabledState() {
        isTerminateEnabled = isStopButtonClicked &&
                selectedHataNedenleri.isNotEmpty() &&
                fireAmount.isNotEmpty() &&
                uretilenAmount.isNotEmpty()
    }

    fun getItemAsync(context: Context, collectionName: String, collectionPath: String): Deferred<List<String>?> = viewModelScope.async {
        val docRef = db.collection(collectionName).document(collectionPath)
        try {
            val document = docRef.get().await()
            if (document.exists()) {
                document.data?.values?.map { it.toString() }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Belge bulunamadı", Toast.LENGTH_SHORT).show()
                }
                null
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Veri alınamadı: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }
}
