attribute vec2 av_Position;
attribute vec2 af_Position;

uniform vec3 viewport;

varying vec2 texPos;

void main() {

    gl_Position = vec4((av_Position + viewport.rg) * viewport.b, 1.0, 1.0);
    texPos = af_Position;

}