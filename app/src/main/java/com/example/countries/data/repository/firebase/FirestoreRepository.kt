package com.example.countries.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.countries.data.model.CountryModel
import com.example.countries.data.model.FirestoreModel
import com.example.countries.data.model.ResponseModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirestoreRepository @Inject constructor() {
    val db = Firebase.firestore

    fun saveAllToFireStore(context: Context, countries: ResponseModel) {

        countries.data.forEach {
            val id = it.code

            db.collection("all countries").document(id).set(it)
                .addOnSuccessListener {
                    Log.d("All countries firestore: ", "country saved")
                }
                .addOnFailureListener {
                    Log.d("All countries firestore: ", "country save failure")

                }
        }

    }

    fun saveToFireStore(context: Context, country: CountryModel) {

        db.collection("countries").document(country.code).set(country)
            .addOnSuccessListener {
                Toast.makeText(context, "Country saved", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failure to save", Toast.LENGTH_SHORT).show()

            }
    }

    fun changeFavState(code:String, isFav:Boolean) {
        db.collection("all countries").document(code).update("fav", isFav)
    }

    fun deleteFromFirestore(context: Context, code: String) {
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
        var collectionReference = db.collection("countries")
        return collectionReference

    }

    fun getAllCountriesFromCollection(): CollectionReference {
        var collectionReference = db.collection("all countries")
        return collectionReference

    }
}
