package com.alesegdia.virusex.screen;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.alesegdia.virusex.GdxGame;
import com.alesegdia.virusex.World;
import com.alesegdia.virusex.assets.Gfx;
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
		this.gw.clear();
		this.start = true;
	}
	
	Node firstCx = null;
	Node secondCx = null;
	
	Node firstPf = null;
	Node secondPf = null;
	
	Node toMove = null;
	
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
							   "F1 => open/close instructions";
	
	List<Node> path = new LinkedList<Node>();
	
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
				Node n = this.gw.findNear( new Vector2( mx, my ) );
				if( n != null )
				{
					toMove = n;
				}
			}
		}
		
		if( toolMode == ToolMode.MOVE && toMove != null && Gdx.input.isButtonPressed(Input.Buttons.LEFT) )
		{
			toMove.position = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
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

	private void spawnVirus()
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

	float timer = 0;
	Matrix4 defmat = new Matrix4();
	
	@Override
	public void render(float delta) {
		timer += delta;
		
		if( gw.getVirus() != null && !this.editMode)
		{
			g.moveCam(gw.getVirus().position.x, gw.getVirus().position.y);
		}
		
		g.cam.update();
		if( start )
		{
			this.loadLevel(this.levelPath);
			start = false;
		}

		gw.update(delta);

		//Gdx.gl.glClearColor(0.24f, 0.11f, 0f, 1);
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		g.batch2.begin();
		g.batch2.draw(Gfx.bg2det, 0, 0);
		if( gw.getVirus() != null ) {
			if( gw.getVirus().detected ) g.batch2.setColor(1f, 1f, 1f, (1f + (float)Math.sin(timer*10))/2f);
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
		if( virus != null )
		{
			String detectionStr = "" + virus.detectionBar + "%";
			g.fontBig.draw(g.batch2, detectionStr, 10, 20);
			if( virus.detected )
			{
				g.fontBig.draw(g.batch2, "" + virus.detectedTimer, 400, 300);
			}
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
				/*
				Scanner kb = new Scanner(System.in);
				String level = kb.next();
				if( level == "" )
				{
					loadLevel();
				}
				else
				{
					loadLevel(level);
				}
				*/
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
