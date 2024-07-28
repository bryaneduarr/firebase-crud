package com.example.firebase_crud

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebase_crud.Classes.Class_Get_Personas.Class_Get_Personas
import com.example.firebase_crud.Classes.Class_Personas.Data_Class_Get_Personas

class ActivityVerPersonas : AppCompatActivity() {
    private val personasList = mutableListOf<Data_Class_Get_Personas>();

    private lateinit var classGetPersonas: Class_Get_Personas;

    private lateinit var regresarButton: ImageButton;

    private lateinit var listView: ListView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_personas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        listView = findViewById(R.id.listView);

        regresarButton = findViewById(R.id.regresarButton);

        classGetPersonas = Class_Get_Personas(this, listView, personasList);

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, id ->
            val selectedPerson = personasList[position];

            val intent = Intent(this, ActivityActualizarPersona::class.java);

            intent.putExtra("PERSONA_ID", selectedPerson.id);

            startActivity(intent);
        }

        regresarButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent);
        }

        classGetPersonas.getPersonas();
    }
}