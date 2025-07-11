package com.meshcreation

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector3

class GameInputs(val cam: Camera): Graphic_Interface(cam) {
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val camera = cam.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
        lightSwitch(camera)
        channelSwitches(camera)
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }
}
