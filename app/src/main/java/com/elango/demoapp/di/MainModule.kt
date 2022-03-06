package com.elango.demoapp.di

import android.content.Context
import com.elango.demoapp.api.Api
import com.elango.demoapp.localstorage.AppDataBase
import com.elango.demoapp.localstorage.DatabaseClient
import com.elango.demoapp.model.MapDataDAO
import com.elango.demoapp.repository.MapRepository
import com.elango.demoapp.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Singleton
    @Provides
    @Named("BaseUrl")
    fun provideBaseURL() = "https://hacker-news.firebaseio.com/"


    @Singleton
    @Provides
    @Named("database")
    fun provideLocalDB(@ApplicationContext context: Context) =
        DatabaseClient(context).getAppDatabase()


    @Singleton
    @Provides
    @Named("LocationDAO")
    fun provideLocationDAO(@Named("database") appDB: AppDataBase) = appDB.getMapModelDao()


    @Singleton
    @Provides
    fun provideRetrofit(@Named("BaseUrl") baseURL: String): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)


    @Singleton
    @Provides
    fun provideMapRepo(@Named("LocationDAO")locationDAO: MapDataDAO): MapRepository = MapRepository(locationDAO)


}