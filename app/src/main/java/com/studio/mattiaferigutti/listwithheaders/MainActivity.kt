package com.studio.mattiaferigutti.listwithheaders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: HeaderRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = listOf(
            //foreplay
            Section.FOREPLAY,

            //hardcore
            Section.HARDCORE,

            //instructions
            Section.INSTRUCTIONS
        )

        recycler.layoutManager = GridLayoutManager(this, 2)

        adapter = HeaderRecyclerViewAdapter(list, recycler,
            { position, headerTitle ->
                //header click
                Toast.makeText(this, headerTitle, Toast.LENGTH_SHORT).show()
            },
            { position, itemTitle ->
                //item click
                Toast.makeText(this, itemTitle, Toast.LENGTH_SHORT).show()
            }
        )

        recycler.adapter = adapter

        addElements()

        addButton.setOnClickListener {
            adapter.add(Game("New Entry", Section.FOREPLAY))
            adapter.add(Game("New Entry", Section.HARDCORE))
            adapter.add(Game("New Entry", Section.INSTRUCTIONS))

            adapter.remove(Game("Foreplay 3"))
        }
    }

    private fun addElements() {
        val list = listOf(
            Game("50 Words", Section.FOREPLAY),
            Game("Foreplay 2", Section.FOREPLAY),
            Game("Foreplay 3", Section.FOREPLAY),
            Game("Foreplay 4", Section.FOREPLAY),
            Game("Foreplay 5", Section.FOREPLAY),
            Game("Hard core 1", Section.HARDCORE),
            Game("Hard core 2", Section.HARDCORE),
            Game("Instructions 1", Section.INSTRUCTIONS),
            Game("Instructions 2", Section.INSTRUCTIONS)
        )
        adapter.addAll(list)
    }

}