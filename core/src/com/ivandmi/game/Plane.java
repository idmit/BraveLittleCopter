package com.ivandmi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class Plane implements Disposable {
	public Vector2 xy = new Vector2();
	public Vector2 launcher = new Vector2();
	private Texture planeTexture = null;
	public static float vWidth;
	public static float vHeight;
	public static Rectangle rect;
	public static int type = 0;
	private int lives;
	public boolean alive;
	public boolean crash;
	public int missiles;
	Copter copter;
	private static int mbase = 0;

	public Plane(String internalPath, Copter copter) {
		this.copter = copter;
		planeTexture = new Texture(Gdx.files.internal(internalPath + (type / 2)
				+ ".png"));
		reset(internalPath);
	}

	public void render(SpriteBatch batch, float delta) {
		batch.draw(planeTexture, xy.x, xy.y, vWidth, vHeight);
		move();
		updateLauncher();
		rect.y = xy.y;
	}

	private void move() {
		float nextY = -200;
		if (!crash) {
			nextY = copter.xy.y;
		}
		if (Math.abs(nextY - xy.y) > 10) {
			xy.y += (nextY - xy.y) / 75;
		}
		if (crash && xy.y < -vHeight) {
			alive = false;
			kill();
		}
	}

	private void updateLauncher() {
		launcher.x = xy.x;
		launcher.y = xy.y + 10;
	}

	public void reset(String internalPath) {
		float localScale = 0.8f;
		planeTexture.dispose();
		planeTexture = new Texture(Gdx.files.internal(internalPath + (type / 2)
				+ ".png"));
		vWidth = localScale * planeTexture.getWidth();
		vHeight = localScale * planeTexture.getHeight();
		xy.set(BLCopter.vpWidth - vWidth - 20, -vHeight);
		rect = new Rectangle(xy.x, xy.y, vWidth, vHeight);

		lives = 2 + type;

		alive = true;
		crash = false;
		missiles = 5 + 2 * mbase;
	}

	public void hit() {
		lives--;
		if (lives < 0) {
			kill();
		}
	}

	private void kill() {
		if (type < 13) {
			type++;
		}
		mbase += 1;
		crash = true;
	}

	public int hitsLeft() {
		return lives + 1;
	}

	@Override
	public void dispose() {
		planeTexture.dispose();
	}
}
