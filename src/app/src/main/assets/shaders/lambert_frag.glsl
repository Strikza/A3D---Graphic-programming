#version 100
precision mediump float;

// Light source definition
uniform bool uLighting;
uniform vec4 uAmbiantLight;
uniform vec3 uLightPos;
uniform vec4 uLightColor;

// Material definition
uniform vec4 uMaterialColor;

// Interpolated data
varying vec4 vPos;
varying vec3 vNormal;

void main(void) {

    vec4 color;

    if (uLighting) {
        vec3 lightdir = normalize(uLightPos-vPos.xyz);
        vec3 normal = normalize(vNormal);
        float weight = max(dot(normal, lightdir), 0.0);
        color = uMaterialColor * (uAmbiantLight + weight * uLightColor);
    }
    else{
        color = uMaterialColor;
    }

    gl_FragColor = color;
}
