package hr.tvz.android.mvpstrbad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import coil.load
import hr.tvz.android.mvpstrbad.databinding.ListItemBinding
import hr.tvz.android.mvpstrbad.model.Picture

class CustomListAdapter(dataSet: List<Picture>, private val itemClick: ItemClick) :
    RecyclerView.Adapter<CustomListAdapter.ViewHolder>() {

    private val dataSet: List<Picture>

    init {
        this.dataSet = dataSet.sortedBy { it.date }.reversed()
    }

    private var lastPosition = -1

    inner class ViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.run {
            val picture = dataSet[position]

            listItemCard.setOnClickListener { itemClick.onPictureClick(picture) }
            listItemTitle.text = picture.title
            listPicture.load(picture.image)

            setAnimation(root, position)
        }
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation =
                AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.list_animation)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun getItemCount() = dataSet.size
}