package hr.tvz.android.listastrbad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.android.listastrbad.databinding.ListItemBinding
import hr.tvz.android.listastrbad.model.Picture

class CustomListAdapter(private val dataSet: List<Picture>) :
    RecyclerView.Adapter<CustomListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.run {
            listItemCard.setOnClickListener {
                println("clicked")
            }
            listItemTitle.text = dataSet[position].title
        }
    }

    override fun getItemCount() = dataSet.size
}