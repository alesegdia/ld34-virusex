package com.alesegdia.virusex;

import com.alesegdia.virusex.assets.Gfx;
import com.alesegdia.virusex.screen.GameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GdxGame extends Game {
	
	public GameScreen gameScreen;
	public SpriteBatch batch;
	public BitmapFont font;
	public ShapeRenderer shapeRenderer;
	
	@Override
	public void create () {
		gameScreen = new GameScreen(this);
		setScreen(gameScreen);
		batch = new SpriteBatch();
		font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		
		Gfx.Initialize();
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}
}
