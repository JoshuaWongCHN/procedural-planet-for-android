package planet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector3;

import static com.joshua.planet.ScalarField.InterpolationType.NEAREST_NEIGHBOUR;
import static com.joshua.planet.ScalarField.InterpolationType.TRI_COSINE;


public class ScalarField {
    enum InterpolationType {
        TRI_COSINE, NEAREST_NEIGHBOUR
    }

    public static Pixmap[] createCubePixmaps(int width) {
        Pixmap[] maps = new Pixmap[6];
        for (int i = 0; i < maps.length; i++) {
            maps[i] = createMap(width, width, i, 512);
        }
        return maps;
    }


    private static Pixmap createMap(int width, int height, int index, int maxDetail) {
        Pixmap map = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Vector3 sphericalCoord = getSphericalCoord(index, x, y, width);
                Color color = getPixel(sphericalCoord, maxDetail);
                // ABGR->RBGA
                map.drawPixel(x, y, color.toIntBits());
            }
        }
        return map;
    }

    private static Vector3 getSphericalCoord(int index, int x, int y, int width) {
        width /= 2;
        x -= width;
        y -= width;
        Vector3 coord = new Vector3();

        if (index == 0) {
            coord.x = width;
            coord.y = -y;
            coord.z = -x;
        } else if (index == 1) {
            coord.x = -width;
            coord.y = -y;
            coord.z = x;
        } else if (index == 2) {
            coord.x = x;
            coord.y = width;
            coord.z = y;
        } else if (index == 3) {
            coord.x = x;
            coord.y = -width;
            coord.z = -y;
        } else if (index == 4) {
            coord.x = x;
            coord.y = -y;
            coord.z = width;
        } else if (index == 5) {
            coord.x = -x;
            coord.y = -y;
            coord.z = -width;
        }

        return coord.nor();
    }

    private static Color getPixel(Vector3 pos, int maxDetail) {
        int resolution1 = 4;
        int resolution2 = 16;
        int resolution3 = 64;
        int resolutionMax = maxDetail;

        float level1 = helper(pos, resolution1, TRI_COSINE);
        float level2 = helper(pos, resolution2, TRI_COSINE);
        float level3 = helper(pos, resolution3, TRI_COSINE);
        float levelMax = helper(pos, resolutionMax, NEAREST_NEIGHBOUR);

        float c = 0.5f;
        c *= 1 + level1 * 0.75f;
        c *= 1 + level2 * 0.25f;
        c *= 1 + level3 * 0.075f;
        c *= 1 + levelMax * 0.04f;

        // 增加高度差
        if (c < 0.3f) {
            c *= 0.7f;
        } else if (c < 0.6f) {
            c *= 0.9;
        }
//        if (c < 0.5f) c *= 0.8f;

        c = Utils.clamp(c, 0f, 1f);

        // ABGR
        return new Color().set(1f, c, c, c);
    }

    private static float helper(Vector3 pos, int resolution, InterpolationType type) {
        // Because the sphere sample function gives normalized coordinates:
        float fx = (pos.x + 1f) / 2f * resolution;
        float fy = (pos.y + 1f) / 2f * resolution;
        float fz = (pos.z + 1f) / 2f * resolution;

        float interpolated = 0;
        if (type == TRI_COSINE) {
            interpolated = Utils.tricosineInterpolation(fx, fy, fz);
        } else if (type == NEAREST_NEIGHBOUR) {
            interpolated = Utils.random4((int) Math.floor(fx), (int) Math.floor(fy), (int) Math.floor(fz));
        }
        return interpolated * 2 - 1; // Gives values (-1, 1)
    }
}