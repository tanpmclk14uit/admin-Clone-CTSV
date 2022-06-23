package com.example.admin_bookmarket

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.admin_bookmarket.data.common.AppUtil
import com.example.admin_bookmarket.data.model.MyUser
import com.example.admin_bookmarket.data.model.StudentAccount
import com.example.admin_bookmarket.databinding.ActivityStudentProfileBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityStudentProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBackButtonClick()
        loadData(AppUtil.currentOrder.studentEmail)
    }
    private val db = Firebase.firestore
    private val dbAccountsReference = db.collection("accounts")

    private fun loadData(email: String) {
        dbAccountsReference.document(email).get()
            .addOnSuccessListener { result ->
                val userMap = result["user"] as HashMap<*, *>
                val recentUser: MyUser = MyUser(
                    fullName = userMap["fullName"].toString(),
                    gender = userMap["gender"].toString(),
                    birthDay = userMap["birthDay"].toString(),
                    studentId = userMap["studentId"].toString(),
                    career = userMap["career"].toString(),
                    studyClass = userMap["studyClass"].toString(),
                    address = userMap["address"].toString(),
                )
                AppUtil.currentStudent.user = recentUser
                setInfoView(AppUtil.currentStudent)
            }
    }


    @SuppressLint("SetTextI18n")
    private fun setInfoView(user: StudentAccount){
        binding.email.text = user.email
        binding.userName.text = user.user.fullName
        binding.address.text = user.user.address
        binding.birthDay.text = user.user.birthDay
        binding.studentId.text = user.user.studentId
        binding.studentClass.text = user.user.studyClass
        binding.career.text = user.user.career
        if(user.user.gender =="Female"){
            binding.avatar.setImageResource(R.drawable.ic_female)
        }else{
            binding.avatar.setImageResource(R.drawable.ic_male)
        }
    }

    private  fun setBackButtonClick(){
        binding.imgBack.setOnClickListener {
            onBackPressed()
            finish()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}