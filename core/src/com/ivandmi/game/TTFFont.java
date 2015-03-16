package com.ivandmi.game;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Disposable;

public class TTFFont implements Disposable {
	public static final String RUSSIAN_CHARACTERS = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
			+ "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
			+ "1234567890.,:;_¡!¿?\"'+-*/()[]={}";
	private BitmapFont ruFont;
	private BitmapFont enFont;
	private BitmapFont ruHeadFont;
	private BitmapFont enHeadFont;

	public TTFFont(String fontName, String headFontName) {
		int fontSize = (int) (0.04 * BLCopter.vpHeight);
		int headerFontSize = (int) (0.145 * BLCopter.vpHeight);
		ruFont = generateFont(fontName, fontSize, RUSSIAN_CHARACTERS);
		enFont = generateFont(fontName, fontSize,
				FreeTypeFontGenerator.DEFAULT_CHARS);
		ruHeadFont = generateFont(headFontName, headerFontSize,
				RUSSIAN_CHARACTERS);
		enHeadFont = generateFont(headFontName, headerFontSize,
				FreeTypeFontGenerator.DEFAULT_CHARS);
	}

	private BitmapFont generateFont(String fontName, int size, String characters) {
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.characters = characters;
		parameter.size = size;

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
				Gdx.files.internal(fontName));
		BitmapFont font = generator.generateFont(parameter);

		generator.dispose();

		return font;
	}

	public BitmapFont getFont(Locale locale) {
		if ("ru".equals(locale.getLanguage())) {
			return ruFont;
		} else {
			return enFont;
		}
	}

	public BitmapFont getHeadFont(Locale locale) {
		if ("ru".equals(locale.getLanguage())) {
			return ruHeadFont;
		} else {
			return enHeadFont;
		}
	}

	@Override
	public void dispose() {
		ruFont.dispose();
		enFont.dispose();
		ruHeadFont.dispose();
		enHeadFont.dispose();
	}
}
