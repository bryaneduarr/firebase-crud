package com.example.firebase_crud.Classes.Class_Get_Personas

import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase_crud.Adapters.PersonasAdapter
import com.example.firebase_crud.Classes.Class_Personas.Data_Class_Get_Personas
import com.google.firebase.firestore.FirebaseFirestore

class Class_Get_Personas(
    private val activity: AppCompatActivity,
    private val listView: ListView,
    private var personasList: MutableList<Data_Class_Get_Personas>
) {
    private val db = FirebaseFirestore.getInstance();

    private lateinit var adapter: PersonasAdapter;

    fun getPersonas() {
        adapter = PersonasAdapter(activity, personasList);

        listView.adapter = adapter;


        db.collection("personas").get().addOnSuccessListener { result ->
            personasList.clear();

            for (document in result) {
                val persona = document.toObject(Data_Class_Get_Personas::class.java).apply {
                    id = document.id;
                };

                personasList.add(persona);
            }
            adapter.notifyDataSetChanged();
        }.addOnFailureListener { exception ->
            Toast.makeText(
                activity, "Error al cargar datos. ${exception.message}", Toast.LENGTH_LONG
            ).show();
        }
    }
}