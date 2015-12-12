package com.alesegdia.virusex.screen;

import com.alesegdia.virusex.GdxGame;
import com.alesegdia.virusex.World;
import com.alesegdia.virusex.entities.GoalNode;
import com.alesegdia.virusex.entities.HardNode;
import com.alesegdia.virusex.entities.Link;
import com.alesegdia.virusex.entities.MidNode;
import com.alesegdia.virusex.entities.Node;
import com.alesegdia.virusex.entities.SpawnerNode;
import com.alesegdia.virusex.entities.StartNode;
import com.alesegdia.virusex.entities.WeakNode;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class GameScreen implements Screen {

	private GdxGame g;
	World gw = new World();
	ToolMode toolMode = ToolMode.WEAK;
	
	public GameScreen( GdxGame g )
	{
		this.g = g;
	}
	
	@Override
	public void show() {
		this.gw.clear();
	}
	
	Node firstCx = null;
	Node secondCx = null;
	
	Node toMove = null;
	
	void handleEditUserInput()
	{
		if( Gdx.input.isKeyJustPressed(Input.Keys.Z) ) toolMode = ToolMode.WEAK;
		if( Gdx.input.isKeyJustPressed(Input.Keys.X) ) toolMode = ToolMode.MID;
		if( Gdx.input.isKeyJustPressed(Input.Keys.C) ) toolMode = ToolMode.HARD;
		if( Gdx.input.isKeyJustPressed(Input.Keys.S) ) toolMode = ToolMode.START;
		if( Gdx.input.isKeyJustPressed(Input.Keys.D) ) toolMode = ToolMode.GOAL;
		if( Gdx.input.isKeyJustPressed(Input.Keys.Q) ) toolMode = ToolMode.SPAWNER;
		if( Gdx.input.isKeyJustPressed(Input.Keys.A) ) toolMode = ToolMode.CONNECT;
		if( Gdx.input.isKeyJustPressed(Input.Keys.W) ) toolMode = ToolMode.MOVE;
		
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
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		handleEditUserInput();
		
		g.shapeRenderer.begin(ShapeType.Line);
		gw.renderLinks(g.shapeRenderer);
		g.shapeRenderer.end();
		
		g.batch.begin();
		this.gw.renderNodes(g.batch);
		
		int x, y;
		x = 10; y = 20;
		if( toolMode == ToolMode.WEAK ) g.font.draw(g.batch, "WEAK", x, y);
		if( toolMode == ToolMode.MID ) g.font.draw(g.batch, "MID", x, y);
		if( toolMode == ToolMode.HARD ) g.font.draw(g.batch, "HARD", x, y);
		if( toolMode == ToolMode.START ) g.font.draw(g.batch, "START", x, y);
		if( toolMode == ToolMode.GOAL ) g.font.draw(g.batch, "GOAL", x, y);
		if( toolMode == ToolMode.SPAWNER ) g.font.draw(g.batch, "SPAWNER", x, y);
		g.batch.end();
		
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
