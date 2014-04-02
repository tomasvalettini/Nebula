package com.example.nebula;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.util.color.Color;

public class HealthBar
{
	public Rectangle sprite;
    public static HealthBar instance;
    Camera mCamera;
    private int maxHP;
    public boolean dec;
    
    HealthBar(int hp)
    {
    	maxHP = hp;
        mCamera = BaseActivity.getSharedInstance().mCamera;
        sprite = new Rectangle(0, 0, BaseActivity.CAMERA_WIDTH / 3, 10, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
        sprite.setPosition(BaseActivity.CAMERA_WIDTH - sprite.getWidth(), 0);
        sprite.setColor(Color.RED);
        dec = false;
    }
    
    public void showHealth(int newHP)
    {
    	sprite.setWidth((BaseActivity.CAMERA_WIDTH / 3) * newHP / maxHP);
    	sprite.setPosition(BaseActivity.CAMERA_WIDTH - sprite.getWidth(), 0);
    }
}
