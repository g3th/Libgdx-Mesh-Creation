#ifdef GL_ES
precision highp float;
#endif

varying vec2 v_texCoords;
uniform sampler2D u_texture0;
uniform sampler2D u_texture1;
uniform sampler2D u_texture2;
uniform float u_time;
uniform int light_state;
uniform mat4 u_projTrans;

struct Dimensions {
    float top;
    float bottom;
    float left;
    float right;
};

Dimensions boxDimensions(){
    Dimensions bd;
    bd.top = 0.76;
    bd.bottom = 0.1;
    bd.left = -0.05;
    bd.right = 0.7;
    return bd;
}

float box(vec2 uv, Dimensions dimensions){
    float boxPosition = 0.1;
    float top = smoothstep(boxPosition, boxPosition, uv.y - dimensions.top);
    float bottom = smoothstep(boxPosition, boxPosition, uv.y - dimensions.bottom);
    float left = smoothstep(boxPosition, boxPosition, uv.x - dimensions.left);
    float right = smoothstep(boxPosition, boxPosition, uv.x - dimensions.right);
    return (right - left) * (top - bottom);
}

void main() {
    vec2 uv = v_texCoords;
    vec4 mainScreen = texture2D(u_texture0, uv);
    vec4 texture1 = texture2D(u_texture1, uv);
    vec2 motionUv = uv;
    float speed = 4.7;
    float distortion_strength = 25.6;
    float offset = 0.04;
    float stretch = 1.;
    motionUv.x *= stretch + sin(distortion_strength * motionUv.y + speed * u_time) * offset;
    vec4 texture2 = texture2D(u_texture2, motionUv);
    texture2.rgb = vec3(0.);
    vec4 blend;
    float box = box(uv, boxDimensions());
    vec4 screenOn = vec4(box, box, box, 1.);
    float oscillation = mix(.7, .7, abs(sin(u_time * 10.)));
    float color_motion = fract(motionUv.x * motionUv.y * 10. * oscillation);
    vec4 screenCol = vec4(color_motion, uv.x, uv.y,  1.) * screenOn;
    vec4 screen_blend = mix(screenOn, screenCol, screenOn.a);
    vec4 sb = mix(screen_blend, texture2, screen_blend.a * texture2.a);
    vec4 mainScreen_blend = mix(sb, mainScreen, mainScreen.a );

    if (light_state == 0) {
        blend = vec4(mainScreen.rgb, 0.4);
    } else {
        blend = mix(texture2, mainScreen_blend, mainScreen_blend.a);
    }
    gl_FragColor = blend;
}
