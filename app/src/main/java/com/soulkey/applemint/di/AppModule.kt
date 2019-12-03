package com.soulkey.applemint.di

import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.soulkey.applemint.data.ArticleRepository
import com.soulkey.applemint.data.ArticleRepositoryImpl
import com.soulkey.applemint.db.AppDatabase
import com.soulkey.applemint.db.ArticleDao
import com.soulkey.applemint.ui.login.LoginViewModel
import com.soulkey.applemint.ui.main.MainViewModel
import com.soulkey.applemint.ui.main.article.ArticleViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "app_database")
        .fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build()
    }
    single { FirebaseFirestore.getInstance() }
    single { get<AppDatabase>().articleDao() }
    single<ArticleRepository> { ArticleRepositoryImpl(get(), get()) }

    viewModel { MainViewModel() }
    viewModel { ArticleViewModel(get(), androidContext(), get()) }
    viewModel { LoginViewModel() }
}