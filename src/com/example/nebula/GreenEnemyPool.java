package com.example.nebula;

import org.andengine.util.adt.pool.GenericPool;
import android.util.Log;

public class GreenEnemyPool extends GenericPool<GreenEnemy>
{
	public static boolean doneRecycling;
	public static GreenEnemyPool instance;

	public static GreenEnemyPool sharedEnemyPool() {

		if (instance == null)
			instance = new GreenEnemyPool();
		return instance;

	}

	private GreenEnemyPool() {
		super();
		doneRecycling = false;
	}
	
	public void clearPool()
	{
		Log.v("Pool", "clear pool");
		instance = null;
	}
	
	@Override
	protected void onHandleObtainItem(GreenEnemy pItem)
	{
		pItem.init();
	}
	
	public void doneRecycling()
	{
		doneRecycling = true;
	}
	
	/** Called when a projectile is sent to the pool */
	@Override
	protected void onHandleRecycleItem(final GreenEnemy e)
	{
		e.sprite.setVisible(false);
		e.sprite.detachSelf();
		e.clean(doneRecycling);
	}
	
	@Override
	protected GreenEnemy onAllocatePoolItem()
	{
		return new GreenEnemy();
	}
}