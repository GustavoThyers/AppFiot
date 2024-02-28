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
import com.example.appinsta.databinding.ActivityCompraDeSeguidoresBinding
import com.example.appinsta.databinding.ActivityCompraDeSeguidoresTikTokBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CompraDeSeguidoresTikTok : AppCompatActivity() {
    private val handler = Handler()

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityCompraDeSeguidoresTikTokBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCompraDeSeguidoresTikTokBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance() // Inicialize a propriedade auth



        binding.btnCompraSeguidoresTikTok.setOnClickListener{
            val quantidadeText = binding.editQuantidadeSeguidoresTikTok.text.toString()
            val quantidadeSeguidores = quantidadeText.toDouble()

            val quantidade = quantidadeSeguidores * 0.025
            val quantidadeTotal = "%.2f".format(quantidade)

            val db = Firebase.firestore

            val currentUser = auth.currentUser
            if (currentUser != null) {
                val userId = currentUser.uid

                val docRef = db.collection("Usuarios").document(userId)

                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val saldo = document["saldo"].toString()


                            if (quantidadeTotal.toDouble() <= saldo.toDouble()){

                                exibirSnackbar("valor da compra $quantidadeTotal, deseja proseguir")

                            }else{
                                Toast.makeText(this, "Voce Nao Tem Saldo O Suficiente", Toast.LENGTH_SHORT).show()
                            }


                        }
                    }




            }


        }


    }

    private fun exibirSnackbar(mensagem: String) {
        val rootView = findViewById<View>(android.R.id.content)
        val snackbar = Snackbar.make(rootView, mensagem, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("OK") {
            Validacao()

            val db = Firebase.firestore

            val currentUser = auth.currentUser
            if (currentUser != null) {
                val quantidadeText = binding.editQuantidadeSeguidoresTikTok.text.toString()
                val quantidadeSeguidores = quantidadeText.toDouble()

                val quantidade = quantidadeSeguidores * 0.025
                val quantidadeTotal = "%.2f".format(quantidade).toDouble()
                //saldo do usuario
                val userId = currentUser.uid

                val docRef = db.collection("Usuarios").document(userId)

                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
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

        // Configura o Snackbar para desaparecer depois de 10 segundos
        handler.postDelayed({
            if (snackbar.isShown) {
                snackbar.dismiss()
            }
        }, 10000) // 10 segundos em milissegundos
    }


    fun Validacao(){
        val link = binding.editLinkSeguidoresTikTok.text.toString()
        val quantidade = binding.editQuantidadeSeguidoresTikTok.text.toString()

        if (link.isNotEmpty()){
            if (quantidade.isNotEmpty()){
                ConfigRetrofit()
            }else{
                Toast.makeText(this, "PREENCHA O CAMPO QUANTIDADE", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "PREENCHA O CAMPO LINK", Toast.LENGTH_SHORT).show()
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
        val serviceId = 264

        val link = binding.editLinkSeguidoresTikTok.text.toString()
        val quantidadetext = binding.editQuantidadeSeguidoresTikTok.text.toString()
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