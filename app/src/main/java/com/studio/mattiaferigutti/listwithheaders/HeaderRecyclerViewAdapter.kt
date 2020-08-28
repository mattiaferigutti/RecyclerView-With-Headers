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
    private val listOfGame = mutableListOf<Pair<Section, Game>>()

    init {
        gameList = list.toMutableList()

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
        listOfGame.clear()
        numberOfItems.clear()
        for (game in gameList) {
            if (game is Section) {
                previousSection = game
            } else {
                previousSection?.let {
                    listOfGame.add(Pair(it, game as Game))
                }
            }
        }
        for (section in  listOfHeader) {
            val currentList = listOfGame.filter {
                it.first == section
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

    /**
     * to update the list
     */
    fun updateData(list: List<Game>) {
        gameList = list.toMutableList()
        notifyDataSetChanged()
    }

    /**
     *  @return the number of headers
     *  @return -1 if [section] does not exist
     */
    fun getNumberOfHeaders(section: Section) : Int {
        return numberOfItems[section] ?: -1
    }

    /**
     * @return the number of headers existing in the list
     */
    fun getHeadersSize() : Int {
        return listOfHeader.size
    }

    /**
     * Add an element to the list
     * There is no need to specify where to add the element
     * @param [Game]
     */
    fun add(game: Game) {
        notifyDataSetChanged()
        var gameToAdd: Pair<Int, Game>? = null
        for ((index, currentGame) in gameList.withIndex()) {
            if (currentGame is Section) {
                if (currentGame == game.section) {
                    numberOfItems[currentGame]?.let {
                        gameToAdd = Pair(index + it +1, game)
                    }
                }
            }
        }
        gameList.add(gameToAdd?.first ?: 0, gameToAdd?.second as Game)
        notifyItemInserted(gameToAdd?.first ?: 0)
    }

    /**
     * Remove the element from the list
     * @param [Game]
     */
    fun remove(game: Game) {
        gameList.remove(game)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return gameList.size
    }

    companion object {
        const val HEADER = 1
        const val ITEMS = 2
    }
}