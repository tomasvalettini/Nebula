package com.example.nebula;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.util.debug.Debug;

public class SoundContainer
{
	public static SoundContainer instance;
	
	public Music firstlvl;
	public Music menuMusicSound;
	public Sound enterthelvlSound;
	public Sound splashSound;
	public Sound shootingSound2;
	public Sound explosionSound2;
	
	public void loadSounds()
	{
		//Sound LOADING
		SoundFactory.setAssetBasePath("mfx/");
		MusicFactory.setAssetBasePath("mfx/");
		
		try
		{
			firstlvl = MusicFactory.createMusicFromAsset(BaseActivity.getSharedInstance().getEngine().getMusicManager(), BaseActivity.getSharedInstance(), "bossbattle.ogg");
			firstlvl.setLooping(true);
			enterthelvlSound = SoundFactory.createSoundFromAsset(BaseActivity.getSharedInstance().getEngine().getSoundManager(), BaseActivity.getSharedInstance(), "enterthelvl.ogg");
			menuMusicSound = MusicFactory.createMusicFromAsset(BaseActivity.getSharedInstance().getEngine().getMusicManager(), BaseActivity.getSharedInstance(), "menumusic.ogg");
			splashSound = SoundFactory.createSoundFromAsset(BaseActivity.getSharedInstance().getEngine().getSoundManager(), BaseActivity.getSharedInstance(), "splash.wav");
			shootingSound2 = SoundFactory.createSoundFromAsset(BaseActivity.getSharedInstance().getEngine().getSoundManager(), BaseActivity.getSharedInstance(), "pewpew2.wav");
			explosionSound2 = SoundFactory.createSoundFromAsset(BaseActivity.getSharedInstance().getEngine().getSoundManager(), BaseActivity.getSharedInstance(), "explosion2.wav");
		}
		catch (final IOException e)
		{
			Debug.e(e);
		}		
	}
	
	public static SoundContainer getSharedInstance()
	{
		if (instance == null)
		{
			instance = new SoundContainer();
		}
		
	    return instance;
	}
}
