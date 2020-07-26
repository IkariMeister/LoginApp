package com.ikarimeister.loginapp.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.ikarimeister.loginapp.R
import com.ikarimeister.loginapp.databinding.ActivityLoginBinding
import com.ikarimeister.loginapp.domain.model.Email
import com.ikarimeister.loginapp.domain.model.EmailValidationErrors
import com.ikarimeister.loginapp.domain.model.IncorrectCredentials
import com.ikarimeister.loginapp.domain.model.LoginError
import com.ikarimeister.loginapp.domain.model.NoConection
import com.ikarimeister.loginapp.domain.model.NotAnEmail
import com.ikarimeister.loginapp.domain.model.NotValidCharsInEmail
import com.ikarimeister.loginapp.domain.model.Password
import com.ikarimeister.loginapp.domain.model.PasswordValidationErrors
import com.ikarimeister.loginapp.domain.model.TooLongEmail
import com.ikarimeister.loginapp.domain.model.TooLongPassword
import com.ikarimeister.loginapp.domain.model.TooShortPassword
import com.ikarimeister.loginapp.domain.model.ValidationErrors
import com.ikarimeister.loginapp.ui.model.ValidationMessage
import com.ikarimeister.loginapp.ui.presenter.LoginPresenter
import com.ikarimeister.loginapp.ui.view.LoginView
import org.koin.androidx.scope.lifecycleScope
import org.koin.core.parameter.parametersOf

class LoginActivity : AppCompatActivity(), LoginView {

    companion object {
        fun navigate(activity: Activity) {
            activity.startActivity(Intent(activity, LoginActivity::class.java))
        }
    }

    private val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val presenter: LoginPresenter by lifecycleScope.inject { parametersOf(this) }

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
        when (error) {
            IncorrectCredentials -> {
                binding.error.text = getString(R.string.login_failed)
                binding.error.visibility = View.VISIBLE
            }
            NoConection -> {
                Snackbar.make(binding.root, R.string.no_connection, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.retry) { loginClick() }
                        .setBackgroundTint(resources.getColor(R.color.primaryDarkColor))
                        .setActionTextColor(resources.getColor(R.color.primaryTextColor))
                        .show()
            }
        }
    }

    override fun showError(errors: List<ValidationErrors>) {
        val messages = errors.fold(ValidationMessage()) { acc, element ->
            when (element) {
                is EmailValidationErrors -> {
                    val message = when (element) {
                        NotAnEmail -> R.string.not_an_email
                        NotValidCharsInEmail -> R.string.not_valid_email
                        TooLongEmail -> R.string.too_long_email
                    }
                    acc.copy(emailError = "${getString(message)}\n${acc.emailError}".trim())
                }
                is PasswordValidationErrors -> {
                    val message = when (element) {
                        TooLongPassword -> R.string.too_long_password
                        TooShortPassword -> R.string.too_short_password
                    }
                    acc.copy(passwordError = "${acc.passwordError}\n${getString(message)}")
                }
            }
        }
        binding.error.visibility = View.VISIBLE
        binding.error.text = "${messages.passwordError}\n${messages.emailError}".trim()
    }

    override fun navigateToLoggedScreen() {
        MainActivity.navigate(this)
    }

    override fun showLoginForm() {
        binding.loading.visibility = View.GONE
        binding.error.visibility = View.GONE
        binding.login.visibility = View.VISIBLE
        binding.password.visibility = View.VISIBLE
        binding.imageView.visibility = View.VISIBLE
        binding.username.visibility = View.VISIBLE
        binding.login.setOnClickListener { loginClick() }
    }

    private fun loginClick() {
        val email = Email(binding.username.text.toString())
        val password = Password(binding.password.text.toString())
        presenter.doLogin(email, password)
    }
}