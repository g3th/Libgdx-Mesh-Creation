package com.meshcreation

import com.badlogic.gdx.Gdx.files
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Mesh
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.VertexAttribute
import com.badlogic.gdx.graphics.glutils.ShaderProgram

/**
 * Creates and renders a simple quad, so that multiple textures can be post-processed at the same time.
 * This is difficult using the default SpriteBatch implementation, as the SpriteBatch overwrites any texture slots bound to anything other than 1,
 * and only one texture will be affected by shader post-processing.
 * @property mesh Creates a mesh used to render two triangles forming a quad.
 * @property vertices The vertices forming the screen space (x, y, z, u and v). In NDCs, they range from -1 to 1. However, the width and height used for this scene
 * is given here instead, taking the camera's center position into account for proper placement. Depth is 0f if dealing with 2D.
 * @property indices Vertices for the two triangles, given in counter-clockwise direction due to back-face culling.
 */

class SimpleQuad {
    lateinit var mesh: Mesh
    lateinit var vertices: FloatArray
    lateinit var indices: ShortArray
    var lightState = 0
    var n = 0
    val currentChannel = listOf(files.internal("Shaders/tiles.frag"), files.internal("Shaders/waves.frag"))
    var shaders = ShaderProgram(files.internal("Shaders/vertexShader.vert"), currentChannel[n])

    fun init(worldWidth: Float, worldHeight: Float, centerX: Float, centerY: Float) {
        val width = worldWidth / 2f
        val height = worldHeight / 2f
        // Set to true for detailed errors. An enormous ball-breaker...
        ShaderProgram.pedantic = false
        /* Mesh Creation: MAX_VERTICES = Four points that represent the screen or quad
           MAX_INDICES = Two Triangles form the quad, so the triangle's vertices CCW (0 - 1 - 2 // 2 - 3 - 0)
           VertexAttributes = The vertex shader's attributes i.e. attribute a_color. IMPORTANT: Must be in order or nothing will be rendered.
         */
        mesh = Mesh(true, 4, 6, VertexAttribute.Position(), VertexAttribute.TexCoords(0))
        /* Normalized Device Coordinates (NDCs)
            Include Z in the float array, set to 0f if dealing with 2D:
            x, y, z, u, v
         */
        vertices = floatArrayOf(
            -width + centerX, -height + centerY, 0f, 0f, 0f,
            width + centerX, -height + centerY, 0f, 1f, 0f,
            width + centerX, height + centerY, 0f, 1f, 1f,
            -width + centerX, height + centerY, 0f, 0f, 1f
        )
        /*
        Two triangles to form the quad
         */
        indices = shortArrayOf(0, 1, 2, 2, 3, 0)
        /*
        Init, mate?
         */
        mesh.setIndices(indices)
        mesh.setVertices(vertices)
    }
    /**
     * @param screenTexture The background texture to be post-processed, passed as a sampler uniform and bound to slot 0.
     * @param spotLight The spotlight texture, or foreground to be blended, passed as a sampler uniform and bound to slot 1.
     * @param camera The Scene's camera, used for projection in the vertex Shader.
     * @param deltaTime Accumulated rendering time, passed as a uniform to the fragment shader.
     * Blend, Bind shaders, bind texture slots, set uniforms and do all multiple texture post-processing here,
     * so the default SpriteBatch implementation does not become too nosy in our affairs, like a SpriteBatch mother-in-law.
     * WARNING: If no blending is set, and the buffer isn't cleared in the main render method, alpha blending
     * will not work in the fragment shader.
     **/

    fun render(screenTexture: Texture, fontsAsTexture: Texture, spotLight: Texture, camera: Camera, deltaTime: Float) {
        gl.glEnable(GL20.GL_BLEND)
        gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        if (SWITCHES.CHANNEL_SWITCHED.STATE) {
            SWITCHES.CHANNEL_SWITCHED.STATE = false
            when {
                SWITCHES.CHANNEL_RIGHT.STATE -> { n = (n + 1).coerceAtMost(currentChannel.size - 1) }
                else -> {
                    n = (n - 1).coerceAtLeast(0)
                }
            }
            shaders = ShaderProgram(files.internal("Shaders/vertexShader.vert"), currentChannel[n])
        }
        lightState = if (SWITCHES.LIGHT.STATE) 1 else 0
        shaders.bind()
        screenTexture.bind(0)
        spotLight.bind(1)
        fontsAsTexture.bind(2)
        shaders.setUniformi("u_texture0", 0)
        shaders.setUniformi("u_texture1", 1)
        shaders.setUniformi("u_texture2", 2)
        shaders.setUniformi("light_state", lightState)
        shaders.setUniformf("u_time", deltaTime)
        shaders.setUniformMatrix("u_projTrans", camera.combined)

        mesh.render(shaders, GL20.GL_TRIANGLES)
        // reset active texture so SpriteBatch can use it
        gl.glActiveTexture(GL20.GL_TEXTURE0)
    }
}
