package com.example.mda

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thphuoc.ssl.SimpleStateLayout
import kotlinx.android.synthetic.main.activity_state.*

class StateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state)

        btnError.setOnClickListener {
            simpleStateLayout.showState(SimpleStateLayout.State.APP_ERROR)
        }

        btnEmpty.setOnClickListener {
            simpleStateLayout.showState(SimpleStateLayout.State.EMPTY)
        }

        btnProgress.setOnClickListener {
            simpleStateLayout.showState(SimpleStateLayout.State.LOADING)
        }
        btnContent.setOnClickListener {
            simpleStateLayout.showState(SimpleStateLayout.State.CONTENT)
        }
        simpleStateLayout.showState(SimpleStateLayout.State.EMPTY)
    }
}