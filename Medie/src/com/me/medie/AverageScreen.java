package com.me.medie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class AverageScreen implements Screen {
	public Stage stage;
	TextButton tbtCalc;
	Button btDark;
	TextField[] tfFields;
	TextField tfAverage;
	Label[] lFields;
	Label lAverage;
	int nrFields = 24;
	boolean isdark = true;
	Medie main;
	public Table tableMain;
	
	public AverageScreen(Medie main, String sectie, int an){
		this.main = main;
		Texture.setEnforcePotImages(false);
		
		stage = new Stage(main.SW, main.SH, true);
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		
		Table tableFields = new Table(main.skin);
		tableFields.setSize(main.SW, main.SH);
		
		tfFields = new TextField[nrFields];
		lFields = new Label[nrFields];
		for(int i = 0;i < nrFields;i++){
			lFields[i] = new Label("Field " + Integer.toString(i) + ":", main.skin);
			tfFields[i] = new TextField("0", main.skin);
			tableFields.add(lFields[i]).left().width(main.SW / 3);
			tableFields.add().width(main.SW / 3);
			tableFields.add(tfFields[i]).right().width(main.SW / 3);
			tableFields.row();
		}

		lAverage = new Label("Average: ", main.skin);
		tableFields.add(lAverage);
		tfAverage = new TextField("", main.skin);
		tableFields.add(tfAverage);
		tableFields.row();

		tbtCalc = new TextButton("Calculeaza", main.skin);
		tableFields.add(tbtCalc);
		tableFields.row();

		tbtCalc.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button){
				try
				{
					tfAverage.setText(getavg(tfFields));
				}
				catch(NumberFormatException e1){
					tfAverage.setText("Introdu valori!");
				}
				
				return true;
			}
		});

		main.skin.add("Dark", new Texture(Gdx.files.internal("data/night.png")));
		Drawable drNight = main.skin.getDrawable("Dark");
		btDark = new Button(new ButtonStyle(drNight, drNight, drNight));
		float ratio = btDark.getWidth() / btDark.getHeight();
		btDark.setHeight(tbtCalc.getHeight() / 2);
		btDark.setWidth(btDark.getHeight() / ratio);
		btDark.setPosition((float) (main.SW - btDark.getWidth()), tbtCalc.getY());
		btDark.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button){
				if(isdark)
					isdark = false;
				else
					isdark = true;
				
				return true;
			}
		}
		);
		
		ScrollPane scroll = new ScrollPane(tableFields, main.skin);
		scroll.layout();
		
		tableMain = new Table();
		tableMain.setSize(main.SW, main.SH);
		tableMain.add(scroll);	
		stage.addActor(tableMain);
		
		scroll.setScrollingDisabled(true, false);

		tableMain.left();
		tableMain.top();
	}
	
	@Override
	public void render(float delta) {
		stage.act();
		
		float deltaY = Gdx.input.getDeltaY();
		if(deltaY != 0){
			
			main.camera.translate(0, deltaY);
			main.camera.update();
		}
		
		if(isdark)
			Gdx.gl.glClearColor(0, 0, 0, 1);
		else
			Gdx.gl.glClearColor(1, 1, 1, 1);
		
		
		main.batch.begin();
		stage.draw();
		main.batch.end();
	}
	
	public String getavg(TextField[] fields){
		double dAvg = 0;
				
		for(int i = 0;i < fields.length;i++)
			dAvg += Double.parseDouble(fields[i].getText());
		
		dAvg /= (double)fields.length;

		return Double.toString(dAvg);
	}

	@Override
	public void resize(int width, int height) {
		main.SW = width;
		main.SH = height;
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
	}

}
