package sashjakk.weather.app.modules

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import sashjakk.weather.app.ui.MainViewModel

val uiModule = module {
    viewModel { MainViewModel(get()) }
}