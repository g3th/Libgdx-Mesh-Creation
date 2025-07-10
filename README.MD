# LibGDX - Mesh Creation
## Create a Mesh, Render a Quad and Post-Process the Scene

https://github.com/user-attachments/assets/298a2852-f752-4f44-bd7c-7b6433611607

LibGDX provides a convenient way to render your scene in its `SpriteBatch` implementation. This will usually suffice for most games rendering simple scenes comprised of sprites, textures and the API's default shader. Rendering this way will also usually be enough if simple post-processing needs to be applied to single sprites or single textures.
 
However (here is the conjuctive adverb connecting two independent clauses), when more complex post-processing is required, or a more involved scene is being rendered, different textures might need to be post-processed by the same fragment shader, blended and rendered simultaneously.
 
In this instance, the default `SpriteBatch` implementation is a very poor choice; once these separate textures are bound to various slots (Texture Units in OpenGL, where at least a minimum of 16 units are available) as the scene is being rendered, only slot 0 will be available due to how the library implements its rendering method. Therefore, when these textures are passed as sampler uniforms in the fragment shader (i.e. `uniform sampler2D u_texture0, u_texture1, etc...`) only one texture will ever be accessible.

Creating a mesh here is the best choice, as it will allow you to bind textures properly, post-process and render the scene as required. There are some complexities involved, such as choosing the correct `sfactor` and `dfactor` parameters when blending with `glBlendFunc`, or knowing how to draw the quad with two triangles, but once these are learned there isn't much to it.

This Android app demonstrates how to:
* Create a Mesh and Quad in LibGDX
* Use Frame Buffers for "first pass" rendering, and obtain the required texture for post-processing (in the case of a TextureAtlas being present)
* Apply some simple domain distortion and blending to each texture in the fragment shader
