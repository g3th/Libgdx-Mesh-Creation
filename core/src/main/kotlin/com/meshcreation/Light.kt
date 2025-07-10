package com.meshcreation

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector3

class Light(val camera: Camera): MyInputs {
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val cam = camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
        if (cam.x == cam.x.coerceIn(27.5f, 29.5f) && cam.y == cam.y.coerceIn(9.2f, 10.8f)) {
            LIGHT.STATE.ON = !LIGHT.STATE.ON
        }
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }
}
