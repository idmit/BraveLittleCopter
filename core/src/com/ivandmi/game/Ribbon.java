package com.ivandmi.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class Ribbon {

	private float[] xs = new float[2];
	private Texture texture;
	private int vHeightHalf;
	private int rWidth;
	private int rdHeight;

	public Ribbon(Texture texture) {
		this.texture = texture;
		xs[0] = 0;
		xs[1] = BLCopter.vpWidth;
		rWidth = texture.getWidth();
		rdHeight = BLCopter.vpWidth * texture.getHeight() / rWidth;
		vHeightHalf = rdHeight / 2;
	}

	public void render(SpriteBatch batch) {
		update();
		batch.draw(texture, xs[0], BLCopter.vpHeight / 2 - vHeightHalf,
				BLCopter.vpWidth, rdHeight);
		batch.draw(texture, xs[1], BLCopter.vpHeight / 2 - vHeightHalf,
				BLCopter.vpWidth, rdHeight);
	}

	private void update() {
		int width = BLCopter.vpWidth;
		float speed = Copter.velocity / 50;

		if (xs[0] <= -width) {
			xs[0] = xs[1] - speed + width;
		} else {
			xs[0] -= speed;
		}

		if (xs[1] <= -width) {
			xs[1] = xs[0] + width;
		} else {
			xs[1] -= speed;
		}
	}
}