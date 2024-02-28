package com.example.appinsta.instagramServiços

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.appinsta.Model.InstagramApi
import com.example.appinsta.R
import com.example.appinsta.Servicos.API
import com.example.appinsta.Servicos.InstagramResponse
import com.example.appinsta.databinding.ActivityCompraDeCurtidasInstagramBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CompraDeCurtidasInstagram : AppCompatActivity() {
    lateinit var binding: ActivityCompraDeCurtidasInstagramBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCompraDeCurtidasInstagramBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnEnviarCurtidasInstagram.setOnClickListener{
            Validacao()
        }


    }


    private fun Validacao(){
        var Link = binding.editLinkCurtidasInstagram.text.toString()
        var Quantidade = binding.editQuantidadeCurtidasInstagram.text.toString()


        if (Link.isNotEmpty()){
            if (Quantidade.isNotEmpty()){
                ConfigRetrofit()
            }else{
                Toast.makeText(this, "Coloque A Quantidade Da Publicação", Toast.LENGTH_SHORT).show()

            }
        }else{
            Toast.makeText(this, "Coloque O link Da Publicação", Toast.LENGTH_SHORT).show()
        }
    }

    private fun ConfigRetrofit(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://borafama.com/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        val instagramApi = retrofit.create(API::class.java)

        val apiKey = "a778d324cac9697ffccc150c424454e6"
        val action = "add"
        val serviceId = 31


        var Link = binding.editLinkCurtidasInstagram.text.toString()
        var QuantidadeString = binding.editQuantidadeCurtidasInstagram.text.toString()
        var Quantidade = QuantidadeString.toInt()


        var Pedido = InstagramApi(action, serviceId, Link, Quantidade)

        Log.d("testando", Pedido.toString())

        val call = instagramApi.adicionarEncomenda(
            apiKey = apiKey,
            action = Pedido.action,
            serviceId = Pedido.servicesId,
            link = Pedido.link,
            quantity = Pedido.Quantity

        )



        call.enqueue(object : Callback<InstagramResponse> {
            override fun onResponse(call: Call<InstagramResponse>, response: Response<InstagramResponse>) {
                if (response.isSuccessful) {
                    val instagramResponse: InstagramResponse? = response.body()
                    // Faça algo com a resposta da API do Instagram
                    Log.d("testeOk","Order ID: ${instagramResponse?.order}")
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