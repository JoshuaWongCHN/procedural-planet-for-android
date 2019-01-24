package com.joshua.planet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector3;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;

public class Utils {
    public static Pixmap heightToNormalMap(Pixmap map) {
        // 贴图转法线贴图
        int width = map.getWidth();
        int height = map.getHeight();

        Pixmap normalMap = new Pixmap(width, height, RGBA8888);

        for (int x = 0; x < width; x++) {
            for(int y = 0; y<height; y++){
                Vector3 pixel00 = new Vector3(0, 0, getHeight(map, x, y));
                Vector3 pixel01 = new Vector3(0, 1, getHeight(map, x, y + 1));
                Vector3 pixel10 = new Vector3(1, 0, getHeight(map, x + 1, y));
                Vector3 orto = pixel10.cpy().sub(pixel00).crs(pixel01.cpy().sub(pixel00)).nor();

                Color color = new Color(orto.x + 0.5f, orto.y + 0.5f, -orto.z, 1f);

                normalMap.drawPixel(x, y, color.toIntBits());
            }
        }
        return normalMap;
    }

    private static float getHeight(Pixmap map, int x, int y) {
        int width = map.getWidth();
        int height = map.getHeight();
        x = Math.min(x, width - 1);
        y = Math.min(y, height - 1);
        Color color = new Color(map.getPixel(x, y));
        return (color.r / 255f + color.g / 255f + color.b / 255f) / 3;
    }
}
