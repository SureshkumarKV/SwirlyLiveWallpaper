#version 300 es
precision highp float;

uniform vec3 uDiffuseColor;
uniform sampler2D uTexture2DSamplers[4];
uniform vec2 uTime;

in vec2 vTexCoord;

out vec4 fragColor;

void main() {
    vec2 texCood = vTexCoord;

    const float SCALE1 = 9.0;
    const float SCALE2 = 7.0;

    texCood = vec2(texCood.x + sin((texCood.y+uTime.y)*SCALE1)*0.1, texCood.y + sin((texCood.x+uTime.x)*SCALE1)*0.1);
    texCood = vec2(texCood.x + sin((texCood.y-uTime.y)*SCALE2)*0.1, texCood.y + sin((texCood.x-uTime.x)*SCALE2)*0.1);

    texCood = vec2(texCood.x + sin((texCood.y+uTime.y)*SCALE1)*0.1, texCood.y + sin((texCood.x+uTime.x)*SCALE1)*0.1);
    texCood = vec2(texCood.x + sin((texCood.y-uTime.y)*SCALE2)*0.1, texCood.y + sin((texCood.x-uTime.x)*SCALE2)*0.1);

    texCood = vec2(texCood.x + sin((texCood.y+uTime.y)*SCALE1)*0.1, texCood.y + sin((texCood.x+uTime.x)*SCALE1)*0.1);
    texCood = vec2(texCood.x + sin((texCood.y-uTime.y)*SCALE2)*0.1, texCood.y + sin((texCood.x-uTime.x)*SCALE2)*0.1);

    texCood = vec2(texCood.x + sin((texCood.y+uTime.y)*SCALE1)*0.1, texCood.y + sin((texCood.x+uTime.x)*SCALE1)*0.1);
    texCood = vec2(texCood.x + sin((texCood.y-uTime.y)*SCALE2)*0.1, texCood.y + sin((texCood.x-uTime.x)*SCALE2)*0.1);

	fragColor = texture(uTexture2DSamplers[0], texCood);
}
