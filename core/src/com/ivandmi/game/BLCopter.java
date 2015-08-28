package com.ivandmi.game;

import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.I18NBundle;

public class BLCopter extends Game {

	static int vpWidth;
	static int vpHeight;
	static public SpriteBatch batch;
	static public ShapeRenderer shapeRenderer;
	static public OrthographicCamera camera;
	static public Texture ribbon;
	static public ReadyScreen readyScreen;
	static public RunningScreen runningScreen;
	static public ControlsScreen controlsScreen;
	private TTFFont ttfFonts;
	static public BitmapFont font;
	static public BitmapFont headFont;
	static public float MWidth;
	static public float MHeight;
	static public I18NBundle bundle;

	static private BLCopter game;
	public static GameState gameState;

	private static Sound bangSound, launchSound, collectSound, fallSound;
	public static Music ambientMusic;
	public static Music copterSound;
	static Texture soundIconOn, soundIconOff;

	public static float bossPeriod;

	@Override
	public void create() {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera();
		camera.setToOrtho(false);

		vpWidth = Gdx.graphics.getWidth();
		vpHeight = Gdx.graphics.getHeight();
		System.out.println(vpHeight);

		ribbon = new Texture(Gdx.files.internal("big.png"));
		soundIconOn = new Texture(Gdx.files.internal("audio_on_icon.png"));
		soundIconOff = new Texture(Gdx.files.internal("audio_off_icon.png"));
		bossPeriod = 20;

		ttfFonts = new TTFFont("fonts/Esqadero.ttf", "fonts/LeagueGothic.ttf");
		bundle = I18NBundle.createBundle(Gdx.files.internal("i18n/Main"));
		font = ttfFonts.getFont(bundle.getLocale());
		String ex = "M";
		if ("ru".equals(bundle.getLocale().getLanguage())) {
			ex = "М";
		}
		MWidth = font.getBounds(ex).width;
		MHeight = font.getBounds(ex).height;
		headFont = ttfFonts.getHeadFont(bundle.getLocale());
		bangSound = Gdx.audio.newSound(Gdx.files.internal("bang.mp3"));
		launchSound = Gdx.audio.newSound(Gdx.files.internal("launch.mp3"));
		collectSound = Gdx.audio.newSound(Gdx.files.internal("collect.wav"));
		fallSound = Gdx.audio.newSound(Gdx.files.internal("fall.ogg"));
		ambientMusic = Gdx.audio.newMusic(Gdx.files.internal("ambient.mp3"));
		copterSound = Gdx.audio.newMusic(Gdx.files.internal("copterSound.mp3"));
		ambientMusic.setLooping(true);
		copterSound.setLooping(true);
		copterSound.setVolume(0.1f);

		readyScreen = new ReadyScreen();
		runningScreen = new RunningScreen();
		controlsScreen = new ControlsScreen();

		game = this;
		BLCopter.setActiveScreen(BLCopter.GameState.Controls);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		setScreen(null);
		ambientMusic.dispose();
		copterSound.dispose();
		bangSound.dispose();
		launchSound.dispose();
		collectSound.dispose();
		fallSound.dispose();
		batch.dispose();
		ribbon.dispose();
		readyScreen.dispose();
		runningScreen.dispose();
		controlsScreen.dispose();
		ttfFonts.dispose();
		shapeRenderer.dispose();
		soundIconOn.dispose();
		soundIconOff.dispose();
		super.dispose();
	}

	@Override
	public void pause() {
		super.pause();
	}

	static public void setActiveScreen(GameState gameState) {
		BLCopter.gameState = gameState;
		switch (gameState) {
		case Controls:
			game.setScreen(controlsScreen);
			break;
		case Pause:
			game.setScreen(readyScreen);
			break;
		case Running:
			game.setScreen(runningScreen);
			break;
		case GameOver:
			BLCopter.gameState = GameState.Pause;
			BLCopter.runningScreen.dispose();
			BLCopter.runningScreen = new RunningScreen();
			game.setScreen(readyScreen);
			break;
		default:
			break;
		}

	}

	static enum GameState {
		Splash, Controls, Pause, Running, GameOver
	}

	public static void playBangSound() {
		if (ambientMusic.isPlaying()) {
			bangSound.play();
		}
	}

	public static void playLaunchSound(HashMap<Missile, Long> map, Missile m) {
		if (ambientMusic.isPlaying()) {
			map.put(m, launchSound.play(0.1f));
		}
	}

	public static void stopLaunchSound(HashMap<Missile, Long> map, Missile m) {
		if (ambientMusic.isPlaying()) {
			launchSound.stop(map.get(m));
		}
	}

	public static void playFallSound(HashMap<Box, Long> map, Box b) {
		if (ambientMusic.isPlaying()) {
			map.put(b, fallSound.play(0.1f));
		}
	}

	public static void stopFallSound(HashMap<Box, Long> map, Box b) {
		if (ambientMusic.isPlaying()) {
			fallSound.stop(map.get(b));
		}
	}

	public static void setSoundState(boolean state) {
		if (state) {
			ambientMusic.play();
		} else {
			ambientMusic.pause();
		}
	}

	public static void playCollectSound() {
		if (ambientMusic.isPlaying()) {
			collectSound.play();
		}
	}
}
