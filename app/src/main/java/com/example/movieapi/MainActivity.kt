package com.example.movieapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.movieapi.model.MovieResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var adapter: Adapter
    private lateinit var layoutManager: LinearLayoutManager
    private var page = 1
    private var totalPage = 1
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        layoutManager = LinearLayoutManager(this)
        swipeRefresh.setOnRefreshListener(this)
        setRecyclerView()
        getMovie(false)
        rvLayout.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val total = adapter.itemCount
                if (!isLoading && page < totalPage){
                    if(visibleItemCount + pastVisibleItem >= total){
                        page++
                        getMovie(false)
                    }
                }
                super.onScrolled(recyclerView, dx, dy)

            }
        })
    }

    private fun getMovie(isOnRefresh: Boolean) {
        isLoading = true
        if(!isOnRefresh) progressBar.visibility = View.VISIBLE
        val parameters = HashMap<String, String>()
        parameters["page"] = page.toString()
        if(page == 1){
            RetrofitClient.instance.getMovie(parameters).enqueue(object : Callback<MovieResponse>{
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    totalPage = response.body()?.totalPages!!
                    val listResponse = response.body()?.results
                    if (listResponse != null){
                        adapter.addList(listResponse)
                    }
                    progressBar.visibility = View.INVISIBLE
                    isLoading = false
                    swipeRefresh.isRefreshing = false
                }
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            Handler().postDelayed({
                RetrofitClient.instance.getMovie(parameters).enqueue(object : Callback<MovieResponse>{
                    override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                        totalPage = response.body()?.totalPages!!
                        val listResponse = response.body()?.results
                        if (listResponse != null){
                            adapter.addList(listResponse)
                        }
                        progressBar.visibility = View.INVISIBLE
                        isLoading = false
                        swipeRefresh.isRefreshing = false
                    }
                    override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            },2000)
        }
    }
    private fun setRecyclerView() {
        rvLayout.setHasFixedSize(true)
        rvLayout.layoutManager = layoutManager
        adapter = Adapter()
        rvLayout.adapter = adapter
    }

    override fun onRefresh() {
        adapter.clear()
        page = 1
        getMovie(true)
    }
}