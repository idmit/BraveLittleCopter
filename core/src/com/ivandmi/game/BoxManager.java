package com.ivandmi.game;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class BoxManager implements Disposable {
	private Texture texture;
	private Sound collect, fall;

	private final Array<Box> visibleBoxes = new Array<Box>();
	private final Pool<Box> boxPool = new Pool<Box>() {
		@Override
		protected Box newObject() {
			return new Box();
		}
	};

	private float scale = 0.175f;
	private float spawnPeriod, spawnDelta = 0;

	private HashMap<Box, Long> map = new HashMap<Box, Long>();

	public BoxManager(String texturePath) {
		texture = new Texture(Gdx.files.internal(texturePath));
		Box.vWidth = scale * texture.getWidth();
		Box.vHeight = scale * texture.getHeight();

		collect = Gdx.audio.newSound(Gdx.files.internal("collect.wav"));
		fall = Gdx.audio.newSound(Gdx.files.internal("fall.ogg"));

		spawnPeriod = 5 + MathUtils.random(15);
	}

	public void render(SpriteBatch batch, float delta) {
		spawnDelta += delta;
		for (Box box : visibleBoxes) {
			box.render(batch, texture);
		}
		update(delta);
	}

	public void spawn() {
		if (spawnDelta < spawnPeriod) {
			return;
		}
		spawnDelta = 0;
		spawnPeriod = 5 + MathUtils.random(15);
		Box box = boxPool.obtain();
		box.init();
		if (BLCopter.soundState) {
			map.put(box, fall.play(0.1f));
		}

		visibleBoxes.add(box);
	}

	public void update(float delta) {
		for (int i = visibleBoxes.size; --i >= 0;) {
			Box box = visibleBoxes.get(i);
			box.fall(-Copter.velocity, delta);
			if (box.xy.y + Box.vHeight < 0) {
				visibleBoxes.removeIndex(i);
				boxPool.free(box);
			}
		}
	}

	@Override
	public void dispose() {
		fall.dispose();
		texture.dispose();
		collect.dispose();
	}

	public boolean collide(Copter cop) {
		Rectangle brect = new Rectangle(0, 0, Box.vWidth, Box.vHeight);
		Rectangle crect = new Rectangle(cop.xy.x, cop.xy.y, Copter.vWidth,
				Copter.vHeight);
		for (int i = visibleBoxes.size; --i >= 0;) {
			Box box = visibleBoxes.get(i);
			brect.x = box.xy.x;
			brect.y = box.xy.y;
			if (Intersector.overlaps(crect, brect) && brect.y < crect.y) {
				visibleBoxes.removeIndex(i);
				boxPool.free(box);
				if (box.hasMissiles) {
					cop.missiles += box.missiles;
				} else {
					cop.lives += 1;
				}
				if (BLCopter.soundState) {
					fall.stop(map.get(box));
					collect.play();
				}
			}
		}
		return false;
	}
}
