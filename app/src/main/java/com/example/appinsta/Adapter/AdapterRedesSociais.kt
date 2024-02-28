package com.example.appinsta.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.appinsta.R


import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appinsta.ClickListener.RecyclerViewClickListener
import com.example.appinsta.Model.ServiçoRedeSocial

class AdapterRedesSociais(private val context: Context, private val listaRedesSociais: MutableList<ServiçoRedeSocial>, private val clickListener: RecyclerViewClickListener) : RecyclerView.Adapter<AdapterRedesSociais.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rede_sociais,  parent, false )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaRedesSociais.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(listaRedesSociais[position].foto).into(holder.Foto)
        holder.Nome.text = listaRedesSociais[position].nome //

        holder.itemView.setOnClickListener {
            clickListener.onItemClicked(position)
        }


    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Foto : ImageView = itemView.findViewById(R.id.imgFotoItem)
        val Nome : TextView = itemView.findViewById(R.id.txtNomeItem)

    }
}