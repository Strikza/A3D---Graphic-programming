#version 100
precision mediump float;

// Light source definition
uniform bool uLighting;
uniform vec4 uAmbiantLight;
uniform vec3 uLightPos;
uniform vec4 uLightColor;
uniform vec4 uLightSpecular;

// Attenuation value
uniform float uConstantAttenuation;
uniform float uLinearAttenuation;
uniform float uQuadraticAttenuation;

// Material definition
uniform vec4 uMaterialColor;
uniform vec4 uMaterialSpecular;
uniform float uMaterialShininess;

// Texture
uniform bool uTexturing;
uniform sampler2D uTextureUnit;

// Interpolated data
varying vec4 vPos;
varying vec3 vNormal;
varying vec2 vTexCoord;

void main(void) {

    vec4 color;
    vec4 color_tmp = uMaterialColor;

    if (uTexturing) color_tmp *= texture2D(uTextureUnit, vTexCoord);

    if (uLighting) {

        // Lambert color
        vec3 lightdir = normalize(uLightPos-vPos.xyz);
        vec3 normal = normalize(vNormal);
        float weight = max(dot(normal, lightdir), 0.0);
        vec4 colorLamb = color_tmp * (uAmbiantLight + weight * uLightColor);

        // Specular color
        vec3 v = normalize(-(vPos.xyz));
        vec3 h = normalize(v + lightdir);

        float specular = pow(
                            max(dot(vNormal, h), 0.0),
                            uMaterialShininess
        );
        vec4 colorSpec = uLightSpecular * specular * uMaterialSpecular;
        color = colorLamb + colorSpec;

        // Attenuation
        float d = length(uLightPos-vPos.xyz);
        float attenuation = 1.0 / (
                                uConstantAttenuation +
                                uLinearAttenuation * d +
                                uQuadraticAttenuation * (d * d)
        );
        color *= attenuation;
    }
    else{
        color = color_tmp;
    }

    gl_FragColor = color;
}
