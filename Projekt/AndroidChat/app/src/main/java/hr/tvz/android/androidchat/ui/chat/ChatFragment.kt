package hr.tvz.android.androidchat.ui.chat

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import hr.tvz.android.androidchat.R
import hr.tvz.android.androidchat.databinding.FragmentChatBinding
import hr.tvz.android.androidchat.viewmodel.ChatViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {
    private lateinit var viewModel: ChatViewModel

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get()
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ChatAdapter(
            viewModel.messages.value ?: mutableListOf(),
            viewModel.profile,
            requireContext()
        )
        binding.chatAdapter = adapter
        binding.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
            reverseLayout = true
        }

        viewModel.messages.observe(viewLifecycleOwner) {
            adapter.messages.clear()
            adapter.messages.addAll(it)
            adapter.notifyDataSetChanged()
        }

        viewModel.clearMessage.observe(viewLifecycleOwner) {
            if (it) {
                binding.newMessageEditText.text.clear()
            }
        }

        binding.chatAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.view_members -> {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val users = viewModel.getMembers()

                        val recyclerView = RecyclerView(requireContext()).also {
                            it.layoutManager = LinearLayoutManager(requireContext())
                            it.adapter = GroupMembersAdapter(
                                users,
                                viewModel.profile,
                                viewModel.amIOwner,
                                viewModel
                            )
                        }

                        requireActivity().runOnUiThread {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(getString(R.string.group_members))
                                .setView(recyclerView)
                                .show()
                        }
                    }
                    true
                }

                R.id.add_member -> {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val allUsers = viewModel.getUsers()
                        val arrayAdapter = ArrayAdapter<String>(
                            requireContext(),
                            android.R.layout.select_dialog_singlechoice
                        ).apply {
                            allUsers.forEach { user ->
                                add(user.displayName)
                            }
                        }

                        requireActivity().runOnUiThread {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(getString(R.string.add_member))
                                .setAdapter(
                                    arrayAdapter
                                ) { _, which ->
                                    viewModel.addUserToGroup(allUsers[which])
                                }
                                .show()
                        }
                    }
                    true
                }

                else -> false
            }
        }
    }
}