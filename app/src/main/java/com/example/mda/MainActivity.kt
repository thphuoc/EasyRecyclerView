package com.example.mda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        repeat(10) {
            listView.addItem(DataItemViewBinder(this, "Item1 $it") { data ->
                listView.removeItem(data)
            })
        }

        listView.addItem(HeaderItemViewBinder(this, "Header1"))

        repeat(10) {
            listView.addItem(DataItemViewBinder(this, "Item2 $it") { data ->
                listView.removeItem(data)
            })
        }

        listView.addItem(HeaderItemViewBinder(this, "Header2"))

        repeat(10) {
            listView.addItem(DataItemViewBinder(this, "Item3 $it") { data ->
                listView.removeItem(data)
            })
        }

        btnAddTop.setOnClickListener {
            listView.addItem(DataItemViewBinder(this, "Item Top") { data ->
                listView.removeItem(data)
            }, 0)
        }

        btnAddBottom.setOnClickListener {
            listView.addItem(DataItemViewBinder(this, "Item Bottom") { data ->
                listView.removeItem(data)
            }, listView.size())
        }
    }
}