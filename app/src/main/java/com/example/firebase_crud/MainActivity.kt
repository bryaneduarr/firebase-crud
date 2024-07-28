package com.example.firebase_crud

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebase_crud.Classes.Class_Camera.Camera
import com.example.firebase_crud.Classes.Class_Camera.Camera_Helper
import com.example.firebase_crud.Classes.Class_Date_Picker.Class_Date_Picker_Helper
import com.example.firebase_crud.Classes.Class_Register.Class_Register_Person
import com.example.firebase_crud.Classes.Class_Validations.Class_Fields_Validation
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    // Campos de texto, imagen y fecha.
    private lateinit var apellidoTextInputEditText: TextInputEditText;
    private lateinit var nombreTextInputEditText: TextInputEditText;
    private lateinit var correoTextInputEditText: TextInputEditText;
    private lateinit var seleccionarFecha: Button;
    private lateinit var textViewFecha: TextView;
    private lateinit var imageView: ImageView;

    // Boton.
    private lateinit var guardarPersona: Button;
    private lateinit var verPersonas: Button;

    // Clases.
    private lateinit var classDatePickerHelper: Class_Date_Picker_Helper;
    private lateinit var classFieldsValidation: Class_Fields_Validation;
    private lateinit var classRegisterPerson: Class_Register_Person;
    private lateinit var classCameraHelper: Camera_Helper;
    private lateinit var classCamera: Camera;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Campos de texto, imagen y fecha.
        apellidoTextInputEditText = findViewById(R.id.apellidoTextInputEditText);
        nombreTextInputEditText = findViewById(R.id.nombreTextInputEditText);
        correoTextInputEditText = findViewById(R.id.correoTextInputEditText);
        seleccionarFecha = findViewById(R.id.seleccionarFecha);
        textViewFecha = findViewById(R.id.textViewFecha);
        imageView = findViewById(R.id.imageView);

        // Boton.
        guardarPersona = findViewById(R.id.guardarPersona);
        verPersonas = findViewById(R.id.verPersonas);

        // Clases.
        classDatePickerHelper = Class_Date_Picker_Helper(this, textViewFecha);
        classFieldsValidation = Class_Fields_Validation(this);
        classCameraHelper = Camera_Helper(this, imageView);
        classRegisterPerson = Class_Register_Person(this);
        classCamera = Camera(this);


        // Manejo de clicks.
        imageView.setOnClickListener {
            classCameraHelper.tomarFoto();
        }

        textViewFecha.setOnClickListener {
            classDatePickerHelper.datePicker();
        }

        seleccionarFecha.setOnClickListener {
            classDatePickerHelper.datePicker();
        }

        verPersonas.setOnClickListener {
            val intent = Intent(this, ActivityVerPersonas::class.java);
            startActivity(intent);
        }

        guardarPersona.setOnClickListener {
            val apellido = apellidoTextInputEditText.text.toString().trim();
            val nombre = nombreTextInputEditText.text.toString().trim();
            val correo = correoTextInputEditText.text.toString().trim();
            val fechaNacimiento = textViewFecha.text.toString().trim();

            if (classFieldsValidation.validateFields(nombre, apellido, fechaNacimiento, correo)) {
                classCamera.subirImageFirebaseStorage(onSuccess = { imageUrl ->
                    classRegisterPerson.registerPersonaFirebase(
                        nombre, apellido, fechaNacimiento, correo, imageUrl
                    );
                }, onFailure = { exception ->
                    Toast.makeText(
                        this, "Error al subir la imagen: ${exception.message}", Toast.LENGTH_SHORT
                    ).show()
                });
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