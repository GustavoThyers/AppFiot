package com.example.appinsta.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.appinsta.MainActivity
import com.example.appinsta.R
import com.example.appinsta.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Login : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth


        binding.btnEntrar.setOnClickListener{
            Validar()
        }

        binding.textEsqueceuSenha.setOnClickListener{
            startActivity(Intent(this, RecuperarConta::class.java))
        }

        binding.textCriarConta.setOnClickListener{
            startActivity(Intent(this, Register::class.java))
        }
    }


    private fun Validar() {
        val Email = binding.editEmailRegister.text.toString()
        val Password = binding.editPasswordRegister.text.toString()

        if (Email.isNotEmpty()){
            if (Password.isNotEmpty()){
                LoginUser(Email, Password)
            }else{
                Toast.makeText(this, "Digite Sua Senha", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "Digite Seu Email", Toast.LENGTH_SHORT).show()
        }
    }

    private fun LoginUser(email : String, password : String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "Bem Vindo De Volta, Boas Compras S2", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                }else{
                    Toast.makeText(this, "Que Pena!! Email Ou Senha Incorretos", Toast.LENGTH_SHORT).show()
                }
            }
    }



}