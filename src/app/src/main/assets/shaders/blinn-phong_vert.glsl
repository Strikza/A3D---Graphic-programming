#version 100
precision mediump float;

// Transformation Matrices
uniform mat4 uModelViewMatrix;
uniform mat4 uProjectionMatrix;
uniform mat3 uNormalMatrix;

// Material definition
uniform bool uNormalizing;

// Texture attributes
uniform bool uTexturing;

// Vertex attributes
attribute vec3 aVertexPosition;
attribute vec3 aVertexNormal;

// Texture attributes
attribute vec2 aTexCoord;

// Interpolated data
varying vec4 vPos;
varying vec3 vNormal;
varying vec2 vTexCoord;

void main(void)
{
    vPos = uModelViewMatrix * vec4(aVertexPosition, 1.0);
    vNormal = uNormalMatrix * aVertexNormal;

    if (uTexturing){
        vTexCoord = aTexCoord;
    }

    if (uNormalizing) vNormal = normalize(vNormal);

    gl_Position = uProjectionMatrix * vPos;
}
