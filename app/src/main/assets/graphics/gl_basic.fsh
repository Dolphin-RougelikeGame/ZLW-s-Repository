precision mediump float;
uniform sampler2D texture;
varying vec2 texPos;

void main() {

  gl_FragColor = texture2D(texture, texPos);

}