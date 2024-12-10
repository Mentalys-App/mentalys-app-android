package com.mentalys.app.ui.music

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mentalys.app.data.local.entity.MusicDetailEntity
import com.mentalys.app.data.local.entity.MusicItemEntity
import com.mentalys.app.data.repository.MusicRepository
import com.mentalys.app.utils.Resource
import kotlinx.coroutines.launch

class MusicViewModel(
    private val repository: MusicRepository
) : ViewModel() {
    private val _musics = MutableLiveData<Resource<List<MusicItemEntity>>>()
    val musics: LiveData<Resource<List<MusicItemEntity>>> = _musics

     private val _music = MutableLiveData<Resource<MusicDetailEntity>>()
     val music: LiveData<Resource<MusicDetailEntity>> = _music

    fun getMusics(query: String) {
        viewModelScope.launch {
            repository.getMusics(query).observeForever { result ->
                _musics.postValue(result)
            }
        }
    }

    fun getMusic(id: Int) {
        viewModelScope.launch {
            repository.searchMusic(id).observeForever { result ->
                _music.postValue(result)
            }
        }
    }
}