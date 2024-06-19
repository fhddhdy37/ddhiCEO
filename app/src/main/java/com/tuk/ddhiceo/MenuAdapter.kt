package com.tuk.ddhiceo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(
    private val menuList: List<MenuItem>,
    private val itemClickListener: OnMenuItemClickListener
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    interface OnMenuItemClickListener {
        fun onDeleteClick(position: Int)
        fun onEditClick(position: Int)
    }

    inner class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val menuNameTextView: TextView = view.findViewById(R.id.menu_name_text_view)
        val menuPriceTextView: TextView = view.findViewById(R.id.menu_price_text_view)
        val editButton: Button = view.findViewById(R.id.edit_button)
        val deleteButton: Button = view.findViewById(R.id.delete_button)
        val buttonContainer: LinearLayout = view.findViewById(R.id.button_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuItem = menuList[position]
        holder.menuNameTextView.text = menuItem.name
        holder.menuPriceTextView.text = menuItem.price

        holder.menuNameTextView.setOnClickListener {
            if (holder.buttonContainer.visibility == View.GONE) {
                holder.buttonContainer.visibility = View.VISIBLE
            } else {
                holder.buttonContainer.visibility = View.GONE
            }
        }

        holder.editButton.setOnClickListener {
            itemClickListener.onEditClick(position)
            holder.buttonContainer.visibility = View.GONE
        }

        holder.deleteButton.setOnClickListener {
            itemClickListener.onDeleteClick(position)
            holder.buttonContainer.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }
}