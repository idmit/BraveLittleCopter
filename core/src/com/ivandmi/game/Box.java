package com.ivandmi.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Box implements Poolable {
	public Vector2 xy = new Vector2(0, 0);
	private float vel = 0;
	public boolean hasMissiles;
	public int missiles;
	public static float vWidth;
	public static float vHeight;

	public void init() {
		float t = (float) (Math.sqrt((225 + BLCopter.vpHeight - vHeight)) - 15) * 2;
		float x = (t * 50);
		xy.set(2 * vWidth + MathUtils.random(x - 2 * vWidth), BLCopter.vpHeight);
		missiles = MathUtils.random(7);
		hasMissiles = MathUtils.randomBoolean();
	}

	@Override
	public void reset() {
		xy.set(0, BLCopter.vpHeight);
		vel = -15;
	}

	public void render(SpriteBatch batch, Texture texture) {
		batch.draw(texture, xy.x, xy.y, vWidth, vHeight);
	}

	public void fall(float velX, float delta) {
		vel -= 0.5f;
		xy.add(-200 * delta, vel * delta);
	}
}
