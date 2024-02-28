package com.example.appinsta.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.appinsta.R
import com.example.appinsta.databinding.ActivityRecuperarContaBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class RecuperarConta : AppCompatActivity() {
    private lateinit var binding: ActivityRecuperarContaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecuperarContaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEntrarRecovery.setOnClickListener{
            RecuperarContaUsuario()
        }

    }

    private fun RecuperarContaUsuario() {
        val Email = binding.editEmailRegisterRecovery.text.toString()
        
        Firebase.auth.sendPasswordResetEmail(Email)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "Verifique o email $Email", Toast.LENGTH_SHORT).show()
                }
            }
    }
}