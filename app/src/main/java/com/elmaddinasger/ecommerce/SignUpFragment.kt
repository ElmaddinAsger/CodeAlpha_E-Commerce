package com.elmaddinasger.ecommerce

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.elmaddinasger.ecommerce.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth


class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignUp.setOnClickListener {
            val username  = binding.inpedtUsername.text.toString()
            val email = binding.inpedtEmail.text.toString()
            val password = binding.inpedtPassword.text.toString()
            val confirmPassword = binding.inpedtConfirmPassword.text.toString()

            signUp(username,email, password,confirmPassword)
        }
    }

    private fun signUpError (message: String) {
        binding.txtvwErrorMessage.apply {
            text = message
            visibility = View.VISIBLE
        }
    }

    private fun signUp(username: String, email: String, password: String, confirmPassword: String) {
            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            if (password == confirmPassword) {
                val auth = FirebaseAuth.getInstance()
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.sendEmailVerification()?.addOnCompleteListener { emailTask ->
                                if (emailTask.isSuccessful) {
                                    findNavController().navigate(R.id.action_signUpFragment_to_successSignUpFragment)
                                } else {
                                    signUpError(getString(R.string.unsuccessful_send_email))

                                }
                            }
                        } else {
                            //val error = task.exception?.message
                            signUpError(getString(R.string.invalid_email_or_password))

                        }
                    }
            } else {
                signUpError(getString(R.string.confirm_incorrect))
            }
        } else {
            signUpError(getString(R.string.empty_info))
        }
    }



}