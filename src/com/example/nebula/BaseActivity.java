package com.example.nebula;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import android.graphics.Color;

public class BaseActivity extends SimpleBaseGameActivity
{

	static final int CAMERA_WIDTH = 480;
	static final int CAMERA_HEIGHT = 800;
	
	public Font mFont;
	public Camera mCamera;
	
	
	//A reference to the current scene
	public Scene mCurrentScene;
	public static BaseActivity instance;
	
	@Override
	public EngineOptions onCreateEngineOptions()
	{
		instance = this;
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
		engineOptions.getAudioOptions().setNeedsSound(true);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		
		return engineOptions;
	}
	
	@Override
	protected void onCreateResources()
	{
		//Loading graphics
		GraphicContainer.getSharedInstance().loadGraphics();
		
		//Loading sounds
		SoundContainer.getSharedInstance().loadSounds();
		    
		mFont = FontFactory.create(this.getFontManager(),this.getTextureManager(), 256, 256, 32, true, Color.WHITE);
	    mFont.load();
	}

	@Override
	protected Scene onCreateScene()
	{
	    mEngine.registerUpdateHandler(new FPSLogger());
	    mCurrentScene = new SplashScene();
	    return mCurrentScene;
	}
	
	public static BaseActivity getSharedInstance()
	{
	    return instance;
	}

	// to change the current main scene
	public void setCurrentScene(Scene scene)
	{
	    mCurrentScene = scene;
	    getEngine().setScene(mCurrentScene);
	}
	
	@Override
	public void onBackPressed()
	{
		if (mCurrentScene instanceof GameScene)
			((GameScene) mCurrentScene).detach();
		
		mCurrentScene = null;
		SensorListener.instance = null;
		super.onBackPressed();
	}
}