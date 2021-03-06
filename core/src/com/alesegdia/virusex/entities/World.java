package com.alesegdia.virusex.entities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.alesegdia.virusex.assets.Gfx;
import com.alesegdia.virusex.entities.node.GoalNode;
import com.alesegdia.virusex.entities.node.HardNode;
import com.alesegdia.virusex.entities.node.Link;
import com.alesegdia.virusex.entities.node.MidNode;
import com.alesegdia.virusex.entities.node.Node;
import com.alesegdia.virusex.entities.node.SpawnerNode;
import com.alesegdia.virusex.entities.node.StartNode;
import com.alesegdia.virusex.entities.node.WeakNode;
import com.alesegdia.virusex.entities.organism.Antibody;
import com.alesegdia.virusex.entities.organism.Organism;
import com.alesegdia.virusex.entities.organism.Virus;
import com.alesegdia.virusex.level.LevelData;
import com.alesegdia.virusex.level.LinkEntry;
import com.alesegdia.virusex.level.NodeEntry;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class World {

	List<Node> nodes = new LinkedList<Node>();
	List<Link> links = new LinkedList<Link>();
	List<Organism> organisms = new ArrayList<Organism>();
	
	private Node rootNode;
	private Virus virus;
	private StartNode startNode;
	private GoalNode goalNode;

	public World()
	{
		
	}
	
	public void addNode(Node e)
	{
		if( e instanceof StartNode )
		{
			this.startNode = ((StartNode)e);
		}
		
		if( e instanceof GoalNode )
		{
			this.goalNode = ((GoalNode)e);
		}
		
		if( nodes.isEmpty() )
		{
			rootNode = e;
		}
		this.nodes.add(e);
	}
	
	public void addLink(Link l)
	{
		l.nodeA.neighboors.add(l.nodeB);
		l.nodeB.neighboors.add(l.nodeA);
		this.links.add(l);
	}
	
	public void addOrganism(Organism o)
	{
		if( o instanceof Virus )
		{
			this.virus = ((Virus)o);
		}
		this.organisms.add(o);
	}
	
	public void update(float delta)
	{
		boolean allCaptured = true;
		for( Entity e : nodes )
		{
			e.update(delta);
			if( !(e instanceof SpawnerNode) && e.faction == Faction.ENEMY )
			{
				allCaptured = false;
			}
		}
		if( virus != null ) virus.allCaptured = allCaptured;
		
		for( int i = 0; i < this.organisms.size(); i++ )
		{
			Entity e = this.organisms.get(i);
			e.update(delta);
		}
		for( Link l : links )
		{
			if( l.nodeA.faction == Faction.PLAYER && l.nodeB.faction == Faction.PLAYER )
			{
				l.isCaptured = true;
			}
		}
	}
	
	public Virus getVirus()
	{
		return virus;
	}
	
	public void renderNodes( SpriteBatch batch )
	{
		for( Node n : nodes )
		{
			if( n.faction == Faction.PLAYER )
			{
				batch.setColor(1,1,1,1);
			}
			renderEntity(n, batch);
			if( n.faction == Faction.PLAYER &&( (n instanceof WeakNode) || (n instanceof MidNode) || (n instanceof HardNode) ))
			{
				TextureRegion tr = Gfx.flagVirusAnim.getKeyFrame(n.capturedTimer);
				batch.draw(tr, n.position.x, n.position.y);
			}
			batch.setColor(1,1,1,1);
		}
	}
	
	public void renderLinks( ShapeRenderer srend )
	{
		for( Link l : links )
		{
			l.render(srend);
		}
	}
	
	public void renderOrganisms(SpriteBatch batch) {
		for( Organism o : organisms )
		{
			renderEntity( o, batch );
		}
	}
	
	public void renderEntity( Entity n, SpriteBatch b )
	{
		b.setColor(1, 1, 1, n.alfa);
		b.draw(n.getDrawable(),
				n.position.x - n.getDrawable().getRegionWidth() / 2,
				n.position.y - n.getDrawable().getRegionHeight() / 2);
		b.setColor(1,1,1,1);
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
		this.links.clear();
		this.organisms.clear();
		this.goalNode = null;
		this.startNode = null;
		this.virus = null;
	}

	public Node findNear(Vector2 mousePos) {
		for( Node n : nodes )
		{
			Vector2 tmp = new Vector2(mousePos);
			tmp.sub(n.position);
			if( tmp.len() < 50 )
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
			if( ne.nodeType.equals("weak") ) 	addNode(new WeakNode(this, ne.x, ne.y));
			if( ne.nodeType.equals("mid") ) 	addNode(new MidNode(this, ne.x, ne.y));
			if( ne.nodeType.equals("hard") ) 	addNode(new HardNode(this, ne.x, ne.y));
			if( ne.nodeType.equals("start") ) 	addNode(new StartNode(this, ne.x, ne.y));
			if( ne.nodeType.equals("goal") ) 	addNode(new GoalNode(this, ne.x, ne.y));
			if( ne.nodeType.equals("spawner") ) addNode(new SpawnerNode(this, ne.x, ne.y));
		}
		
		for( LinkEntry le : ld.links )
		{
			Link l = new Link( this.findNear(new Vector2(le.x1, le.y1)), this.findNear(new Vector2(le.x2, le.y2)));
			this.addLink(l);
		}
	}
	
	public void sanitizeLinks()
	{
		List<Link> todel = new LinkedList<Link>();
		for( Link li : links )
		{
			if( li.nodeA == li.nodeB )
			{
				todel.add(li);
			}
		}
		
		for( Link li : todel )
		{
			links.remove(li);
		}
	}

	public void removeLink(Node n1, Node n2) {
		List<Link> todel = new LinkedList<Link>();
		for( Link li : links )
		{
			if( (li.nodeA == n1 && li.nodeB == n2) || (li.nodeB == n1 && li.nodeA == n2) )
			{
				todel.add(li);
			}
		}
		
		for( Link li : todel )
		{
			links.remove(li);			
		}
	}

	public Node getRootNode() {
		return rootNode;
	}

	public GoalNode getGoalNode() {
		return this.goalNode;
	}
	public StartNode getStartNode() {
		return this.startNode;
	}

	public void spawnAntibodies() {
		for( Node n : this.nodes )
		{
			if( n instanceof SpawnerNode && !n.areNeighboorsCaptured() )
			{
				SpawnerNode sn = (SpawnerNode)n;
				sn.spawnAntibody();
			}
		}
	}

	public void removeAntibody(Antibody antibody) {
		this.organisms.remove(antibody);
	}

	public OrthographicCamera camera;

	public boolean connectedSpawners() {
		for( Node n : this.nodes )
		{
			if( n instanceof SpawnerNode )
			{
				SpawnerNode sn = (SpawnerNode)n;
				if( !sn.areNeighboorsCaptured() )
				{
					return true;
				}
			}
		}
		return false;
	}
	
}
