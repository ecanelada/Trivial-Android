package com.example.triviadeclase;

import java.util.Random;

import com.example.triviadeclase.UsuarioSQLiteHelper;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	Button [] opc = new Button[4];
	Button nuevaPartida;
	TextView pregunta, numPartida;
	UsuarioSQLiteHelper MiUsuario = new UsuarioSQLiteHelper(this, "DbUsuarios", null, 1);
	SQLiteDatabase dataB;
	boolean interruptor=false;
	static int numPregunta=0;
	static int puntuacion;
	static int r2;
	Random r = new Random();
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View decorView = getWindow().getDecorView();
		int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		decorView.setSystemUiVisibility(uiOptions);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_main);
		
		opc[0] = (Button) findViewById(R.id.button1);
		opc[1] = (Button) findViewById(R.id.button2);
		opc[2] = (Button) findViewById(R.id.button3);
		opc[3] = (Button) findViewById(R.id.button4);
		
		pregunta = (TextView) findViewById(R.id.pregunta);
		numPartida = (TextView) findViewById(R.id.contadorPreguntas);
		SharedPreferences preferencias = getSharedPreferences("datos", MODE_PRIVATE); //crea un fichero de almacenamiento de pequeñas cantidades de datos (puntuaciones de juego, contraseñas y similares y se cargan al finalizar la aplicación)
		numPregunta = preferencias.getInt("numParti", 0);
		numPartida.setText(numPregunta+"/10");
		
		
		int rand = 0 + (int) (Math.random()*10);
		dataB = MiUsuario.getWritableDatabase();
		dataB = MiUsuario.getReadableDatabase();
		
		puntuacion = preferencias.getInt("puntos", 0);
		Cursor cs = dataB.rawQuery("select pregunta from Preguntas where codigoP = '"+(rand)+"'", null);
		if (cs!=null && cs.moveToFirst()) {
		    String a = cs.getString(cs.getColumnIndex("pregunta"));
		    pregunta.setText("Resultado de "+a+"\n\nPuntos: "+puntuacion);
		}
		
	if (numPregunta<10){
		cs.close();
		Cursor cs1 = dataB.rawQuery("select respuesta from Respuestas where codigoR = '"+(rand)+"'", null);
		if (cs1!=null && cs1.moveToFirst()) {
		    String a = cs1.getString(cs1.getColumnIndex("respuesta"));
		    r2 = r.nextInt(4);
		    opc[r2].setText(a);
		    for (int j=0;j<4 && j!=r2;j++){
		    	while(opc[j].getText().toString().contains("Respuesta")){
		    		generaRandom r = new generaRandom();
		    		opc[j].setText(Integer.toString(r.getRa()));
		    	}
		    }
		}
		cs1.close();
		
		opc[0].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				restart();
				puntos(0);
			}
		});
		opc[1].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				restart();
				puntos(1);
			}
		});
		opc[2].setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				restart();
				puntos(2);
			}
		});
		opc[3].setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				restart();
				puntos(3);
			}
		});
	}else{
		//hace que desaparezcan los botones
		for(int i=0;i<4;i++){
			opc[i].setVisibility(View.GONE);
		}
			pregunta.setText("PARTIDA ACABADA \n\n Puntos totales: "+puntuacion);
			
			nuevaPartida = (Button) findViewById(R.id.button5);
			nuevaPartida.setVisibility(View.VISIBLE);
			nuevaPartida.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Editor e = getSharedPreferences("datos", MODE_PRIVATE).edit(); //crea un fichero de almacenamiento de pequeñas cantidades de datos (puntuaciones de juego, contraseñas y similares y se cargan al finalizar la aplicación)
					e.remove("numParti").commit();
					e.remove("puntos").commit();
					Intent intent = new Intent(MainActivity.this, Presentacion.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					finish();
					startActivity(intent);
				}
			});
		
	}
	}

	private void restart(){
		if (numPregunta<10){
			Intent intent = getIntent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			SharedPreferences preferencias = getSharedPreferences("datos", MODE_PRIVATE);
			Editor editor = preferencias.edit();
			editor.putInt("numParti", (numPregunta+1)); //mete en el fichero los datos de tipo entero
			editor.commit();
			finish();
			startActivity(intent);
		}
	}
	
	/*private void getPregResp(int rand){
		Cursor cs = dataB.rawQuery("select * from Preguntas where codigo like '"+(rand+1)+"'", null);
		if (cs.moveToFirst()) {
		    String a = cs.getString(cs.getColumnIndex("pregunta"));
		    cs.close();
		    pregunta.setText("Resultado de "+a);
		}
		cs.close();
		Cursor cs1 = dataB.rawQuery("select * from Respuestas where codigo like '"+(rand+1)+"'", null);
		if (cs.moveToFirst()) {
		    String a = cs1.getString(cs1.getColumnIndex("respuesta"));
		    cs1.close();
		    int rand2 = 0 + (int) (Math.random()*4);
		    opc[rand2].setText(a);
		    for (int j=0;j<4 && j!=rand2;j++){
		    	int rand3 = 0 + (int) (Math.random()*1000);
		    	opc[j].setText(rand3);
		    }
		}
		cs1.close();
	}*/
	
	private void puntos(int p){
		SharedPreferences preferencias = getSharedPreferences("datos", MODE_PRIVATE);
		Editor editor = preferencias.edit();
		if (p==r2){
			editor.putInt("puntos", (puntuacion+100)); //mete en el fichero los datos de tipo entero
			editor.commit();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		
	}
}
/* trivial de 10 preguntas (datos almacenados en base de datos) y 10 preguntas
 * si la respuesta es correcta almacena acierto y suma puntos
 * con mysqlite
 * 
 * usar: acceso a bd, archivo preferencias y todos los controles posibles*/