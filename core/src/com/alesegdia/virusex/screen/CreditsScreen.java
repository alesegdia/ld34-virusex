package com.alesegdia.virusex.screen;

import com.alesegdia.virusex.GdxGame;
import com.alesegdia.virusex.assets.Sfx;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class CreditsScreen implements Screen {

	private GdxGame g;
	private String showText;
	private String credits;
	private float nextLetter = 0.5f;
	private int nextLetterIndex;

	public CreditsScreen(GdxGame gdxGame) {
		this.g = gdxGame;
	}

	@Override
	public void show() {
		this.showText = "";
		this.credits = "made with <3 by\n\n" +
				   		" * vanilino6 (music/sfx)\n" +
					   " * nosghy (gfx)\n" +
					   " * gonzobrain (gfx)\n" +
				   	   " * razieliyo (code)\n\n" +
				   	   "if you pay us with some nanites\nwe will make an epic ending\n\n" +
				   	   "lmb => main";
		this.nextLetter = 0.1f;
		this.nextLetterIndex = 0;
		Sfx.Credits();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		System.out.println(this.nextLetter);
		if( this.nextLetter < 0 && this.nextLetterIndex < this.credits.length() )
		{
			this.nextLetter = 0.05f;
			this.showText += this.credits.charAt(this.nextLetterIndex);
			this.nextLetterIndex++;
		}
		else
		{
			this.nextLetter -= delta;			
		}
		
		if( this.showText.length() == this.credits.length() )
		{
			if( Gdx.input.justTouched() )
			{
				g.setScreen(g.introScreen);
			}
		}
		g.batch2.begin();
		g.fontBig.draw(g.batch2, this.showText, 10, Gdx.graphics.getHeight() - 10);
		g.batch2.end();
		
		System.out.println("atcredits....");
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
