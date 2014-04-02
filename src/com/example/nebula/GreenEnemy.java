package com.example.nebula;

import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.math.MathUtils;



import android.annotation.SuppressLint;
import android.util.FloatMath;
import android.util.Log;

@SuppressLint("FloatMath")
public class GreenEnemy extends GenericEnemy
{
	protected final int MAX_HEALTH = 1;
	protected final int MAX_DAMAGE = 1;
	protected final int MAX_SIZE = 30;
	protected final int MAX_SPEED = 1;
	private float scale;
	static Random r = new Random();
	float dx = (float) (Math.PI / 6);
    float dy = (float) (Math.PI / 6);
    private int rand;
    private boolean alive;
    private Engine g;
    int it = 0;
    int rev = 0;
    int degree = 0;
    float pos = 0;
	
	GreenEnemy()
	{
		hp = MAX_HEALTH;
		damage = MAX_DAMAGE;
		speed = MAX_SPEED;
		g = BaseActivity.getSharedInstance().getEngine();
		

		alive = true;
		type = MathUtils.random(1, 3);
		
		switch(type)
		{
			case 1 :
				scale = 0.3f;
				sprite = new Sprite(512,512, GraphicContainer.getSharedInstance().pheonix, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
				sprite.setWidth((float)sprite.getWidth() * scale);
		    	sprite.setHeight((float)sprite.getHeight() * scale);
				break;
			case 2 :
				scale = 0.35f;
				sprite = new Sprite(512,512, GraphicContainer.getSharedInstance().voidray, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
				sprite.setWidth((float)sprite.getWidth() * scale);
		    	sprite.setHeight((float)sprite.getHeight() * scale);
				break;
			case 3 :
				scale = 0.4f;
				sprite = new Sprite(512,512, GraphicContainer.getSharedInstance().carrier, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
				sprite.setWidth((float)sprite.getWidth() * scale);
		    	sprite.setHeight((float)sprite.getHeight() * scale);
				break;			
		}
		
		if (MathUtils.random(0, 1) >= 0.5)
		{
			rev = 1;
		}
		else
		{
			rev = -1;
		}
		
		//rand = r.nextInt(5) + 1;
		
        timer = new TimerHandler(0.25f, true, new ITimerCallback(){
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
            	if (hp > 0)
            	{
            		rand = r.nextInt(99) + 1;
            		if (rand % 10 == 0)
            			shoot();
            	}
            }
        });
        g.registerUpdateHandler(timer);

	}
	
	@Override
	public void init()
	{
		Log.v("HELLO", "does it get here");
		//r.setSeed(System.currentTimeMillis());
		alive = true;
		hp = MAX_HEALTH;
		sprite.setPosition(MathUtils.random(0, BaseActivity.CAMERA_WIDTH), 0);
		sprite.setVisible(true);
	}
	
	@Override
	synchronized public void clean(boolean unregisterTimer)
	{
		sprite.clearEntityModifiers();
		sprite.clearUpdateHandlers();
		//hp = MAX_HEALTH;
		
		if (unregisterTimer)
		{
	        g.unregisterUpdateHandler(timer);
	        timer = null;
		}
	}

	@Override
	public void shoot()
	{
		if (!alive)
			return;
		
		if (BaseActivity.getSharedInstance().mCurrentScene instanceof GameScene)
		{
			GameScene scene = (GameScene) BaseActivity.getSharedInstance().mCurrentScene;
	
	        EnemyBullet b = EnemyBulletPool.sharedBulletPool().obtainPoolItem();
	        b.sprite.setPosition(sprite.getX() + sprite.getWidth() / 2 - b.sprite.getWidth() / 2, sprite.getY());
	        MoveYModifier mod = new MoveYModifier(1.5f, b.sprite.getY(), BaseActivity.CAMERA_HEIGHT);
	
	        b.sprite.setVisible(true);
	        b.sprite.detachSelf();
	        scene.attachChild(b.sprite);
	        scene.enemyBulletList.add(b);
	        b.sprite.registerEntityModifier(mod);
	        scene.enemyBulletCount++;
	        SoundContainer.getSharedInstance().shootingSound2.setVolume(0.4f);
	        SoundContainer.getSharedInstance().shootingSound2.play();
		}

        	
	}
	
	@Override
	public void move()
	{
		switch(type)
		{
			case 1:
				moveOne();
				break;
			case 2:
				moveTwo();
				break;
			case 3:
				moveThree();
				break;
		}
	}

	private void moveOne()
	{
		int lL = 0;
        int rL = BaseActivity.CAMERA_WIDTH - (int) sprite.getWidth();
        float newX = sprite.getX() + (FloatMath.cos(dx) * speed);
        float newY = sprite.getY() + (FloatMath.sin(dy) * speed);

        // Calculate New X,Y Coordinates within Limits
        if (newX <= lL)
        {
            newX = lL;
            dx += (float) Math.PI - 2 * dx;
        }
        else if (newX >= rL)
        {
            newX = rL;
            dx += (float) Math.PI - 2 * dx;
        }
        
        sprite.setPosition(newX, newY);
	}
	
	private void moveTwo()
	{
		int lL = 0;
        int rL = BaseActivity.CAMERA_WIDTH - (int) sprite.getWidth();
        float newX = 0;// = sprite.getX() + (FloatMath.cos(dx) * speed);
        float newY = 0;// = sprite.getY() + speed;
        
        it++;
        if ((it % 200) < 140)
        {
        	newX = sprite.getX();
        	newY = sprite.getY() + speed;
        }
        else
        {
        	newX = sprite.getX() + (speed * rev);
        	newY = sprite.getY() - speed;
        }

        // Calculate New X,Y Coordinates within Limits
        if (newX <= lL)
        {
            newX = lL;
            rev *= -1;
        }
        else if (newX >= rL)
        {
            newX = rL;
            rev *= -1;
        }
        
        sprite.setPosition(newX, newY);
	}
	
	private void moveThree()
	{
		int lL = 0;
        int rL = BaseActivity.CAMERA_WIDTH - (int) sprite.getWidth();
        pos += (Math.PI / 180);
        float newX = sprite.getX() + (FloatMath.cos(pos) * speed);
        float newY = (float) (sprite.getY() + (speed * 0.8));

        // Calculate New X,Y Coordinates within Limits
        if (newX <= lL)
        {
            newX = lL;
        }
        else if (newX >= rL)
        {
            newX = rL;
        }
        
        sprite.setPosition(newX, newY);
	}
	
	@Override
	public boolean registerHit()
	{
		hp--;
		
		if (hp <= 0)
		{
			alive = false;
			GreenEnemyPool.sharedEnemyPool().recyclePoolItem(this);
			return false;
		}
		else
			return true;
	}
	
	@Override
	public void outOfBounds()
	{
		hp = 0;
		GreenEnemyPool.sharedEnemyPool().recyclePoolItem(this);
	}
}
