package com.joshua.planet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class PlanetShader extends DefaultShader {
    public PlanetShader(Renderable renderable) {
        super(renderable, new Config(), new ShaderProgram(Gdx.files.internal("planet.vert").readString(),
                Gdx.files.internal("planet.frag").readString()));
    }

    @Override
    public void render(Renderable renderable) {

        super.render(renderable);
    }
}
