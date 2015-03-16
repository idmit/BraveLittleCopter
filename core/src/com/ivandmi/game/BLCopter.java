package com.ivandmi.game;

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

	private static Sound bangSound;
	private static Music ambientMusic;
	static boolean soundState = true;
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
		ambientMusic = Gdx.audio.newMusic(Gdx.files.internal("ambient.mp3"));
		ambientMusic.setLooping(true);

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
		bangSound.dispose();
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
		ambientMusic.stop();
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
		if (soundState) {
			bangSound.play();
		}
	}

	public static void replayAmbientMusic() {
		// ambientMusic.stop();
		ambientMusic.play();
	}

	public static void stopAmbientMusic() {
		ambientMusic.stop();
	}

	public static void setSoundState(boolean state) {
		soundState = state;
		if (state) {
			replayAmbientMusic();
		} else {
			stopAmbientMusic();
		}
	}
}
