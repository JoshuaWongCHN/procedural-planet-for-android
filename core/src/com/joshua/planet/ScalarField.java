package com.joshua.planet;

import com.badlogic.gdx.graphics.Color;

import static com.joshua.planet.ScalarField.InterpolationType.NEAREST_NEIGHBOUR;
import static com.joshua.planet.ScalarField.InterpolationType.TRI_COSINE;


public class ScalarField {
    enum InterpolationType {
        TRI_COSINE, NEAREST_NEIGHBOUR
    }

    public static Color getPixel(int x, int y, int z, int maxDetail) {
        int resolution1 = 4;
        int resolution2 = 16;
        int resolution3 = 64;
        int resolutionMax = maxDetail;

        float randomScalarField = Utils.random4(x, y, z);

        float level1 = helper(x, y, z, randomScalarField, resolution1, TRI_COSINE);
        float level2 = helper(x, y, z, randomScalarField, resolution2, TRI_COSINE);
        float level3 = helper(x, y, z, randomScalarField, resolution3, TRI_COSINE);
        float levelMax = helper(x, y, z, randomScalarField, resolutionMax, NEAREST_NEIGHBOUR);

        float c = 0.5f;
        c *= 1 + level1 * 0.75f;
        c *= 1 + level2 * 0.25f;
        c *= 1 + level3 * 0.075f;
        c *= 1 + levelMax * (1 / 25);

        if (c < 0.5f) c *= 0.9f;

        c = Utils.clamp(c, 0, 1);

        return new Color().set(c, c, c, 1f);
    }

    private static float helper(int x, int y, int z, float scalarField, int resolution, InterpolationType type) {
        // Because the sphere sample function gives normalized coordinates:
        float fx = (x + 1f) / 2f * resolution;
        float fy = (y + 1f) / 2f * resolution;
        float fz = (z + 1f) / 2f * resolution;

        float interpolated = 0;
        if (type == TRI_COSINE) {
            interpolated = Utils.tricosineInterpolation(fx, fy, fz);
        } else if (type == NEAREST_NEIGHBOUR) {
            interpolated = Utils.random4((int) Math.floor(fx), (int) Math.floor(fy), (int) Math.floor(fz));
        }
        return interpolated * 2 - 1; // Gives values (-1, 1)
    }
}