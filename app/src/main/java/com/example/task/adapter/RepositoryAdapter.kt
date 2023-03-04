package com.example.task.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task.R
import com.example.task.databinding.RowRepositoryItemLayoutBinding
import com.example.task.entityTables.RepositoryEntity

class RepositoryAdapter(
    private val activity: Activity,
    private val repositoryList: List<RepositoryEntity>,
) : RecyclerView.Adapter<RepositoryAdapter.MyViewHolder>() {

    private val TAG = "RepositoryAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val viewBinding = RowRepositoryItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
        return repositoryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val repository = repositoryList[position]
        holder.bind(repository)
    }

    inner class MyViewHolder(private val viewBinding: RowRepositoryItemLayoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(repository: RepositoryEntity) {

            viewBinding.apply {
                txtName.text = repository.name
                txtDescription.text = repository.description

                root.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repository.htmlUrl))
                    activity.startActivity(intent)
                }

                imgShareRepo.setOnClickListener {
                    try {
                        val shareMessage = "${repository.name} \n ${repository.htmlUrl}"
                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(
                            Intent.EXTRA_SUBJECT,
                            activity.resources.getString(R.string.app_name)
                        )
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                        activity.startActivity(Intent.createChooser(shareIntent, "Share"))
                    } catch (exception: Exception) {
                        Log.e(TAG, "bind:exception:${exception.message} ")
                    }
                }
            }
        }
    }
}