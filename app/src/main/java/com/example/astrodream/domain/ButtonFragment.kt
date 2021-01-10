package com.example.astrodream.domain

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.astrodream.R

class ButtonFragment(val searchList: ArrayList<String>) : Fragment() {
    var displayList = searchList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_button, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_asteroids, menu)
        val searchItem = menu.findItem(R.id.menu_search_asteroid)
        if (searchItem != null){
            val searchView = searchItem.actionView as
                    SearchView
            val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            editText.hint = "Digite o nome do asteroide..."

            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText!!.isNotEmpty()){
                        displayList.clear()
                        val search = newText.toLowerCase()
                        searchList.forEach {
                            if(it.toLowerCase().contains(search)) displayList.add(it)
                        }
                    } else {
                        displayList.clear()
                        displayList = searchList
                    }
                    return true
                }
            })
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val mSearchMenuItem = menu.findItem(R.id.menu_search_asteroid)
        val searchView = mSearchMenuItem.actionView as SearchView
    }

}