package com.ivandmi.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PlaneMissile implements Poolable {
	private Animation animation;
	public Vector2 xy;
	static public float vWidth, vHeight;
	private float stateTime;
	public Rectangle rect;
	public float velocity;
	public boolean banged;

	public PlaneMissile() {
		stateTime = 0;
		xy = new Vector2(vWidth / 2, BLCopter.vpHeight / 2);
		rect = new Rectangle(xy.x, xy.y, vWidth, vHeight);
		banged = false;
		velocity = Copter.velocity + 10;
	}

	public void init(float posX, float posY, Animation animation) {
		xy.set(posX, posY);
		this.animation = animation;
	}

	@Override
	public void reset() {
		xy.set(0, 0);
		animation = null;
		banged = false;
		velocity = Copter.velocity + 10;
	}

	public void render(SpriteBatch batch, float delta) {
		stateTime += delta;
		float localScale = banged ? 1.5f : 1;
		if (!banged || !animation.isAnimationFinished(stateTime)) {
			TextureRegion frame = animation.getKeyFrame(stateTime);
			batch.draw(frame, xy.x, xy.y, localScale * vWidth, localScale
					* vHeight);
		}
	}

	public void resetAnimation(Animation animation) {
		this.animation = animation;
		stateTime = 0;
		banged = true;
	}

	public void move(float dx, float dy) {
		if (xy.y < 0) {
			xy.y = 0;
		}
		if (xy.y + vHeight > BLCopter.vpHeight) {
			xy.y = BLCopter.vpHeight - vHeight;
		}
		xy.add(dx, dy);
	}
}
