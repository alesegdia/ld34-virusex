package com.alesegdia.virusex.screen;

import com.alesegdia.virusex.GdxGame;
import com.alesegdia.virusex.World;
import com.alesegdia.virusex.entities.node.GoalNode;
import com.alesegdia.virusex.entities.node.HardNode;
import com.alesegdia.virusex.entities.node.Link;
import com.alesegdia.virusex.entities.node.MidNode;
import com.alesegdia.virusex.entities.node.Node;
import com.alesegdia.virusex.entities.node.SpawnerNode;
import com.alesegdia.virusex.entities.node.StartNode;
import com.alesegdia.virusex.entities.node.WeakNode;
import com.alesegdia.virusex.level.LevelData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;

public class GameScreen implements Screen {

	private GdxGame g;
	World gw = new World();
	ToolMode toolMode = ToolMode.WEAK;
	private boolean edit;
	
	public GameScreen( GdxGame g )
	{
		this(g, true);
	}
	
	public GameScreen( GdxGame g, boolean edit )
	{
		this.g = g;
		this.edit = edit;
	}
	
	@Override
	public void show() {
		this.gw.clear();
	}
	
	Node firstCx = null;
	Node secondCx = null;
	
	Node toMove = null;
	
	boolean instructions = true;
	
	String instructions_text = "INSTRUCTIONS\n" +
							   "============\n" +
							   "q/w/e => connect/move/delete\n" +
							   "a/s/d => spawner/start/goal\n" +
							   "z/x/c => weak/mid/high\n" +
							   "F2/F4 => save/load\n" +
							   "F1 => open/close instructions";
	
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
		
		if( Gdx.input.isKeyJustPressed(Input.Keys.O) )
		{
			gw.disconnectNodes();
		}
		
		if( Gdx.input.justTouched() )
		{
			int mx = Gdx.input.getX();
			int my = Gdx.graphics.getHeight() - Gdx.input.getY();
			if( toolMode == ToolMode.WEAK ) this.gw.addNode(new WeakNode(mx, my));
			if( toolMode == ToolMode.MID ) this.gw.addNode(new MidNode(mx, my));
			if( toolMode == ToolMode.HARD ) this.gw.addNode(new HardNode(mx, my));
			if( toolMode == ToolMode.GOAL ) this.gw.addNode(new GoalNode(mx, my));
			if( toolMode == ToolMode.START ) this.gw.addNode(new StartNode(mx, my));
			if( toolMode == ToolMode.SPAWNER ) this.gw.addNode(new SpawnerNode(mx, my));
			
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
	}

	@Override
	public void render(float delta) {
		
		gw.update(delta);
		
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if( this.edit )
		{
			handleEdit();
		}
		
		g.shapeRenderer.begin(ShapeType.Line);
		gw.renderLinks(g.shapeRenderer);
		g.shapeRenderer.end();
		
		g.batch.begin();
		this.gw.renderNodes(g.batch);
		
		if( this.edit )
		{
			int x, y;
			x = 10; y = 20;
			if( toolMode == ToolMode.WEAK ) g.font.draw(g.batch, "WEAK", x, y);
			if( toolMode == ToolMode.MID ) g.font.draw(g.batch, "MID", x, y);
			if( toolMode == ToolMode.HARD ) g.font.draw(g.batch, "HARD", x, y);
			if( toolMode == ToolMode.START ) g.font.draw(g.batch, "START", x, y);
			if( toolMode == ToolMode.GOAL ) g.font.draw(g.batch, "GOAL", x, y);
			if( toolMode == ToolMode.SPAWNER ) g.font.draw(g.batch, "SPAWNER", x, y);
			if( toolMode == ToolMode.CONNECT ) g.font.draw(g.batch, "CONNECT", x, y);
			if( toolMode == ToolMode.MOVE ) g.font.draw(g.batch, "MOVE", x, y);
			if( toolMode == ToolMode.DELETE ) g.font.draw(g.batch, "DELETE", x, y);
			if( toolMode == ToolMode.DISCONNECT ) g.font.draw(g.batch, "DISCONNECT", x, y);
			
			if( instructions ) g.font.draw(g.batch, instructions_text, 10, Gdx.graphics.getHeight() - 10);
			g.batch.end();
			
			if( Gdx.input.isKeyJustPressed(Input.Keys.F2))
			{
				saveLevel();
			}
			if( Gdx.input.isKeyJustPressed(Input.Keys.F4))
			{
				loadLevel();
			}
		}
	}
	
	public void saveLevel()
	{
		LevelData ld = gw.makeLevelData();
		Json json = new Json();
		System.out.println(json.prettyPrint(ld));
		FileHandle file = Gdx.files.local("newLevel.json");
		file.writeString(json.toJson(ld), false);
	}
	
	public void loadLevel()
	{
		gw.clear();
		
		FileHandle file = Gdx.files.local("newLevel.json");
		String jsonString = file.readString();
		
		System.out.println(jsonString);
		Json json = new Json();
		LevelData ld = json.fromJson(LevelData.class, jsonString);
		
		gw.loadLevelData(ld);
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
