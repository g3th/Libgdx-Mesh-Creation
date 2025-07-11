#ifdef GL_ES
precision highp float;
#endif

varying vec2 v_texCoords;
uniform sampler2D u_texture0;
uniform int light_state;
uniform float u_time;

struct Dimensions {
    vec2 position;
    vec2 size;
    float radius;
};

Dimensions roundedBoxDimensions(){
    Dimensions dim;
    dim.position = vec2(.41, .53);
    dim.size = vec2(.21, .16);
    dim.radius = .95;
    return dim;
}

float roundBox (vec2 uv, Dimensions dimensions) {
    float d = length(max(abs(uv - dimensions.position), dimensions.size) - dimensions.size) * dimensions.radius;
    return smoothstep(0.5, 0.49, d * 3.0);
}

vec2 getLocalizedUV(vec2 uv, Dimensions dimensions) {
    vec2 localUV = (uv - (dimensions.position - dimensions.size)) / (dimensions.size * 2.0);
    return localUV;
}

void main(){
    vec4 blend;
    vec2 uv = v_texCoords;
    Dimensions boxDim = roundedBoxDimensions();
    float boxMask = roundBox(uv, boxDim);
    vec2 localUV = getLocalizedUV(uv, boxDim);
    vec2 boxes = fract(localUV * 35.0);
    float pattern = step(0.2, boxes.x) * step(0.2, boxes.y);
    vec4 computerScreen = texture2D(u_texture0, uv);
    float finalPattern = pattern * boxMask;
    vec4 screen = vec4(finalPattern * uv.x, finalPattern * uv.y, finalPattern, 1.0);
    if (light_state == 0) {
        blend = vec4(computerScreen.rgb, 0.4);
    } else {
        blend = mix(screen, computerScreen, computerScreen.a);
    }
    gl_FragColor = blend;
}