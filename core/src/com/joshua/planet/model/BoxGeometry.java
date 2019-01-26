package com.joshua.planet.model;

import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;

public class BoxGeometry {
    public final ShortArray indices = new ShortArray();
    public final FloatArray vertices = new FloatArray();
    public final FloatArray normals = new FloatArray();
    public final FloatArray uvs = new FloatArray();

    public int numberOfVertices = 0;

    public BoxGeometry(float width, float height, float depth, int widthSegments, int heightSegments, int
            depthSegments) {
        // build each side of the box geometry
        buildPlane(2, 1, 0, -1, -1, depth, height, width, depthSegments, heightSegments, 0); // px
        buildPlane(2, 1, 0, 1, -1, depth, height, -width, depthSegments, heightSegments, 1); // nx
        buildPlane(0, 2, 1, 1, 1, width, depth, height, widthSegments, depthSegments, 2); // py
        buildPlane(0, 2, 1, 1, -1, width, depth, -height, widthSegments, depthSegments, 3); // ny
        buildPlane(0, 1, 2, 1, -1, width, height, depth, widthSegments, heightSegments, 4); // pz
        buildPlane(0, 1, 2, -1, -1, width, height, -depth, widthSegments, heightSegments, 5); // nz
    }

    private void buildPlane(int u, int v, int w, int udir, int vdir, float width, float height, float depth, int
            gridX, int gridY,
                            int materialIndex) {

        float segmentWidth = width / gridX;
        float segmentHeight = height / gridY;

        float widthHalf = width / 2;
        float heightHalf = height / 2;
        float depthHalf = depth / 2;

        int gridX1 = gridX + 1;
        int gridY1 = gridY + 1;

        int vertexCounter = 0;
        int groupCount = 0;

        int ix, iy;

        float[] vector = new float[3];

        // generate vertices, normals and uvs

        for (iy = 0; iy < gridY1; iy++) {

            float y = iy * segmentHeight - heightHalf;

            for (ix = 0; ix < gridX1; ix++) {

                float x = ix * segmentWidth - widthHalf;

                // set values to correct vector component
                vector[u] = x * udir;
                vector[v] = y * vdir;
                vector[w] = depthHalf;

                // now apply vector to vertex buffer
                vertices.add(vector[0], vector[1], vector[2]);

                // set values to correct vector component
                vector[u] = 0;
                vector[v] = 0;
                vector[w] = depth > 0 ? 1 : -1;

                // now apply vector to normal buffer
                normals.add(vector[0], vector[1], vector[2]);

                uvs.add(ix / gridX);
                uvs.add(1 - (iy / gridY));

                // counters
                vertexCounter += 1;
            }

        }

        // indices

        // 1. you need three indices to draw a single face
        // 2. a single segment consists of two faces
        // 3. so we need to generate six (2*3) indices per segment
        for (iy = 0; iy < gridY; iy++) {
            for (ix = 0; ix < gridX; ix++) {
                short a = (short) (numberOfVertices + ix + gridX1 * iy);
                short b = (short) (numberOfVertices + ix + gridX1 * (iy + 1));
                short c = (short) (numberOfVertices + (ix + 1) + gridX1 * (iy + 1));
                short d = (short) (numberOfVertices + (ix + 1) + gridX1 * iy);

                // faces
                indices.add(a, b, d);
                indices.add(b, c, d);

                // increase counter
                groupCount += 6;
            }
        }
        numberOfVertices += vertexCounter;
    }
}
