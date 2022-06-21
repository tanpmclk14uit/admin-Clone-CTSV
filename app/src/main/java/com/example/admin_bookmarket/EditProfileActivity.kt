package com.example.admin_bookmarket

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import com.example.admin_bookmarket.ViewModel.UserViewModel
import com.example.admin_bookmarket.data.common.AppUtil
import com.example.admin_bookmarket.data.model.Information
import com.example.admin_bookmarket.databinding.ActivityEditProfileBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.regex.Pattern

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {
    val viewModel: UserViewModel by viewModels()
    lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()
        setSaveButtonCommand()
        setBackButton()
    }

    private fun setBackButton() {
        binding.imgBack.setOnClickListener {
            finish()
        }
    }

    private fun setSaveButtonCommand() {
        binding.btnSaveProfile.setOnClickListener {
            if (isValidName() && isValidPhoneNumber() && !isEmptyInformation()) {
                val information =
                    Information(
                        fullName = binding.edtName.text.toString(),
                        introduction = binding.edtIntroduction.text.toString(),
                        address = binding.edtAddress.text.toString(),
                        phoneNumber = binding.edtPhoneNumber.text.toString(),
                        webSite = binding.edtWebsite.text.toString(),
                    )
                viewModel.updateUserInfo(information)
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                startActivity(Intent(baseContext, MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(this, "Thông tin không hợp lệ!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isEmptyInformation(): Boolean {
        return binding.edtName.text.isNullOrBlank() &&
                binding.edtIntroduction.text.isNullOrBlank() &&
                binding.edtAddress.text.isNullOrBlank() &&
                binding.edtPhoneNumber.text.isNullOrBlank() &&
                binding.edtWebsite.text.isNullOrBlank()
    }

    private fun isValidName(): Boolean {
        return if (binding.edtName.text.isEmpty()) {
            binding.edtName.error = "Name can not empty"
            false
        } else {
            return if (isNameContainNumberOrSpecialCharacter(binding.edtName.text.toString())) {
                binding.edtName.error = "Name can not contain number of special character"
                false
            } else {
                binding.edtName.error = null
                true
            }
        }
    }

    private fun isNameContainNumberOrSpecialCharacter(name: String): Boolean {
        var hasNumber: Boolean = Pattern.compile(
            "[0-9]"
        ).matcher(name).find()
        var hasSpecialCharacter: Boolean = Pattern.compile(
            "[!@#$%&.,\"':;?*()_+=|<>?{}\\[\\]~-]"
        ).matcher(name).find()
        return hasNumber || hasSpecialCharacter
    }

    private fun isValidPhoneNumber(): Boolean {
        var result = false
        if (binding.edtPhoneNumber.text.isNotEmpty()) {
            result = Pattern.compile(
                "^[+]?[0-9]{10,13}\$"
            ).matcher(binding.edtPhoneNumber.text).find()
            if (!result) {
                binding.edtPhoneNumber.error = "Please enter right phone number!"
            }
        }

        return result
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun setData() {
        val information = viewModel.getUserInfo()
        binding.edtName.setText(information.fullName)
        binding.edtWebsite.setText(information.webSite)
        binding.edtAddress.setText(information.address)
        binding.edtPhoneNumber.setText(information.phoneNumber)
        binding.edtIntroduction.setText(information.introduction)
    }
}