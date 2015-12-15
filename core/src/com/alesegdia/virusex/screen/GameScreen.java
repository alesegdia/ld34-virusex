package com.alesegdia.virusex.screen;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.alesegdia.virusex.GdxGame;
import com.alesegdia.virusex.assets.Gfx;
import com.alesegdia.virusex.assets.Sfx;
import com.alesegdia.virusex.entities.World;
import com.alesegdia.virusex.entities.node.GoalNode;
import com.alesegdia.virusex.entities.node.HardNode;
import com.alesegdia.virusex.entities.node.Link;
import com.alesegdia.virusex.entities.node.MidNode;
import com.alesegdia.virusex.entities.node.Node;
import com.alesegdia.virusex.entities.node.SpawnerNode;
import com.alesegdia.virusex.entities.node.StartNode;
import com.alesegdia.virusex.entities.node.WeakNode;
import com.alesegdia.virusex.entities.organism.Pathfinder;
import com.alesegdia.virusex.entities.organism.Virus;
import com.alesegdia.virusex.level.LevelData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;

public class GameScreen implements Screen {

	private GdxGame g;
	World gw;
	ToolMode toolMode = ToolMode.WEAK;
	private boolean devMode;
	private String levelPath;
	private boolean start;
	private boolean editMode;
	
	private float timeLeft = 100;
	
	public GameScreen( GdxGame g, String levelPath )
	{
		this(g, levelPath, true);
	}
	
	public GameScreen( GdxGame g, String levelPath, boolean devMode )
	{
		this.g = g;
		this.devMode = devMode;
		this.editMode = devMode;
		this.levelPath = levelPath;
		gw = new World();
		gw.camera = g.cam;
	}
	
	@Override
	public void show() {
		this.start = true;
		if( !devMode )
		{
			reloadLevel(g.levelPaths[g.level]);
		}
		this.timeLeft = 100;
		if( g.level == 0 )
		{
			//reloadLevel("levels/Level-05.json");
		}
	}
	
	public void reloadLevel(String levelPath)
	{
		gw.clear();
		loadLevel(levelPath);
		spawnVirus();
	}
	
	Node firstCx = null;
	Node secondCx = null;
	
	Node firstPf = null;
	Node secondPf = null;
	
	Node toMove = null;
	
	String helpText = "";
	
	String levelText = "";
	
	String levelFile = "newLevel.json";
	
	boolean instructions = true;
	
	String instructions_text = "INSTRUCTIONS\n" +
							   "============\n" +
							   "q/w/e => connect/move/delete\n" +
							   "a/s/d => spawner/start/goal\n" +
							   "z/x/c => weak/mid/high\n" +
							   "F2/F4 => save/load\n" +
							   "F6 => spawn virus\n" +
							   "F8 => toggle edit mode\n" +
							   "F1 => open/close instructions\n" +
							   "arrows => scroll\n";
	
	List<Node> path = new LinkedList<Node>();
	private float helpTextTimer;
	
