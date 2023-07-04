package hr.tvz.android.androidchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import hr.tvz.android.androidchat.databinding.FragmentGroupsBinding
import hr.tvz.android.androidchat.ui.grouplist.GroupClick
import hr.tvz.android.androidchat.ui.grouplist.GroupItem
import hr.tvz.android.androidchat.ui.grouplist.GroupListAdapter
import hr.tvz.android.androidchat.viewmodel.MainViewModel


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

        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = GroupListAdapter(
            mutableListOf(),
            object : GroupClick {
                override fun onClick(groupId: GroupItem) {
                }
            }
        )
        binding.groupAdapter = adapter
        binding.layoutManager = LinearLayoutManager(requireContext())

        viewModel.groupItems.observe(viewLifecycleOwner) {
            adapter.groups.clear()
            adapter.groups.addAll(it)
        }

        return binding.root
    }
}