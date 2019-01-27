package com.joshua.planet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

import java.util.logging.Logger;

public class PlanetShader extends DefaultShader {
    private static final boolean LOAD_MAP_ASSETS = true;
    private int u_lightPos;
    private int u_cubeMap;
    private int u_normalCubeMap;

    private Cubemap mCubemap = null;
    private Cubemap mNormalCubemap = null;

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
        Pixmap[] maps;
        if (LOAD_MAP_ASSETS) {
            maps = new Pixmap[6];
            for (int i = 0; i < 6; i++) {
                maps[i] = new Pixmap(Gdx.files.internal(Constants.RES_PATH + Constants.CUBE_MAP[i]));
            }
        } else {
            maps = ScalarField.createCubePixmaps(1024);
//            for (int i = 0; i < maps.length; i++) {
//                ByteBuffer buffer = maps[i].getPixels();
//                Bitmap bitmap = BitmapUtils.buffer2Bitmap(buffer, 1024, 1024, Bitmap.Config.ARGB_8888);
//                BitmapUtils.saveBitmap(bitmap, Constants.RES_PATH + Constants.CUBE_MAP[i], Bitmap.CompressFormat.PNG);
//                buffer.clear();
//                bitmap.recycle();
//            }
        }
        mCubemap = new Cubemap(maps[0], maps[1], maps[2], maps[3], maps[4], maps[5]);

        if (LOAD_MAP_ASSETS) {
            Pixmap[] normalMaps = new Pixmap[6];
            for (int i = 0; i < 6; i++) {
                normalMaps[i] = new Pixmap(Gdx.files.internal(Constants.RES_PATH + Constants.CUBE_NORMAL_MAP[i]));
            }
            mNormalCubemap = new Cubemap(normalMaps[0], normalMaps[1], normalMaps[2], normalMaps[3], normalMaps[4],
                    normalMaps[5]);
        } else {
            Pixmap[] normalMaps = {
                    Utils.heightToNormalMap(maps[0]),
                    Utils.heightToNormalMap(maps[1]),
                    Utils.heightToNormalMap(maps[2]),
                    Utils.heightToNormalMap(maps[3]),
                    Utils.heightToNormalMap(maps[4]),
                    Utils.heightToNormalMap(maps[5])};

//            for (int i = 0; i < maps.length; i++) {
//                ByteBuffer buffer = normalMaps[i].getPixels();
//                Bitmap bitmap = BitmapUtils.buffer2Bitmap(buffer, 1024, 1024, Bitmap.Config.ARGB_8888);
//                BitmapUtils.saveBitmap(bitmap, Constants.RES_PATH + Constants.CUBE_NORMAL_MAP[i],
//                        Bitmap.CompressFormat.PNG);
//                buffer.clear();
//                bitmap.recycle();
//            }
            mNormalCubemap = new Cubemap(normalMaps[0], normalMaps[1], normalMaps[2], normalMaps[3], normalMaps[4],
                    normalMaps[5]
            );
        }
    }

    @Override
    public void render(Renderable renderable) {
        set(u_lightPos, new Vector3(100, 0, 0));
        if (mCubemap != null) {
            set(u_cubeMap, mCubemap);
        }
        if (mNormalCubemap != null) {
            set(u_normalCubeMap, mNormalCubemap);
        }
        super.render(renderable);
    }
}
