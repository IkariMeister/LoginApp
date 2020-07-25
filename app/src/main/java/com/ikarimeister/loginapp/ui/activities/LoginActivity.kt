package com.ikarimeister.loginapp.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ikarimeister.loginapp.R
import com.ikarimeister.loginapp.databinding.ActivityLoginBinding
import com.ikarimeister.loginapp.domain.model.Email
import com.ikarimeister.loginapp.domain.model.LoginError
import com.ikarimeister.loginapp.domain.model.Password
import com.ikarimeister.loginapp.domain.model.ValidationErrors
import com.ikarimeister.loginapp.ui.presenter.LoginPresenter
import com.ikarimeister.loginapp.ui.view.LoginView

class LoginActivity : AppCompatActivity(), LoginView {

    private val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val presenter: LoginPresenter by lazy { LoginPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        presenter.onStart()
    }

    override fun showLoading() {
        binding.loading.visibility = View.VISIBLE
        binding.login.visibility = View.GONE
        binding.password.visibility = View.GONE
        binding.imageView.visibility = View.GONE
        binding.username.visibility = View.GONE
    }

    override fun hideLoading() {
        binding.loading.visibility = View.GONE
        binding.login.visibility = View.VISIBLE
        binding.password.visibility = View.VISIBLE
        binding.imageView.visibility = View.VISIBLE
        binding.username.visibility = View.VISIBLE
    }

    override fun showError(error: LoginError) {
    }

    override fun showError(errors: List<ValidationErrors>) {
    }

    override fun navigateToLoggedScreen() {
    }

    override fun showLoginForm() {
        binding.loading.visibility = View.GONE
        binding.login.visibility = View.VISIBLE
        binding.password.visibility = View.VISIBLE
        binding.imageView.visibility = View.VISIBLE
        binding.username.visibility = View.VISIBLE
        binding.login.setOnClickListener {
            val email = Email(binding.username.text.toString())
            val password = Password(binding.password.text.toString())
            presenter.doLogin(email, password)
        }
    }
}
