package com.fairmoney.assignment.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.fairmoney.assignment.R
import com.fairmoney.assignment.databinding.ActivityUserDetailBinding
import com.fairmoney.assignment.db.User
import com.fairmoney.assignment.db.UserDb
import com.fairmoney.assignment.db.UserDetail
import com.fairmoney.assignment.network.ServiceApi
import com.fairmoney.assignment.repo.UserRepository
import com.fairmoney.assignment.utils.getFormattedDobForUserDetails
import com.fairmoney.assignment.utils.isNetworkConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

class UserDetailActivity : AppCompatActivity() {
    companion object {
        private const val USER = "user"
        fun getIntent(context: Context, user: User?) =
            Intent(context, UserDetailActivity::class.java).apply {
                putExtra(USER, user)
            }
    }

    private lateinit var binding: ActivityUserDetailBinding

    private val viewModel: UserDetailViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val repo = UserRepository(
                    db = UserDb.create(applicationContext),
                    serviceApi = ServiceApi.create()
                )
                return UserDetailViewModel(repo) as T
            }
        }
    }

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = intent?.getParcelableExtra(USER)
        initUi()

        if (!isNetworkConnected(this)) {
            Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
        }

        user?.id?.let {
            lifecycleScope.launchWhenCreated {
                withContext(Dispatchers.IO) {
                    viewModel.getUser(it).collectLatest {
                        runOnUiThread {
                            initUi(it)
                        }
                    }
                }
            }
        }
    }

    private fun initUi(userDetail: UserDetail? = null) {
        if (userDetail != null) {
            if (!userDetail.picture.isNullOrBlank()) {
                Glide.with(this).load(userDetail.picture)
                    .centerCrop()
                    .into(binding.image)
            }

            binding.title.text = "${userDetail.title}. ${userDetail.firstName} ${userDetail.lastName}"

            binding.gender.text = getString(R.string.gender_value, userDetail.gender)
            binding.email.text = getString(R.string.email_value, userDetail.email)
            binding.phone.text = getString(R.string.phone_value, userDetail.phone)
            userDetail.dateOfBirth?.let {
                binding.dob.text = getString(R.string.dob_value, getFormattedDobForUserDetails(it))
            }
        } else {
            if (!user?.picture.isNullOrBlank()) {
                Glide.with(this).load(user?.picture)
                    .centerCrop()
                    .into(binding.image)
            }

            binding.title.text = "${user?.title}. ${user?.firstName} ${user?.lastName}"
        }
    }
}