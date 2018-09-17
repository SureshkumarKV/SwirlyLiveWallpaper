package in.sureshkumarkv.swirlylivewallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.preference.PreferenceManager;
import android.view.MotionEvent;

import in.sureshkumarkv.renderlib.AndroidUtil;
import in.sureshkumarkv.renderlib.RbGeometry;
import in.sureshkumarkv.renderlib.RbInstance;
import in.sureshkumarkv.renderlib.RbListener;
import in.sureshkumarkv.renderlib.RbMaterial;
import in.sureshkumarkv.renderlib.RbMesh;
import in.sureshkumarkv.renderlib.RbNode;
import in.sureshkumarkv.renderlib.RbScene;
import in.sureshkumarkv.renderlib.RbShader;
import in.sureshkumarkv.renderlib.RbTexture;
import in.sureshkumarkv.renderlib.RbWorld;
import in.sureshkumarkv.renderlib.geometry.RbRectangleGeometry;
import in.sureshkumarkv.renderlib.texture.RbBitmapTexture;
import in.sureshkumarkv.renderlib.types.RbMatrix;
import in.sureshkumarkv.renderlib.wallpaper.RbWallpaperService;

/**
 * Created by SureshkumarKV on 17/09/2018.
 */

public class SwirlyWallpaperService extends RbWallpaperService  {
    public SwirlyWallpaperService(){
        super(SwirlyWallpaperEngine.class);
    }

    public static class SwirlyWallpaperEngine implements RbWallpaperEngine, SharedPreferences.OnSharedPreferenceChangeListener {
        private Context mContext;
        private RbInstance mInstance;
        private float mAnimationSpeed;
        private RbTexture mTexture;
        private RbMaterial mMaterial;

        @Override
        public void onCreate(GLSurfaceView glSurfaceView) {
            mContext = glSurfaceView.getContext().getApplicationContext();

            mInstance = new RbInstance();
            RbWorld world = new RbWorld();
            RbScene scene = new RbScene();
            final RbNode node = new RbNode();
            RbMesh mesh = new RbMesh();
            RbGeometry geometry = new RbRectangleGeometry(2, 2, 0, 0, 1, 1, true);
            mMaterial = new RbMaterial();
            final RbShader shader = new RbShader(mInstance, AndroidUtil.getAssetFile(glSurfaceView.getContext(), "shader/swirly.vs.glsl"), AndroidUtil.getAssetFile(glSurfaceView.getContext(), "shader/swirly.fs.glsl"));
            mTexture = RbBitmapTexture.fromAsset(glSurfaceView.getContext(), "texture/texture_2.png");
            mTexture.setWrappingMode(RbTexture.WRAP_MODE.REPEAT, RbTexture.WRAP_MODE.REPEAT);
            //No cameras as we just want a screen quad.

            mMaterial.setShader(shader);
            mMaterial.addTexture(mTexture);
            mesh.setMaterial(mMaterial);
            mesh.addGeometry(geometry);
            node.addMesh(mesh);
            scene.setRootNode(node);
            world.addScene(scene);

            mInstance.setWorld(world);
            mInstance.setResolution(1);
            mInstance.setFPS(50);
            mInstance.setView(glSurfaceView);
            mInstance.setListener(new RbListener() {
                private float mTime1=0.3f, mTime2=0.5f;
                private RbShader.Uniform timeUniform;

                @Override
                public void onSetupWorld(boolean isRecursive, int width, int height, RbInstance instance, RbWorld world) {
                    timeUniform = shader.getUniform("uTime");
                }

                @Override
                public void onRenderGeometry(int width, int height, RbInstance instance, RbWorld world, RbScene scene, RbNode node, RbMatrix transform, RbMesh mesh, RbGeometry geometry, RbMaterial material, long deltaTimeNanos){
                    mTime1 += mAnimationSpeed * 0.01f * deltaTimeNanos/1e9f;
                    mTime1 %= 6.28f;

                    mTime2 += mAnimationSpeed * 0.005f * deltaTimeNanos/1e9f;
                    mTime2 %= 6.28f;

                    if(timeUniform != null){
                        timeUniform.set(mTime1, mTime2);
                    }
                }
            });

            handlePreferences();
        }

        @Override
        public void onResize(int width, int height){

        }

        @Override
        public void onTouch(MotionEvent event) {

        }

        @Override
        public void onOffset(float xOffset, float yOffset) {

        }

        @Override
        public void onShown() {
            mInstance.setActive(true);
        }

        @Override
        public void onHidden() {
            mInstance.setActive(false);
        }

        @Override
        public void onDestroy(){

        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if("fps".equals(key)){
                mInstance.setFPS(5 + 55*sharedPreferences.getInt(key, 50)/100);

            }if("resolution".equals(key)){
                mInstance.setResolution(0.3f + 0.7f*sharedPreferences.getInt(key, 100)/100.0f);

            }else if("pattern".equals(key)){
                if(mTexture != null){
                    mMaterial.removeTexture(mTexture);
                }
                mTexture = RbBitmapTexture.fromAsset(mContext, "texture/texture_" + (sharedPreferences.getInt(key, 1)+1) + ".png");
                mTexture.setWrappingMode(RbTexture.WRAP_MODE.REPEAT, RbTexture.WRAP_MODE.REPEAT);
                mMaterial.addTexture(mTexture);

            }else if("speed".equals(key)){
                mAnimationSpeed = 0.1f + 2 * sharedPreferences.getInt(key, 25)/100.0f;

            }else if("reset".equals(key)){
                SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                mSharedPreferences.edit()
                        .putInt("fps", 50)
                        .putInt("resolution", 100)
                        .putInt("pattern", 1)
                        .putInt("speed", 25)
                        .commit();
            }
        }

        private void handlePreferences(){
            SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            mSharedPreferences.registerOnSharedPreferenceChangeListener(this);

            int mFPS = mSharedPreferences.getInt("fps", 50);
            int mResolution = mSharedPreferences.getInt("resolution", 100);
            int mPatternIndex = mSharedPreferences.getInt("pattern", 1);
            int mSpeed = mSharedPreferences.getInt("speed", 25);

            mSharedPreferences.edit()
                    .putInt("fps", mFPS)
                    .putInt("resolution", mResolution)
                    .putInt("pattern", mPatternIndex)
                    .putInt("speed", mSpeed)
                    .commit();

            onSharedPreferenceChanged(mSharedPreferences, "fps");
            onSharedPreferenceChanged(mSharedPreferences, "resolution");
            onSharedPreferenceChanged(mSharedPreferences, "pattern");
            onSharedPreferenceChanged(mSharedPreferences, "speed");
        }
    }
}
