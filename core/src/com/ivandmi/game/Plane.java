package com.ivandmi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Plane implements Disposable {
	public Vector2 xy = new Vector2();
	public Vector2 launcher = new Vector2();
	private Array<Texture> planeTextures = new Array<Texture>();
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
		for (int i = 0; i < 7; ++i) {
			planeTextures.add(null);
		}
		reset(internalPath);
	}

	public void render(SpriteBatch batch, float delta) {
		batch.draw(planeTextures.get(type / 2), xy.x, xy.y, vWidth, vHeight);
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
		if (planeTextures.get(type / 2) == null) {
			planeTextures.set(
					type / 2,
					new Texture(Gdx.files.internal(internalPath + (type / 2)
							+ ".png")));
		}
		float localScale = 0.8f;
		vWidth = localScale * planeTextures.get(type / 2).getWidth();
		vHeight = localScale * planeTextures.get(type / 2).getHeight();
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
		for (Texture tex : planeTextures) {
			tex.dispose();
		}
	}
}
