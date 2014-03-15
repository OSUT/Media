package com.me.medie;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import jxl.*;
import jxl.read.biff.BiffException;

public class AverageScreen implements Screen {
	public Stage stage;
	TextButton tbtCalc;
	Button btDark;
	SelectBox[] sbFields;
	TextField tfAverage;
	Label lAverage;
	int nrFields;
	boolean isdark = true;
	Medie main;
	public Table tableMain;
	double[] ponderi;
	
	public AverageScreen(Medie main, String strFacultate, String strSectie, int an){
		this.main = main;
		Texture.setEnforcePotImages(false);
		
		stage = new Stage(main.SW, main.SH, true);
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		
		Table tableFields = new Table(main.skin);
		tableFields.setSize(main.SW, main.SH);

		String path = System.getProperty("user.dir");
		
		Workbook wbPlan = null;
	
			if(Gdx.app.getType() == ApplicationType.Desktop)
				path = "/bin/data/studii/"+strFacultate+"/"+strSectie+".xls";
			else
				path = "/data/studii/"+strFacultate+"/"+strSectie+".xls";
			
			System.out.println(path);
		
			try {
				wbPlan = Workbook.getWorkbook(new FileHandle(path).file());
			} catch (BiffException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
		Sheet shPlan = wbPlan.getSheet(0);//index incepe la 0
		
		Cell[] cellsMaterii = shPlan.getColumn(0);
		
		nrFields = cellsMaterii.length;
		
		ponderi = new double[nrFields];
		
		Cell[] cellsCredite = shPlan.getColumn(1);
		
		for(int i = nrFields - 1;i >= 0;i--)
			if(cellsMaterii[i].getContents().isEmpty() || cellsCredite[i].getContents().isEmpty())
				nrFields--;
		
		System.out.println(nrFields);
		System.out.println(cellsMaterii[nrFields-1].getContents());
		System.out.println(cellsCredite[nrFields-1].getContents());
		
		double sumacredite = 0;
		
		for(int i = 0;i < nrFields;i++){
			ponderi[i] = Double.parseDouble(cellsCredite[i].getContents());
			sumacredite += ponderi[i];
		}
		
		for(int i = 0;i < nrFields;i++)
			ponderi[i] /= sumacredite;

		sbFields = new SelectBox[nrFields];
		String[] strNote = new String[10];
		for(int i = 0;i < 10;i++)
			strNote[i] = Integer.toString(i+1);
		
		for(int i = 0;i < nrFields;i++){
			Label lField = new Label(cellsMaterii[i].getContents(), main.skin);
			lField.setWrap(true);
			tableFields.add(lField).left().width(main.SW / 2);

			sbFields[i] = new SelectBox(strNote, main.skin);
			sbFields[i].setSelection("5");
			
			tableFields.add(sbFields[i]).width(main.SW / 2);
			tableFields.row();
			
		}
		
		tableFields.add().height(50).row();
		
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
					tfAverage.setText(getavg(sbFields));
				}
				catch(NumberFormatException e1){
					tfAverage.setText("Introdu valori!");
				}
				
				return true;
			}
		});

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
	
	public String getavg(SelectBox[] fields){
		double dAvg = 0;
		
		for(int i = 0;i < fields.length;i++)
			dAvg += Double.parseDouble(fields[i].getSelection()) * ponderi[i];
		
		return String.format("%.2f", dAvg);
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
