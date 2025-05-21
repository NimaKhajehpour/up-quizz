package com.nima.upquizz.di

import com.nima.upquizz.network.Api
import com.nima.upquizz.repository.Repository
import com.nima.upquizz.utils.Constants
import com.nima.upquizz.viewmodels.LoginViewModel
import com.nima.upquizz.viewmodels.MainViewModel
import com.nima.upquizz.viewmodels.RegisterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val module = module {

    single {
        Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(
            GsonConverterFactory.create()
        ).build().create(Api::class.java)
    }

    single {
        Repository(get())
    }

    viewModel {
        LoginViewModel(get())
    }
    viewModel {
        RegisterViewModel(get())
    }
    viewModel {
        MainViewModel(get())
    }
}