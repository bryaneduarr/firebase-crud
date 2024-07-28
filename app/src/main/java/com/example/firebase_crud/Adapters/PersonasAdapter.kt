package com.example.firebase_crud.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.firebase_crud.Classes.Class_Personas.Data_Class_Get_Personas
import com.example.firebase_crud.R

class PersonasAdapter(
    private val context: Context, private val personasList: List<Data_Class_Get_Personas>
) : BaseAdapter() {
    override fun getCount(): Int {
        return personasList.size;
    }

    override fun getItem(position: Int): Any {
        return personasList[position];
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_persona, parent, false);

        val persona = getItem(position) as Data_Class_Get_Personas;

        val nombreTextView: TextView = view.findViewById(R.id.nombreTextView);
        val imageView: ImageView = view.findViewById(R.id.imageView);

        nombreTextView.text = persona.nombre;
        Glide.with(context).load(persona.imageUrl).into(imageView);

        return view;
    }
}