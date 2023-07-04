package hr.tvz.android.androidchat.ui.grouplist

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import hr.tvz.android.androidchat.MainActivity
import hr.tvz.android.androidchat.R
import hr.tvz.android.androidchat.databinding.FragmentGroupsBinding
import hr.tvz.android.androidchat.viewmodel.AuthViewModel
import hr.tvz.android.androidchat.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class GroupsFragment : Fragment() {
    private lateinit var viewModel: MainViewModel

    private var _binding: FragmentGroupsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get()
        _binding = FragmentGroupsBinding.inflate(inflater, container, false)

        val authViewModel: AuthViewModel = ViewModelProvider(requireActivity()).get()
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.log_out -> {
                    authViewModel.logout()
                    true
                }

                R.id.create_group -> {
                    val input = EditText(requireContext())
                    input.inputType = InputType.TYPE_CLASS_TEXT;

                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getString(R.string.create_group))
                        .setView(input)
                        .setPositiveButton(
                            getString(R.string.ok)
                        ) { _, _ ->
                            lifecycleScope.launch(Dispatchers.IO) {
                                authViewModel.createGroup(input.text.toString())
                                viewModel.refreshGroups()
                            }
                        }
                        .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                            dialog.cancel()
                        }
                        .show()
                    true
                }

                else -> false
            }
        }

        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()

        if (activity is MainActivity) {
            val adapter = GroupListAdapter(
                mutableListOf(),
                activity
            )
            binding.groupAdapter = adapter
            binding.layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = LinearLayoutManager.VERTICAL
            }

            viewModel.groupItems.observe(viewLifecycleOwner) {
                adapter.groups.clear()
                adapter.groups.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }
    }
}