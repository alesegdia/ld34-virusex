package com.alesegdia.virusex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.alesegdia.virusex.entities.Entity;
import com.alesegdia.virusex.entities.node.GoalNode;
import com.alesegdia.virusex.entities.node.HardNode;
import com.alesegdia.virusex.entities.node.Link;
import com.alesegdia.virusex.entities.node.MidNode;
import com.alesegdia.virusex.entities.node.Node;
import com.alesegdia.virusex.entities.node.SpawnerNode;
import com.alesegdia.virusex.entities.node.StartNode;
import com.alesegdia.virusex.entities.node.WeakNode;
import com.alesegdia.virusex.entities.organism.Organism;
import com.alesegdia.virusex.level.LevelData;
import com.alesegdia.virusex.level.LinkEntry;
import com.alesegdia.virusex.level.NodeEntry;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class World {

	List<Node> nodes = new LinkedList<Node>();
	List<Link> links = new LinkedList<Link>();

	public World()
	{
		
	}
	
	public void addNode(Node e)
	{
		this.nodes.add(e);
	}
	
	public void addLink(Link l)
	{
		l.nodeA.neighboors.add(l.nodeB);
		l.nodeB.neighboors.add(l.nodeA);
		this.links.add(l);
	}
	
	public void update(float delta)
	{
		for( Entity e : nodes )
		{
			e.update(delta);
		}
	}
	
	public void renderNodes( SpriteBatch batch )
	{
		for( Node n : nodes )
		{
			batch.draw(n.getDrawable(),
					n.position.x - n.getDrawable().getRegionWidth() / 2,
					n.position.y - n.getDrawable().getRegionHeight() / 2);
		}
	}
	
	public void renderLinks( ShapeRenderer srend )
	{
		for( Link l : links )
		{
			l.render(srend);
		}
	}
	
	public void disconnectNodes()
	{
		this.links.clear();
		for( Node n : nodes )
		{
			n.neighboors.clear();
		}
	}
	
	public void removeNode( Node n )
	{
		ArrayList<Link> todel = new ArrayList<Link>();
		for( Link l : links )
		{
			if( l.nodeA == n || l.nodeB == n ) todel.add(l);
		}
		for( Link l : todel )
		{
			this.links.remove(l);
		}
		this.nodes.remove(n);
	}
	
	public void clear() {
		disconnectNodes();
		this.nodes.clear();
	}

	public Node findNear(Vector2 mousePos) {
		for( Node n : nodes )
		{
			Vector2 tmp = new Vector2(mousePos);
			tmp.sub(n.position);
			if( tmp.len() < 100 )
			{
				return n;
			}
		}
		return null;
	}
	
	public LevelData makeLevelData()
	{
		LevelData ld = new LevelData();
		
		for( Node n : nodes )
		{
			NodeEntry ne = new NodeEntry();
			if( n instanceof WeakNode ) ne.nodeType = "weak";
			if( n instanceof MidNode ) ne.nodeType = "mid";
			if( n instanceof HardNode ) ne.nodeType = "hard";
			if( n instanceof StartNode ) ne.nodeType = "start";
			if( n instanceof GoalNode ) ne.nodeType = "goal";
			if( n instanceof SpawnerNode ) ne.nodeType = "spawner";
			ne.x = n.position.x;
			ne.y = n.position.y;
			ld.nodes.add(ne);
		}
		
		for( Link l : links )
		{
			LinkEntry le = new LinkEntry();
			le.x1 = l.nodeA.position.x;
			le.y1 = l.nodeA.position.y;
			le.x2 = l.nodeB.position.x;
			le.y2 = l.nodeB.position.y;
			ld.links.add(le);
		}
		
		return ld;
	}

	public void loadLevelData(LevelData ld) {
		for( NodeEntry ne : ld.nodes )
		{
			if( ne.nodeType.equals("weak") ) 	addNode(new WeakNode(ne.x, ne.y));
			if( ne.nodeType.equals("mid") ) 	addNode(new MidNode(ne.x, ne.y));
			if( ne.nodeType.equals("hard") ) 	addNode(new HardNode(ne.x, ne.y));
			if( ne.nodeType.equals("start") ) 	addNode(new StartNode(ne.x, ne.y));
			if( ne.nodeType.equals("goal") ) 	addNode(new GoalNode(ne.x, ne.y));
			if( ne.nodeType.equals("spawner") ) addNode(new SpawnerNode(ne.x, ne.y));
		}
		
		/*
		for( LinkEntry le : ld.links )
		{
			Link l = new Link( this.findNear(new Vector2(le.x1, le.y1)), this.findNear(new Vector2(le.x2, le.y2)));
			this.addLink(l);
		}
		*/
	}
	
}
