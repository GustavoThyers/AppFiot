package com.example.appinsta.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appinsta.ClickListener.RecyclerViewClickListener

import com.example.appinsta.Model.ServiçosInstagram
import com.example.appinsta.Model.ServiçosTikTok
import com.example.appinsta.R

class AdapterServiçosInstagram(private val context: Context, private val listServicosInstagram: MutableList<ServiçosInstagram>, private val clickListener: RecyclerViewClickListener) : RecyclerView.Adapter<AdapterServiçosInstagram.MyViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_servicos_instagram, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listServicosInstagram.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(listServicosInstagram[position].Foto).into(holder.Foto)
        holder.Nome.text = listServicosInstagram[position].Nome

        holder.itemView.setOnClickListener {
            clickListener.onItemClicked(position)
        }


    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Foto : ImageView = itemView.findViewById(R.id.imgFotoServicoInstagram)
        val Nome : TextView = itemView.findViewById(R.id.txtNomeServicoInstagram)
    }
}