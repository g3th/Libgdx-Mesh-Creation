# Libgdx - Mesh Creation

## Create a Mesh, Render a Quad and Post-Process the Scene

Libgdx provides a convenient way to render your scene, in its ```SpriteBatch``` implementation. This will usually suffice for most games rendering simple scenes comprised of sprites, textures and the api's default shader. Rendering this way will also usually be enough if simple post-processing will need to be applied to single sprites or single textures.

 
However (here is the conjuctive adverb connecting two independent clauses), when more complex post-processing is required, or a more involved scene is being rendered, different textures might need to be post-processed by the same fragment shader, blended and rendered at the same time.

 
In this instance, the default ```SpriteBatch``` implementation is a very poor choice; once these separate textures are bound to various slots (Texture Units in OpenGL, where at least a minimum of 16 units are available) as the scene is being rendered the only available slot will always be 0, due to how the library implements its rendering method. Therefore, once these textures are passed as sampler uniforms in the fragment shader (i.e. ```uniform sampler2D u_texture0, 1 etc.. ```) only one texture will ever be available.


Creating a mesh here is the best choice, as it will allow you to bind textures properly, post-process and render the scene as required. There are some complexities involved, such as choosing the correct ```s``` and ```d``` factors when blending with ```gl.BlendFunc```, or knowing how to draw the quad with two triangles etc..., but once these are learnt there isn't much to it.


This android app shows how to:

* Create a Mesh and Quad in Libgdx.

* Use Frame buffers for "first pass" rendering, and obtain the required texture for post processing (in the case of a TextureAtlas being present).

* Apply some simple domain distortion and blending to each texture in the fragment shader. 




 

