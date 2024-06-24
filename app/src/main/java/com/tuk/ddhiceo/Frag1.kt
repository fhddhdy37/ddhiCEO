package com.tuk.ddhiceo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.widget.Toast

class Frag1 : Fragment(), MenuAdapter.OnMenuItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var menuList: MutableList<MenuItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag1, container, false)

        val button: Button = view.findViewById(R.id.menu_button)
        button.setOnClickListener {
            val intent = Intent(activity, MenuInputActivity::class.java)
            startActivity(intent)
        }

        recyclerView = view.findViewById(R.id.menu_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        menuList = loadMenuList()
        menuAdapter = MenuAdapter(menuList, this)
        recyclerView.adapter = menuAdapter

        return view
    }

    override fun onDeleteClick(position: Int) {
        menuList.removeAt(position)
        menuAdapter.notifyItemRemoved(position)
        saveMenuList(menuList)
        Toast.makeText(activity, "메뉴가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onEditClick(position: Int) {
        val intent = Intent(activity, MenuInputActivity::class.java).apply {
            putExtra("position", position)
            putExtra("menu_name", menuList[position].name)
            putExtra("menu_price", menuList[position].price)
        }
        startActivityForResult(intent, EDIT_MENU_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_MENU_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val position = data?.getIntExtra("position", -1) ?: -1
            val editedName = data?.getStringExtra("menu_name") ?: ""
            val editedPrice = data?.getStringExtra("menu_price") ?: ""
            if (position != -1) {
                menuList[position] = MenuItem(editedName, editedPrice)
                menuAdapter.notifyItemChanged(position)
                saveMenuList(menuList)
            }
        }
    }

    private fun loadMenuList(): MutableList<MenuItem> {
        val sharedPref = activity?.getSharedPreferences("MenuData", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref?.getString("menu_list", null)
        val type = object : TypeToken<MutableList<MenuItem>>() {}.type
        return if (json == null) {
            mutableListOf()
        } else {
            gson.fromJson(json, type)
        }
    }

    private fun saveMenuList(menuList: MutableList<MenuItem>) {
        val sharedPref = activity?.getSharedPreferences("MenuData", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(menuList)
        with(sharedPref?.edit()) {
            this?.putString("menu_list", json)
            this?.apply()
        }
    }

    private fun showSnackbar(message: String) {
        view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show() }
    }

    companion object {
        const val EDIT_MENU_REQUEST_CODE = 101
    }
}