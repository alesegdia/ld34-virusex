package com.alesegdia.virusex;

import com.alesegdia.virusex.assets.Gfx;
import com.alesegdia.virusex.screen.GameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GdxGame extends Game {
	
	public GameScreen gameScreen;
	public SpriteBatch batch;
	public SpriteBatch batch2;
	public BitmapFont font;
	public ShapeRenderer shapeRenderer;
	private boolean editorMode;
	public BitmapFont fontBig;
	public OrthographicCamera cam;
	
	public GdxGame(boolean b) {
		this.editorMode = b;
		RNG.rng = new RNG();
	}

	@Override
	public void create () {
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		gameScreen = new GameScreen(this, "newLevel.json", editorMode );
		setScreen(gameScreen);
		batch = new SpriteBatch();
		batch2 = new SpriteBatch();
		font = new BitmapFont();
		fontBig = new BitmapFont();
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		Gfx.Initialize();
		cam.position.x = Gdx.graphics.getWidth()/2;
		cam.position.y = Gdx.graphics.getHeight()/2;
		
	}

	public void moveCam( float x, float y )
	{
		cam.position.x = x;
		cam.position.y = y;
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
