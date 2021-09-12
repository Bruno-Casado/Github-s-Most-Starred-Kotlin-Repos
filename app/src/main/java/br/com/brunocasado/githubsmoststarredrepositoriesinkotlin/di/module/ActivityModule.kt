package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.di.module

import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.presentation.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}