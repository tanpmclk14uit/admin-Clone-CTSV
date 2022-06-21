package com.example.admin_bookmarket.ViewModel

import androidx.lifecycle.ViewModel
import com.example.admin_bookmarket.data.model.AccountRepository
import com.example.admin_bookmarket.data.model.AppAccount
import com.example.admin_bookmarket.data.model.Information
import com.example.admin_bookmarket.data.model.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    fun getUserInfo(): Information
    {
        userRepository.loadData()
        return userRepository.information
    }
    fun getAccountInfo(): AppAccount {
        accountRepository.loadData()
        return  accountRepository.account
    }
    fun updateUserInfo(information: Information){
        userRepository.updateUserData(information)
    }


}