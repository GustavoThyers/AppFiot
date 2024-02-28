package com.example.appinsta.Adapter

import android.view.LayoutInflater
import android.content.Context

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.appinsta.R
import androidx.recyclerview.widget.RecyclerView
import com.example.appinsta.ADM.ActivityUsuarios
import com.example.appinsta.Model.Usuarios
import org.w3c.dom.Text

class AdapterUsuarios(private val context: Context, private val listUsuarios: List<Usuarios>) : RecyclerView.Adapter<AdapterUsuarios.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_usuarios, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listUsuarios.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.nome.text = listUsuarios[position].nome
        holder.email.text = listUsuarios[position].email
        holder.saldo.text = listUsuarios[position].saldo.toString()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome : TextView = itemView.findViewById(R.id.textNomeUsuario)
        val email : TextView = itemView.findViewById(R.id.textEmailUsuario)
        val saldo : TextView = itemView.findViewById(R.id.textSaldoUsuario)
    }
}