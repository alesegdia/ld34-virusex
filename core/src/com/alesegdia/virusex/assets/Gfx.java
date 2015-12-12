package com.alesegdia.virusex.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class Gfx {

	public static Spritesheet weakNodeSheet;
	public static Spritesheet midNodeSheet;
	public static Spritesheet hardNodeSheet;
	public static Spritesheet startNodeSheet;
	public static Spritesheet goalNodeSheet;
	public static Spritesheet spawnerNodeSheet;
	public static Spritesheet playerSheet;
	public static Spritesheet enemySheet;
	
	public static Animation weakNodeAnim;
	public static Animation midNodeAnim;
	public static Animation hardNodeAnim;
	public static Animation startNodeAnim;
	public static Animation goalNodeAnim;
	public static Animation spawnerNodeAnim;
	public static Animation playerAnim;
	public static Animation enemyAnim;

	public static void Initialize()
	{
		System.out.println("Initializing assets...");
		
		weakNodeSheet = new Spritesheet("verde.png", 1, 1);
		weakNodeAnim = new Animation(1f, weakNodeSheet.get(0));
		
		midNodeSheet = new Spritesheet("azul.png", 1, 1);
		midNodeAnim = new Animation(1f, midNodeSheet.get(0));
		
		hardNodeSheet = new Spritesheet("rednode.png", 1, 10);
		hardNodeAnim = new Animation(0.05f, hardNodeSheet.getAll());
		hardNodeAnim.setPlayMode(PlayMode.LOOP);
		
		startNodeSheet = new Spritesheet("blanco.png", 1, 1);
		startNodeAnim = new Animation(1f, startNodeSheet.get(0));
		
		goalNodeSheet = new Spritesheet("pinknode.png", 8, 9);
		goalNodeAnim = new Animation(0.05f, goalNodeSheet.getRange(0, 68));
		goalNodeAnim.setPlayMode(PlayMode.LOOP);
		
		spawnerNodeSheet = new Spritesheet("brownode.png", 1, 10);
		spawnerNodeAnim = new Animation(0.05f, spawnerNodeSheet.getAll());
		spawnerNodeAnim.setPlayMode(PlayMode.LOOP);

		System.out.println("Assets loaded");
	}
	
}
