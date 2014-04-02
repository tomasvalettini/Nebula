package com.example.nebula;

import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.sprite.Sprite;

public class GenericEnemy
{
	public int hp;
	public int speed;
	public int damage;
	public Sprite sprite;
	public TimerHandler timer;
	public int type;
	
	public void shoot(){}
	public void move(){}
	public void init(){}
	public void clean(boolean t){}
	public void outOfBounds(){}
	
	public boolean registerHit()
	{
		return false;
	}
}