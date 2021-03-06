package com.enixma.sample.charity.data.di


import android.content.Context
import com.enixma.sample.charity.R
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ServiceFactoryModule constructor(private val context: Context) {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
                .setExclusionStrategies(object : ExclusionStrategy {
                    override fun shouldSkipField(f: FieldAttributes): Boolean {
                        return false
                    }

                    override fun shouldSkipClass(clazz: Class<*>): Boolean {
                        return false
                    }
                })
                .create()
    }

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return Interceptor { chain ->
            var newRequest = chain.request().newBuilder().build()
            chain.proceed(newRequest)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(context.resources.getInteger(R.integer.CONNECTION_TIMEOUT_IN_SECOND).toLong(), TimeUnit.SECONDS)
        builder.readTimeout(context.resources.getInteger(R.integer.READ_TIMEOUT_IN_SECOND).toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(context.resources.getInteger(R.integer.WRITE_TIMEOUT_IN_SECOND).toLong(), TimeUnit.SECONDS)
        builder.addInterceptor(interceptor)

        if (context.getString(R.string.LOG_REQUEST).toBoolean()) {
            val mInterceptor = HttpLoggingInterceptor()
            mInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
            mInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(mInterceptor)
        }

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRestAdapter(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        val builder = Retrofit.Builder()
        builder.client(okHttpClient)
                .baseUrl(context.getString(R.string.BASE_URL))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
        return builder.build()
    }

}