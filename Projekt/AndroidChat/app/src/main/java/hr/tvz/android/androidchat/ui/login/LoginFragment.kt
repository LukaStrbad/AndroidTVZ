package hr.tvz.android.androidchat.ui.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import hr.tvz.android.androidchat.databinding.FragmentLoginBinding

import hr.tvz.android.androidchat.R
import hr.tvz.android.androidchat.model.Auth
import hr.tvz.android.androidchat.viewmodel.AuthViewModel
import hr.tvz.android.androidchat.viewmodel.ChatViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private lateinit var authViewModel: AuthViewModel

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        authViewModel = ViewModelProvider(requireActivity()).get()
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewModel = authViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val chatViewModel: ChatViewModel = ViewModelProvider(requireActivity()).get()

        authViewModel.loggedIn.observe(viewLifecycleOwner) {
            if (it) {
                chatViewModel.refreshProfile()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}