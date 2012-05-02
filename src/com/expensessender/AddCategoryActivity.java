package com.expensessender;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class AddCategoryActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.add_category_activity);
		 
		 EditText view = (EditText) findViewById(R.id.category_field);
		 view.requestFocus();
		 
		 AddCategoryButtonListener listener = new AddCategoryButtonListener(view);
		 
		 
		 Button addCategory = (Button) findViewById(R.id.add_category_button);
		 addCategory.setOnClickListener(listener);
		 
		 Button cancelCat = (Button) findViewById(R.id.cancel_button);
		 cancelCat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				Intent move = new Intent(AddCategoryActivity.this, MainActivity.class);
				startActivity(move);
			}
			 
		 });
	}
	
	class AddCategoryButtonListener implements OnClickListener {
		
		private TextView category_name_field = null;
		
		AddCategoryButtonListener(TextView view) {
			this.category_name_field = view;
		}
		

		@Override
		public void onClick(View v) {
			
			String categoryName = (String) category_name_field.getText().toString();
			if (categoryName.trim().length() == 0) {
				AlertDialog alertDialog;
				alertDialog = new AlertDialog.Builder(v.getContext()).create();
				alertDialog.setTitle("Empty Category Name");
				alertDialog.setMessage("Please enter a category name. Category name is empty.");
				alertDialog.show();
				alertDialog.setCanceledOnTouchOutside(true);
				return;
			}
			CategoryManager manager = new CategoryManager(v.getContext());
			manager.open();
			manager.addCategory(categoryName);

			Intent move = new Intent(AddCategoryActivity.this, MainActivity.class);
			startActivity(move);

		}
		
	}

}
