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

// Interpolated data
varying vec4 vPos;
varying vec3 vNormal;
varying vec3 h;

void main(void) {

    vec4 color;

    if (uLighting) {
        // Lambert color
        vec3 lightdir = normalize(uLightPos-vPos.xyz);
        vec3 normal = normalize(vNormal);
        float weight = max(dot(normal, lightdir), 0.0);
        vec4 colorLamb = uMaterialColor * (uAmbiantLight + weight * uLightColor);

        // Specular color
        float specular = pow(
                            max(dot(vNormal, normalize(h)), 0.0),
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
        ) ;
        color *= attenuation;
    }
    else{
        color = uMaterialColor;
    }

    gl_FragColor = color;
}
