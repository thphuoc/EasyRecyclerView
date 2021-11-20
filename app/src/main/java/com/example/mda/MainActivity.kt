package com.example.mda

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.thphuoc.erv.LoadMoreViewBinder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        repeat(10) {
            listView.addItem(DataItemViewBinder(this, UserDAO("User1 $it")) { data ->
                listView.removeItem(data)
            })
        }

        listView.disableRecycled(R.layout.item_view_header)

        listView.addItem(HeaderItemViewBinder(this, "Header1"))

        var pageIndex = 0

        repeat(10) {
            listView.addItem(DataItemViewBinder(this, UserDAO("User2 $it")) { data ->
                listView.removeItem(data)
            })
        }

        listView.addItem(HozListViewBinder())

        listView.setLoadMore(LoadMoreViewBinder {
            if (pageIndex < 4) {
                Handler().postDelayed({
                    pageIndex++
                    listView.loadCompleted()
                    repeat(4) {
                        listView.addItem(DataItemViewBinder(this, UserDAO("User2 $pageIndex$it")) { data ->
                            listView.removeItem(data)
                        })
                    }
                }, 1000L)
            } else {
                listView.noMoreToLoad()
            }
        })

        btnAddTop.setOnClickListener {
            listView.addItem(DataItemViewBinder(this, UserDAO("User Top")) { data ->
                listView.removeItem(data)
            }, 0)
        }

        btnAddBottom.setOnClickListener {
            listView.addItem(DataItemViewBinder(this, UserDAO("User Bottom")) { data ->
                listView.removeItem(data)
            }, listView.size())
        }
    }
}