package com.joshua.planet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.joshua.planet.model.BoxSphereBuilder;

import static com.badlogic.gdx.graphics.VertexAttributes.Usage.Normal;
import static com.badlogic.gdx.graphics.VertexAttributes.Usage.Position;
import static com.badlogic.gdx.graphics.VertexAttributes.Usage.TextureCoordinates;

public class ProceduralPlanet extends ApplicationAdapter {
    private Camera mCamera;
    private PlanetShader mShader;
    private Renderable mRenderable;
    private CameraInputController mController;
    private RenderContext mRenderContex;
    private Vector2 mCameraStartPos = new Vector2((float) (-Math.PI * 2 / 4), (float) (-Math.PI * 1 / 4)).scl(1f);

    @Override
    public void create() {
        Color bgColor = Color.valueOf("#000000");
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);

        mCamera = new PerspectiveCamera(60f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mCamera.near = 0.1f;
        mCamera.far = 10000f;
        mCamera.update();

        MeshPart boxSphere = new BoxSphereBuilder().build(5, 64, Position | Normal | TextureCoordinates);
        mRenderable = new Renderable();
        mRenderable.meshPart.set(boxSphere);
        mRenderable.worldTransform.idt();

        mRenderContex = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));

        mShader = new PlanetShader(mRenderable);
        mShader.init();

//        mController = new CameraInputController(mCamera);
//        Gdx.input.setInputProcessor(mController);
    }

    private long mTime = System.currentTimeMillis();

    @Override
    public void render() {
//        mController.update();

        long newTime = System.currentTimeMillis();
        long diff = newTime - mTime;
        mTime = newTime;
        mCameraStartPos.x += diff / 3000f;
        Vector2 cameraPos = mCameraStartPos.cpy();
        mCamera.position.y = -cameraPos.y;
        mCamera.position.x = (float) Math.sin(cameraPos.x);
        mCamera.position.z = (float) Math.cos(cameraPos.x);

        zoomCamera();

        mCamera.lookAt(new Vector3(0, 0, 0));
        mCamera.update();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        mRenderContex.begin();
        mShader.begin(mCamera, mRenderContex);
        mShader.render(mRenderable);
        mShader.end();
        mRenderContex.end();
    }

    private float getScaledRadius(float radius) {
        return (float) (Math.exp(radius) - Math.exp(6f) + 15f);
    }

    private void zoomCamera() {
        mCamera.position.nor().scl(getScaledRadius(6f));
    }

    @Override
    public void dispose() {
        mShader.dispose();
    }
}
