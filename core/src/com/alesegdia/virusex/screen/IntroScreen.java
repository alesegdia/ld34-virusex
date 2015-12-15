package com.alesegdia.virusex.screen;

import com.alesegdia.virusex.GdxGame;
import com.alesegdia.virusex.assets.Gfx;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class IntroScreen implements Screen {

	private GdxGame g;

	public IntroScreen( GdxGame g )
	{
		this.g = g;
	}
	
	@Override
	public void show() {
		state = 0;
		
	}

	int state = 0;
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		g.batch2.begin();
		if( state == 0 )
		{
			g.batch2.draw(Gfx.splash, 0, 0);
			g.font.draw(g.batch2, "play", 610, 200);

		}
		else if( state == 1 )
		{
			g.batch2.draw(Gfx.controls, 150, 100);
			g.fontRlyBig.draw(g.batch2, "play", 340, 400);
		}
		else if( state == 2 )
		{
			g.batch2.draw(Gfx.basics, 0, 0);
		}
		else if( state == 3 )
		{
			g.batch2.draw(Gfx.basics2, 0, 0);
		}
		else if( state == 4 )
		{
			g.setScreen(g.gameScreen);
		}
		
		if( Gdx.input.justTouched() )
		{
			state++;
		}
		g.batch2.end();

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
