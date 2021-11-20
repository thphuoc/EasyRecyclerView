# EasyRecyclerView

![alt screenshot width="200"](/screenshot.png "Text to show on mouseover")

![alt screenshot width="200"](/SharedScreenshot.jpg "Text to show on mouseover")

[![](/https://youtu.be/WVl4ox0PmcM)](https://youtu.be/WVl4ox0PmcM "")

Why we have to define adapter for a RecyclerView. It's very take time when we have a lot of list view right?
With Easy RecyclerView we just need to care about 2 things
1. Data Object Model (DAO)
2. ViewBinder -> Input of ViewBinder consist of DAO and View item. It's contain only one function bind() to bind DAO with View item

That's is whats we need to care about.
For now, we will handle as below with 3 steps only:

Step 1: In your xml
```xml
<com.thphuoc.erv.EasyRecyclerView
        android:id="@+id/listView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintBottom_toTopOf="@id/btnAddBottom"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toBottomOf="@id/btnAddTop"
        tools:ignore="SpeakableTextPresentCheck" />
```

Step 2: In your kotlin Add Item view binder. In here, we define 2 type of view types (data item and header item)
```kotlin
data class DataItemViewBinder(
    val context: Context,
    val name: String,
    val onRemove: (viewBinder: DataItemViewBinder) -> Unit
) : EasyItemViewBinder {
    override val viewResId: Int = R.layout.item_view

    override fun bind(view: View) {
        view.tvName.text = name
        view.btnDelete.setOnClickListener {
            onRemove(this)
        }
    }
}

data class HeaderItemViewBinder(
    val context: Context,
    val name: String
) : EasyItemViewBinder {
    override val viewResId: Int = R.layout.item_view_header

    override fun bind(view: View) {
        view.tvName.text = name
    }
}
```

Step 3: In your Activity/Fragment add list of view binder
```kotlin
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
```

Easy to handle load more:
```kotlin
listView.setLoadMore(LoadMoreViewBinder {
    //Load more in here
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

```

Define Layout type in xml:
```xml
<attr name="layout_type" format="enum">
    <enum name="VERTICAL" value="0"/>
    <enum name="HORIZONTAL" value="1"/>
    <enum name="VGRID2" value="2"/>
    <enum name="VGRID3" value="3"/>
    <enum name="VGRID4" value="4"/>
    <enum name="HGRID2" value="5"/>
    <enum name="HGRID3" value="6"/>
    <enum name="HGRID4" value="7"/>
</attr>
```
Example:
```xml
<com.thphuoc.erv.EasyRecyclerView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="100dp"
    android:id="@+id/hozList"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:layout_type="HORIZONTAL"/>
```