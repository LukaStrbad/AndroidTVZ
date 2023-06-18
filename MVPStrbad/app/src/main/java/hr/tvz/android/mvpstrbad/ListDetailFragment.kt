package hr.tvz.android.mvpstrbad

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import hr.tvz.android.mvpstrbad.databinding.FragmentListDetailBinding
import hr.tvz.android.mvpstrbad.extensions.parcelable
import hr.tvz.android.mvpstrbad.model.Picture
import hr.tvz.android.mvpstrbad.viewmodels.ListDetailViewModel
import kotlinx.coroutines.launch

class ListDetailFragment : Fragment() {
    private var _binding: FragmentListDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ListDetailViewModel by viewModels {
        SavedStateViewModelFactory(requireActivity().application, this, arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListDetailBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        binding.run {
            pictureDetailsImage.load(viewModel?.image)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.shareState.collect { state ->
                    if (state) {
                        val dialog =
                            AlertDialog.Builder(this@ListDetailFragment.requireActivity()).run {
                                setMessage(getString(R.string.share_message))
                                setTitle(getString(R.string.share))

                                setPositiveButton(R.string.yes) { _, _ ->
                                    Intent().also { intent ->
                                        intent.action = "hr.tvz.android.fragmentistrbad.SHARE"
                                        intent.putExtra("picture", viewModel.picture)
                                        this@ListDetailFragment.requireActivity()
                                            .sendBroadcast(intent)
                                    }
                                }
                                setNegativeButton(R.string.no) { _, _ -> /* Ignored */ }

                                create()
                            }

                        dialog.show()
                    }
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