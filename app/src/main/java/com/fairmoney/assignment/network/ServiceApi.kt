package com.fairmoney.assignment.network

import com.fairmoney.assignment.db.UserDetail
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceApi {

    @GET("user")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): GetUsersResponse

    @GET("user/{id}")
    suspend fun getUser(
        @Path("id") id: String
    ): UserDetail

    companion object {
        private const val BASE_URL = "https://dummyapi.io/data/v1/"

        fun create(): ServiceApi {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val header = Interceptor {
                val request = it.request().newBuilder()
                    .addHeader("app-id", "617fc43cab18d27042ae1617")
                    .build()
                it.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(header)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(ServiceApi::class.java)
        }
    }

}