package hr.tvz.android.mvpstrbad

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import coil.load
import hr.tvz.android.mvpstrbad.databinding.FragmentListDetailBinding
import hr.tvz.android.mvpstrbad.extensions.parcelable
import hr.tvz.android.mvpstrbad.model.Picture

class ListDetailFragment : Fragment() {
    private var _binding: FragmentListDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListDetailBinding.inflate(inflater, container, false)

        val picture = arguments?.parcelable<Picture>("picture")

        binding.run {
            pictureDetailsTitle.text = picture?.title
            pictureDetailsDescription.text = picture?.description

            pictureDetailsImage.load(picture?.pictureUrl)
            pictureDetailsImage.setOnClickListener {
                val intent =
                    Intent(this@ListDetailFragment.requireActivity(), PictureViewActivity::class.java)
                intent.putExtra("picture", picture)
                startActivity(intent)
            }

            pictureDetailsLinkButton.visibility = View.GONE
            if (picture != null) {
                pictureDetailsLinkButton.visibility = View.VISIBLE
                pictureDetailsLinkButton.setOnClickListener {
                    val browserIntent = Intent(Intent.ACTION_VIEW, picture.webLink.toUri())
                    startActivity(browserIntent)

                }
            }

            pictureDetailsShareButton.visibility = View.GONE
            if (picture!= null) {
                pictureDetailsShareButton.visibility = View.VISIBLE
                pictureDetailsShareButton.setOnClickListener {
                    val dialog = AlertDialog.Builder(this@ListDetailFragment.requireActivity()).run {
                        setMessage(getString(R.string.share_message))
                        setTitle(getString(R.string.share))

                        setPositiveButton(R.string.yes) { _, _ ->
                            Intent().also { intent ->
                                intent.action = "hr.tvz.android.fragmentistrbad.SHARE"
                                intent.putExtra("picture", picture)
                                this@ListDetailFragment.requireActivity().sendBroadcast(intent)
                            }
                        }
                        setNegativeButton(R.string.no) { _, _ -> /* Ignored */ }

                        create()
                    }

                    dialog.show()
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}