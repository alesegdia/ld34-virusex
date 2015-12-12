package com.alesegdia.virusex.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;

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
		weakNodeSheet = new Spritesheet("verde.png", 1, 1);
		weakNodeAnim = new Animation(1f, weakNodeSheet.get(0));
		
		midNodeSheet = new Spritesheet("azul.png", 1, 1);
		midNodeAnim = new Animation(1f, midNodeSheet.get(0));
		
		hardNodeSheet = new Spritesheet("rojo.png", 1, 1);
		hardNodeAnim = new Animation(1f, hardNodeSheet.get(0));
		
		startNodeSheet = new Spritesheet("blanco.png", 1, 1);
		startNodeAnim = new Animation(1f, startNodeSheet.get(0));
		
		goalNodeSheet = new Spritesheet("rosa.png", 1, 1);
		weakNodeAnim = new Animation(1f, weakNodeSheet.get(0));
		
		spawnerNodeSheet = new Spritesheet("marron.png", 1, 1);
		weakNodeAnim = new Animation(1f, weakNodeSheet.get(0));
	}
	
}
