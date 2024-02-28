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
import com.example.appinsta.Model.ServiçosTikTok
import com.example.appinsta.Model.Usuarios
import com.example.appinsta.R


class AdapterTikTokServiços(private val context : Context, private val listServicosTikTok : List<ServiçosTikTok>, private val clickListener: RecyclerViewClickListener) : RecyclerView.Adapter<AdapterTikTokServiços.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tiktok, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listServicosTikTok.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(listServicosTikTok[position].Foto).into(holder.foto)
        holder.nome.text = listServicosTikTok[position].Nome

        holder.itemView.setOnClickListener {
            clickListener.onItemClicked(position)
        }
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foto : ImageView = itemView.findViewById(R.id.imgTikTokServicos)
        val nome : TextView = itemView.findViewById(R.id.textTikTokServicos)
    }
}