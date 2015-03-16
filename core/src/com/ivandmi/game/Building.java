package com.ivandmi.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Building implements Poolable {
	public Vector2 xy;
	int color;
	public boolean crash;
	
	public Building() {
		this.xy = new Vector2();
	}

	public void init(float posX, float posY) {
		xy.set(posX, posY);
	}

	@Override
	public void reset() {
		xy.set(0, 0);
		crash = false;
		color = MathUtils.random(5);
	}
}
