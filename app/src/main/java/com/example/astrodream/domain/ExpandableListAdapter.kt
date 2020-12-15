package com.example.astrodream.domain

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.astrodream.R
import com.example.astrodream.ui.asteroids.AsteroidActivity


class ExpandableListAdapter (val context: AsteroidActivity): BaseExpandableListAdapter() {
    var listButtons = ArrayList<String>()
    var listAsteroids = LinkedHashMap<String, ArrayList<Asteroid>>()

    override fun getGroup(groupPosition: Int): Any {
        return listButtons[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val headerTitle = getGroup(groupPosition) as String
        var view = convertView
        if (convertView == null) {
            val li: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = li.inflate(R.layout.btn_asteroids, null)
        }

        val tvListHeader: TextView = view?.findViewById(R.id.tv_btn_list) as TextView
        tvListHeader.setTypeface(null, Typeface.NORMAL)
        tvListHeader.text = headerTitle

        (view.findViewById(R.id.ic_btn) as ImageView).setImageResource(if (isExpanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)
        view.setBackgroundResource(if (isExpanded) R.drawable.button_style_click else R.drawable.button_style)

        context.viewModel.listResults

        return view
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return listAsteroids[listButtons[groupPosition]]?.size ?: 0
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any? {
        return listAsteroids[listButtons[groupPosition]]?.get(childPosition)
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val childAsteroid = getChild(groupPosition, childPosition) as Asteroid?
        var view = convertView
        if (convertView == null) {
          val li: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = li.inflate(R.layout.item_btn_asteroids, null)
        }
        val txtListChild: TextView = view?.findViewById(R.id.tv_name_asteroid) as TextView
        val txtDataListChild: TextView = view.findViewById(R.id.tv_date_asteroid) as TextView

        view.setBackgroundResource(if (isLastChild) R.drawable.button_style_click_itens else R.color.gigas)

        txtListChild.text = childAsteroid?.name
        txtDataListChild.text = childAsteroid?.date
        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return listButtons.size
    }

    fun addListButtons(list: ArrayList<String>){
        listButtons.addAll(list)
        notifyDataSetChanged()
    }

    fun addListAsteroids(map: HashMap<String, ArrayList<Asteroid>>){
        listAsteroids.putAll(map)
        notifyDataSetChanged()
    }
}
