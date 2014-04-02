package com.example.nebula;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

//placeHolder scene class for the main menu, currently only includes a start menu item 
public class MainMenuScene extends MenuScene
{
	BaseActivity activity;
	final int MENU_START = 0;

	public MainMenuScene()
	{
		
		super(BaseActivity.getSharedInstance().mCamera);
		
		activity = BaseActivity.getSharedInstance();
		
		SpriteBackground bg = new SpriteBackground(new Sprite(0, 0, GraphicContainer.getSharedInstance().menuBgTexture,BaseActivity.getSharedInstance().getVertexBufferObjectManager()));
		setBackground(bg);
		
		ButtonSprite startbutton = new ButtonSprite(35, 300, GraphicContainer.getSharedInstance().startGameMenu, activity.getVertexBufferObjectManager())
		{
			@Override
            public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)
			{
                if(pTouchEvent.isActionDown())
                {
                	activity.setCurrentScene(new GameScene());
                	SoundContainer.getSharedInstance().menuMusicSound.stop();
                }
                
                return super.onAreaTouched(pTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
	     };
	     
	     ButtonSprite helpbutton = new ButtonSprite(250, 480, GraphicContainer.getSharedInstance().helpButton, activity.getVertexBufferObjectManager())
	     {
	         @Override
	         public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)
	         {
	             if(pTouchEvent.isActionDown())
	             {
	             	activity.setCurrentScene(new HelpScene());
	             }
	             return super.onAreaTouched(pTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	         }
	     };
	     
	     ButtonSprite creditsbutton = new ButtonSprite(35, 650, GraphicContainer.getSharedInstance().creditsButton, activity.getVertexBufferObjectManager())
	     {
	    	 @Override
	    	 public boolean onAreaTouched(TouchEvent pTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)
	    	 {
	    		 if(pTouchEvent.isActionDown())
	    		 {
	    			 activity.setCurrentScene(new CreditsScene());
	    		 }
	    		 
	    		 return super.onAreaTouched(pTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	    	 }
	     };
	     
	     this.attachChild(startbutton);
	     this.registerTouchArea(startbutton);
	     this.attachChild(helpbutton);
	     this.registerTouchArea(helpbutton);
	     this.attachChild(creditsbutton);
	     this.registerTouchArea(creditsbutton);
	     this.setTouchAreaBindingOnActionDownEnabled(true);
	}
}