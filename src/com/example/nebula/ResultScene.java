package com.example.nebula;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

public class ResultScene extends CameraScene implements IOnSceneTouchListener
{
	boolean done;
	BaseActivity activity;
	
	public ResultScene(Camera mCamera, int score)
	{
	    super(mCamera);
	    activity = BaseActivity.getSharedInstance();
	    setBackgroundEnabled(false);
	    
	    Text result = new Text(0, 0, activity.mFont, activity.getString(R.string.score) + " " + score + "!", BaseActivity.getSharedInstance().getVertexBufferObjectManager());
	    final int x = (int) (mCamera.getWidth() / 2 - result.getWidth() / 2);
	    final int y = (int) (mCamera.getHeight() / 2 - result.getHeight() / 2);
	    done = false;
	    result.setPosition(x, mCamera.getHeight() + result.getHeight());
	    MoveYModifier mod = new MoveYModifier(5, result.getY(), y)
	    {
	        @Override
	        protected void onModifierFinished(IEntity pItem)
	        {
	            done = true;
	        }
	    };
	    
	    attachChild(result);
	    result.registerEntityModifier(mod);
	    setOnSceneTouchListener(this);
	    
	    SoundContainer.getSharedInstance().firstlvl.pause();
	    //BaseActivity.deadMusicSound.resume();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
	{
		if (!done)
		    return true;
		
		//BaseActivity.deadMusicSound.pause();
		SoundContainer.getSharedInstance().firstlvl.resume();
		((GameScene) activity.mCurrentScene).resetValues();
		Level.getSharedInstance().setLevel(1);
		return false;
	}
}
