package hr.tvz.android.listastrbad

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.android.listastrbad.databinding.ListItemBinding
import hr.tvz.android.listastrbad.model.Picture

class CustomListAdapter(private val dataSet: List<Picture>) :
    RecyclerView.Adapter<CustomListAdapter.ViewHolder>() {

    private var lastPosition = -1

    inner class ViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.run {
            val picture = dataSet[position]

            listItemCard.setOnClickListener {
                // Start DetailsActivity
                val intent = Intent(it.context, DetailsActivity::class.java)
                intent.putExtra("picture", picture)
                it.context.startActivity(intent)
            }
            listItemTitle.text = picture.title
            listPicture.setImageResource(picture.pictureResource)

            setAnimation(root, position)
        }
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.list_animation)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun getItemCount() = dataSet.size
}