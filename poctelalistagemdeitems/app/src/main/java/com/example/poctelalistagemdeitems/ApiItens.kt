package com.example.poctelalistagemdeitems

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiItens {

    @GET("itens")
    fun get() : Call<List<Item>> // Call do pacote retrofit2

    @GET("itens/{id}")
    fun get(@Path("id") id:Int) : Call<Item>

    @DELETE("itens/{id}")
    fun delete(@Path("id") id:Int) : Call<Void>

    /*
    @Query("nome") -> indica um endpoint assim:
    /itens?nome=blablbalba
     */
    @GET("itens")
    fun get(@Query("nome") nome:String) : Call<Item>

    @POST("itens")
    fun post(@Body novoItem: Item) : Call<Item>

    @PUT("itens/{id}")
    fun post(@Path("id") id:Int, @Body itemEditado:Item) : Call<Item>

    // companion object serve para disponibilizar método estático
    companion object {
        var BASE_URL = "https://654bf6eb5b38a59f28eff806.mockapi.io/"

        fun criar() : ApiItens {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiItens::class.java)
        }
    }
}