	void handleEdit()
	{
		if( Gdx.input.isKeyJustPressed(Input.Keys.Z) ) toolMode = ToolMode.WEAK;
		if( Gdx.input.isKeyJustPressed(Input.Keys.X) ) toolMode = ToolMode.MID;
		if( Gdx.input.isKeyJustPressed(Input.Keys.C) ) toolMode = ToolMode.HARD;
		if( Gdx.input.isKeyJustPressed(Input.Keys.S) ) toolMode = ToolMode.START;
		if( Gdx.input.isKeyJustPressed(Input.Keys.D) ) toolMode = ToolMode.GOAL;
		if( Gdx.input.isKeyJustPressed(Input.Keys.A) ) toolMode = ToolMode.SPAWNER;
		if( Gdx.input.isKeyJustPressed(Input.Keys.Q) ) toolMode = ToolMode.CONNECT;
		if( Gdx.input.isKeyJustPressed(Input.Keys.W) ) toolMode = ToolMode.MOVE;
		if( Gdx.input.isKeyJustPressed(Input.Keys.E) ) toolMode = ToolMode.DELETE;
		if( Gdx.input.isKeyJustPressed(Input.Keys.O) ) toolMode = ToolMode.DISCONNECT;
		if( Gdx.input.isKeyJustPressed(Input.Keys.P) ) toolMode = ToolMode.PATHFIND;
		
		if( Gdx.input.isKeyPressed(Input.Keys.UP) )	g.cam.position.y += 5;
		if( Gdx.input.isKeyPressed(Input.Keys.DOWN) )	g.cam.position.y -= 5;
		if( Gdx.input.isKeyPressed(Input.Keys.LEFT) )	g.cam.position.x -= 5;
		if( Gdx.input.isKeyPressed(Input.Keys.RIGHT) )	g.cam.position.x += 5;
		
		
		if( Gdx.input.isKeyJustPressed(Input.Keys.O) )
		{
			gw.disconnectNodes();
		}
		
		if( Gdx.input.justTouched() )
		{
			float mx = Gdx.input.getX() + this.g.cam.position.x - this.g.cam.viewportWidth/2;
			float my = Gdx.graphics.getHeight() - Gdx.input.getY() + this.g.cam.position.y- this.g.cam.viewportHeight/2;
			if( toolMode == ToolMode.WEAK ) this.gw.addNode(new WeakNode(gw, mx, my));
			if( toolMode == ToolMode.MID ) this.gw.addNode(new MidNode(gw, mx, my));
			if( toolMode == ToolMode.HARD ) this.gw.addNode(new HardNode(gw, mx, my));
			if( toolMode == ToolMode.GOAL ) this.gw.addNode(new GoalNode(gw, mx, my));
			if( toolMode == ToolMode.START ) this.gw.addNode(new StartNode(gw, mx, my));
			if( toolMode == ToolMode.SPAWNER ) this.gw.addNode(new SpawnerNode(gw, mx, my));
			
			if( toolMode == ToolMode.DELETE )
			{
				Node n = this.gw.findNear( new Vector2(mx, my) );
				if( n != null )
				{
					gw.removeNode(n);
				}
			}

			if( toolMode == ToolMode.CONNECT )
			{
				Node n = this.gw.findNear( new Vector2(mx, my) );
				if( n != null )
				{
					System.out.println("Selected node");
					if( firstCx == null )
					{
						firstCx = n;
					}
					else
					{
						if( firstCx != secondCx )
						{
							System.out.println("Creating new link");
							secondCx = n;
							gw.addLink(new Link(firstCx, secondCx));
							firstCx = null;
							secondCx = null;
						}
					}
				}
			}
			
			if( toolMode == ToolMode.PATHFIND )
			{
				Node n = this.gw.findNear( new Vector2( mx, my ) );
				if( n != null )
				{
					System.out.println("Selected node");
					if( firstPf == null )
					{
						firstPf = n;
					}
					else
					{
						if( firstPf != secondPf )
						{
							secondPf = n;

							Pathfinder pf = new Pathfinder(gw.getRootNode());
							path = pf.findNextNode(firstPf, secondPf);
							
							System.out.println("path size: " + path.size());
							
							firstPf = null;
							secondPf = null;
						}
					}
				}
			}
			
			if( toolMode == ToolMode.MOVE )
			{
				//Node n = this.gw.findNear( new Vector2( mx + gw.camera.position.x - gw.camera.viewportWidth/2, my + gw.camera.position.y - gw.camera.viewportHeight/2 ) );
				Node n = this.gw.findNear( new Vector2( mx, my ) );
				
				if( n != null )
				{
					toMove = n;
				}
			}
		}

		if( toolMode == ToolMode.MOVE && toMove != null && Gdx.input.isButtonPressed(Input.Buttons.LEFT) )
		{
			float mx = Gdx.input.getX() + this.g.cam.position.x - this.g.cam.viewportWidth/2;
			float my = Gdx.graphics.getHeight() - Gdx.input.getY() + this.g.cam.position.y- this.g.cam.viewportHeight/2;
			toMove.position = new Vector2(mx, my);
		}
		
		if( Gdx.input.isKeyJustPressed(Input.Keys.F1) )
		{
			instructions = !instructions;
		}
		
		if( Gdx.input.isKeyJustPressed(Input.Keys.F6) )
		{
			spawnVirus();
		}
	}
	
	private void helpTextFn(String text)
	{
		this.helpText = text;
		this.helpTextTimer = 2f;
	}

