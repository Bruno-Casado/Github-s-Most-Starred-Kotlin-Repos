package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.RepoListViewModel
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.di.ViewModelFactory
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RepoListViewModel::class)
    abstract fun bindRepoListViewModel(repoListViewModel: RepoListViewModel): ViewModel

    @Binds
    abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}