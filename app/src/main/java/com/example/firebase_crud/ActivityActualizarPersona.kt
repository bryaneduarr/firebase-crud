package com.example.firebase_crud

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.firebase_crud.Classes.Class_Camera.Camera
import com.example.firebase_crud.Classes.Class_Camera.Camera_Helper
import com.example.firebase_crud.Classes.Class_Date_Picker.Class_Date_Picker_Helper
import com.example.firebase_crud.Classes.Class_Personas.Data_Class_Get_Personas
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class ActivityActualizarPersona : AppCompatActivity() {
    private lateinit var imageView: ImageView;

    private lateinit var nombreEditText: TextInputEditText;
    private lateinit var apellidoEditText: TextInputEditText;
    private lateinit var correoEditText: TextInputEditText;
    private lateinit var seleccionarFecha: Button;
    private lateinit var textViewFecha: TextView;

    private lateinit var regresarButton: ImageButton;
    private lateinit var actualizarButton: Button;
    private lateinit var eliminarButton: Button;

    private lateinit var classDatePickerHelper: Class_Date_Picker_Helper;
    private lateinit var classCameraHelper: Camera_Helper;
    private lateinit var classCamera: Camera;


    private var personaId: String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_persona)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        imageView = findViewById(R.id.imageView);

        nombreEditText = findViewById(R.id.nombreTextInputEditText);
        apellidoEditText = findViewById(R.id.apellidoTextInputEditText);
        correoEditText = findViewById(R.id.correoTextInputEditText);
        textViewFecha = findViewById(R.id.textViewFecha);
        seleccionarFecha = findViewById(R.id.seleccionarFecha);

        actualizarButton = findViewById(R.id.actualizarButton);
        eliminarButton = findViewById(R.id.eliminarButton);
        regresarButton = findViewById(R.id.regresarButton);

        classDatePickerHelper = Class_Date_Picker_Helper(this, textViewFecha);
        classCameraHelper = Camera_Helper(this, imageView);
        classCamera = Camera(this);

        imageView.setOnClickListener {
            classCameraHelper.tomarFoto();
        }





        seleccionarFecha.setOnClickListener {
            classDatePickerHelper.datePicker();
        }

        regresarButton.setOnClickListener {
            val intent = Intent(this, ActivityVerPersonas::class.java);
            startActivity(intent);
        }

        actualizarButton.setOnClickListener {
            personaId?.let {
                actualizarDatosPersona(it)
            }
        }

        personaId = intent.getStringExtra("PERSONA_ID");

        personaId?.let {
            cargarDatosPersona(it)
        }

        eliminarButton.setOnClickListener {
            personaId?.let { id ->
                eliminarPersona(id)
            }
        }
    }

    fun cargarDatosPersona(id: String) {
        val db = FirebaseFirestore.getInstance();

        db.collection("personas").document(id).get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val persona = document.toObject(Data_Class_Get_Personas::class.java);

                persona?.let {
                    nombreEditText.setText(it.nombre)
                    apellidoEditText.setText(document.getString("apellido"))
                    correoEditText.setText(document.getString("correo"))
                    textViewFecha.text = document.getString("fechaNacimiento")
                    Glide.with(this).load(it.imageUrl).into(imageView)
                }
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(
                this, "Error al cargar a la persona. ${exception.message}", Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun eliminarPersona(id: String) {
        val db = FirebaseFirestore.getInstance();

        db.collection("personas").document(id).delete().addOnSuccessListener {
            Toast.makeText(this, "Persona eliminada.", Toast.LENGTH_SHORT).show();
            finish();
        }.addOnFailureListener { exception ->
            Toast.makeText(
                this,
                "Error al eliminar persona. ${exception.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun actualizarDatosPersona(id: String) {
        val db = FirebaseFirestore.getInstance();
        val storage = FirebaseStorage.getInstance();
        val storageReference = storage.reference.child("ImagenesDePersonas/${UUID.randomUUID()}");

        classCamera.getPhotoUri()?.let { uri: Uri ->
            storageReference.putFile(uri).addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString();

                    val updatedPersona = hashMapOf<String, Any>(
                        "nombre" to nombreEditText.text.toString(),
                        "apellido" to apellidoEditText.text.toString(),
                        "correo" to correoEditText.text.toString(),
                        "fechaNacimiento" to textViewFecha.text.toString(),
                        "imageUrl" to imageUrl
                    );

                    db.collection("personas").document(id).update(updatedPersona)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Persona actualizada.", Toast.LENGTH_SHORT).show();
                            finish();
                        }.addOnFailureListener { exception ->
                            Toast.makeText(
                                this,
                                "Error al actualizar persona. ${exception.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    this, "Error al subir la imagen. ${exception.message}", Toast.LENGTH_LONG
                ).show()
            }
        } ?: run {
            val updatedPersona = hashMapOf<String, Any>(
                "nombre" to nombreEditText.text.toString(),
                "apellido" to apellidoEditText.text.toString(),
                "correo" to correoEditText.text.toString(),
                "fechaNacimiento" to textViewFecha.text.toString()
            );

            db.collection("personas").document(id).update(updatedPersona).addOnSuccessListener {
                Toast.makeText(this, "Persona actualizada.", Toast.LENGTH_SHORT).show();
                finish();
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    this, "Error al actualizar persona. ${exception.message}", Toast.LENGTH_LONG
                ).show()
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        classCamera.onRequestPermissionsResult(requestCode, grantResults, {
            val takePictureIntent = classCamera.dispatchTakePictureIntent();

            if (takePictureIntent != null) {
                classCameraHelper.tomarFotoVal.launch(takePictureIntent);
            }
        }, {
            Toast.makeText(this, "Acceso Denegado!", Toast.LENGTH_LONG).show();
        });
    }
}