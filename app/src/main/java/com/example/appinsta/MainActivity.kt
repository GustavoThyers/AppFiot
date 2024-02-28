package com.example.appinsta

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appinsta.ADM.ActivityUsuarios
import com.example.appinsta.Adapter.AdapterRedesSociais
import com.example.appinsta.ClickListener.RecyclerViewClickListener
import com.example.appinsta.Login.Register
import com.example.appinsta.Model.ServiçoRedeSocial
import com.example.appinsta.TikTokServiços.TikTokActivityServicos


import com.example.appinsta.databinding.ActivityMainBinding
import com.example.appinsta.instagramServiços.InstagramActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity(), RecyclerViewClickListener {
    private lateinit var auth: FirebaseAuth

    lateinit var binding: ActivityMainBinding
    val listRedeSocial: MutableList<ServiçoRedeSocial> = ArrayList<ServiçoRedeSocial>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        Admnistraçao()


        VerificarLogin()

        AdicionarServiços()

        configAdapter()

        binding.imgMenu.setOnClickListener{
            showPopupMenu(it, false)

        }


        binding.textTelasDeStreaming.setOnClickListener{
            MudancaTelasStreaming()
        }

        binding.textRedesSociaisInfo.setOnClickListener{
            MudancaRedeSociais()
        }




    }
    private fun showPopupMenu(view: View, admin : Boolean) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(if (admin) R.menu.menu_admin else R.menu.menu) // o arquivo XML que define os itens do menu
        popupMenu.setOnMenuItemClickListener { item: MenuItem? ->
            // Lógica para lidar com os itens do menu selecionados
            when (item?.itemId) {
                R.id.item1 -> {
                    // Ação para o item 1 do menu
                    startActivity(Intent(this, DadosUsuarioActivity::class.java))
                    true
                }
                R.id.item2 -> {
                    // Ação para o item 2 do menu
                    Toast.makeText(this, "item 2", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.item3 ->{
                    startActivity(Intent(this, ActivityUsuarios::class.java))
                    true
                }
                R.id.item4 ->{
                    Toast.makeText(this, "item 4", Toast.LENGTH_SHORT).show()
                    true
                }

                // Adicione mais casos conforme necessário
                else -> false
            }
        }
        popupMenu.show()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    private fun MudancaTelasStreaming(){
        if (binding.rvRedesSociais.visibility == View.VISIBLE){
            binding.rvRedesSociais.visibility = View.GONE
            binding.fragmentContainerView.visibility = View.VISIBLE

            binding.textTelasDeStreaming.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.textTelasDeStreaming.setBackground(ContextCompat.getDrawable(this, R.drawable.canto_arredondado))

            binding.textRedesSociaisInfo.setBackground(ContextCompat.getDrawable(this, R.drawable.form))
            binding.textRedesSociaisInfo.setTextColor(ContextCompat.getColor(this, R.color.gray))




        }
    }

    private fun MudancaRedeSociais(){

        if (binding.rvRedesSociais.visibility == View.GONE){
            binding.rvRedesSociais.visibility = View.VISIBLE
            binding.fragmentContainerView.visibility = View.INVISIBLE
            binding.textTelasDeStreaming.setTextColor(ContextCompat.getColor(this, R.color.gray))

            binding.textTelasDeStreaming.setBackground(ContextCompat.getDrawable(this, R.drawable.form))
            binding.textRedesSociaisInfo.setBackground(ContextCompat.getDrawable(this, R.drawable.canto_arredondado))
            binding.textRedesSociaisInfo.setTextColor(ContextCompat.getColor(this, R.color.white))






        }
    }

    override fun onItemClicked(position: Int) {
        // Determine qual atividade abrir com base na posição do item clicado
        when (position) {
            0 -> startActivity(Intent(this, InstagramActivity::class.java))

            1 -> startActivity(Intent(this, TikTokActivityServicos::class.java))
            // Adicione casos para os demais itens
        }
    }

    fun AdicionarServiços(){
        val Rede1 = ServiçoRedeSocial(R.drawable.instagram, "Instagram")
        listRedeSocial.add(Rede1)
        val Rede2 = ServiçoRedeSocial(R.drawable.tiktok, "Tik Tok")
        listRedeSocial.add(Rede2)

    }

    fun VerificarLogin(){
        val auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser

        if (currentUser == null) {
            startActivity(Intent(this, Register::class.java))
        }
    }

    fun configAdapter(){
        val adapterRedeSocial = AdapterRedesSociais(applicationContext, listRedeSocial, this)
        val linearLayoutManager: LinearLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRedesSociais.layoutManager = linearLayoutManager // Correção aqui
        binding.rvRedesSociais.setHasFixedSize(true)
        binding.rvRedesSociais.adapter = adapterRedeSocial // Correção aqui
    }

    fun Admnistraçao(){
        val db = Firebase.firestore

        val currentUser = auth.currentUser

            val uid = currentUser!!.uid

            val docRef = db.collection("Usuarios").document(uid)

            docRef.get()
                .addOnSuccessListener { document ->

                        val nivelString = document["nivel"].toString()
                        val nivel = nivelString.toInt()

                        if (nivel == 1){
                            Toast.makeText(this, "ADMINISTRACAO", Toast.LENGTH_SHORT).show()
                            binding.imgMenu.setOnClickListener{
                                showPopupMenu(it, true)
                            }
                        }
                    }

    }



}