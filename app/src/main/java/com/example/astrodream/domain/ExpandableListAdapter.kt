package com.example.astrodream.domain

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.astrodream.R


class ExpandableListAdapter (val context: Context): BaseExpandableListAdapter() {
    private val listButtons = ArrayList<String>()
    private val listAsteroids = HashMap<String, ArrayList<Asteroids>>()

    override fun getGroup(groupPosition: Int): Any {
        return listButtons.get(groupPosition)
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
        if (convertView == null) {
            val li: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val convertView = li.inflate(R.layout.btn_asteroids, null)
        }

        val tvListHeader: TextView = convertView?.findViewById(R.id.tv_btn_name) as TextView
        tvListHeader.setTypeface(null, Typeface.NORMAL)
        tvListHeader.text = headerTitle

        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return listAsteroids[listButtons[groupPosition]]?.size ?: -1
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any? {
        return listAsteroids[listButtons[groupPosition]]?.get(childPosition)
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition as Long
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val childAsteroid = getChild(groupPosition, childPosition) as Asteroids?

        if (convertView == null) {
          val li: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val convertView = li.inflate(R.layout.item_btn_asteroids, null)
        }
        val txtListChild: TextView = convertView?.findViewById(R.id.tv_name_asteroid) as TextView
        val txtDataListChild: TextView = convertView?.findViewById(R.id.tv_date_asteroid) as TextView

        txtListChild.text = childAsteroid?.name
        txtDataListChild.text = childAsteroid?.date
        return convertView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition as Long
    }

    override fun getGroupCount(): Int {
        return listButtons.size
    }

    fun addListButtons(list: ArrayList<String>){
        listButtons.addAll(list)
    }

    fun addListAsteroids(map: HashMap<String, ArrayList<Asteroids>>){
        listAsteroids.putAll(map)
    }
}