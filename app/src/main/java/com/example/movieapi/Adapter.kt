package com.example.movieapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapi.model.Movie
import kotlinx.android.synthetic.main.list_movie.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Adapter : RecyclerView.Adapter<Adapter.viewHolder>(){
    private var list = ArrayList<Movie>()
    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)

        fun bind(movie: Movie){
            with(itemView){
                val inputDate = movie.releaseDate
                val date = inputFormat.parse(inputDate)
                val outputDate = outputFormat.format(date!!)

                titleMovie.text = movie.title
                overview.text = movie.overview
                voteMovie.text = movie.voteAverage.toString()
                releaseDate.text = outputDate

                Glide.with(posterMovie)
                    .load("https://image.tmdb.org/t/p/original/"+movie.posterPath)
                    .error(R.drawable.ic_launcher_background)
                    .into(posterMovie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_movie, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun addList(items: ArrayList<Movie>){
        list.addAll(items)
        notifyDataSetChanged()
    }

    fun clear(){
        list.clear()
        notifyDataSetChanged()
    }
}