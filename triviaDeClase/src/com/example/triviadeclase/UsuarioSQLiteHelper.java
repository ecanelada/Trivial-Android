package com.example.triviadeclase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class UsuarioSQLiteHelper extends SQLiteOpenHelper {

	String tablaPreguntas = "create table Preguntas (codigoP int, pregunta varchar(30))";
	String tablaRespuestas = "create table Respuestas (codigoR int, respuesta varchar(30))";

	String [] updatePreguntas = {"34x6","77x5","18x13","23x23","9x14","8x88","9x45","12x11","8x71","9x13"};
	String [] updateRespuestas = {"204","385","234","529","126","704","405","132","568","117"};
	
	public UsuarioSQLiteHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//ejecuto sentencia sql que me permite crear la tabla
		db.execSQL(tablaPreguntas);
		db.execSQL(tablaRespuestas);
		for (int i=0;i<updatePreguntas.length;i++){
			db.execSQL("insert into Preguntas (codigoP, pregunta) values ('"+(i)+"', '"+updatePreguntas[i]+"')");
			db.execSQL("insert into Respuestas (codigoR, respuesta) values ('"+(i)+"', '"+updateRespuestas[i]+"')");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//elimino la tabla que existe y la creo de nuevo vacía, pero migrando los datos de la antigua a esta nueva
		db.execSQL("drop table Preguntas if exists");
		db.execSQL("drop table Respuestas if exists");
		db.execSQL(tablaPreguntas);
		db.execSQL(tablaRespuestas);
		for (int i=0;i<updatePreguntas.length;i++){
			db.execSQL("insert into Preguntas (codigoP, pregunta) values ('"+(i)+"', '"+updatePreguntas[i]+"')");
			db.execSQL("insert into Respuestas (codigoR, respuesta) values ('"+(i)+"', '"+updateRespuestas[i]+"')");
		}
	}
}
