package com.example.nebula;

import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;

public class GraphicContainer
{
	public static GraphicContainer instance;
	
	private BitmapTextureAtlas mBitmapTextureAtlas;
	public TextureRegion mShipTextureRegion;
	public TextureRegion pheonix;
	public TextureRegion voidray;
	public TextureRegion carrier;
	public TextureRegion shipBullet;
	public TextureRegion enemyBullet;
	public TextureRegion mBgTexture;
	public TextureRegion menuBgTexture;
	public TextureRegion startGameMenu;
	public TextureRegion helpButton;
	public TextureRegion creditsButton;
	public TextureRegion helpBackground;
	public TextureRegion creditsBackground;
	public TextureRegion nebulaTitle;
	public TextureRegion backbutton;
	
	public void loadGraphics()
	{
		//TEXTURE LOADING
		mBitmapTextureAtlas = new BitmapTextureAtlas(BaseActivity.getSharedInstance().getEngine().getTextureManager(), 2048, 2048, TextureOptions.DEFAULT);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		mBgTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, BaseActivity.getSharedInstance(), "startBackground.png", 0, 0);
		menuBgTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, BaseActivity.getSharedInstance(), "Backgroundmenu.png", 480, 0);
		creditsBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, BaseActivity.getSharedInstance(), "creditsmenu.png", 0, 800);
		helpBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, BaseActivity.getSharedInstance(), "Helpmenu.png", 480, 800);
		carrier = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, BaseActivity.getSharedInstance(), "carrier.png", 960, 0);
		voidray = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, BaseActivity.getSharedInstance(), "void_ray.png", 960, 297);
		pheonix = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, BaseActivity.getSharedInstance(), "realPheonix.png", 960, 539);
		mShipTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, BaseActivity.getSharedInstance(), "xwing.png", 960, 685);
		enemyBullet = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, BaseActivity.getSharedInstance(), "bluebullet.png", 960, 866);
		shipBullet = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, BaseActivity.getSharedInstance(), "greenbullet.png", 1034, 866);
		backbutton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, BaseActivity.getSharedInstance(), "back.JPG", 960, 956);
		creditsButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, BaseActivity.getSharedInstance(), "Credits.JPG", 960, 1021);
		helpButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, BaseActivity.getSharedInstance(), "Help.JPG", 960, 1062);
		startGameMenu = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, BaseActivity.getSharedInstance(), "playGame.JPG", 960, 1104);
		nebulaTitle = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, BaseActivity.getSharedInstance(), "titlenebula.JPG", 960, 1146);
		
		BaseActivity.getSharedInstance().getEngine().getTextureManager().loadTexture(mBitmapTextureAtlas);
	}
	
	public static GraphicContainer getSharedInstance()
	{
		if (instance == null)
		{
			instance = new GraphicContainer();
		}
		
	    return instance;
	}
}
