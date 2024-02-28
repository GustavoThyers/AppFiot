package com.example.appinsta.instagramServiços

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.appinsta.Login.Login
import com.example.appinsta.MainActivity
import com.example.appinsta.Model.InstagramApi
import com.example.appinsta.Servicos.API
import com.example.appinsta.Servicos.InstagramResponse
import com.example.appinsta.databinding.ActivityCompraDeSeguidoresBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat

class CompraDeSeguidoresActivity : AppCompatActivity() {
    private val handler = Handler()

    private lateinit var auth: FirebaseAuth
    lateinit var Adm : String

    lateinit var binding: ActivityCompraDeSeguidoresBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCompraDeSeguidoresBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val db = Firebase.firestore

        //logica matematica de saldo
        //valor * 0.01 = R$







        binding.btnEnviarSeguidores.setOnClickListener {

            val quantidadeText = binding.editQuantidadeSeguidores.text.toString()
            val quantidadeSeguidores = quantidadeText.toDouble()

            val quantidade = quantidadeSeguidores * 0.01
            val quantidadeTotal = "%.2f".format(quantidade)

            val currentUser = auth.currentUser

            if (currentUser != null) {
                val userId = currentUser.uid

                val docRef = db.collection("Usuarios").document(userId)

                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val saldo = document["saldo"].toString()


                                if (quantidadeTotal.toDouble() <= saldo.toDouble()){

                                    exibirSnackbar("Valor da Compra : R$$quantidadeTotal, desejas seguir?")


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
                val quantidadeText = binding.editQuantidadeSeguidores.text.toString()
                val quantidadeSeguidores = quantidadeText.toDouble()

                val quantidade = quantidadeSeguidores * 0.01
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

    private fun Validacao() {
        var Nome = binding.editLinkSeguidores.text.toString()
        var QuantidadeString = binding.editQuantidadeSeguidores.text.toString()

        if (Nome.isNotEmpty()) {
            if (QuantidadeString.isNotEmpty()) {

                ConfigRetrofit()
            } else {
                Toast.makeText(this, "Coloque A Quantidade", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Coloque O Link De seu Perfil", Toast.LENGTH_SHORT).show()
        }
    }


    fun ConfigRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://borafama.com/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val instagramApi = retrofit.create(API::class.java)

        val apiKey = "a778d324cac9697ffccc150c424454e6"
        val action = "add"
        val serviceId = 290

        val link = binding.editLinkSeguidores.text.toString()
        val quantidadetext = binding.editQuantidadeSeguidores.text.toString()
        val quantidade = quantidadetext.toInt()

        val Pedido = InstagramApi(action, serviceId, link, quantidade)

        val call = instagramApi.adicionarEncomenda(
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

    override fun onStart() {
        super.onStart()

        val db = Firebase.firestore
        auth = FirebaseAuth.getInstance()


        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid

            val docRef = db.collection("Usuarios").document(userId)

            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {

                        val saldo = document["saldo"]


                        binding.textCompraSeguidoresInstagramSaldo.text =
                            "Seu Saldo Atual: R$${saldo.toString()}"

                    }
                }


        } else {
            startActivity(Intent(this, Login::class.java))
        }

    }
}