package com.tuk.ddhiceo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class Frag3 : Fragment() {

    private lateinit var storeImageView: ImageView
    private lateinit var storeNameTextView: TextView
    private lateinit var storeAddressTextView: TextView
    private lateinit var storeHoursTextView: TextView
    private lateinit var storePhoneTextView: TextView
    private lateinit var storeTagsTextView: TextView
    private lateinit var editStoreInfoButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag3, container, false)
        storeImageView = view.findViewById(R.id.store_image)
        storeNameTextView = view.findViewById(R.id.store_name)
        storeAddressTextView = view.findViewById(R.id.store_address)
        storeHoursTextView = view.findViewById(R.id.store_hours)
        storePhoneTextView = view.findViewById(R.id.store_phone)
        storeTagsTextView = view.findViewById(R.id.store_tags)
        editStoreInfoButton = view.findViewById(R.id.edit_store_info)

        editStoreInfoButton.setOnClickListener {
            val intent = Intent(activity, StoreInfoInputActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val storeName = data.getStringExtra("storeName")
            val storeAddress = data.getStringExtra("storeAddress")
            val storeHours = data.getStringExtra("storeHours")
            val storePhone = data.getStringExtra("storePhone")
            val storeTags = data.getStringExtra("storeTags")
            val storeImageUri = data.getStringExtra("storeImage")

            storeNameTextView.text = storeName
            storeAddressTextView.text = storeAddress
            storeHoursTextView.text = storeHours
            storePhoneTextView.text = storePhone
            storeTagsTextView.text = storeTags
            Glide.with(this).load(storeImageUri).placeholder(R.drawable.ic_launcher_foreground).into(storeImageView)
        }
    }

    companion object {
        private const val REQUEST_CODE = 100
    }
}