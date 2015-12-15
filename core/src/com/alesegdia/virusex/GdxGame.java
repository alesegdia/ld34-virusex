package com.alesegdia.virusex;

import com.alesegdia.virusex.assets.Gfx;
import com.alesegdia.virusex.assets.Sfx;
import com.alesegdia.virusex.screen.CreditsScreen;
import com.alesegdia.virusex.screen.GameScreen;
import com.alesegdia.virusex.screen.IntroScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GdxGame extends Game {
	
	public GameScreen gameScreen;
	public IntroScreen introScreen;
	public SpriteBatch batch;
	public SpriteBatch batch2;
	public ShapeRenderer shapeRenderer;
	private boolean editorMode;
	public OrthographicCamera cam;
	public BitmapFont font;
	public BitmapFont fontBig;
	public BitmapFont fontRlyBig;
	public int level = 0;
	public String[] levelPaths = {
			"levels/Level-01.json",
			//"levels/Level-02.json",
			/*"levels/Level-03.json",
			"levels/Level-04.json",
			"levels/Level-05.json",*/
	};
	public String[] levelTexts = {
			"your presence is so insignificant\nas the fact of your existence",
			"i am the many",
			"you pile of junk",
			"... and Ayla is Broken, and you know it Ronimo.",
			""
	};
	public CreditsScreen creditsScreen;
	
	public GdxGame(boolean b) {
		this.editorMode = b;
		RNG.rng = new RNG();
	}

	@Override
	public void create () {
		Gfx.Initialize();
		Sfx.Initialize();

		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if( editorMode )
		{
			gameScreen = new GameScreen(this, "newLevel.json", editorMode );
		}
		else
		{
			gameScreen = new GameScreen(this, "levels/Level-01.json", editorMode );
		}
		introScreen = new IntroScreen(this);
		creditsScreen = new CreditsScreen(this);
		if( editorMode )setScreen(gameScreen);
		else setScreen(introScreen);
		batch = new SpriteBatch();
		batch2 = new SpriteBatch();
		font = new BitmapFont();
		fontBig = new BitmapFont();
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		Sfx.maintheme.setLooping(true);
		Sfx.maintheme.play();
		Sfx.detected.play();
		cam.position.x = Gdx.graphics.getWidth()/2;
		cam.position.y = Gdx.graphics.getHeight()/2;
		
		FreeTypeFontLoaderParameter fontParams = new FreeTypeFontLoaderParameter();
		fontParams.fontFileName = "visitor.ttf";
		fontParams.fontParameters.size = 20;
		fontParams.fontParameters.color = new Color(1,0,0,1);
		FileHandle fontFile = Gdx.files.internal("visitor1.ttf");
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
		fontBig = generator.generateFont(40);
		font = generator.generateFont(20);
		fontRlyBig = generator.generateFont(60);		
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
