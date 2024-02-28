package com.example.appinsta.instagramServiços

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appinsta.Adapter.AdapterServiçosInstagram
import com.example.appinsta.ClickListener.RecyclerViewClickListener
import com.example.appinsta.Login.Register
import com.example.appinsta.Model.ServiçosInstagram
import com.example.appinsta.R
import com.example.appinsta.databinding.ActivityInstagramBinding
import com.google.firebase.auth.FirebaseAuth

class InstagramActivity : AppCompatActivity(), RecyclerViewClickListener {
    lateinit var binding: ActivityInstagramBinding
    val listServiçosInstagram: MutableList<ServiçosInstagram> = ArrayList<ServiçosInstagram>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInstagramBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser

        if (currentUser == null) {
            startActivity(Intent(this, Register::class.java))
        }

        val Serviço1 = ServiçosInstagram(R.drawable.logoseguidoresinsta, "Seguidores")
        val Serviço2 = ServiçosInstagram(R.drawable.logocurtidasinsta, "Curtidas")

        listServiçosInstagram.add(Serviço1)
        listServiçosInstagram.add(Serviço2)


        ConfigRecyclerServiçoInstagram()




    }



    fun ConfigRecyclerServiçoInstagram(){
        val adapterServiçosInstagram = AdapterServiçosInstagram(applicationContext, listServiçosInstagram, this)
        val linearLayoutManager: LinearLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvServicosInstagram.layoutManager = linearLayoutManager // Correção aqui
        binding.rvServicosInstagram.setHasFixedSize(true)
        binding.rvServicosInstagram.adapter = adapterServiçosInstagram // Correção aqui
    }

    override fun onItemClicked(position: Int) {
        // Determine qual atividade abrir com base na posição do item clicado
        when (position) {
            0 -> startActivity(Intent(this, CompraDeSeguidoresActivity::class.java))
            1 -> startActivity(Intent(this, CompraDeCurtidasInstagram::class.java))
            // Adicione casos para os demais itens
        }
    }




}

