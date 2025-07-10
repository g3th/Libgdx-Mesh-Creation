package com.meshcreation

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.Sprite

class MainMenu : Game(){
    lateinit var gameSprites: Map<String, Sprite>
    val assets = Assets()

    override fun create(){
        gameSprites = assets.load()
        this.setScreen(MainScreen(gameSprites))
    }

    override fun dispose() {
        super.dispose()
        assets.dispose()
    }

}
