#version 100
precision mediump float;

// Transformation Matrices
uniform mat4 uModelViewMatrix;
uniform mat4 uProjectionMatrix;
uniform mat3 uNormalMatrix;

uniform bool uNormalizing;

// vertex attributes
attribute vec3 aVertexPosition;
attribute vec3 aVertexNormal;

// Interpolated data
varying vec4 vPos;
varying vec3 vNormal;

void main(void)
{
    vPos = uModelViewMatrix * vec4(aVertexPosition, 1.0);
    vNormal = uNormalMatrix * aVertexNormal;
    if (uNormalizing) vNormal = normalize(vNormal);

    gl_Position = uProjectionMatrix * vPos;
}
