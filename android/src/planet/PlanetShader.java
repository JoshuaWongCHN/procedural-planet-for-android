package planet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

import java.util.logging.Logger;

public class PlanetShader extends DefaultShader {
    private int u_lightPos;
    private int u_cubeMap;
    private int u_normalCubeMap;

    private Cubemap mCubemap;
    private Cubemap mNormalCubemap;

    public PlanetShader(Renderable renderable) {
        super(renderable, new Config(), new ShaderProgram(Gdx.files.internal("planet.vert").readString(),
                Gdx.files.internal("planet.frag").readString()));
        u_lightPos = register(new Uniform("u_lightPos"));
        u_cubeMap = register(new Uniform("u_cubeMap"));
        u_normalCubeMap = register(new Uniform("u_normalCubeMap"));

        long time = System.currentTimeMillis();
        initCubemap();
        long diff = System.currentTimeMillis() - time;
        Logger.getLogger("joshua").info(String.valueOf(diff));
    }

    private void initCubemap() {
        Pixmap[] maps = ScalarField.createCubePixmaps(1024);

        mCubemap = new Cubemap(maps[0], maps[1], maps[2], maps[3], maps[4], maps[5]);
        mNormalCubemap = new Cubemap(
                Utils.heightToNormalMap(maps[0]),
                Utils.heightToNormalMap(maps[1]),
                Utils.heightToNormalMap(maps[2]),
                Utils.heightToNormalMap(maps[3]),
                Utils.heightToNormalMap(maps[4]),
                Utils.heightToNormalMap(maps[5]));
    }

    @Override
    public void render(Renderable renderable) {
        set(u_lightPos, new Vector3(100, 0, 0));
        set(u_cubeMap, mCubemap);
        set(u_normalCubeMap, mNormalCubemap);
        super.render(renderable);
    }
}
