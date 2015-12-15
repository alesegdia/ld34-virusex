package com.alesegdia.virusex.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class Gfx {

	public static Spritesheet weakNodeSheet;
	public static Spritesheet midNodeSheet;
	public static Spritesheet hardNodeSheet;
	public static Spritesheet startNodeSheet;
	public static Spritesheet goalNodeSheet;
	public static Spritesheet spawnerNodeSheet;
	public static Spritesheet virusSheet;
	public static Spritesheet enemySheet;
	public static Spritesheet flagVirusSheet;
	
	public static Animation weakNodeAnim;
	public static Animation midNodeAnim;
	public static Animation hardNodeAnim;
	public static Animation startNodeAnim;
	public static Animation goalNodeAnim;
	public static Animation spawnerNodeAnim;
	public static Animation virusAnim;
	public static Animation enemyAnim;
	public static Animation flagVirusAnim;
	
	public static Texture bg2;
	public static Texture bg2det;
	
	public static Texture controls;
	public static Texture splash;

	public static Texture basics;
	public static Texture basics2;

	public static void Initialize()
	{
		System.out.println("Initializing assets...");
		
		bg2 = new Texture("bg2.png");
		bg2det = new Texture("bg2blue.png");
		
		weakNodeSheet = new Spritesheet("greenode_8x8.png", 8, 8);
		weakNodeAnim = new Animation(0.05f, weakNodeSheet.getAll());
		weakNodeAnim.setPlayMode(PlayMode.LOOP);

		midNodeSheet = new Spritesheet("bluenode_4x5.png", 5, 4);
		midNodeAnim = new Animation(0.1f, midNodeSheet.getRange(0, 17));
		midNodeAnim.setPlayMode(PlayMode.LOOP);

		hardNodeSheet = new Spritesheet("rednode.png", 1, 10);
		hardNodeAnim = new Animation(0.05f, hardNodeSheet.getAll());
		hardNodeAnim.setPlayMode(PlayMode.LOOP);
		
		startNodeSheet = new Spritesheet("whitenode_4x4.png", 4, 4);
		startNodeAnim = new Animation(0.1f, startNodeSheet.getRange(0, 13));
		startNodeAnim.setPlayMode(PlayMode.LOOP);

		goalNodeSheet = new Spritesheet("pinknode.png", 8, 9);
		goalNodeAnim = new Animation(0.05f, goalNodeSheet.getRange(0, 68));
		goalNodeAnim.setPlayMode(PlayMode.LOOP);
		
		spawnerNodeSheet = new Spritesheet("brownode.png", 1, 10);
		spawnerNodeAnim = new Animation(0.05f, spawnerNodeSheet.getAll());
		spawnerNodeAnim.setPlayMode(PlayMode.LOOP);
		
		virusSheet = new Spritesheet("virus_1x12.png", 1, 12);
		virusAnim = new Animation(0.1f, virusSheet.getAll());
		virusAnim.setPlayMode(PlayMode.LOOP);
		
		enemySheet = new Spritesheet("antibody.png", 1, 17);
		enemyAnim = new Animation(0.05f, enemySheet.getAll());
		enemyAnim.setPlayMode(PlayMode.LOOP);
		
		flagVirusSheet = new Spritesheet("flagvirus_1x13.png", 1, 13);
		flagVirusAnim = new Animation(0.1f, flagVirusSheet.getAll());
		flagVirusAnim.setPlayMode(PlayMode.LOOP);
		
		controls = new Texture("controlsx2.png");
		splash = new Texture("main6.png");

		basics = new Texture("basics.png");
		basics2 = new Texture("basics2.png");
		

		System.out.println("Assets loaded");
	}
	
}
