package com.example.admin_bookmarket

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.admin_bookmarket.ViewModel.AddItemViewModel
import com.example.admin_bookmarket.data.common.AppUtil
import com.example.admin_bookmarket.data.common.Constants
import com.example.admin_bookmarket.databinding.ActivityAddItemBinding
import com.example.admin_bookmarket.ui.login.LoadDialog
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class AddItemActivity : AppCompatActivity() {

    //set up fire storage

    private val reference: StorageReference = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null
    private lateinit var loadDialog: LoadDialog
    lateinit var binding: ActivityAddItemBinding
    private var newBook: MutableMap<String, Any> = mutableMapOf()
    private val viewModel: AddItemViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.idAddBook.setOnClickListener {
            pushImageToStorage()
        }


        binding.idBack.setOnClickListener {
            onBackPressed()
        }
        binding.idThumbnail.setOnClickListener {
            openGallery()
        }
        val statusValue = resources.getStringArray(R.array.kind)
        val arrayAdapter = ArrayAdapter(
            binding.root.context,
            R.layout.status_dropdown_item,
            statusValue
        )
        binding.idKind.setAdapter(arrayAdapter)

    }

    private fun checkValidEditText(editText: EditText): Boolean {
        return if (editText.text.isBlank()) {
            editText.error = "Vui lòng điền đầy đủ thông tin"
            false
        } else {
            true
        }
    }

    private fun checkValidKind(): Boolean {
        return if (binding.idKind.text.isNotEmpty()) {
            true
        } else {
            binding.idKind.error = "Chọn loại thông báo"
            false
        }
    }


    private fun pushImageToStorage() {
        if (checkValidEditText(binding.idTitle) &&
            checkValidEditText(binding.idDescription) &&
            checkValidKind()
        ) {
            if (imageUri != null) {
                loadDialog = LoadDialog(this)
                loadDialog.startLoading()
                val imgId =
                    System.currentTimeMillis().toString() + "." + getFileExtension(imageUri as Uri)
                val fileRef: StorageReference = reference.child(imgId)

                fileRef.putFile(imageUri as Uri).addOnSuccessListener {
                    fileRef.downloadUrl.addOnSuccessListener {
                        Toast.makeText(this, "Tải hình ảnh thành công", Toast.LENGTH_SHORT)
                            .show()
                        addNewBook(it.toString(), imgId)
                        loadDialog.dismissDialog()
                    }
                }.addOnFailureListener {
                    loadDialog.dismissDialog()
                    Toast.makeText(this, "Tải hình ảnh lên thất bại $it", Toast.LENGTH_SHORT)
                        .show()

                }
            } else {
                loadDialog = LoadDialog(this)
                loadDialog.startLoading()
                val imgId = "default"
                // default image
                val imgURL = "https://tuoitre.uit.edu.vn/wp-content/uploads/2015/07/logo-uit.png"
                addNewBook(imgURL, imgId)
                loadDialog.dismissDialog()
            }

        } else {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        var cr: ContentResolver = contentResolver
        var mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri))
    }

    private fun openGallery() {
        val galleryIntent: Intent = Intent()
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data!!
            binding.idThumbnail.setImageURI(imageUri)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun addNewBook(imgUrl: String, imgId: String) {
        newBook = mutableMapOf(
            "Image" to imgUrl,
            "Name" to binding.idTitle.text.toString(),
            "Kind" to binding.idKind.text.toString(),
            "Description" to binding.idDescription.text.toString(),
            "ImageID" to imgId,
        )
        viewModel.addtoDb(newBook)
        binding.idTitle.setText("", TextView.BufferType.EDITABLE)
        binding.idDescription.setText("", TextView.BufferType.EDITABLE)
        binding.idKind.setText("", TextView.BufferType.EDITABLE)
        binding.idThumbnail.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_add_a_photo_24))
        // binding.idTnBackground.setBackgroundDrawable(resources.getDrawable(R.drawable.add_new_book))
        Toast.makeText(this, "Thêm thông báo thành công", Toast.LENGTH_SHORT).show()
    }

}