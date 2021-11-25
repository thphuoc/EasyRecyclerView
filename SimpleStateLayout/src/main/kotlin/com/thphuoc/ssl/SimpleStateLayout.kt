package com.thphuoc.ssl

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.LayoutRes

class SimpleStateLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val stateView = hashMapOf<State, Int>()
    private val defaultLayout = R.layout.view_state

    init {
        setLayout()
    }

    fun setLayout(
        @LayoutRes initLayout: Int = defaultLayout,
        @LayoutRes loadingLayout: Int = defaultLayout,
        @LayoutRes emptyLayout: Int = defaultLayout,
        @LayoutRes connectionErrorLayout: Int = defaultLayout,
        @LayoutRes appErrorLayout: Int = defaultLayout,
        @LayoutRes contentView: Int = defaultLayout
    ) {
        stateView[State.INIT] = initLayout
        stateView[State.LOADING] = loadingLayout
        stateView[State.EMPTY] = emptyLayout
        stateView[State.CONNECTION_ERROR] = connectionErrorLayout
        stateView[State.APP_ERROR] = appErrorLayout
        stateView[State.CONTENT] = contentView
    }

    fun showState(state: State) {
        removeAllViews()
        LayoutInflater.from(context).inflate(stateView[state]!!, this, true)
    }

    enum class State {
        INIT, LOADING, EMPTY, CONNECTION_ERROR, CONTENT, APP_ERROR
    }
}