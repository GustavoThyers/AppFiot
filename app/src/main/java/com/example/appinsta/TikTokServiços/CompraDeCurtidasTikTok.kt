package com.example.appinsta.TikTokServiços

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.appinsta.MainActivity
import com.example.appinsta.Model.InstagramApi
import com.example.appinsta.R
import com.example.appinsta.Servicos.API
import com.example.appinsta.Servicos.InstagramResponse
import com.example.appinsta.databinding.ActivityCompraDeCurtidasTikTokBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CompraDeCurtidasTikTok : AppCompatActivity() {
    private val handler = Handler()

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityCompraDeCurtidasTikTokBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCompraDeCurtidasTikTokBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val db = Firebase.firestore
        auth = FirebaseAuth.getInstance() // Inicialize a propriedade auth




        binding.btnEnviarCurtidasTiktok.setOnClickListener{
            val quantidadeText = binding.editQuantidadesCurtidasTikTok.text.toString()
            val quantidadeSeguidores = quantidadeText.toDouble()

            val quantidade = quantidadeSeguidores * 0.01
            val quantidadeTotal = "%.2f".format(quantidade)

            val currentUser = auth.currentUser

            if (currentUser != null){
                val userId = currentUser.uid
                val docRef = db.collection("Usuarios").document(userId)

                docRef.get()
                    .addOnSuccessListener { document->
                        if (document != null){
                            val saldo = document["saldo"].toString()
                            if (quantidadeTotal.toDouble() <= saldo.toDouble()){
                                exibirSnackbar("Valor Da Compra: $quantidadeTotal, deseja prosseguir?")
                            }else{
                                Toast.makeText(this, "Voce nao tem saldo suficiente", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            }
        }



    }


    private fun exibirSnackbar(mensagem: String){
        val rootView = findViewById<View>(android.R.id.content)
        val snackbar = Snackbar.make(rootView, mensagem, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("OK"){
            Validacao()
            val db = Firebase.firestore

            val currentUser = auth.currentUser
            if (currentUser != null){
                val quantidadeText = binding.editQuantidadesCurtidasTikTok.text.toString()
                val quantidadeSeguidores = quantidadeText.toDouble()

                val quantidade = quantidadeSeguidores * 0.01
                val quantidadeTotal = "%.2f".format(quantidade).toDouble()

                val userId = currentUser.uid

                val docRef = db.collection("Usuarios").document(userId)

                docRef.get()
                    .addOnSuccessListener { document->
                        if (document != null){
                            val saldo = document["saldo"].toString()
                            val saldoDouble = saldo.toDouble()

                            val saldoAtualizado = saldoDouble - quantidadeTotal

                            db.collection("Usuarios").document(userId)
                                .update(
                                    mapOf(
                                        "saldo" to saldoAtualizado
                                    )
                                )
                        }
                    }
            }
            startActivity(Intent(this, MainActivity::class.java))
            snackbar.dismiss() // Dismiss ao tocar em OK
            handler.removeCallbacksAndMessages(null) // Cancela o fechamento programado
        }
        snackbar.show()

        handler.postDelayed({
            if (snackbar.isShown) {
                snackbar.dismiss()
            }
        }, 10000) // 10 segundos em milissegundos

    }


    private fun Validacao(){
        val link = binding.editLinkCurtidasTikTok.text.toString()
        val quantidade = binding.editQuantidadesCurtidasTikTok.text.toString()
        
        if (link.isNotEmpty()){
            if (quantidade.isNotEmpty()){
                ConfigRetrofit()
            }else{
                Toast.makeText(this, "Coloque A Quantidade", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "Coloque Seu Link", Toast.LENGTH_SHORT).show()
        }
    }

    fun ConfigRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://borafama.com/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val tikTokApi = retrofit.create(API::class.java)

        val apiKey = "a778d324cac9697ffccc150c424454e6"
        val action = "add"
        val serviceId = 26

        val link = binding.editLinkCurtidasTikTok.text.toString()
        val quantidadetext = binding.editQuantidadesCurtidasTikTok.text.toString()
        val quantidade = quantidadetext.toInt()

        val Pedido = InstagramApi(action, serviceId, link, quantidade)
        Log.d("testandoTkk", Pedido.toString())

        val call = tikTokApi.adicionarEncomenda(
            apiKey = apiKey,
            action = Pedido.action,
            serviceId = Pedido.servicesId,
            link = Pedido.link,
            quantity = Pedido.Quantity

        )

        call.enqueue(object : Callback<InstagramResponse> {
            override fun onResponse(
                call: Call<InstagramResponse>,
                response: Response<InstagramResponse>
            ) {
                if (response.isSuccessful) {
                    val instagramResponse: InstagramResponse? = response.body()
                    // Faça algo com a resposta da API do Instagram
                    Log.d("testeOk", "Order ID: ${instagramResponse?.order}")
                } else {
                    // Trate erros de resposta aqui
                    println("Erro na requisição: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<InstagramResponse>, t: Throwable) {
                // Trate falhas de chamada aqui
                println("Falha na requisição: ${t.message}")
            }
        })

    }

}