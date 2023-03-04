package com.example.task.retrofit

import com.example.task.helperClass.ConstantVariable
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Url

interface ApiInterface {

    companion object {
        private var retrofit: Retrofit? = null
        private var gson = GsonBuilder().setLenient().create()

        fun getRetrofitClient(): Retrofit {

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(ConstantVariable.baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit!!
        }
    }

    @GET
    fun getExecuteApi(
        @Url url: String,
        @HeaderMap header: HashMap<String, String>
    ): Call<JsonElement>
}