package com.ivandmi.game;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class PlaneMissileManager implements Disposable {
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

	private float spawnPeriod, spawnDelta = 0;

	private HashMap<Missile, Long> map = new HashMap<Missile, Long>();

	public PlaneMissileManager(String flightAtlasPath, String bangAtlasPath,
			float frameDuration) {
		flightAtlas = new TextureAtlas(Gdx.files.internal(flightAtlasPath));
		flightAnimation = new Animation(frameDuration,
				flightAtlas.getRegions(), Animation.PlayMode.LOOP_PINGPONG);
		for (TextureRegion tr : flightAnimation.getKeyFrames()) {
			tr.flip(true, false);
		}
		bangAtlas = new TextureAtlas(Gdx.files.internal(bangAtlasPath));
		bangAnimation = new Animation(frameDuration / 3,
				bangAtlas.getRegions(), Animation.PlayMode.NORMAL);
		for (TextureRegion tr : bangAnimation.getKeyFrames()) {
			tr.flip(true, false);
		}
		PlaneMissile.vWidth = Missile.vWidth;
		PlaneMissile.vHeight = Missile.vHeight;

		spawnPeriod = 1.5f;
	}

	public void render(SpriteBatch batch, float delta, Copter copter) {
		spawnDelta += delta;
		for (Missile m : aliveMissiles) {
			m.render(batch, delta);
		}
		update(delta, copter);
	}

	public void spawn(Plane plane, Copter copter) {
		if (spawnDelta > spawnPeriod) {
			plane.missiles -= 1;
			spawnDelta = 0;
			spawnPeriod = Math.max(Math.abs(copter.xy.y - plane.xy.y) / 100,
					1.5f);
			Missile m = missilePool.obtain();
			m.init(plane.launcher.x, plane.launcher.y, flightAnimation);
			m.velocity.x *= -1;
			float flightTime = (copter.xy.x - plane.xy.x) / m.velocity.x;
			m.velocity.y = (copter.xy.y - plane.xy.y) / flightTime;
			aliveMissiles.add(m);
			BLCopter.playLaunchSound(map, m);
		}
	}

	public void update(float delta, Copter copter) {
		for (int i = aliveMissiles.size; --i >= 0;) {
			Missile m = aliveMissiles.get(i);
			m.xy.mulAdd(m.velocity, delta);
			if (m.xy.x + PlaneMissile.vWidth < 0) {
				aliveMissiles.removeIndex(i);
				missilePool.free(m);
			}
		}
	}

	@Override
	public void dispose() {
		flightAtlas.dispose();
		bangAtlas.dispose();
	}

	public boolean collide(Copter cop) {
		Rectangle mrect = new Rectangle(0, 0, Missile.vWidth, Missile.vHeight);
		Rectangle crect = new Rectangle(cop.xy.x, cop.xy.y, Copter.vWidth,
				Copter.vHeight);
		for (Missile m : aliveMissiles) {
			if (m.banged) {
				continue;
			}
			mrect.x = m.xy.x;
			mrect.y = m.xy.y;
			if (Intersector.overlaps(crect, mrect)) {
				m.velocity.x = Copter.velocity;
				m.resetAnimation(bangAnimation);
				BLCopter.playBangSound();
				BLCopter.stopLaunchSound(map, m);
				cop.takeShot();
			}
		}
		return false;
	}
}
