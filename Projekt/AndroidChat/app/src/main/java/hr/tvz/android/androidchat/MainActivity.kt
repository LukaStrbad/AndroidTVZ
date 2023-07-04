package hr.tvz.android.androidchat

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import dagger.hilt.android.AndroidEntryPoint
import hr.tvz.android.androidchat.databinding.ActivityMainBinding
import hr.tvz.android.androidchat.ui.chat.ChatFragment
import hr.tvz.android.androidchat.ui.grouplist.GroupClick
import hr.tvz.android.androidchat.ui.grouplist.GroupItem
import hr.tvz.android.androidchat.ui.grouplist.GroupsFragment
import hr.tvz.android.androidchat.ui.login.LoginFragment
import hr.tvz.android.androidchat.viewmodel.AuthViewModel
import hr.tvz.android.androidchat.viewmodel.ChatViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), GroupClick {
    private lateinit var binding: ActivityMainBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var chatViewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = ViewModelProvider(this).get()
        chatViewModel = ViewModelProvider(this).get()
        binding = ActivityMainBinding.inflate(layoutInflater)

        supportFragmentManager.commit {
            replace(R.id.nav_host_fragment, LoginFragment())
            setReorderingAllowed(true)
            addToBackStack(null)
        }

        setContentView(binding.root)

        twoPane = binding.sideFragment != null

        authViewModel.loggedIn.observe(this) {
            val fragment = if (it) {
                binding.sideFragment?.visibility = View.VISIBLE
                GroupsFragment()
            } else {
                binding.sideFragment?.visibility = View.GONE
                LoginFragment()
            }

            supportFragmentManager.commit {
                replace(R.id.nav_host_fragment, fragment)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        authViewModel.checkLoggedIn()
    }

    override fun onClick(groupItem: GroupItem) {
        if (twoPane) {
            supportFragmentManager.commit {
                replace<ChatFragment>(R.id.side_fragment)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        } else {
            supportFragmentManager.commit {
                replace<ChatFragment>(R.id.nav_host_fragment)
                setReorderingAllowed(true)
                addToBackStack("chatFragment")
            }
        }

        chatViewModel.groupItem = groupItem
        chatViewModel.refreshMessages()
    }

    companion object {
        var twoPane = false
    }
}