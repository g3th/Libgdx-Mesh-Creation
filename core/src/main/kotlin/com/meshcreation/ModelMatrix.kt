/*      More Complex Model Matrix for Screen Co-ordinates

        // Enable blending
        gl.glEnable(GL20.GL_BLEND)
        gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        // Set up model matrix for positioning
        // (x, y) camera.position.x and y
        modelMatrix.idt() // Reset to identity
        modelMatrix.translate(x, y, 0f)

        // Combine model, view, and projection matrices
        val mvpMatrix = Matrix4(camera.combined)
        mvpMatrix.mul(modelMatrix)

        shaders.bind()
        screenTexture.bind(0)
        shaders.setUniformi("u_texture", 0)
        shaders.setUniformMatrix("u_projTrans", mvpMatrix)

        mesh.render(shaders, GL20.GL_TRIANGLES)
        // mesh.unbind(shaders)
 */

