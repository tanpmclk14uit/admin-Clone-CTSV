package com.example.admin_bookmarket


import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.admin_bookmarket.ViewModel.ItemDetailViewModel
import com.example.admin_bookmarket.data.common.Constants
import com.example.admin_bookmarket.data.model.Book
import com.example.admin_bookmarket.databinding.ActivityItemDetailBinding
import com.example.admin_bookmarket.ui.login.LoadDialog
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt


@AndroidEntryPoint
class ItemDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityItemDetailBinding
    private val viewModel: ItemDetailViewModel by viewModels()
    private var displayItem: Book = Book()
    private var imageUri: Uri? = null
    private var oldImageUrl: String? = ""
    private var oldImageId: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val statusValue = resources.getStringArray(R.array.kind)
        val arrayAdapter = ArrayAdapter(
            binding.root.context,
            R.layout.status_dropdown_item,
            statusValue
        )
        binding.idKind.setAdapter(arrayAdapter)

        viewModel.book.observe(this, changeObserver)

        setupOnClickListener()


        binding.idDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this@ItemDetailActivity)
            builder.setMessage("Bạn chắc chắn muốn Xóa thông báo này?")
                .setCancelable(false)
                .setPositiveButton("Đồng ý") { dialog, id ->
                    // Delete selected note from database
                    deleteCurrentBook()
                }
                .setNegativeButton("Không") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

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
            oldImageId = ""
            oldImageUrl = ""
        }
    }

    private fun deleteCurrentBook() {
        viewModel.deleteBook(displayItem)
        startActivity(Intent(baseContext, MainActivity::class.java))
    }

    private val changeObserver = Observer<Book> { value ->
        value?.let {
            displayItem = value
            binding.idTitle.setText(value.Name, TextView.BufferType.EDITABLE)
            binding.idKind.setText(value.Kind, false)
            binding.idDescription.setText(value.Description, TextView.BufferType.EDITABLE)
            oldImageUrl = value.Image
            oldImageId = value.imageId
            it.Image?.let { uri -> loadImageFromUri(Uri.parse(uri)) }
        }
    }

    private fun loadImageFromUri(uri: Uri) {
        Glide
            .with(baseContext)
            .load(uri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerInside()
            .placeholder(Constants.DEFAULT_IMG_PLACEHOLDER)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.idThumbnail)
    }

    private fun setupOnClickListener() {
        binding.idBack.setOnClickListener { onBackPressed() }
        binding.idUpdate.setOnClickListener { updateToDb() }
        binding.idThumbnail.setOnClickListener { openGallery() }
        binding.dismissImage.setOnClickListener { dismissChangeButton() }
    }

    private fun dismissChangeButton() {
        oldImageUrl = displayItem.Image
        oldImageId = displayItem.imageId
        displayItem.Image?.let { uri -> loadImageFromUri(Uri.parse(uri)) }

    }

    private fun checkValidEditText(editText: EditText): Boolean {
        return if (editText.text.isBlank()) {
            editText.error = "Vui lòng điển đầy đủ thông tin"
            false
        } else {
            true
        }
    }

    private fun checkValidKind(): Boolean {
        return if (binding.idKind.text.toString() != "Kind") {
            true
        } else {
            binding.idKind.error = "Chọn loại thông báo"
            false
        }
    }

    private val reference: StorageReference = FirebaseStorage.getInstance().reference
    private lateinit var loadDialog: LoadDialog
    private fun updateToDb() {
        if (checkValidEditText(binding.idTitle) &&
            checkValidKind() &&
            checkValidEditText(binding.idDescription)
        ) {
            val newBook = Book(
                displayItem.id,
                oldImageUrl,
                binding.idTitle.text.toString(),
                binding.idKind.text.toString(),
                binding.idDescription.text.toString(),
                oldImageId,
            )
            if (newBook != displayItem) {
                if (imageUri != null) {
                    loadDialog = LoadDialog(this)
                    loadDialog.startLoading()
                    val imgId =
                        System.currentTimeMillis()
                            .toString() + "." + getFileExtension(imageUri as Uri)
                    val fileRef: StorageReference = reference.child(imgId)
                    fileRef.putFile(imageUri as Uri).addOnSuccessListener {
                        fileRef.downloadUrl.addOnSuccessListener {
                            Toast.makeText(this, "Tải ảnh lên thành công!", Toast.LENGTH_SHORT).show()
                            val desertRef = reference.child(displayItem.imageId!!)
                            desertRef.delete()
                            newBook.imageId = imgId
                            newBook.Image = it.toString()
                            viewModel.updateToDb(displayItem.id!!, newBook)
                            loadDialog.dismissDialog()
                        }
                    }.addOnFailureListener {
                        loadDialog.dismissDialog()
                        Toast.makeText(this, "Tải ảnh lên thất bại", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    viewModel.updateToDb(displayItem.id!!, newBook)
                }
            }else{
                Toast.makeText(this, "Không có gì để cập nhật", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        var cr: ContentResolver = contentResolver
        var mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri))
    }
}