package planet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888;

public class Utils {
    private static final Random sRandom = new Random();
    private static final Random sSeedRandom = new Random(0);

    public static float clamp(float number, float from, float to) {
        return Math.max(Math.min(number, to), from);
    }

    private static final int N = 256 * 256;
    private static final float[] G = new float[N];
    private static final int[] P = new int[N];

    static {
        for (int i = 0; i < N; i++) {
            G[i] = (float) Math.random();
        }
        for (int i = 0; i < N; i++) {
            P[i] = randomInt(0, N - 1);
        }
    }

    public static float random4(int i, int j, int k) {
        return G[(i + P[(j + P[k % N]) % N]) % N];
    }

    private static int randomInt(int from, int to) {
        return (int) Math.floor(sSeedRandom.nextFloat() * (to + 1 - from) + from);
    }

    //
//    SS.util.nearestNeighbour = function(coordFloat, scalarField) {
//        return scalarField(Math.floor(coordFloat.x), Math.floor(coordFloat.y), Math.floor(coordFloat.z));
//    }
//
    private static float cosineInterpolation(float a, float b, float x) {
        float ft = x * 3.1415927f;
        float f = (float) ((1 - Math.cos(ft)) * 0.5f);
        return a * (1 - f) + b * f;
    }

    public static float tricosineInterpolation(float x, float y, float z) {
        int[] coord0 = {(int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z)};
        int[] coord1 = {coord0[0] + 1, coord0[1] + 1, coord0[2] + 1};
        float xd = (x - coord0[0]) / Math.max(1, (coord1[0] - coord0[0]));
        float yd = (y - coord0[1]) / Math.max(1, (coord1[1] - coord0[1]));
        float zd = (z - coord0[2]) / Math.max(1, (coord1[2] - coord0[2]));
        float c00 = cosineInterpolation(random4(coord0[0], coord0[1], coord0[2]), random4(coord1[0], coord0[1],
                coord0[2]),
                xd);
        float c10 = cosineInterpolation(random4(coord0[0], coord1[1], coord0[2]), random4(coord1[0], coord1[1],
                coord0[2]),
                xd);
        float c01 = cosineInterpolation(random4(coord0[0], coord0[1], coord1[2]), random4(coord1[0], coord0[1],
                coord1[2]),
                xd);
        float c11 = cosineInterpolation(random4(coord0[0], coord1[1], coord1[2]), random4(coord1[0], coord1[1],
                coord1[2]),
                xd);
        float c0 = cosineInterpolation(c00, c10, yd);
        float c1 = cosineInterpolation(c01, c11, yd);
        return cosineInterpolation(c0, c1, zd);
    }

    public static Pixmap heightToNormalMap(Pixmap map) {
        // 贴图转法线贴图
        int width = map.getWidth();
        int height = map.getHeight();

        Pixmap normalMap = new Pixmap(width, height, RGBA8888);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Vector3 pixel00 = new Vector3(0, 0, getHeight(map, x, y));
                Vector3 pixel01 = new Vector3(0, 1, getHeight(map, x, y + 1));
                Vector3 pixel10 = new Vector3(1, 0, getHeight(map, x + 1, y));
                Vector3 orto = pixel10.cpy().sub(pixel00).crs(pixel01.cpy().sub(pixel00)).nor();

                // ABGR
                Color color = new Color(1f, -orto.z, orto.y + 0.5f, orto.x + 0.5f);

                normalMap.drawPixel(x, y, color.toIntBits()/*ABGR->RBGA*/);
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
        return (color.r + color.g + color.b) / 3f;
    }
}
