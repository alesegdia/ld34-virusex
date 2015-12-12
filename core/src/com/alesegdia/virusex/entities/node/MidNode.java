package com.alesegdia.virusex.entities.node;

import com.alesegdia.virusex.World;
import com.alesegdia.virusex.assets.Gfx;

public class MidNode extends Node {

	public MidNode(World w, float x, float y) {
		super(w, x, y, Gfx.midNodeAnim);
	}

}
