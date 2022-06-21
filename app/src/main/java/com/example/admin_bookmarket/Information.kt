package com.example.admin_bookmarket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.admin_bookmarket.ViewModel.UserViewModel
import com.example.admin_bookmarket.data.common.AppUtil
import com.example.admin_bookmarket.data.model.AppAccount
import com.example.admin_bookmarket.data.model.Information
import com.example.admin_bookmarket.databinding.FragmentInformationBinding
import com.example.admin_bookmarket.ui.login.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Information : Fragment() {
    private lateinit var binding:FragmentInformationBinding
    val viewModel: UserViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInformationBinding.inflate(inflater, container, false)
        setInfoView()
        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            AppUtil.currentInformation = Information()
            AppUtil.currentAccount = AppAccount()
            startActivity(Intent(binding.root.context, LoginActivity::class.java))
            requireActivity().finish()
        }
        binding.btnEdit.setOnClickListener {
            startActivity(Intent(binding.root.context, EditProfileActivity::class.java))

        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setInfoView(){
        val information = viewModel.getUserInfo()
        binding.email.text = viewModel.getAccountInfo().email
        binding.userName.text = information.fullName
        binding.website.text = information.webSite
        binding.address.text = information.address
        binding.phone.text = information.phoneNumber
        binding.introduction.text = information.introduction
    }
}