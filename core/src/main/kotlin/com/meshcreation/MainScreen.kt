package com.meshcreation

import com.badlogic.gdx.Gdx.files
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.Gdx.graphics
import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport

enum class SWITCHES(var STATE: Boolean = false) {
    LIGHT(),
    CHANNEL_SWITCHED(),
    CHANNEL_RIGHT(),
}

class MainScreen(val gameAssets: Map<String, Sprite>): MyScreen {
    lateinit var fonts: BitmapFont
    lateinit var fontsFBO: FrameBuffer
    lateinit var screenFBO: FrameBuffer
    lateinit var screenTexture: Texture
    lateinit var fontsAsTexture: Texture
    lateinit var spotLightTexture: Texture
    lateinit var fontsAsSprite: Sprite
    lateinit var computerScreenSprite: Sprite
    lateinit var lightOnSprite: Sprite
    lateinit var lightOffSprite: Sprite
    var fontsCamera: OrthographicCamera
    var fontsViewport: Viewport
    var mainViewPort: Viewport
    var mainRenderCamera: OrthographicCamera
    val batch = SpriteBatch()
    val fontBatch = SpriteBatch()
    val uiScreenDimensions = Vector2(graphics.width.toFloat(), graphics.height.toFloat())
    val mainScreenDimensions = Vector2(30f, 15f)
    var deltaTime = 0f
    val simpleQuad = SimpleQuad()
    val pixmap: Pixmap

    init {
        fontsCamera = OrthographicCamera(uiScreenDimensions.x, uiScreenDimensions.y)
        fontsViewport = FitViewport(fontsCamera.viewportWidth, fontsCamera.viewportHeight)
        fontsCamera.position.set(fontsCamera.viewportWidth / 2f, fontsCamera.viewportHeight / 2f, 0f)
        mainRenderCamera = OrthographicCamera(mainScreenDimensions.x, mainScreenDimensions.y)
        mainViewPort = FitViewport(mainRenderCamera.viewportWidth, mainRenderCamera.viewportHeight)
        mainRenderCamera.position.set(mainRenderCamera.viewportWidth / 2f, mainRenderCamera.viewportHeight / 2f, 0f)
        pixmap = Pixmap(mainRenderCamera.viewportWidth.toInt(), mainRenderCamera.viewportHeight.toInt(), Pixmap.Format.RGBA8888)
        simpleQuad.init(mainRenderCamera.viewportWidth, mainRenderCamera.viewportHeight,
            mainRenderCamera.position.x, mainRenderCamera.position.y)
    }

    override fun show() {
        val lightX = 27.5f
        val lightY = 9f
        val lightWidth = 2f
        val lightHeight = 2f
        computerScreenSprite = gameAssets["computer_screen_with_buttons"]!!
        lightOnSprite = gameAssets["light_on"]!!
        lightOffSprite = gameAssets["light_off"]!!
        fonts = BitmapFont(files.internal("Fonts/nimbusMono.fnt"))
        fonts.data.setScale(3f)
        // Sprite has empty space on bottom, hence negative y co-ordinates
        computerScreenSprite.setBounds(0f, -3f, 25f, 16f)
        lightOnSprite.setBounds(lightX, lightY, lightWidth, lightHeight)
        lightOffSprite.setBounds(lightX, lightY, lightWidth, lightHeight)
        pixmap.setColor(0f, 0f, 0f, 1f)
        pixmap.fill()
        spotLightTexture = Texture(pixmap)
        input.inputProcessor = GameInputs(mainRenderCamera)
    }

    fun fontsAsTexture(){
        fontsFBO = FrameBuffer(Pixmap.Format.RGBA8888, uiScreenDimensions.x.toInt(), uiScreenDimensions.y.toInt(), false)
        val helloX = ( 3f / mainRenderCamera.viewportWidth) * fontsCamera.viewportWidth
        val helloY = (20f / mainRenderCamera.viewportWidth) * fontsCamera.viewportHeight
        val mateyX = (16f / mainRenderCamera.viewportWidth) * fontsCamera.viewportHeight
        val mateyY = (13f / mainRenderCamera.viewportWidth) * fontsCamera.viewportHeight
        fontsFBO.begin()
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        gl.glClearColor(0f, 0f, 0f, 0f)
        fontBatch.begin()
        fonts.draw(fontBatch, "Hello,", helloX, helloY)
        fonts.draw(fontBatch, "Matey!", mateyX, mateyY)
        fontBatch.end()
        fontsFBO.end()
        fontsAsTexture = fontsFBO.colorBufferTexture
        fontsAsSprite = Sprite(fontsAsTexture)
        fontsAsSprite.flip(false, true)
        fontsAsSprite.setBounds(1f, 1f, uiScreenDimensions.x, uiScreenDimensions.y)
    }

    fun screenAsTexture() {
        fontsAsTexture()
        screenFBO = FrameBuffer(Pixmap.Format.RGBA8888, graphics.width, graphics.height, false)
        screenFBO.begin()
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        gl.glClearColor(0f, 0f, 0f, 0f)
        batch.shader = null
        batch.projectionMatrix = mainRenderCamera.combined
        batch.begin()
        computerScreenSprite.draw(batch)
        batch.end()
        screenFBO.end()
        screenTexture = screenFBO.colorBufferTexture
    }

    override fun resize(width: Int, height: Int) {
        fontsViewport.update(width, height)
        fontsCamera.update()
        mainViewPort.update(width, height)
        mainRenderCamera.update()
    }

    override fun render(delta: Float) {
        deltaTime += delta
        if (!::fontsFBO.isInitialized){
            screenAsTexture()
        }
        gl.glClearColor(0f, 0f, 0f, 0f)
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        simpleQuad.render(screenTexture, fontsAsTexture, spotLightTexture, mainRenderCamera, deltaTime)
        batch.projectionMatrix = mainRenderCamera.combined
        batch.begin()
        if (!SWITCHES.LIGHT.STATE){
            lightOffSprite.draw(batch)
        } else {
            lightOnSprite.draw(batch)
        }
        batch.end()
    }

    override fun dispose(){
        batch.dispose()
        fonts.dispose()
        fontsFBO.dispose()
        fontBatch.dispose()
        fontsAsTexture.dispose()
        screenFBO.dispose()
        screenTexture.dispose()
        pixmap.dispose()
        spotLightTexture.dispose()
    }
}

