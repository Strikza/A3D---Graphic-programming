package fr.univ_poitiers.dptinfo.algo3d.shaders;

import android.content.Context;

/**
 * Implementation class of shaders to compute diffuse lighting using Lambertian reflectance.
 * @author Samuel Goubeau
 */
public class BlinnPhongShaders extends LightingShaders
{
    /**
     * Constructor : compile shaders and group them in a GLES program,
     * and set the various GLSL variables
     */
    public BlinnPhongShaders(Context context)
    {
        super(context);
    }
    /**
     * Method to create shaders.
     * @return program id created after compiling and linking shader programs
     */
    @Override
    public int createProgram(Context context)
    {
        return initializeShadersFromResources(context, "blinn-phong_vert.glsl", "blinn-phong_frag.glsl");
    }

}
