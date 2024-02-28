package com.example.appinsta.TikTokServiços

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appinsta.Adapter.AdapterServiçosInstagram
import com.example.appinsta.Adapter.AdapterTikTokServiços
import com.example.appinsta.ClickListener.RecyclerViewClickListener
import com.example.appinsta.Model.ServiçosInstagram
import com.example.appinsta.Model.ServiçosTikTok
import com.example.appinsta.R
import com.example.appinsta.databinding.ActivityTikTok2Binding

class TikTokActivityServicos : AppCompatActivity(), RecyclerViewClickListener {
    private lateinit var binding: ActivityTikTok2Binding
    val listServiçosTikTok: MutableList<ServiçosTikTok> = ArrayList<ServiçosTikTok>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTikTok2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ConfigRecyclerServiçoInstagram()

        val Serviço1 = ServiçosTikTok(R.drawable.logotiktok, "Seguidores")
        val Serviço2 = ServiçosTikTok(R.drawable.logotiktok, "Curtidas")

        listServiçosTikTok.add(Serviço1)
        listServiçosTikTok.add(Serviço2)


    }

    fun ConfigRecyclerServiçoInstagram(){
        val adapterTikTok = AdapterTikTokServiços(applicationContext, listServiçosTikTok, this)
        val linearLayoutManager: LinearLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvServicosTiktok.layoutManager = linearLayoutManager // Correção aqui
        binding.rvServicosTiktok.setHasFixedSize(true)
        binding.rvServicosTiktok.adapter = adapterTikTok // Correção aqui
    }

    override fun onItemClicked(position: Int) {
        when (position) {
            0 -> startActivity(Intent(this, CompraDeSeguidoresTikTok::class.java))
            1 -> startActivity(Intent(this, CompraDeCurtidasTikTok::class.java))
            // Adicione casos para os demais itens
        }
    }

}