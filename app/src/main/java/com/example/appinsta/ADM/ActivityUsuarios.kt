package com.example.appinsta.ADM

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appinsta.Adapter.AdapterRedesSociais
import com.example.appinsta.Adapter.AdapterUsuarios
import com.example.appinsta.Model.Usuarios
import com.example.appinsta.R
import com.example.appinsta.databinding.ActivityUsuariosBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ActivityUsuarios : AppCompatActivity() {
    lateinit var binding: ActivityUsuariosBinding
    val usuariosList: MutableList<Usuarios> = ArrayList<Usuarios>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUsuariosBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        obterUsuarios()
        configAdapter()





    }



    fun configAdapter(){
        val adapterUsuarios = AdapterUsuarios(applicationContext, usuariosList)
        val linearLayoutManager: LinearLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvUsuarios.layoutManager = linearLayoutManager // Correção aqui
        binding.rvUsuarios.setHasFixedSize(true)
        binding.rvUsuarios.adapter = adapterUsuarios // Correção aqui
    }

    fun obterUsuarios(){
        val db = Firebase.firestore

        db.collection("Usuarios")
            .get()
            .addOnSuccessListener { result ->
                usuariosList.clear() // Limpa a lista existente antes de adicionar os novos usuários
                for (document in result){
                    val nome = document["nome"]
                    val email = document["email"]
                    val saldoString = document["saldo"].toString()
                    val saldo = saldoString.toDouble()

                    val usuario = Usuarios(nome.toString(), email.toString(), saldo)
                    usuariosList.add(usuario)
                }
                // Após adicionar os usuários, notifique o adapter para atualizar o RecyclerView
                binding.rvUsuarios.adapter?.notifyDataSetChanged()
            }
    }

}