package com.example.nebula;

import org.andengine.entity.sprite.Sprite;

public class Bullet
{
	protected final float SCALE = 0.3f;
    public Sprite sprite;
    
    public Bullet()
    {
    	sprite = new Sprite(0, 0, GraphicContainer.getSharedInstance().shipBullet, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
    	sprite.setWidth((float)sprite.getWidth() * SCALE);
    	sprite.setHeight((float)sprite.getHeight() * SCALE);
    }
    
    /*public void moveBullet()
    {
    	
    }*/
}

