package com.meshcreation

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

interface MyScreen: Screen {
    override fun show() {}
    override fun pause(){}
    override fun resume(){}
    override fun hide(){}
    override fun resize(width: Int, height: Int) {}
    override fun render(delta: Float) {}
    override fun dispose(){}
}

interface MyInputs: InputProcessor {
    override fun keyDown(keycode: Int): Boolean {return false}
    override fun keyTyped(character: Char): Boolean {return false}
    override fun keyUp(keycode: Int): Boolean {return false}
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {return false}
    override fun scrolled(amountX: Float, amountY: Float): Boolean {return false}
    override fun touchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {return false}
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {return false}
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {return false}
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {return false}
}

/**
 * Abstract class for input processing, with methods for all GUI buttons.
 * Methods:
 * ```
 * debugScreenCoordinates(camera: Vector3)
 * lighSwitch(camera: Vector3)
 * channelSwitches(camera: Vector3)
 * ```
 */
abstract class GraphicInterface(val camera: Camera): MyInputs {

    /**
     * Used to discover unprojected co-ordinates for the point on the screen which has been clicked.
     */
    fun debugScreenCoordinates(cam: Vector3){
        Logging.normal("${cam.x} - ${cam.y}")
    }

    /**
     * This method simply sets the light state to ON or OFF, so that it will affect the rendering/post-processing in some way.
     */

    fun lightSwitch(cam: Vector3) {
        if (cam.x == cam.x.coerceIn(27.5f, 29.5f) && cam.y == cam.y.coerceIn(9.2f, 10.8f)) {
            SWITCHES.LIGHT.STATE = !SWITCHES.LIGHT.STATE
        }
    }

    /**
     * This method calculates clicks within a triangle with Barycentric co-ordinates collision;
     * this is usually overkill as a GUI would use bounding boxes or circles regardless. However,
     * since the triangle sprites are quite big, this might be the preferred method for more precision.
     *
     *  @param cam The unprojected camera.
      */

    fun channelSwitches(cam: Vector3) {
        val clickPoint = Vector2(cam.x, cam.y)
        val channelSwitchLeft = listOf(Vector2(9.25f, 1.91f), Vector2(11.13f, 2.66f), Vector2(11.10f,1.20f))
        val channelSwitchRight = listOf(Vector2(14.46f, 1.91f), Vector2(12.58f, 2.68f), Vector2(12.58f, 1.25f))
        if (triangleClicked(clickPoint, channelSwitchLeft[0], channelSwitchLeft[1], channelSwitchLeft[2])) {
            SWITCHES.CHANNEL_SWITCHED.STATE = true
            SWITCHES.CHANNEL_RIGHT.STATE = false
        }
        if (triangleClicked(clickPoint, channelSwitchRight[0], channelSwitchRight[1], channelSwitchRight[2])) {
            SWITCHES.CHANNEL_RIGHT.STATE = true
            SWITCHES.CHANNEL_SWITCHED.STATE = true
        }
    }

    fun triangleClicked(clickPoint: Vector2, a: Vector2, b: Vector2, c: Vector2): Boolean{
        val denom = (b.y - c.y) * (a.x - c.x) + (c.x - b.x) * (a.y - c.y)
        val alpha = ((b.y - c.y) * (clickPoint.x - c.x) + (c.x - b.x) * (clickPoint.y - c.y)) / denom
        val beta = ((c.y - a.y) * (clickPoint.x - c.x) + (a.x - c.x) * (clickPoint.y - c.y)) / denom
        val gamma = 1 - alpha - beta
        return alpha >= 0 && beta >= 0 && gamma >= 0
    }
}