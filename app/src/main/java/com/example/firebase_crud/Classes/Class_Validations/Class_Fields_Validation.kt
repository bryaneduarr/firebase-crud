package com.example.firebase_crud.Classes.Class_Validations

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Class_Fields_Validation(
    private val activity: AppCompatActivity,
) {
    fun validateFields(
        nombre: String, apellido: String, correo: String, fechaNacimiento: String
    ): Boolean {
        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || fechaNacimiento.isEmpty()) {
            Toast.makeText(activity, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT)
                .show();

            return false
        }
        return true
    }
}