package com.studio.mattiaferigutti.listwithheaders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HeaderRecyclerViewAdapter(list: List<Any>, recyclerView: RecyclerView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var gameList = mutableListOf<Any>()
    private val listOfHeader = mutableListOf<Section>()
    private val listOfItems = mutableListOf<Game>()
    private val numberOfItems = mutableMapOf<Section, Int>()
    private val mapOfGame = mutableMapOf<Section, Game>()

    init {
        gameList = list.toMutableList()

        updateData()

        this.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                updateData()
            }
        })

        val layoutManager = recyclerView.layoutManager as GridLayoutManager?
        layoutManager?.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                //set the number of columns
                return if (isHeaderPosition(position)) layoutManager?.spanCount!! else 1
            }
        }
    }

    private fun updateData() {
        var previousSection: Section? = null
        for (game in gameList) {
            if (game is Section) {
                previousSection = game
            } else {
                previousSection?.let {
                    mapOfGame[it] = game as Game
                }
            }
        }

        for (section in  listOfHeader) {
            val currentList = mapOfGame.filter {
                it.key == section
            }
            numberOfItems[section] = currentList.size
        }
    }

    class HeaderViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val txtHeader: TextView = item.findViewById<TextView>(R.id.txtHeader)
    }

    class ItemsViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val txtName: TextView = item.findViewById<TextView>(R.id.txtName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == HEADER) {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            ItemsViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.txtHeader.text = (gameList[position] as Section).value
        } else if (holder is ItemsViewHolder) {
            holder.txtName.text = (gameList[position] as Game).title
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isHeaderPosition(position)) {
            listOfHeader.add(gameList[position] as Section)
            HEADER
        } else {
            listOfItems.add(gameList[position] as Game)
            ITEMS
        }
    }

    private fun isHeaderPosition(position: Int) : Boolean {
        return gameList[position] is Section
    }

    fun updateData(list: List<Game>) {
        gameList = list.toMutableList()
        notifyDataSetChanged()
    }
    
    fun add(game: Game) {
        var gameToAdd: Pair<Int, Game>? = null
        for ((index, currentGame) in gameList.withIndex()) {
            if (currentGame is Section) {
                if (currentGame == game.section) {
                    gameToAdd = Pair(index + numberOfItems[currentGame]!!, game)
                }
            }
        }
        gameList.add(gameToAdd?.first ?: 0, gameToAdd?.second as Game)
        notifyItemInserted(gameToAdd.first)
    }

    override fun getItemCount(): Int {
        return gameList.size
    }

    companion object {
        const val HEADER = 1
        const val ITEMS = 2
    }
}