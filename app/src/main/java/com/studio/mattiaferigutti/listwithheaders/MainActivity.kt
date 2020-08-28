package com.studio.mattiaferigutti.listwithheaders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = listOf(
            //foreplay
            Section.FOREPLAY,
            Game("50 Words"),
            Game("Foreplay 2"),
            Game("Foreplay 3"),
            Game("Foreplay 4"),
            Game("Foreplay 5"),

            //hardcore
            Section.HARDCORE,
            Game("Hard core 1"),
            Game("Hard core 2"),

            //instructions
            Section.INSTRUCTIONS,
            Game("Instructions 1"),
            Game("Instructions 2")
        )

        recycler.layoutManager = GridLayoutManager(this, 2)

        val adapter = HeaderRecyclerViewAdapter(list, recycler,
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

        addButton.setOnClickListener {
            adapter.add(Game("New Entry", Section.FOREPLAY))
            adapter.add(Game("New Entry", Section.HARDCORE))
            adapter.add(Game("New Entry", Section.INSTRUCTIONS))

            adapter.remove(Game("Foreplay 3"))
        }

    }
}