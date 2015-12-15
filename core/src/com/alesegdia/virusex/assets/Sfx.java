package com.alesegdia.virusex.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Sfx {

	public static Music maintheme;
	public static Music detected;
	public static Music win;
	public static Music lose;
	public static Music mainmenu;
	public static Music credits;
	
	public static Sound hack;
	
	private static Music lastPlaying;
	
	public static void MusicOff()
	{
		if( lastPlaying != null ) lastPlaying.setVolume(0.2f);
	}
	
	public static void MusicOn()
	{
		lastPlaying.setVolume(1);
		mainmenu.setVolume(0);
	}
	
	public static void Detected()
	{
		maintheme.setVolume(0);
		detected.setVolume(1);
		lastPlaying = detected;
		mainmenu.setVolume(0);

	}
	
	public static void MainMenu()
	{
		MusicOff();
		maintheme.setVolume(0);
		detected.setVolume(0);
		mainmenu.play();
		mainmenu.setVolume(1);
	}
	
	public static void Credits()
	{
		//MusicOff();
		mainmenu.setVolume(0);
		maintheme.setVolume(0);
		detected.setVolume(0);

		credits.play();
		credits.setVolume(1);


	}
	
	public static void Undetected()
	{
		maintheme.setVolume(1);
		detected.setVolume(0f);
		lastPlaying = maintheme;
		mainmenu.setVolume(0);

	}
	
	public static void Initialize()
	{
		win = Gdx.audio.newMusic(Gdx.files.internal("sfx/win_2.ogg"));
		lose = Gdx.audio.newMusic(Gdx.files.internal("sfx/lose_2.ogg"));
		
		mainmenu = Gdx.audio.newMusic(Gdx.files.internal("sfx/tension.ogg"));
		mainmenu.setLooping(true);
		
		credits  = Gdx.audio.newMusic(Gdx.files.internal("sfx/credits.ogg"));
		credits.setLooping(true);
		
		maintheme = Gdx.audio.newMusic(Gdx.files.internal("sfx/maintheme.ogg"));
		detected  = Gdx.audio.newMusic(Gdx.files.internal("sfx/detected.ogg"));
		maintheme.setLooping(true);
		maintheme.setVolume(1);
		detected.setLooping(true);
		detected.setVolume(1);
		hack = Gdx.audio.newSound(Gdx.files.internal("sfx/hack.ogg"));
	}

}
