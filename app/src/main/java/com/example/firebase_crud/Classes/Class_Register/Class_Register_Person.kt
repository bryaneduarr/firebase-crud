package com.example.firebase_crud.Classes.Class_Register

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase_crud.Classes.Class_Personas.Data_Class_Personas
import com.google.firebase.firestore.FirebaseFirestore

class Class_Register_Person(
    private val activity: AppCompatActivity,
) {
    // Firebase.
    private lateinit var fireStore: FirebaseFirestore;

    // Subir informacion a firebase firestore.
    fun registerPersonaFirebase(
        nombre: String, apellido: String, fechaNacimiento: String, correo: String, imageUrl: String
    ) {
        val persona = Data_Class_Personas(nombre, apellido, correo, fechaNacimiento, imageUrl);

        fireStore = FirebaseFirestore.getInstance();

        fireStore.collection("personas").add(persona).addOnSuccessListener { documentReference ->
            Toast.makeText(
                activity,
                "Registro satisfactorio, con ID: ${documentReference.id}.",
                Toast.LENGTH_SHORT
            ).show();

        }.addOnFailureListener { error ->
            Toast.makeText(
                activity, "Error al registrar la persona. ${error.message}", Toast.LENGTH_SHORT
            ).show();

            Log.e("Error", error.toString());
        }
    }
}