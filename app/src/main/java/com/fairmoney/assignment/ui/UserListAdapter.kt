package com.fairmoney.assignment.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.fairmoney.assignment.databinding.ItemUserListBinding
import com.fairmoney.assignment.db.User

class UserListAdapter(private val glide: RequestManager, private val onClickListener: View.OnClickListener) : PagingDataAdapter<User, UserListAdapter.UserVH>(UserDiffUtils()) {

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        holder.bind(getItem(position))

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH {
        return UserVH.create(parent, glide, onClickListener)
    }

    class UserDiffUtils : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

    }

    class UserVH (private val itemBinding: ItemUserListBinding, private val glide: RequestManager, private val onClickListener: View.OnClickListener) : RecyclerView.ViewHolder(itemBinding.root) {
        companion object {
            fun create(parent: ViewGroup, glide: RequestManager, onClickListener: View.OnClickListener) = UserVH(
                ItemUserListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                glide,
                onClickListener
            )
        }

        init {
            itemBinding.itemRoot.setOnClickListener(onClickListener)
        }

        fun bind(user: User?) {
            itemBinding.itemRoot.tag = user

            itemBinding.title.text = "${user?.title}. ${user?.firstName} ${user?.lastName}"

            if (user?.picture.isNullOrBlank()) {
                glide.clear(itemBinding.thumbnail)
            } else {
                glide.load(user?.picture)
                    .centerCrop()
                    .into(itemBinding.thumbnail)
            }
        }
    }
}