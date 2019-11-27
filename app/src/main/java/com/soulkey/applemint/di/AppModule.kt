package com.soulkey.applemint.di

import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.soulkey.applemint.data.ArticleRepository
import com.soulkey.applemint.data.ArticleRepositoryImpl
import com.soulkey.applemint.data.BookmarkRepository
import com.soulkey.applemint.data.BookmarkRepositoryImpl
import com.soulkey.applemint.db.AppDatabase
import com.soulkey.applemint.ui.login.LoginViewModel
import com.soulkey.applemint.ui.main.MainViewModel
import com.soulkey.applemint.ui.main.article.ArticleViewModel
import com.soulkey.applemint.ui.main.bookmark.BookmarkViewModel
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
    single { get<AppDatabase>().bookmarkDao() }
    single<ArticleRepository> { ArticleRepositoryImpl(get()) }
    single<BookmarkRepository> { BookmarkRepositoryImpl(get(), get()) }

    viewModel { MainViewModel() }
    viewModel { ArticleViewModel(get(), get(), get()) }
    viewModel { BookmarkViewModel(get()) }
    viewModel { LoginViewModel(get(), get(), get(), androidContext()) }
}