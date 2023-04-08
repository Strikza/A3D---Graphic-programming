#version 100
precision mediump float;

// Transformation Matrices
uniform mat4 uModelViewMatrix;
uniform mat4 uProjectionMatrix;
uniform mat3 uNormalMatrix;

// Light source definition
uniform vec3 uLightPos;

// Material definition
uniform bool uNormalizing;

// vertex attributes
attribute vec3 aVertexPosition;
attribute vec3 aVertexNormal;

// Interpolated data
varying vec4 vPos;
varying vec3 vNormal;
varying vec3 h;

void main(void)
{
    vPos = uModelViewMatrix * vec4(aVertexPosition, 1.0);
    vNormal = uNormalMatrix * aVertexNormal;
    if (uNormalizing) vNormal = normalize(vNormal);

    // Specular calcul before interpolated
    vec3 lightdir = normalize(uLightPos - vPos.xyz);
    vec3 v = normalize(-(vPos.xyz));
    h = normalize(v + lightdir);

    gl_Position = uProjectionMatrix * vPos;
}
