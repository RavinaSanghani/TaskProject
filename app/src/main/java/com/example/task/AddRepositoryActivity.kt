package com.example.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.task.databinding.ActivityAddRepositoryBinding
import com.example.task.entityTables.RepositoryEntity
import com.example.task.helperClass.ConstantVariable
import com.example.task.viewModel.RepositoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddRepositoryActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "AddRepositoryActivity"
    private lateinit var viewBinding: ActivityAddRepositoryBinding
    private lateinit var viewModelRepository: RepositoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAddRepositoryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        init()

        setViewModel()

    }

    private fun init() {
        viewBinding.apply {

            clToolbar.imgAddRepo.visibility = View.GONE
            clToolbar.txtTitle.text = getString(R.string.add_repository)

            clToolbar.imgBack.setOnClickListener(this@AddRepositoryActivity)
            btnAddRepo.setOnClickListener(this@AddRepositoryActivity)

        }
    }

    private fun validation(): Boolean {
        if (viewBinding.etOwnerName.text.isBlank() || viewBinding.etOwnerName.text.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.owner_name_valid_msg),
                Toast.LENGTH_LONG
            ).show()
            return false
        } else if (viewBinding.etRepository.text.isBlank() || viewBinding.etRepository.text.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.repository_valid_msg),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }

    private fun setViewModel() {
        viewModelRepository = ViewModelProvider(
            this@AddRepositoryActivity,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[RepositoryViewModel::class.java]

        viewModelRepository.onSuccess.observe(this) { response ->
            Log.e(TAG, "setUpViewModel: response:$response")

            val name = response.getString("name")
            val htmlUrl = response.getString("html_url")
           var description =""
            if (response.has("description")){
                description = response.getString("description")
            }

            CoroutineScope(Dispatchers.Main).launch {
                viewModelRepository.addRoomDBRepository(
                    this@AddRepositoryActivity,
                    RepositoryEntity(name, description, htmlUrl)
                )

                finish()
            }
        }

        viewModelRepository.onFailure.observe(this) { errorMessage ->
            Log.e(TAG, "setUpViewModel: errorMessage:$errorMessage")
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.btnAddRepo -> {
                if (validation()) {
                    viewModelRepository.getPostExecution(
                        this,
                        "${ConstantVariable.reposMethod}/${viewBinding.etOwnerName.text}/${viewBinding.etRepository.text}",
                        viewModelRepository
                    )
                }
            }
        }
    }
}
