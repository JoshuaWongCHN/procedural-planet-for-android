package com.joshua.planet.model;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.logging.Logger;

public class BoxSphereBuilder {
    private final MeshBuilder mMeshBuilder = new MeshBuilder();
    private final VertexInfo mVerTemp = new VertexInfo();

    private float radius = 1f;
    private int detail = 1;

    public MeshPart build(float radius, int detail, long attributes) {
        this.radius = radius;
        this.detail = detail;
        mMeshBuilder.begin(attributes, GL20.GL_TRIANGLES);
        MeshPart part = mMeshBuilder.part("boxsphere", GL20.GL_TRIANGLES);

        mVerTemp.set(null, null, null, null);
        mVerTemp.hasUV = mVerTemp.hasPosition = mVerTemp.hasNormal = true;

        subdivide();
        mMeshBuilder.end();
        new BoxShapeBuilder().build()
        return part;
    }

    private void subdivide() {
        Vector3 a = new Vector3();
        Vector3 b = new Vector3();
        Vector3 c = new Vector3();

        // iterate over all faces and apply a subdivison with the given detail value
        for (int i = 0; i < ICO_INDICES.length; i += 3) {
            // get the vertices of the face
            getVertexByIndex(ICO_INDICES[i + 0], a);
            getVertexByIndex(ICO_INDICES[i + 1], b);
            getVertexByIndex(ICO_INDICES[i + 2], c);
            // perform subdivision
            subdivideFace(a, b, c);
        }
    }

    private void subdivideFace(Vector3 a, Vector3 b, Vector3 c) {
        int cols = (int) Math.pow(2, detail);
        // we use this multidimensional array as a data structure for creating the subdivision
        Vector3[][] v = new Vector3[cols + 1][];
        int i, j;
        // construct all of the vertices for this subdivision
        for (i = 0; i <= cols; i++) {
            Vector3 aj = a.cpy().lerp(c, i * 1f / cols);
            Vector3 bj = b.cpy().lerp(c, i * 1f / cols);

            int rows = cols - i;
            v[i] = new Vector3[rows + 1];

            for (j = 0; j <= rows; j++) {
                if (j == 0 && i == cols) {
                    v[i][j] = aj;
                } else {
                    v[i][j] = aj.cpy().lerp(bj, j * 1f / rows);
                }
            }
        }

        // construct all of the faces
        for (i = 0; i < cols; i++) {
            for (j = 0; j < 2 * (cols - i) - 1; j++) {
                int k = (int) Math.floor(j / 2);
                if (j % 2 == 0) {
                    pushVertex(v[i][k + 1]);
                    pushVertex(v[i + 1][k]);
                    pushVertex(v[i][k]);
                } else {
                    pushVertex(v[i][k + 1]);
                    pushVertex(v[i + 1][k + 1]);
                    pushVertex(v[i + 1][k]);
                }
            }
        }
    }

    int count = 0;

    private void pushVertex(Vector3 vertex) {
        count++;
        Logger.getLogger("joshua").info("x= " + vertex.x + " y= " + vertex.y + " z= " + vertex.z);
        mVerTemp.position.set(applyRadius(vertex));
        mVerTemp.normal.set(vertex.cpy().nor());
        mVerTemp.uv.set(generateUVs(vertex));
        mMeshBuilder.index(mMeshBuilder.vertex(mVerTemp));
    }

    private Vector3 applyRadius(Vector3 vertex) {
        Vector3 temp = vertex.cpy();
        return temp.nor().scl(radius);
    }

    private Vector2 generateUVs(Vector3 vertex) {
        float u = (float) (azimuth(vertex) / 2 / Math.PI + 0.5f);
        float v = (float) (inclination(vertex) / Math.PI + 0.5f);
        return new Vector2(u, v);
    }

    private void getVertexByIndex(int index, Vector3 vertex) {
        int stride = index * 3;
        vertex.x = ICO_VERTICES[stride + 0];
        vertex.y = ICO_VERTICES[stride + 1];
        vertex.z = ICO_VERTICES[stride + 2];
    }

}
