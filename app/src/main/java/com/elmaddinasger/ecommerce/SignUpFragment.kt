package com.elmaddinasger.ecommerce

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            val email = binding.inpedtEmail.text.toString()
            val password = binding.inpedtPassword.text.toString()
            if (email.isNotEmpty() || password.isNotEmpty()) {
                signUp(email,password)
            }
        }
    }

    private fun signUp(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()?.addOnCompleteListener { emailTask ->
                        if (emailTask.isSuccessful) {
                            Toast.makeText(requireContext(),"Doğrulama e-postası gönderildi: ${user.email}",Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(),"Doğrulama e-postası gönderilemedi: ${emailTask.exception?.message}",Toast.LENGTH_SHORT).show()
                        }
                    }
                    Toast.makeText(requireContext(),"Success", Toast.LENGTH_SHORT).show()
                } else {
                    val error = task.exception?.message
                    Toast.makeText(requireContext(),"Unsuccess", Toast.LENGTH_SHORT).show()
                }
            }
    }



}