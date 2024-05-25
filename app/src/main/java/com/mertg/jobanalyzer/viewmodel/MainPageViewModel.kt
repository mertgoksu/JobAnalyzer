package com.mertg.jobanalyzer.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mertg.jobanalyzer.MainActivity
import com.mertg.jobanalyzer.model.JobPackage
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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
    var isPaused by mutableStateOf(false)
    var stopButtonText by mutableStateOf("Durdur")

    private var totalWasteTimeMillis = 0L
    private var lastPauseStartTimeMillis = 0L
    private var startTimeMillis = 0L

    fun onStartClicked(context: Context) {
        isStartEnabled = false
        startTimeMillis = System.currentTimeMillis()
        updateStopEnabledState()
        isStopButtonClicked = false
        createAndSaveJobPackage(context)
    }

    private fun createAndSaveJobPackage(context: Context) {
        jobPackage = JobPackage(
            isemriKod = selectedIsEmriKod,
            islemMerkeziKod = selectedIslemMerkeziKod,
            yapilanIslem = selectedYapilanIslem,
            calisanNo = selectedCalisanNo,
            baslangicTarihi = Date(startTimeMillis) // Şu anki tarih ve saat
        )

        //saveJobPackageToFirebase(context, jobPackage!!)
    }

    private fun saveJobPackageToFirebase(context: Context, jobPackage: JobPackage) {
        viewModelScope.launch {
            try {
                db.collection("YapilanIsGecmisi").add(jobPackage).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Job Package başarıyla kaydedildi!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Job Package kaydedilirken hata oluştu: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun onStopReasonSelected(reason: String) {
        selectedStopReason = reason
        updateStopEnabledState()
    }

    fun onStopClicked() {
        if (isPaused) {
            val currentTimeMillis = System.currentTimeMillis()
            totalWasteTimeMillis += currentTimeMillis - lastPauseStartTimeMillis
            isPaused = false
            stopButtonText = "Durdur"
        } else {
            lastPauseStartTimeMillis = System.currentTimeMillis()
            isPaused = true
            stopButtonText = "Devam et"
            selectedStopReason = ""
        }
        updateStopEnabledState()
        updateTerminateEnabledState()
    }

    fun onTerminateClicked(context: Context) {
        val endTimeMillis = System.currentTimeMillis()
        if (isPaused) {
            totalWasteTimeMillis += endTimeMillis - lastPauseStartTimeMillis
        }
        val totalTimeWorkedMillis = endTimeMillis - startTimeMillis
        val workTimeMillis = totalTimeWorkedMillis - totalWasteTimeMillis

        val totalWasteMinutes = totalWasteTimeMillis / 60000
        val totalWasteSeconds = (totalWasteTimeMillis % 60000) / 1000
        val totalTimeWorkedMinutes = totalTimeWorkedMillis / 60000
        val totalTimeWorkedSeconds = (totalTimeWorkedMillis % 60000) / 1000
        val workTimeMinutes = workTimeMillis / 60000
        val workTimeSeconds = (workTimeMillis % 60000) / 1000

        jobPackage = jobPackage?.copy(
            durdurulmaSuresi = "$totalWasteMinutes dakika, $totalWasteSeconds saniye",
            gecirilenTumZaman = "$totalTimeWorkedMinutes dakika, $totalTimeWorkedSeconds saniye",
            calismaSuresi = "$workTimeMinutes dakika, $workTimeSeconds saniye",
            hataNedenleri = selectedHataNedenleri,
            fireSayisi = fireAmount,
            uretilenMiktar = uretilenAmount,
            bitisTarihi = Date(endTimeMillis) // İş emrinin sonlandırıldığı zaman
        )

        isStartEnabled = true
        isStopEnabled = false
        isTerminateEnabled = false
        selectedStopReason = ""

        selectedIsEmriKod = ""
        selectedIslemMerkeziKod = ""
        selectedYapilanIslem = ""
        selectedCalisanNo = ""
        selectedHataNedenleri = ""
        fireAmount = ""
        uretilenAmount = ""
        isPaused = false
        stopButtonText = "Durdur"

        saveJobPackageToFirebase(context, jobPackage!!)

        // Uygulamayı baştan başlatma
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        if (context is Activity) {
            context.finish()
        }
    }
    private fun updateStopEnabledState() {
        isStopEnabled = if (isPaused) true else selectedStopReason.isNotEmpty() && !isStartEnabled
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
        isTerminateEnabled = selectedIsEmriKod.isNotEmpty() &&
                selectedIslemMerkeziKod.isNotEmpty() &&
                selectedYapilanIslem.isNotEmpty() &&
                selectedCalisanNo.isNotEmpty() &&
                !isStartEnabled &&
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
