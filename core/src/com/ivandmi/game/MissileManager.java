package com.ivandmi.game;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class MissileManager implements Disposable {
	private TextureAtlas flightAtlas;
	private Animation flightAnimation;
	private TextureAtlas bangAtlas;
	private Animation bangAnimation;

	private final Array<Missile> aliveMissiles = new Array<Missile>();
	private final Pool<Missile> missilePool = new Pool<Missile>() {
		@Override
		protected Missile newObject() {
			return new Missile();
		}
	};

	private float scale = 0.25f;
	private float spawnPeriod, spawnDelta = 0;
	private Sound launchSound;

	private HashMap<Missile, Long> map = new HashMap<Missile, Long>();

	public MissileManager(String flightAtlasPath, String bangAtlasPath,
			float frameDuration) {
		flightAtlas = new TextureAtlas(Gdx.files.internal(flightAtlasPath));
		flightAnimation = new Animation(frameDuration,
				flightAtlas.getRegions(), Animation.PlayMode.LOOP_PINGPONG);
		bangAtlas = new TextureAtlas(Gdx.files.internal(bangAtlasPath));
		bangAnimation = new Animation(frameDuration / 3,
				bangAtlas.getRegions(), Animation.PlayMode.NORMAL);
		Sprite sprite = new Sprite(flightAnimation.getKeyFrame(0, true));
		Missile.vWidth = scale * sprite.getWidth();
		Missile.vHeight = scale * sprite.getHeight();

		launchSound = Gdx.audio.newSound(Gdx.files.internal("launch.mp3"));

		spawnPeriod = 0.5f;
	}

	public void render(SpriteBatch batch, float delta) {
		spawnDelta += delta;
		for (Missile m : aliveMissiles) {
			m.render(batch, delta);
		}
		update(delta);
	}

	public void spawn(Copter copter) {
		if (spawnDelta < spawnPeriod || copter.missiles == 0) {
			return;
		}
		spawnDelta = 0;
		copter.missiles--;
		Missile m = missilePool.obtain();
		m.init(copter.xy.x + Copter.vWidth, copter.xy.y, flightAnimation);
		aliveMissiles.add(m);
		if (BLCopter.soundState) {
			map.put(m, launchSound.play(0.1f));
		}
	}

	public void update(float delta) {
		for (int i = aliveMissiles.size; --i >= 0;) {
			Missile m = aliveMissiles.get(i);
			m.xy.x += m.velocity.x * delta;
			if (m.xy.x > BLCopter.vpWidth) {
				aliveMissiles.removeIndex(i);
				missilePool.free(m);
			}
		}
	}

	@Override
	public void dispose() {
		launchSound.dispose();
		flightAtlas.dispose();
		bangAtlas.dispose();
	}

	public boolean collide(BuildingManager bm, Plane plane) {
		Rectangle mrect = new Rectangle(0, 0, Missile.vWidth, Missile.vHeight);
		Rectangle brect = new Rectangle(0, 0, BuildingManager.vWidth,
				BuildingManager.vHeight);

		for (Missile m : aliveMissiles) {
			if (m.banged) {
				continue;
			}
			mrect.x = m.xy.x;
			mrect.y = m.xy.y;
			if (plane.alive && Intersector.overlaps(Plane.rect, mrect)) {
				m.velocity.x = -Copter.velocity;
				m.resetAnimation(bangAnimation);
				BLCopter.playBangSound();
				if (BLCopter.soundState) {
					launchSound.stop(map.get(m));
				}
				plane.hit();
				break;
			}
			for (Building b : bm.visibleBuildings) {
				brect.x = b.xy.x;
				brect.y = b.xy.y;
				if (Intersector.overlaps(brect, mrect)) {
					m.velocity.x = -Copter.velocity;
					m.resetAnimation(bangAnimation);
					BLCopter.playBangSound();
					if (BLCopter.soundState) {
						launchSound.stop(map.get(m));
					}
				}
			}
			brect.width = BuildingManager.aWidth;
			brect.height = BuildingManager.aHeight;
			for (Building b : bm.visibleAirs) {
				brect.x = b.xy.x;
				brect.y = b.xy.y;
				if (Intersector.overlaps(brect, mrect)) {
					m.velocity.x = -Copter.velocity;
					m.resetAnimation(bangAnimation);
					BLCopter.playBangSound();
					b.crash = true;
					if (BLCopter.soundState) {
						launchSound.stop(map.get(m));
					}
				}
			}
		}
		return false;
	}
}
