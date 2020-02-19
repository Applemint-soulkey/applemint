package com.soulkey.applemint.di

import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.soulkey.applemint.common.Dapina
import com.soulkey.applemint.common.MessagingService
import com.soulkey.applemint.common.raindrop.RaindropAPI
import com.soulkey.applemint.common.raindrop.RaindropClient
import com.soulkey.applemint.common.raindrop.RaindropInterceptor
import com.soulkey.applemint.config.Constant.CONNECT_TIMEOUT
import com.soulkey.applemint.config.Constant.READ_TIMEOUT
import com.soulkey.applemint.config.Constant.WRITE_TIMEOUT
import com.soulkey.applemint.config.CurrentUser
import com.soulkey.applemint.data.*
import com.soulkey.applemint.db.AppDatabase
import com.soulkey.applemint.ui.analyze.AnalyzeViewModel
import com.soulkey.applemint.ui.bookmark.BookmarkViewModel
import com.soulkey.applemint.ui.login.LoginViewModel
import com.soulkey.applemint.ui.main.MainViewModel
import com.soulkey.applemint.ui.main.article.ArticleViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val AppModule = module {
    //Firestore
    single { FirebaseFirestore.getInstance() }

    //Push
    single { MessagingService(get()) }

    //current User
    single { CurrentUser() }

    //Room
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "app_database")
        .fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build()
    }
    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().raindropCollectionDao() }
    single<ArticleRepository> { ArticleRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get(), get()) }
    single<RaindropCollectionRepository> { RaindropCollectionRepositoryImpl(get()) }

    //OkHttpClient
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })   //Http Logger
            .addInterceptor(RaindropInterceptor(get())) //Custom Interceptor
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    //Retrofit
    single {
        Retrofit.Builder()
            .baseUrl(RaindropAPI.EndPoint.baseURL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(RaindropAPI::class.java)
    }

    //Dapina
    single { Dapina(get()) }
    single { get<Dapina>().getDapinaClient() }

    //Raindrop
    single { RaindropClient(get()) }

    //ViewModel
    viewModel { MainViewModel(get()) }
    viewModel { ArticleViewModel(androidContext(), get(), get(), get(), get()) }
    viewModel { LoginViewModel(androidContext(), get(), get(), get(), get()) }
    viewModel { AnalyzeViewModel(get()) }
    viewModel { BookmarkViewModel(get()) }
}