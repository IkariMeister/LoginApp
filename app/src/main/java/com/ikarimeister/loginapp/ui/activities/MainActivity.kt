package com.ikarimeister.loginapp.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.ikarimeister.loginapp.R
import com.ikarimeister.loginapp.databinding.ActivityMainBinding
import com.ikarimeister.loginapp.domain.model.StorageError
import com.ikarimeister.loginapp.ui.presenter.MainPresenter
import com.ikarimeister.loginapp.ui.view.MainView
import org.koin.androidx.scope.lifecycleScope
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity(), MainView {

    companion object {
        fun navigate(activity: Activity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val presenter: MainPresenter by lifecycleScope.inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.logout.setOnClickListener { presenter.doLogout() }
    }

    override fun showError(error: StorageError) {
        Snackbar.make(binding.root, R.string.unknown_error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry) { presenter.doLogout() }
                .show()
    }

    override fun navigateToLoginScreen() {
        LoginActivity.navigate(this)
    }
}