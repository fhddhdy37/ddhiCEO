package com.tuk.ddhiceo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class StoreInfoInputActivity : AppCompatActivity() {

    private lateinit var storeImageInput: ImageView
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_info_input)

        val storeNameInput: EditText = findViewById(R.id.store_name_input)
        val storeAddressInput: EditText = findViewById(R.id.store_address_input)
        val storeHoursInput: EditText = findViewById(R.id.store_hours_input)
        val storePhoneInput: EditText = findViewById(R.id.store_phone_input)
        val storeTagsInput: EditText = findViewById(R.id.store_tags_input)
        storeImageInput = findViewById(R.id.store_image_input)

        storeImageInput.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }

        val submitButton: Button = findViewById(R.id.submit_button)
        submitButton.setOnClickListener {
            val intent = Intent().apply {
                putExtra("storeName", storeNameInput.text.toString())
                putExtra("storeAddress", storeAddressInput.text.toString())
                putExtra("storeHours", storeHoursInput.text.toString())
                putExtra("storePhone", storePhoneInput.text.toString())
                putExtra("storeTags", storeTagsInput.text.toString())
                putExtra("storeImage", selectedImageUri.toString())
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            storeImageInput.setImageURI(selectedImageUri)
        }
    }

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 101
    }
}