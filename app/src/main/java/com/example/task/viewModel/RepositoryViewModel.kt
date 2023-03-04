package com.example.task.viewModel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.task.dao.RepositoryDAO
import com.example.task.entityTables.RepositoryEntity
import com.example.task.repository.ApiRoomDBRepository
import com.example.task.helperClass.RoomDBHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class RepositoryViewModel(application: Application) : AndroidViewModel(application) {

    var onSuccess = MutableLiveData<JSONObject>()
    var onFailure = MutableLiveData<String>()

    private var dao: RepositoryDAO

    init {
        dao = RoomDBHelper.getDatabase(application).getAllRepository()
    }

    suspend fun addRoomDBRepository(activity: Activity, repositoryEntity: RepositoryEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            val repository = ApiRoomDBRepository(activity, dao)
            repository.roomDBInsert(repositoryEntity)
        }

    fun getAllRepositoryRoomDb(activity: Activity): LiveData<List<RepositoryEntity>> {
        val repository = ApiRoomDBRepository(activity, dao)
        return repository.getAllRepository()
    }

    fun getPostExecution(
        activity: Activity,
        method: String,
        repositoryViewModel: RepositoryViewModel
    ) {
        val repository = ApiRoomDBRepository(activity, dao)
        repository.getExecuteRepository(method, repositoryViewModel)
    }
}