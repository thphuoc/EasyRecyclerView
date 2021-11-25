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
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SimpleStateLayout,
            0, 0
        ).apply {
            setLayout(
                initLayout = getResourceId(R.styleable.SimpleStateLayout_layout_init, defaultLayout),
                loadingLayout = getResourceId(R.styleable.SimpleStateLayout_layout_loading, defaultLayout),
                emptyLayout = getResourceId(R.styleable.SimpleStateLayout_layout_empty, defaultLayout),
                connectionErrorLayout = getResourceId(R.styleable.SimpleStateLayout_layout_connection_error, defaultLayout),
                appErrorLayout = getResourceId(R.styleable.SimpleStateLayout_layout_app_error, defaultLayout),
                contentView = getResourceId(R.styleable.SimpleStateLayout_layout_content, defaultLayout)
            )
        }
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