package com.me.medie;

import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class AverageScreen implements Screen {
	public Stage stage;
	Medie main;
	public Table tableMain, tableFields;
	Sheet shPlan;
	SelectBox[][] sbFields;//notele pt sem 1 si 2
	Cell[][] cellsCredite;//creditele pt sem 1 si 2
	
	public String getavg(SelectBox[] fields){
		double avg = 0;
		int nrmaterii = 0;
		for(int i = 0;i < fields.length;i++){
			if(!fields[i].getSelection().equals("-")){
				try{
					avg += Double.parseDouble(fields[i].getSelection());
					nrmaterii++;
				}
				catch(Exception e)
				{
					continue;
				}
			}
		}
		
		return String.format("%.2f", avg / nrmaterii);
	}

	public String getweightedavg(SelectBox[] fields, Cell[] cellCredite){
		double dAvg = 0;
		double totalcredite = 0;
		
		for(int i = 0;i < fields.length;i++){
			if(!fields[i].getSelection().equals("-")){//daca se ia in considerare
				double credite = Double.parseDouble(cellCredite[i].getContents());

				totalcredite += credite;
				
				if(fields[i].getSelection().equals("admis")){
					dAvg += 10;
					continue;
				}
				
				if(fields[i].getSelection().equals("respins"))
					continue;
				
				dAvg += Double.parseDouble(fields[i].getSelection()) * credite; 
			}
		}
		
		return String.format("%.2f", dAvg / totalcredite);
	}
	
	void adaugasemestru(final int sem){
		final Label lAverage;
		final Label lWeightedAverage;

		int startid = (sem - 1) * 3;
		
		Cell[] cellsMaterii = shPlan.getColumn(startid);

		int nrFields = cellsMaterii.length;
		
		sbFields[sem-1] = new SelectBox[nrFields];
		
		for(int i = nrFields - 1;i >= 0;i--)
			if(cellsMaterii[i].getContents().isEmpty())
			nrFields--;

		cellsCredite[sem-1] = shPlan.getColumn(startid+1);
		Cell[] cellsNota = shPlan.getColumn(startid + 2);//metoda de notare
		
		sbFields[sem-1] = new SelectBox[nrFields];//drop-down list
		
		String[] strNote = new String[]{"-", "0","-2", "1","2","3","4","5","6","7","8","9","10"};		
		String[] strCalificative = new String[]{"admis", "respins"};
		
		Label lsem = new Label("Semestrul " + Integer.toString(sem), main.skin);
		lsem.setColor(Color.RED);
		tableFields.add(lsem).center().row();
		
		for(int i = 0;i < nrFields;i++){
			Label lField = new Label(cellsMaterii[i].getContents(), main.skin);
			lField.setWrap(true);
			tableFields.add(lField).center().width(main.SW / 2);
			
			if(cellsNota[i].getContents().equals("nota")){
				sbFields[sem-1][i] = new SelectBox(strNote, main.skin);
				sbFields[sem-1][i].setSelection("-");
			}

			if(cellsNota[i].getContents().equals("calificativ"))
				sbFields[sem-1][i] = new SelectBox(strCalificative, main.skin);
			
			tableFields.add(sbFields[sem-1][i]).width(main.SW / 2);
			tableFields.row();
		}

		tableFields.add().height(50).row();

		lAverage = new Label("", main.skin);
		lWeightedAverage = new Label("", main.skin);
		
		lAverage.setColor(Color.GREEN);
		lWeightedAverage.setColor(Color.GREEN);

		tableFields.add(new Label("Media aritmetica: ", main.skin));
		
		tableFields.add(lAverage);
		tableFields.row();
		
		tableFields.add(new Label("Media ponderata: ", main.skin));
		
		tableFields.add(lWeightedAverage);
		tableFields.row();

		TextButton tbtCalc = new TextButton("Calculeaza media", main.skin);
		tableFields.add(tbtCalc);
		tableFields.row();

		tbtCalc.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button){
				lWeightedAverage.setText(getweightedavg(sbFields[sem-1], cellsCredite[sem-1]));
				lAverage.setText(getavg(sbFields[sem-1]));
				return true;
			}
		});
	}
	
	public AverageScreen(final Medie main, String strFacultate, String strSectie, int an){
		this.main = main;
		Texture.setEnforcePotImages(false);
		
		stage = new Stage(main.SW, main.SH, true);
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		
		FileHandle fh;

		Workbook wbPlan = null;
	
		if(Gdx.app.getType() == ApplicationType.Desktop)
			fh = new FileHandle(System.getProperty("user.dir") + "/bin/data/studii/"+strFacultate+"/"+strSectie+".xls");
		else
			fh = Gdx.files.internal("data/studii/"+strFacultate+"/"+strSectie+".xls");
			
		try {
			wbPlan = Workbook.getWorkbook(fh.read());
		} catch (BiffException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		shPlan = wbPlan.getSheet(an-1);	

		tableFields = new Table(main.skin);
		tableMain = new Table(main.skin);
		
		tableMain.pad(25);
		
		TextButton tbBack = new TextButton("Inapoi", main.skin);
		tbBack.setColor(Color.GREEN);
		tbBack.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button){
				main.setScreen(new FacultateScreen(main));
				return true;
			}
		});
		
		tableFields.add(tbBack).left().row();
		
		sbFields = new SelectBox[2][];
		cellsCredite = new Cell[2][];
		
		adaugasemestru(1);
		
		tableFields.add().height(50).row();
		
		adaugasemestru(2);

		tableFields.add().height(50).row();
		
		Label lMediean = new Label("Media pe an: ", main.skin);
		lMediean.setColor(Color.RED);
		tableFields.add(lMediean).row();
		
		tableFields.add(new Label("Media aritmetica:", main.skin));
		final Label lMediean2 = new Label("", main.skin);
		lMediean2.setColor(Color.GREEN);
		tableFields.add(lMediean2).row();

		tableFields.add(new Label("Media ponderata:", main.skin));
		final Label lMediean1 = new Label("", main.skin);
		lMediean1.setColor(Color.GREEN);
		tableFields.add(lMediean1).row();
		
		TextButton tbtCalcAn = new TextButton("Calculeaza media", main.skin);
		tbtCalcAn.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button){
				String strWeighted1 = getweightedavg(sbFields[0], cellsCredite[0]);
				String strWeighted2 = getweightedavg(sbFields[1], cellsCredite[1]);
				
				String strAvg1 = getavg(sbFields[0]);
				String strAvg2 = getavg(sbFields[1]);
				
				double dWeighted = (Double.parseDouble(strWeighted1) + Double.parseDouble(strWeighted2)) / 2;
				double dAvg = (Double.parseDouble(strAvg1) + Double.parseDouble(strAvg2)) / 2;
				
				lMediean1.setText(String.format("%.2f", dWeighted));
				lMediean2.setText(String.format("%.2f", dAvg));
				
				return true;
			}
		});
		
		tableFields.add(tbtCalcAn);

		ScrollPane scroll = new ScrollPane(tableFields, main.skin);
		scroll.setScrollingDisabled(true, false);
		scroll.setSize(main.SW, main.SH);
		
		tableMain.addActor(scroll);
		
		stage.addActor(tableMain);
	}
	
	@Override
	public void render(float delta) {
		stage.act();
		
		if(Gdx.input.isKeyPressed(Keys.BACK))
			main.setScreen(new FacultateScreen(main));

		Gdx.gl.glClearColor(0, 0, 0, 1);
		
		main.batch.begin();
		stage.draw();
		main.batch.end();
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
		stage.dispose();
	}

}
