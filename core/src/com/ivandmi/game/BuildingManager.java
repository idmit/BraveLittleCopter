package com.ivandmi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class BuildingManager implements Disposable {
	public final Array<Building> visibleBuildings = new Array<Building>();
	public final Array<Building> visibleAirs = new Array<Building>();
	private final Pool<Building> buildingPool = new Pool<Building>() {
		@Override
		protected Building newObject() {
			return new Building();
		}
	};

	private Texture texture;
	private Array<Texture> airTextures = new Array<Texture>();
	public static int vWidth;
	public static int vHeight;
	public static float aWidth;
	public static float aHeight;
	private float maxHeight;
	private float minHeight;
	private float spawnPeriod, spawnDelta = 0;

	public BuildingManager(String internalPath, float spawnPeriod) {
		this.texture = new Texture(Gdx.files.internal(internalPath));
		for (int i = 0; i < 6; i++) {
			this.airTextures.add(new Texture(Gdx.files.internal("air" + i
					+ ".png")));
		}
		vWidth = texture.getWidth();
		vHeight = texture.getHeight();

		aWidth = vWidth / 1.5f;
		aHeight = this.airTextures.get(0).getHeight() * vWidth
				/ this.airTextures.get(0).getWidth() / 1.5f;

		maxHeight = BLCopter.vpHeight - 2 * Copter.vHeight;
		minHeight = BLCopter.vpHeight / 8;
		this.spawnPeriod = spawnPeriod;
	}

	public void render(SpriteBatch batch, float delta) {
		for (Building b : visibleBuildings) {
			batch.draw(texture, b.xy.x, b.xy.y);
		}
		for (Building b : visibleAirs) {
			Texture t = airTextures.get(b.color);
			batch.draw(t, b.xy.x, b.xy.y, aWidth, aHeight);
		}
		update(delta);
	}

	public void spawn(float delta) {
		spawnPeriod = (vWidth + 20) / Copter.velocity;
		spawnDelta += delta;
		if (spawnDelta > spawnPeriod) {
			Building b = null;
			if (MathUtils.randomBoolean(0.7f)) {
				b = buildingPool.obtain();
				b.init(BLCopter.vpWidth,
						maxHeight - vHeight
								- MathUtils.random(maxHeight - minHeight));
				visibleBuildings.add(b);
			}
			float airProb = 0.1f;
			if (Copter.velocity > 300) {
				airProb = 0.2f;
			}
			if (Copter.velocity > 600 && airProb == 0.2) {
				airProb = 0.3f;
			}

			if (MathUtils.randomBoolean(airProb)) {
				Building a = buildingPool.obtain();
				if (b != null && maxHeight - (b.xy.y + vHeight) < aHeight + 5) {
					buildingPool.free(a);
				} else {
					a.init(BLCopter.vpWidth, BLCopter.vpHeight - aHeight - 2);
					visibleAirs.add(a);
				}
			}
			spawnDelta -= spawnPeriod;
		}
	}

	public void update(float delta) {
		for (int i = visibleBuildings.size; --i >= 0;) {
			Building b = visibleBuildings.get(i);
			b.xy.x -= Copter.velocity * delta;
			if (b.xy.x < -vWidth) {
				visibleBuildings.removeIndex(i);
				buildingPool.free(b);
			}
		}
		for (int i = visibleAirs.size; --i >= 0;) {
			Building b = visibleAirs.get(i);
			b.xy.x -= Copter.velocity * delta + 0.05;
			if (b.crash) {
				b.xy.y -= 5;
			}
			if (b.xy.x < -vWidth || b.xy.y + aHeight < 0) {
				visibleAirs.removeIndex(i);
				buildingPool.free(b);
			}
		}
	}

	@Override
	public void dispose() {
		texture.dispose();
		for (Texture tex : airTextures) {
			tex.dispose();
		}
	}

	public boolean hasCollision(Copter copter) {
		Rectangle crect = copter.rect;
		Rectangle brect = new Rectangle(0, 0, vWidth, vHeight);
		for (Building b : visibleBuildings) {
			brect.x = b.xy.x;
			brect.y = b.xy.y;
			if (!copter.banged && Intersector.overlaps(brect, crect)) {
				BLCopter.playBangSound();
				copter.resetAnimation(Copter.bangAnimation);
				return true;
			}
		}
		for (Building b : visibleAirs) {
			brect.x = b.xy.x;
			brect.y = b.xy.y;
			brect.width = aWidth;
			brect.height = aHeight;
			if (!copter.banged && Intersector.overlaps(brect, crect)) {
				BLCopter.playBangSound();
				copter.resetAnimation(Copter.bangAnimation);
				return true;
			}
		}
		return false;
	}
}
