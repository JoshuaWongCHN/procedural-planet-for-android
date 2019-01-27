package com.joshua.planet.model;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.math.Vector3;

public class BoxSphereBuilder {
    private final MeshBuilder mMeshBuilder = new MeshBuilder();
    private final VertexInfo mVerTemp = new VertexInfo();

    public MeshPart build(float radius, int segment, long attributes) {
        mMeshBuilder.begin(attributes, GL20.GL_TRIANGLES);
        MeshPart part = mMeshBuilder.part("boxsphere", GL20.GL_TRIANGLES);

        mVerTemp.set(null, null, null, null);
        mVerTemp.hasUV = mVerTemp.hasPosition = mVerTemp.hasNormal = true;

        BoxGeometry box = new BoxGeometry(1, 1, 1, segment, segment, segment);
        for (int i = 0; i < box.numberOfVertices; i++) {
            Vector3 position = new Vector3(box.vertices.get(i * 3),
                    box.vertices.get(i * 3 + 1), box.vertices.get(i * 3 + 2));
            mVerTemp.position.set(position.nor().scl(radius));
            mVerTemp.normal.set(position.nor());
            mVerTemp.uv.set(box.uvs.get(i * 2), box.uvs.get(i * 2 + 1));
            mMeshBuilder.vertex(mVerTemp);
        }
        for (int i = 0; i < box.indices.size; i++) {
            mMeshBuilder.index(box.indices.get(i));
        }
        mMeshBuilder.end();
        return part;
    }
}
