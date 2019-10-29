package com.soulkey.applemint.di

import androidx.room.Room
import com.soulkey.applemint.data.ArticleRepository
import com.soulkey.applemint.data.ArticleRepositoryImpl
import com.soulkey.applemint.db.AppDatabase
import com.soulkey.applemint.ui.login.LoginViewModel
import com.soulkey.applemint.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ArticleModule = module {
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "app_database").allowMainThreadQueries().build() }
    single { get<AppDatabase>().articleDao() }

    single<ArticleRepository> { ArticleRepositoryImpl(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { LoginViewModel() }
}