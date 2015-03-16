package com.ivandmi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class Copter implements Disposable {
	Vector2 xy;
	static public float vWidth, vHeight;
	private TextureAtlas atlas;
	private Animation animation;
	private TextureAtlas bangAtlas;
	static Animation bangAnimation;
	private float stateTime, scale = 0.25f;
	public Rectangle rect;
	public static float velocity;
	int renderCount = 0;
	private Music sound = Gdx.audio.newMusic(Gdx.files
			.internal("copterSound.mp3"));
	int lives;
	public int missiles;
	boolean banged = false;
	int dead = 0;

	public Copter(String atlasPath, String bangAtlasPath, float frameDuration) {
		atlas = new TextureAtlas(Gdx.files.internal(atlasPath));
		animation = new Animation(frameDuration, atlas.getRegions(),
				Animation.PlayMode.LOOP_PINGPONG);
		bangAtlas = new TextureAtlas(Gdx.files.internal(bangAtlasPath));
		bangAnimation = new Animation(frameDuration / 3,
				bangAtlas.getRegions(), Animation.PlayMode.NORMAL);
		stateTime = 0;
		Sprite sprite = new Sprite(animation.getKeyFrame(0, true));
		vWidth = scale * sprite.getWidth();
		vHeight = scale * sprite.getHeight();

		xy = new Vector2(vWidth / 2, BLCopter.vpHeight / 2);
		velocity = 100;
		lives = 5;
		missiles = 15;
		rect = new Rectangle(xy.x, xy.y, vWidth, vHeight);
		sound.setLooping(true);
	}

	public void startSound() {
		if (BLCopter.soundState) {
			sound.setVolume(0.1f);
			sound.play();
		}
	}

	public void stopSound() {
		sound.stop();
	}

	public void render(SpriteBatch batch, float delta) {
		stateTime += delta;
		renderCount++;
		if (renderCount > 30 && velocity < 1000) {
			velocity += 100 * delta;
			renderCount = 0;
		}
		if (banged) {
			xy.x -= Copter.velocity * delta;
		}
		float localScale = banged ? 1.5f : 1;
		if (!banged || !animation.isAnimationFinished(stateTime)) {
			TextureRegion frame = animation.getKeyFrame(stateTime);
			batch.draw(frame, xy.x, xy.y, localScale * vWidth, localScale
					* vHeight);
		} else {
			stateTime = 0;
			dead++;
			isDead();
		}
	}

	public boolean isDead() {
		return dead == 5;
	}

	public void resetAnimation(Animation animation) {
		this.animation = animation;
		stateTime = 0;
		banged = true;
	}

	public void move(float dx, float dy) {
		xy.add(dx, dy);
		rect.x = xy.x;
		rect.y = xy.y;
	}

	public void checkPos() {
		if (xy.y < 0) {
			xy.y = 0;
		} else if (xy.y + vHeight > BLCopter.vpHeight) {
			xy.y = BLCopter.vpHeight - vHeight;
		}

		rect.x = xy.x;
		rect.y = xy.y;
	}

	@Override
	public void dispose() {
		sound.stop();
		sound.dispose();
		atlas.dispose();
		bangAtlas.dispose();
	}

	public void takeShot() {
		lives--;
		if (lives < 0) {
			lives++;
			resetAnimation(Copter.bangAnimation);
		}

	}

	public void kill() {
		Plane.type = 0;
		ReadyScreen.updateTopDistance((int) (RunningScreen.distance / 10));
		BLCopter.setActiveScreen(BLCopter.GameState.GameOver);
	}
}
