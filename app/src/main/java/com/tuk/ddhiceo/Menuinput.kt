package com.tuk.ddhiceo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.widget.Toast

class Menuinput : AppCompatActivity() {
    private var editPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menuinput)

        val menuNameEditText: EditText = findViewById(R.id.menu_name)
        val menuPriceEditText: EditText = findViewById(R.id.menu_price)
        val submitButton: Button = findViewById(R.id.submit_button)

        // Check if we are editing an existing item
        editPosition = intent.getIntExtra("position", -1)
        if (editPosition != -1) {
            menuNameEditText.setText(intent.getStringExtra("menu_name"))
            menuPriceEditText.setText(intent.getStringExtra("menu_price"))
        }

        submitButton.setOnClickListener {
            val menuName = menuNameEditText.text.toString()
            val menuPrice = menuPriceEditText.text.toString()

            if (menuName.isEmpty() || menuPrice.isEmpty()) {
                Toast.makeText(this, "메뉴 이름과 가격을 모두 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Load existing menu items
            val sharedPref = getSharedPreferences("MenuData", MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPref.getString("menu_list", null)
            val type = object : TypeToken<MutableList<MenuItem>>() {}.type
            val menuList: MutableList<MenuItem> = if (json == null) {
                mutableListOf()
            } else {
                gson.fromJson(json, type)
            }

            // Add or update menu item
            if (editPosition == -1) {
                menuList.add(MenuItem(menuName, menuPrice))
                Toast.makeText(this, "새로운 메뉴가 추가되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                menuList[editPosition!!] = MenuItem(menuName, menuPrice)
                Toast.makeText(this, "메뉴가 수정되었습니다.", Toast.LENGTH_SHORT).show()
            }

            // Save updated menu list
            with(sharedPref.edit()) {
                putString("menu_list", gson.toJson(menuList))
                apply()
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}