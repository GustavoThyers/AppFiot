package com.example.appinsta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.appinsta.Login.Login
import com.example.appinsta.Login.Register
import com.example.appinsta.databinding.ActivityDadosUsuarioBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class DadosUsuarioActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    lateinit var binding: ActivityDadosUsuarioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDadosUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    override fun onStart() {
        super.onStart()

        val db = Firebase.firestore
        auth = FirebaseAuth.getInstance()



        val currentUser = auth.currentUser

        if (currentUser != null){
            val userId = currentUser.uid

            val docRef = db.collection("Usuarios").document(userId)

            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null){
                        val nome = document["nome"]
                        val email = document["email"]
                        val saldo = document["saldo"]

                        binding.textDadosNome.text = "Nome: ${nome.toString()}"
                        binding.textDadosEmail.text = "Email: ${email.toString()}"
                        binding.textDadosSaldo.text = "Saldo: R$${saldo.toString()}"

                        Log.d("testeUsuario", nome.toString())


                    }
                }


        }else{
            startActivity(Intent(this, Login::class.java))
        }




        binding.btnDeslogar.setOnClickListener{
            auth.signOut()
            startActivity(Intent(this, Login::class.java))
        }

    }
}