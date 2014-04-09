package com.me.medie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SplashScreen implements Screen {
	Stage stage;
	Table table;
	
	Medie main;
	
	public SplashScreen(Medie game){
		Texture.setEnforcePotImages(false);
		main = game;

		stage = new Stage(main.SW, main.SH, true);
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		
		table = new Table(main.skin);
		table.add(new Image(new Texture(Gdx.files.internal("data/background.png")))).height(main.SH / 3).row();
		
		TextButton tbtStart = new TextButton("Start", main.skin);
		tbtStart.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button){
				main.setScreen(new FacultateScreen(main));
				
				return true;
			}
		});
		
		table.add().height(main.SH / 3).row();
		
		table.add(tbtStart).row();
		
		table.setPosition(0, 0);
		table.setSize(main.SW, main.SH);
		
		stage.addActor(table);
	}
	
	@Override
	public void render(float delta) {
		if(Gdx.input.isKeyPressed(Keys.BACK))
			Gdx.app.exit();
		
		stage.act();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		main.batch.begin();
		stage.draw();
		main.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
