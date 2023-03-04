package com.example.task

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task.databinding.ActivityRepositoryListBinding
import com.example.task.adapter.RepositoryAdapter
import com.example.task.entityTables.RepositoryEntity
import com.example.task.viewModel.RepositoryViewModel

class RepositoryListActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewBinding: ActivityRepositoryListBinding
    private lateinit var viewModelRepository: RepositoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityRepositoryListBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        init()

        setViewModel()
    }

    private fun init() {
        viewBinding.apply {

            clToolbar.imgBack.visibility = View.GONE
            clToolbar.txtTitle.text = getString(R.string.repository_list)

            clToolbar.imgAddRepo.setOnClickListener(this@RepositoryListActivity)
            btnAddRepo.setOnClickListener(this@RepositoryListActivity)

            rvRepository.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.top = 20
                    outRect.bottom = 20
                    outRect.left = 0
                    outRect.right = 0
                }
            })
        }
    }

    private fun setViewModel() {
        viewModelRepository = ViewModelProvider(
            this@RepositoryListActivity,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[RepositoryViewModel::class.java]

        viewModelRepository.getAllRepositoryRoomDb(this)
            .observe(this@RepositoryListActivity) { list ->
                list?.let {
                    viewBinding.progressBarLoading.visibility = View.VISIBLE
                    setRepositoryAdapter(it)
                }
            }
    }

    private fun showRepoList(isShow: Boolean) {

        viewBinding.apply {
            if (isShow) {
                rvRepository.visibility = View.VISIBLE
                clNoRepoAvailable.visibility = View.GONE
                clToolbar.imgAddRepo.visibility = View.VISIBLE
            } else {
                rvRepository.visibility = View.GONE
                clNoRepoAvailable.visibility = View.VISIBLE
                clToolbar.imgAddRepo.visibility = View.GONE
            }
        }

    }

    private fun setRepositoryAdapter(repositoryEntitiesList: List<RepositoryEntity>) {
        viewBinding.apply {

            progressBarLoading.visibility = View.GONE

            if (repositoryEntitiesList.isNotEmpty()) {
                showRepoList(true)
                rvRepository.layoutManager =
                    LinearLayoutManager(this@RepositoryListActivity, RecyclerView.VERTICAL, false)
                rvRepository.adapter =
                    RepositoryAdapter(this@RepositoryListActivity, repositoryEntitiesList)
            } else {
                showRepoList(false)
            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imgAddRepo -> {
                startActivity(Intent(this, AddRepositoryActivity::class.java))
            }
            R.id.btnAddRepo -> {
                viewBinding.clToolbar.imgAddRepo.performClick()
            }
        }
    }
}