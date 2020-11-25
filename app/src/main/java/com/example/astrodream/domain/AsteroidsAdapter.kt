package com.example.astrodream.domain

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.astrodream.R

class AsteroidsAdapter(
    val listener: OnClickAsteroidsListener,
    var listAsteroids: ArrayList<Asteroids>
) : RecyclerView.Adapter<AsteroidsAdapter.AsteroidsViewHolder>() {

    interface OnClickAsteroidsListener {
        fun onClickAsteroids(position: Int)
    }

    inner class AsteroidsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val tvName: TextView = itemView.findViewById(R.id.tv_name_asteroid)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date_asteroid)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION) listener.onClickAsteroids(
                adapterPosition
            )
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AsteroidsAdapter.AsteroidsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_btn_asteroids, parent, false)
        return AsteroidsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AsteroidsAdapter.AsteroidsViewHolder, position: Int) {
        val asteroid = listAsteroids[position]
        holder.tvName.text = asteroid.name
        holder.tvDate.text = holder.itemView.resources.getString(R.string.date_template).format(asteroid.date)
    }

    override fun getItemCount(): Int = listAsteroids.size
}