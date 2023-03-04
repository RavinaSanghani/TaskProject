package com.example.task.repository

import android.app.Activity
import android.app.Dialog
import android.util.Log
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import com.example.task.R
import com.example.task.databinding.RowCustomDialogLayoutBinding
import com.example.task.dao.RepositoryDAO
import com.example.task.entityTables.RepositoryEntity
import com.example.task.helperClass.ConstantVariable
import com.example.task.helperClass.Utility
import com.example.task.retrofit.ApiInterface
import com.example.task.viewModel.RepositoryViewModel
import com.google.gson.Gson
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiRoomDBRepository(private val activity: Activity, private val repositoryDAO: RepositoryDAO) {

    private val TAG = "ApiRoomDBRepository"
    private val utilities = Utility(activity)
    private var dialogCheckInternet: Dialog? = null

    suspend fun roomDBInsert(repositoryEntity: RepositoryEntity) {
        repositoryDAO.insert(repositoryEntity)
    }

    fun getAllRepository(): LiveData<List<RepositoryEntity>> {
        return repositoryDAO.getAllRepository()
    }

    fun getExecuteRepository(method: String, repositoryViewModel: RepositoryViewModel, ) {

        val header = HashMap<String, String>()
        header[ConstantVariable.keyAccept] = ConstantVariable.keyAcceptGithub
        header[ConstantVariable.keyAuthorization] = ConstantVariable.keyAuthorizationToken
        header[ConstantVariable.keyGithubVersion] = ConstantVariable.keyGithubVersionDate

        if (utilities.checkInternetConnection()) {
            if (dialogCheckInternet != null) {
                if (dialogCheckInternet!!.isShowing) {
                    dialogCheckInternet!!.dismiss()
                }
            }
            utilities.progressDialogShow()

            val apiInterface: ApiInterface =
                ApiInterface.getRetrofitClient().create(ApiInterface::class.java)
            val call: Call<JsonElement> =
                apiInterface.getExecuteApi(ConstantVariable.baseUrl + method, header)

            Log.e(TAG, "getExecuteRepository: call:${call.request()}" )
            call.enqueue(object : Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    Log.e(TAG, "getExecuteRepository: response:${response}" )
                    utilities.progressDialogHide()
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val responseObject = response.body()!!.asJsonObject
                            val jsonResponse = Gson().toJson(responseObject)
                            val jsonObject = JSONObject(jsonResponse)
                            repositoryViewModel.onSuccess.postValue(jsonObject)
                        } else {
                            repositoryViewModel.onFailure.postValue(activity.getString(R.string.response_null))
                        }
                    } else {
                        if (response.code() == 404){
                            repositoryViewModel.onFailure.postValue(activity.getString(R.string.repository_not_found))
                        }else{
                            repositoryViewModel.onFailure.postValue(activity.getString(R.string.response_fail))
                        }
                    }
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    utilities.progressDialogHide()
                    Log.e(TAG, "onFailure:${t.message} " )
                    repositoryViewModel.onFailure.postValue(t.message!!)
                }

            })
        } else {
            connectionPopUpOpen(method,repositoryViewModel)
        }
    }

    private fun connectionPopUpOpen(method: String, repositoryViewModel: RepositoryViewModel) {
        if (dialogCheckInternet == null) {
            val viewCustomBinding = RowCustomDialogLayoutBinding.inflate(activity.layoutInflater)
            dialogCheckInternet = Dialog(activity)
            dialogCheckInternet!!.setContentView(viewCustomBinding.root)
            dialogCheckInternet!!.setCancelable(false)

            viewCustomBinding.txtMessage.text = activity.getString(R.string.no_internet)
            viewCustomBinding.btnOk.text = activity.getString(R.string.retry)

            viewCustomBinding.btnCancel.setOnClickListener {
                dialogCheckInternet!!.dismiss()
            }
            viewCustomBinding.btnOk.setOnClickListener {
                dialogCheckInternet!!.dismiss()
                getExecuteRepository(method, repositoryViewModel)
            }

            dialogCheckInternet!!.window!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            dialogCheckInternet!!.show()
        } else {
            if (utilities.checkInternetConnection()) {
                if (dialogCheckInternet != null) {
                    if (dialogCheckInternet!!.isShowing) {
                        dialogCheckInternet!!.dismiss()
                    }
                }
                getExecuteRepository(method, repositoryViewModel)
            }
        }

    }
}