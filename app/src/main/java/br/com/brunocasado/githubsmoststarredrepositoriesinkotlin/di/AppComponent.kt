package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.di

import android.app.Application
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.CustomApplication
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityModule::class,
        NetworkModule::class,
        DatabaseModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(customApplication: CustomApplication)
}