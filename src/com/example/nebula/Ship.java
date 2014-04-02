package com.example.nebula;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.sprite.Sprite;

public class Ship
{
	protected final int MAX_HP = 5;
	protected final float SCALE = 0.4f;
    public Sprite sprite;
    public static Ship instance;
    Camera mCamera;
	private boolean moveable;
	private int hp;

    public static Ship getSharedInstance()
    {
        if (instance == null)
            instance = new Ship();
        
        return instance;
    }

    private Ship()
    {
    	sprite = new Sprite(0, 0, GraphicContainer.getSharedInstance().mShipTextureRegion, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
    	//sprite.setScale(0.4f);
    	
    	sprite.setWidth((float)sprite.getWidth() * SCALE);
    	sprite.setHeight((float)sprite.getHeight() * SCALE);
    	hp = MAX_HP;
    	mCamera = BaseActivity.getSharedInstance().mCamera;
        sprite.setPosition(240, 800);
        SoundContainer.getSharedInstance().enterthelvlSound.play();
        sprite.registerEntityModifier(new MoveYModifier(3, 800, 700));
        moveable = true;
    }
    
    public void moveShip(float accelerometerSpeedX, float accelerometerSpeedY)
    {
        if (!moveable)
            return;

        if ((accelerometerSpeedX != 0) || (accelerometerSpeedY != 0))
        {
            int lL = 0;
            int rL = (int) (mCamera.getWidth() - (int) sprite.getWidth());
            int uL = 0;
            int bL = (int) (mCamera.getHeight() - (int) sprite.getHeight());
            float newX = sprite.getX() + accelerometerSpeedX;
            float newY = sprite.getY() + accelerometerSpeedY;

            // Calculate New X,Y Coordinates within Limits
            if (newX < lL)
                newX = lL ;
            else if (newX > rL)
                newX = rL;
            
            if (newY < uL)
                newY = uL;
            else if (newY > bL)
                newY = bL;
            
            sprite.setPosition(newX, newY);
        }
    }
    
    // shoots bullets
    public void shoot()
    {
        if (!moveable)
            return;
    
        GameScene scene = (GameScene) BaseActivity.getSharedInstance().mCurrentScene;

        Bullet b = BulletPool.sharedBulletPool().obtainPoolItem();
        b.sprite.setPosition(sprite.getX() + sprite.getWidth() / 2 - b.sprite.getWidth() / 2, sprite.getY());
        MoveYModifier mod = new MoveYModifier(1.5f, b.sprite.getY(), -b.sprite.getHeight());

        b.sprite.setVisible(true);
        b.sprite.detachSelf();
        scene.attachChild(b.sprite);
        scene.bulletList.add(b);
        b.sprite.registerEntityModifier(mod);
        scene.bulletCount++;
        
     
        SoundContainer.getSharedInstance().shootingSound2.setVolume(0.8f);
        SoundContainer.getSharedInstance().shootingSound2.play();
    }
    
    // resets the ship to the middle of the screen
    public void restart()
    {
        moveable = false;
        Camera mCamera = BaseActivity.getSharedInstance().mCamera;
        MoveXModifier mod = new MoveXModifier(0.2f, sprite.getX(), mCamera.getWidth() / 2 - sprite.getWidth() / 2)
        {
            @Override
            protected void onModifierFinished(IEntity pItem)
            {
                super.onModifierFinished(pItem);
                moveable = true;
            }
        };
        
        sprite.registerEntityModifier(mod);
    }
    
    public boolean isDead()
    {
    	if (hp <= 0)
    	{
    		moveable = false;
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    public boolean registerHit()
    {
    	hp--;
    	return isDead();
    }
    
    public int getHP()
    {
    	return hp;
    }
    
    public void applyHealthPU(int health)
    {
    	if (MAX_HP < (health + hp))
    	{
    		hp = MAX_HP;
    	}
    	else
    	{
    		hp += health;
    	}
    }
}

