package com.example.nebula;

import org.andengine.entity.sprite.Sprite;


public class EnemyBullet
{
	protected final float SCALE = 0.3f;
	public Sprite sprite;
	
	public EnemyBullet()
    {
		sprite = new Sprite(0, 0, GraphicContainer.getSharedInstance().enemyBullet, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		sprite.setWidth((float)sprite.getWidth() * SCALE);
    	sprite.setHeight((float)sprite.getHeight() * SCALE);
    }
}
