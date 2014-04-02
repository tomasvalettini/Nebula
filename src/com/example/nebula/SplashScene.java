package com.example.nebula;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.modifier.IModifier;

public class SplashScene extends Scene
{
	BaseActivity activity;
	public Sprite sprite;
	
	public SplashScene()
	{
		SpriteBackground bg = new SpriteBackground(new Sprite(0, 0, GraphicContainer.getSharedInstance().mBgTexture,BaseActivity.getSharedInstance().getVertexBufferObjectManager()));
		setBackground(bg);
		activity = BaseActivity.getSharedInstance();
		sprite = new Sprite(30,0, GraphicContainer.getSharedInstance().nebulaTitle, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		SoundContainer.getSharedInstance().menuMusicSound.play();
		attachChild(sprite);
		sprite.registerEntityModifier(new MoveYModifier(6, 900, 31));
		loadResources();
	}
	
	void loadResources()
	{
		DelayModifier dMod = new DelayModifier(7, new IEntityModifierListener()
		{
			@Override
			public void onModifierStarted(IModifier<IEntity> arg0, IEntity arg1){}
			
			public void onModifierFinished(IModifier<IEntity> arg0, IEntity arg1)
			{
				activity.setCurrentScene(new MainMenuScene());
			}
		});
	
		registerEntityModifier(dMod);
	}
}
