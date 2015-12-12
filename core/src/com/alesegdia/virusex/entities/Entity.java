package com.alesegdia.virusex.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Entity {

	public Vector2 position;
	public float rotation;
	public Animation animation;
	public float timer = 0;
	public float zOrder = 0;
	
	public Entity(SpriteBatch batch, float x, float y)
	{
		this.position = new Vector2(x, y);
	}
	
	public void update( float delta )
	{
		this.timer += delta;
	}

	public TextureRegion getDrawable()
	{
		return this.animation.getKeyFrame(this.timer);
	}
	
}
