package com.example.appinsta.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.appinsta.MainActivity
import com.example.appinsta.R
import com.example.appinsta.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.util.HashMap
import java.util.Objects

class Register : AppCompatActivity() {
    lateinit var usuarioId : String

    lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.textEntrarConta.setOnClickListener{
            startActivity(Intent(this, Login::class.java))
        }


        binding.buttonRegister.setOnClickListener{
            Validar()
        }


    }

    private fun Validar() {

        val Nome = binding.editNomeRegister.text.toString()
        val Email = binding.editEmailRegister.text.toString()
        val Password = binding.editPasswordRegister.text.toString()
        
        if (Nome.isNotEmpty()){
            if (Email.isNotEmpty()){
                if (Password.isNotEmpty()){
                    RegisterUser(Email, Password)


                }else{
                    Toast.makeText(this, "Digite O Campo Senha", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Digite O Campo Email", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "Digite O Campo Nome", Toast.LENGTH_SHORT).show()
        }
    }

    private fun RegisterUser(email : String, password : String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful){
                    val db = Firebase.firestore

                    Toast.makeText(this, "Obrigado Por Se Cadastrar, Boas Compras", Toast.LENGTH_SHORT).show()
                    val usuarios: MutableMap<String, Any> = HashMap()

                    val Nivel = 0
                    val Nome = binding.editNomeRegister.text.toString()

                    val Saldo = 0
                    usuarios.put("nome", Nome)
                    usuarios.put("email", email)
                    usuarios.put("nivel", Nivel)
                    usuarios.put("saldo", Saldo)


                    val usuarioId = requireNotNull(FirebaseAuth.getInstance().currentUser?.uid) { "usuarioId não pode ser nulo" }


                    db.collection("Usuarios").document(usuarioId).set(usuarios).addOnSuccessListener { documentReference ->
                        Log.d("testeAdicionar", "tudo certo")
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                }else{
                    Log.d("ErroFirebase", " ${task.exception}")

                }
            }
    }
}