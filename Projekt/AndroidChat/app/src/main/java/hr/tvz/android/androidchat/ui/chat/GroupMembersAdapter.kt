package hr.tvz.android.androidchat.ui.chat

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.android.androidchat.databinding.GroupMemberItemBinding
import hr.tvz.android.androidchat.model.User

class GroupMembersAdapter(
    private val members: List<User>,
    private val currentUser: User,
    private val allowDelete: Boolean,
    private val onRemoveMemberClick: OnRemoveMemberClick
) : RecyclerView.Adapter<GroupMembersAdapter.GroupMembersViewHolder>() {
    inner class GroupMembersViewHolder(val binding: GroupMemberItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMembersViewHolder {
        val binding = GroupMemberItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return GroupMembersViewHolder(binding)
    }

    override fun getItemCount() = members.size

    override fun onBindViewHolder(holder: GroupMembersViewHolder, position: Int) {
        holder.binding.run {
            onRemove = onRemoveMemberClick
            user = members[position]
            closeButton.visibility = if (allowDelete && members[position].username != currentUser.username) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}