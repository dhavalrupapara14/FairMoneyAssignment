package com.fairmoney.assignment.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.fairmoney.assignment.R
import com.fairmoney.assignment.databinding.ActivityMainBinding
import com.fairmoney.assignment.db.User
import com.fairmoney.assignment.db.UserDb
import com.fairmoney.assignment.network.ServiceApi
import com.fairmoney.assignment.repo.UserRepository
import com.fairmoney.assignment.utils.isNetworkConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val repo = UserRepository(
                    db = UserDb.create(applicationContext),
                    serviceApi = ServiceApi.create()
                )
                return MainViewModel(repo) as T
            }
        }
    }

    private lateinit var adapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!isNetworkConnected(this)) {
            Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
        }

        initAdapter()
    }

    private fun initAdapter() {
        adapter = UserListAdapter(Glide.with(this)) {
            startActivity(
                UserDetailActivity.getIntent(this, it.tag as User)
            )
        }
        binding.list.adapter = adapter.withLoadStateFooter(
            footer = UserLoadStateAdapter(adapter)
        )

        lifecycleScope.launchWhenCreated {
            viewModel.users.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}