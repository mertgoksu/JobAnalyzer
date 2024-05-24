package com.mertg.jobanalyzer.viewmodel


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainPageViewModel : ViewModel() {
    private val db = Firebase.firestore

    fun getItem(context: Context, collectionName: String, collectionPath: String, callback: (List<String>?) -> Unit) {
        // İstenen koleksiyonundaki belgeyi al
        val docRef = db.collection(collectionName).document(collectionPath)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Belge varsa ürün isimlerini al ve callback'i çağır
                    val itemNames = document.data?.values?.map { it.toString() }
                    callback(itemNames)
                } else {
                    Toast.makeText(context, "Belge bulunamadı", Toast.LENGTH_SHORT).show()
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Veri alınamadı: ${exception.message}", Toast.LENGTH_SHORT).show()
                callback(null)
            }
    }
}