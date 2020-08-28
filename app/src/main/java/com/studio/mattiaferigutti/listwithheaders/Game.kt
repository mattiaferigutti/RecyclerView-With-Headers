package com.studio.mattiaferigutti.listwithheaders

data class Game (val title: String, val section: Section? = null)

class GameLabeled(private val _labelGroupOfGames: Section, private vararg val _games: Game) {

    val section: String
    get() {
        return _labelGroupOfGames.value
    }

    val games: List<Game>
    get() {
        return _games.asList()
    }
}

enum class Section(val value: String) {
    FOREPLAY("Foreplay"),
    HARDCORE("Hard core"),
    INSTRUCTIONS("Instructions")
}