	private void spawnVirus()
	{
		if( gw.getStartNode() == null )
		{
			helpTextFn("Start node not set");
		}
		else if( gw.getGoalNode() == null )
		{
			helpTextFn("Goal node not set");
		}
		else if( gw.getVirus() != null )
		{
			helpTextFn("Virus already spawned");
		}
		else
		{
			Node sn = gw.getStartNode();
			Vector2 start;
			if( sn != null )
			{
				start = gw.getStartNode().position;				
			}
			else
			{
				start = new Vector2(100,100);
			}
			gw.addOrganism(new Virus(gw, start.x, start.y));		
		}
	}
	
	float timer = 0;
	Matrix4 defmat = new Matrix4();
	
	@Override
	public void render(float delta)
	{

	
		if( gw.getVirus() != null )
		{
			if( !gw.getVirus().hasLost && !gw.getVirus().hasWon )
			{
				if( gw.getVirus().isStealth() )
				{
					timeLeft -= delta * (7 + (gw.getVirus().numStealths));			
				}
				else
				{
					timeLeft -= delta;
				}
			}
		}
		
		if( timeLeft < 0 && gw.getVirus() != null ) 
		{
			timeLeft = 0;
			gw.getVirus().hasLost = true;
			Sfx.MusicOff();
			Sfx.lose.play();
		}
		
		if( helpTextTimer > 0 )
		{
			helpTextTimer -= delta;
		}
		
		if( start && devMode )
		{
			this.loadLevel(this.levelPath);
			if( !devMode ) spawnVirus();
			start = false;
		}

		timer += delta;
		
		if( gw.getVirus() != null && !this.editMode)
		{
			g.moveCam(gw.getVirus().position.x, gw.getVirus().position.y);
		}
		
		g.cam.update();

		if( gw.getVirus() != null )
		{
			if( gw.getVirus().hasWon || gw.getVirus().hasLost )
			{
				gw.update(delta/4);
			}
			else
			{
				gw.update(delta);
			}
		}
		else
		{
			gw.update(delta);			
		}

		//Gdx.gl.glClearColor(0.24f, 0.11f, 0f, 1);
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			g.batch2.begin();
			g.batch2.draw(Gfx.bg2det, 0, 0);
			if( gw.getVirus() != null ) {
				if( gw.getVirus().hasWon ) g.batch2.setColor(1,1,1,0);
				else if( gw.getVirus().hasLost ) g.batch2.setColor(1,0,0,1);
				else if( gw.getVirus().detected ) g.batch2.setColor(1f, 1f, 1f, (1f + (float)Math.sin(timer*10))/2f);
				else g.batch2.setColor(1,1,1,1);
			}
			g.batch2.draw(Gfx.bg2, 0, 0);
			g.batch2.setColor(1,1,1,1);
			g.batch2.end();
		
		if( this.devMode && this.editMode )
		{
			handleEdit();
		}
		
		g.batch.setProjectionMatrix(g.cam.combined);
		g.shapeRenderer.setProjectionMatrix(g.cam.combined);
		g.shapeRenderer.begin(ShapeType.Line);
		gw.renderLinks(g.shapeRenderer);
		g.shapeRenderer.end();
		
		g.batch.begin();
		this.gw.renderNodes(g.batch);
		this.gw.renderOrganisms(g.batch);
		g.batch.end();
		
		if( this.devMode )
		{
			if( Gdx.input.isKeyJustPressed(Input.Keys.F8) )
			{
				this.editMode = !this.editMode;
			}
		}
		
		Virus virus = gw.getVirus();
		
		g.batch2.begin();
		if( virus != null && !editMode )
		{
			String detectionStr = "" + Math.round(100 - virus.detectionBar) + "%";
			g.fontBig.draw(g.batch2, detectionStr, 10, Gdx.graphics.getHeight() - 10);
			g.fontBig.draw(g.batch2, Math.round(timeLeft) + "", 730, Gdx.graphics.getHeight() - 10);
			if( virus.detected )
			{
				g.fontRlyBig.draw(g.batch2, "" + Math.round(virus.detectedTimer), 370, 380);
			}
			g.fontBig.draw(g.batch2, "lvl-" + (g.level), 10, 30);
			g.font.draw(g.batch2, g.levelTexts[g.level], 110, Gdx.graphics.getHeight() - 10);
			
		}
		
		if( this.devMode && this.editMode )
		{
			int x, y;
			x = 10; y = 20;
			if( toolMode == ToolMode.WEAK ) g.font.draw(g.batch2, "WEAK", x, y);
			if( toolMode == ToolMode.MID ) g.font.draw(g.batch2, "MID", x, y);
			if( toolMode == ToolMode.HARD ) g.font.draw(g.batch2, "HARD", x, y);
			if( toolMode == ToolMode.START ) g.font.draw(g.batch2, "START", x, y);
			if( toolMode == ToolMode.GOAL ) g.font.draw(g.batch2, "GOAL", x, y);
			if( toolMode == ToolMode.SPAWNER ) g.font.draw(g.batch2, "SPAWNER", x, y);
			if( toolMode == ToolMode.CONNECT ) g.font.draw(g.batch2, "CONNECT", x, y);
			if( toolMode == ToolMode.MOVE ) g.font.draw(g.batch2, "MOVE", x, y);
			if( toolMode == ToolMode.DELETE ) g.font.draw(g.batch2, "DELETE", x, y);
			if( toolMode == ToolMode.DISCONNECT ) g.font.draw(g.batch2, "DISCONNECT", x, y);
			if( toolMode == ToolMode.PATHFIND ) g.font.draw(g.batch2, "PATHFIND", x, y);
			
			if( instructions ) g.font.draw(g.batch2, instructions_text, 10, Gdx.graphics.getHeight() - 10);
			
			if( Gdx.input.isKeyJustPressed(Input.Keys.F2))
			{
				saveLevel();
			}
			if( Gdx.input.isKeyJustPressed(Input.Keys.F4))
			{
				loadLevel();
			}
		}
		
		g.batch2.end();
		
		g.shapeRenderer.begin(ShapeType.Filled);
		for( Node n : path )
		{
			g.shapeRenderer.setColor(1, 1, 0, 1);
			g.shapeRenderer.circle(n.position.x, n.position.y, 10);
			g.shapeRenderer.setColor(1, 1, 1, 1);
		}

		g.shapeRenderer.end();
		
		if( !devMode && gw.getVirus() != null )
		{
			
			if( gw.getVirus().hasWon && !Sfx.win.isPlaying() )
			{
				Sfx.MusicOn();

				g.level++;
				if( g.level >= g.levelPaths.length )
				{
					g.level = 0;
					g.setScreen(g.creditsScreen);
					System.out.println("credits!");
				}
				else
				{
					g.setScreen(g.gameScreen);					
				}
			}
			else if( gw.getVirus().hasLost && !Sfx.lose.isPlaying() )
			{
				Sfx.MusicOn();

				g.setScreen(g.introScreen);
			}
		}

		if( gw.getVirus() != null )
		{
			this.timeLeft += gw.getVirus().timeadd;
			gw.getVirus().timeadd = 0;
			
			if( gw.getVirus().virusHelpMsg == 1 ) this.helpTextFn("you need to infect all nodes");
			if( gw.getVirus().virusHelpMsg == 2 ) this.helpTextFn("spawners can't be infected");
		}
		
		if( helpTextTimer > 0 )
		{
			System.out.println(helpText);
			g.batch2.begin();
			g.font.draw(g.batch2, helpText, 10, 60);
			g.batch2.end();
		}
		else if( virus != null && virus.allCaptured )
		{
			g.batch2.begin();
			g.font.draw(g.batch2, "you can now keep spreading evil", 10, 60);
			g.batch2.end();
		}

	}
	
	public void saveLevel()
	{
		LevelData ld = gw.makeLevelData();
		Json json = new Json();
		System.out.println(json.prettyPrint(ld));
		FileHandle file = Gdx.files.local(levelPath);
		file.writeString(json.toJson(ld), false);
	}
	
	public void loadLevel()
	{
		loadLevel("newLevel.json");
	}
	
	public void loadLevel(String levelPath)
	{
		this.levelFile = levelPath;
		gw.clear();
		
		FileHandle file = Gdx.files.local(levelPath);
		if( file.exists() )
		{
			String jsonString = file.readString();
			
			System.out.println(jsonString);
			Json json = new Json();
			LevelData ld = json.fromJson(LevelData.class, jsonString);
			
			gw.loadLevelData(ld);
		}
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
		gw.clear();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
