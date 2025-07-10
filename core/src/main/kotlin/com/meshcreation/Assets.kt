package com.meshcreation

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Disposable

class Assets(): Disposable {
    lateinit var loadAssets: TextureAtlas
    var spriteMap = emptyMap<String, Sprite>()
    val loader = AssetManager()

    object SPRITE_KEYS {
        val sprites = listOf("computer_screen", "light_on", "light_off")
    }

    fun load(): Map<String, Sprite> {
        loader.load("Sprites/spriteSheet.atlas", TextureAtlas::class.java)
        loader.finishLoading()
        loadAssets = loader.get("Sprites/spriteSheet.atlas")
        SPRITE_KEYS.sprites.forEach {
            val sprite = loadAssets.createSprite(it)
            spriteMap += mapOf(it to sprite!!)
        }
        return spriteMap
    }

    override fun dispose(){
        loader.dispose()
    }

}
