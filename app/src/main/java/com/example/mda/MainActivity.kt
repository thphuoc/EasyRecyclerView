package com.example.mda

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repeat(50) {
            listView.addItem(HozItemViewBinder())
        }


        btnAddTop.setOnClickListener {
            listView.addItem(DataItemViewBinder(this, UserDAO("User Top")) { data ->
                listView.removeItem(data)
            }, 0)
        }

        edtSearch.addTextChangedListener { text ->
            listView.filterBy {
                when (it) {
                    is DataItemViewBinder -> {
                        it.user.name.lowercase().contains(text.toString().lowercase())
                    }
                    else -> true
                }
            }
        }
    }
}