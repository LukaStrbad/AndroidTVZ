package hr.tvz.android.androidchat.ui.grouplist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.android.androidchat.databinding.GroupItemBinding

class GroupListAdapter(
    val groups: MutableList<GroupItem>,
    private val clickListener: GroupClick
) : RecyclerView.Adapter<GroupListAdapter.GroupViewHolder>() {

    inner class GroupViewHolder(val binding: GroupItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val binding = GroupItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return GroupViewHolder(binding)
    }

    override fun getItemCount() = groups.size

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val groupItem = groups[position]
        holder.binding.item = groupItem
        holder.binding.itemOnClickListener = clickListener
    }
}