package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.R
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.databinding.StarredKotlinItemBinding
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.model.RepoVO
import com.bumptech.glide.Glide

class StarredKotlinListAdapter :
    ListAdapter<RepoVO, StarredKotlinListAdapter.ViewHolder>(RepoDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(
        private val binding: StarredKotlinItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RepoVO) {
            Glide.with(binding.authorsPhotoImageView).load(item.photo)
                .into(binding.authorsPhotoImageView)
            binding.repoNameTextView.text = item.repoName
            binding.authorsNameTextView.text = item.authorsName
            binding.starsCountTextView.text = context.getString(R.string.stars_count, item.stars)
            binding.forksCountTextView.text = context.getString(R.string.forks_count, item.forks)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StarredKotlinItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, parent.context)
            }
        }
    }
}

class RepoDiffCallback : DiffUtil.ItemCallback<RepoVO>() {
    override fun areItemsTheSame(oldItem: RepoVO, newItem: RepoVO): Boolean {
        return oldItem.repoName == newItem.repoName
    }

    override fun areContentsTheSame(oldItem: RepoVO, newItem: RepoVO): Boolean {
        return oldItem == newItem
    }
}
