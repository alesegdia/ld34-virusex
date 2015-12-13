package com.alesegdia.virusex.entities.node;

import com.alesegdia.virusex.World;
import com.alesegdia.virusex.assets.Gfx;
import com.alesegdia.virusex.entities.Faction;
import com.alesegdia.virusex.entities.organism.Antibody;

public class SpawnerNode extends Node {

	public SpawnerNode(World w, float x, float y) {
		super(w, x, y, Gfx.spawnerNodeAnim, Faction.ENEMY);
	}
	
	public void spawnAntibody() {
		this.gw.addOrganism(new Antibody(this, this.gw, this.position.x, this.position.y));
	}

}
