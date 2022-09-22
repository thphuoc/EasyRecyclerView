package com.example.mda

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.thphuoc.erv.LoadMoreViewBinder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.addItem(HeaderItemViewBinder())
        recyclerView.setLoadMore(LoadMoreViewBinder {
            Handler().postDelayed({
                recyclerView.loadCompleted()
            }, 3000)
        })
        repeat(60) {
            recyclerView.addItem(HozItemViewBinder())
        }
    }
}