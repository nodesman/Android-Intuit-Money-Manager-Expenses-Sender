package com.expensessender;

import com.expensessender.CategoryManager.CategoryListHelper;

import android.app.Activity;
import android.content.Context;
import android.database.*;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.Context;
public class CategoryManager {
	
	private Context context;
	private static String createTableQuery = "CREATE TABLE " +
					"categories (category varchar(100) not null primary key);";
	private SQLiteDatabase db;
	private CategoryListHelper adapter;
	private static String databaseName = "thermo";
	private static int version = 1;
			
	
	CategoryManager(Context context) {
		this.context = context;
		this.adapter = new CategoryListHelper(context, databaseName, null, version);

	}
	
	public CategoryManager open() throws SQLException {
		db = this.adapter.getWritableDatabase();
		return this;		
	}
	
	public void close() {
		db.close();
	}
	
	public void addCategory(String categoryName)
	{
		ContentValues values = new ContentValues();
		values.put("category", categoryName);
		db.insert("categories", null , values);
	}
	
	public void deleteCategory(String categoryName)
	{
		
	}
	
	
	public boolean categoryExists(String categoryName)
	{
		return false;
	}
	
	
	public static class CategoryListHelper extends SQLiteOpenHelper {

		public CategoryListHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(createTableQuery);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion)
		{
			
		}
		
	}
	
	public static String [] getCategories() {
		

		String[] list = {
			"Food",
			"Transit",
			"Medical",
			"Wasteful",
			"Movies",
			"Recreation",
			"Electronics",
			"Groceries",
			"Eating Out",
			"Ice cream",
			"Traffic Fine"
		};
		
		
		return list;
	}
	

	
	

}
