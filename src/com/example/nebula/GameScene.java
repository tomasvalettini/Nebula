package com.example.nebula;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.RotationParticleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.entity.text.Text;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class GameScene extends Scene implements IOnSceneTouchListener
{
	public Ship ship;
	Camera mCamera;
	public float accelerometerSpeedX;
	public float accelerometerSpeedY;
	private SensorManager sensorManager;
	public LinkedList<Bullet> bulletList;
	public LinkedList<EnemyBullet> enemyBulletList;
	public LinkedList<GenericEnemy> enemyList;
	public int bulletCount;
	public int enemyBulletCount;
	public int missCount;
	public int combo;
	public GameLoopUpdateHandler gluh;
	public Score score;
	public HealthBar hpBar;
	
	public GameScene()
	{
		SpriteBackground bg = new SpriteBackground(new Sprite(0, 0, GraphicContainer.getSharedInstance().mBgTexture,BaseActivity.getSharedInstance().getVertexBufferObjectManager()));
        setBackground(bg);
	    
	    mCamera = BaseActivity.getSharedInstance().mCamera;
	    BaseActivity.getSharedInstance().setCurrentScene(this);
	    sensorManager = (SensorManager) BaseActivity.getSharedInstance().getSystemService(Context.SENSOR_SERVICE);
	    SensorListener.getSharedInstance();
	    sensorManager.registerListener(SensorListener.getSharedInstance(), sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);

	    gluh = new GameLoopUpdateHandler();
	    score = new Score();
	    combo = 0;
	    
	    BaseActivity.getSharedInstance().getEngine().registerUpdateHandler(gluh);
	    
	    setOnSceneTouchListener(this);
	    
	    // attaching an EnemyLayer entity with 12 enemies on it
	    attachChild(new Level(1));
	    
	    resetValues();
	}
	
	public void initVars()
	{
		ship = Ship.getSharedInstance();
	    attachChild(ship.sprite);
	    hpBar = new HealthBar(ship.getHP());
	    bulletList = new LinkedList<Bullet>();
	    enemyBulletList = new LinkedList<EnemyBullet>();
	    SoundContainer.getSharedInstance().firstlvl.setVolume(0.7f);
	    SoundContainer.getSharedInstance().firstlvl.play();
	    detachChild(Level.getSharedInstance());
	}
	
	public GameScene getInstance()
	{
		return this;
	}
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
	{
		if (!CoolDown.getSharedInstance().checkValidity())
			return false;
		
	    synchronized (this)
	    {
	        ship.shoot();
	    }
	    
	    return true;
	}
	
	public void moveShip()
	{
	    ship.moveShip(accelerometerSpeedX, this.accelerometerSpeedY);
	}
	
	public void moveEnemies()
	{
		synchronized (this)
	    {
			// if all Enemies are killed
	    	if (Level.isEmpty())
	    	{
	    	    return;
	    	}
	    	
	    	Iterator<GenericEnemy> eIt = Level.getIterator();
            while (eIt.hasNext())
            {
                GenericEnemy e = eIt.next();
                e.move();
            }
	    }
	}
	
	public void cleaner()
	{
		
	    synchronized (this)
	    {
	    	// if all Enemies are killed
	    	if (Level.isEmpty() || ship.isDead())
	    	{
	    		if (ship.isDead())
	    		{
	    			Level.getSharedInstance().endGame();
	    			Iterator<GenericEnemy> eIt = Level.getIterator();
	    			while (eIt.hasNext())
	    			{
	    				GenericEnemy e = eIt.next();
	    				e.outOfBounds();
	    				eIt.remove();
	    			}
	    		}
	    		
	    		this.detach();
	    		BaseActivity.getSharedInstance().getEngine().unregisterUpdateHandler(gluh);
	    		setChildScene(new ResultScene(mCamera, score.getScore()));
	    		
	    		/*if (Level.isEmpty())
	    		{
	    			//this.detach();
	    			
	    			Level.getSharedInstance().setLevel(2);
	    			
	    		}
	    		else
	    		{
		    		clearUpdateHandlers();
		    		clearEntityModifiers();
		    		//this.detach();
		    		//resetValues();
		    	    
		    	    //clearUpdateHandlers();
		    	    //clearEntityModifiers();
	    		}*/
	    	}

	    	
	    	//Handle Ship bullets and Ship colliding with enemies and going outOfBounds
	    	Iterator<GenericEnemy> eIt = Level.getIterator();
            while (eIt.hasNext())
            {
                GenericEnemy e = eIt.next();
                Iterator<Bullet> it = bulletList.iterator();
               
                
                
                if (ship.sprite.collidesWith(e.sprite) && !ship.isDead())
                {
                	if (!e.registerHit())
                    {
                		score.enemyKilled();
                    	createExplosion(e.sprite.getX(), e.sprite.getY(), Level.getSharedInstance(), BaseActivity.getSharedInstance());
                        eIt.remove();
                    }
                	hpBar.showHealth(ship.getHP() - 1);
                	combo = 0;
                	if (ship.registerHit())
                	{
                		createExplosion(ship.sprite.getX() + ship.sprite.getWidth()/2, ship.sprite.getY() + ship.sprite.getWidth()/2, Level.getSharedInstance(), BaseActivity.getSharedInstance());
                		
                		BaseActivity.getSharedInstance().getEngine().runOnUpdateThread(new Runnable() {
                            @Override
                            public void run() {
                                    /* Now it is save to remove the entity! */
                            	detachChild(ship.sprite);
                            }
                    });
                	}
                   
                }
                
                if (e.sprite.getY() >= (BaseActivity.CAMERA_HEIGHT - e.sprite.getHeight()))
                {
                	e.outOfBounds();
                	eIt.remove();
                }
                

                
    	    	//Handle ship bullets
                while (it.hasNext())
                {
                    Bullet b = it.next();
                    if (b.sprite.getY() <= -b.sprite.getHeight())
                    {
                    	BulletPool.sharedBulletPool().recyclePoolItem(b);
                        it.remove();
                        missCount++;
                        continue;
                    }

                    if (b.sprite.collidesWith(e.sprite))
                    {
                    	combo++;
                    	if (combo == 10)
                    	{
                    		combo = 0;
                    		Random coin = new Random();
                    		int coinFlip = coin.nextInt(99);
                    		
                    		if (coinFlip % 2 == 0)
                    		{
                    			ship.applyHealthPU(3);
                    			hpBar.showHealth(ship.getHP());
                    			bonusDisplay("5 Kill Streak! \nHealth Bonus!");
                    		}
                    		else
                    		{
                    			score.applyPointsPU(200);
                    			bonusDisplay("5 Kill Streak! \nPoints Bonus!");
                    		}
             
                    	}
                        if (!e.registerHit())
                        {
							score.enemyKilled();
                        	createExplosion(e.sprite.getX() + e.sprite.getWidth()/2, e.sprite.getY() +  e.sprite.getHeight()/2, Level.getSharedInstance(), BaseActivity.getSharedInstance());
                            eIt.remove();
                        }
                        
	                    BulletPool.sharedBulletPool().recyclePoolItem(b);
	                    it.remove();
	                    break;
	                }
                }
            }
            
	    	//Handle enemy bullets
            Iterator<EnemyBullet> ebIt = enemyBulletList.iterator();
	    	while (ebIt.hasNext())
	    	{
	    		EnemyBullet eb = ebIt.next();
                if (eb.sprite.getY() >= BaseActivity.CAMERA_HEIGHT)
                {
                    EnemyBulletPool.sharedBulletPool().recyclePoolItem(eb);
                    ebIt.remove();
                    continue;
                }
                
                if (eb.sprite.collidesWith(ship.sprite))
                {
                	hpBar.showHealth(ship.getHP() - 1);
                	combo = 0;
                	if (ship.registerHit())
                	{
                		createExplosion(ship.sprite.getX() + ship.sprite.getWidth()/2, ship.sprite.getY() + ship.sprite.getWidth()/2, Level.getSharedInstance(), BaseActivity.getSharedInstance());
                		
                		BaseActivity.getSharedInstance().getEngine().runOnUpdateThread(new Runnable() {
                            @Override
                            public void run() {
                                    /* Now it is save to remove the entity! */
                            	detachChild(ship.sprite);
                            }
                    });
                		
                	}
                	
                	EnemyBulletPool.sharedBulletPool().recyclePoolItem(eb);
                    ebIt.remove();
                    break;
                }
	    	}
	    }
	}
	
	// method to reset values and restart the game
	public void resetValues()
	{
		initVars();
		
	    missCount = 0;
	    bulletCount = 0;
	    ship.restart();
	    //EnemyLayer.purgeAndRestart();
	    clearChildScene();	    
	    BaseActivity.getSharedInstance().getEngine().registerUpdateHandler(gluh);
	    score.startTimer();
	    score.resetScore();
	    attachChild(score.text1);
	    attachChild(hpBar.sprite);
	    attachChild(Level.getSharedInstance());
	}
	
	public void detach()
	{
		clearUpdateHandlers();
		
		for (Bullet b : bulletList)
		{
			BulletPool.sharedBulletPool().recyclePoolItem(b);
		}
		
		for (EnemyBullet eb : enemyBulletList)
		{
			EnemyBulletPool.sharedBulletPool().recyclePoolItem(eb);
		}
		
		enemyBulletList.clear();
		bulletList.clear();
		detachChild(score.text1);
		detachChild(Level.getSharedInstance());
		BaseActivity.getSharedInstance().getEngine().runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                    /* Now it is save to remove the entity! */
            	detachChildren();
            }
    });
		Ship.instance = null;
		//EnemyPool.instance = null;
		BulletPool.instance = null;
		EnemyBulletPool.instance = null;
	}
	private void bonusDisplay(String msg)
	{
		final Text startText = new Text(0, 0, BaseActivity.getSharedInstance().mFont, msg, BaseActivity.getSharedInstance().getVertexBufferObjectManager());		
		startText.setPosition((BaseActivity.CAMERA_WIDTH - startText.getWidth()) * 0.5f, (BaseActivity.CAMERA_HEIGHT - startText.getHeight()) * 0.5f);
		startText.setVisible(true);
		startText.registerEntityModifier(new FadeInModifier(1f));
		startText.registerEntityModifier(new FadeOutModifier(2f));
		attachChild(startText);
		
	}
	
	private void createExplosion(final float posX, final float posY, final IEntity target, final SimpleBaseGameActivity activity)
	{
		SoundContainer.getSharedInstance().explosionSound2.setVolume(25f);
		SoundContainer.getSharedInstance().explosionSound2.play();
		int mNumPart = 15;
		int mTimePart = 2;
		PointParticleEmitter particleEmitter = new PointParticleEmitter(posX, posY);
		IEntityFactory<Rectangle> recFact = new IEntityFactory<Rectangle>()
		{
			@Override
			public Rectangle create(float pX, float pY)
			{
				Rectangle rect = new Rectangle(posX, posY, 10, 10, activity.getVertexBufferObjectManager());
				rect.setColor(Color.YELLOW);
				return rect;
			}	
		};
		
		final ParticleSystem<Rectangle> particleSystem = new ParticleSystem<Rectangle>(recFact, particleEmitter, 500, 500, mNumPart);
		particleSystem.addParticleInitializer(new VelocityParticleInitializer<Rectangle>(-50, 50, -50, 50));
		particleSystem.addParticleModifier(new AlphaParticleModifier<Rectangle>(0, 0.6f * mTimePart, 1, 0));
		particleSystem.addParticleModifier(new RotationParticleModifier<Rectangle>(0, mTimePart, 0, 360));
		
		target.attachChild(particleSystem);
		target.registerUpdateHandler(new TimerHandler(mTimePart, new ITimerCallback()
		{
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler)
			{
				particleSystem.detachSelf();
				target.sortChildren();
				target.unregisterUpdateHandler(pTimerHandler);
			}
		}));
	}
}
