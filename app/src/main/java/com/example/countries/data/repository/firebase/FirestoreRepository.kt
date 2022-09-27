package com.example.countries.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.countries.data.model.CountryModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirestoreRepository @Inject constructor() {

    fun saveToFireStore(context: Context, country: CountryModel, code: String) {
        val db = Firebase.firestore
        val countryItem: MutableMap<String, Any> = HashMap()
        countryItem["code"] = country.code
        countryItem["name"] = country.name

        db.collection("countries").document(code).set(countryItem)
            .addOnSuccessListener {
                Toast.makeText(context, "Country saved", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failure to save", Toast.LENGTH_SHORT).show()

            }
    }

    fun deleteFromFirestore(context: Context, code: String) {

        val db = Firebase.firestore
        db.collection("countries").document(code)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Country unsaved", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Country couldn't unsaved", Toast.LENGTH_SHORT)
                    .show()
            }
    }


    fun getFavouriteCountriesFromCollection(): CollectionReference {
        val db = Firebase.firestore
        var collectionReference = db.collection("countries")
        return collectionReference

    }
}
