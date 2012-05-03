package com.expensessender;

import com.expensessender.MainActivity.Category;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	
	private ListView view;
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cat_list);

        String [] categoryList;
        
        CategoryManager man = new CategoryManager(getBaseContext());
        man.open();
        
        categoryList = man.getCategories();
        
        Category [] categories = new Category[categoryList.length];
        
        for (int iter=0;iter < categoryList.length;iter++) {
        	categories[iter] = new Category(categoryList[iter]);
        }
        
        Button currB = null;
        
        view = (ListView) findViewById(R.id.category_listview);
        
        CategoryListAdapter adapter = new CategoryListAdapter(getBaseContext(), categories);
        view.setAdapter(adapter);
        
       /* for (int iter=0;iter < categoryList.length; iter++)
        {
        	currB = new Button(this);
        	currB.setText(categoryList[iter]);
        	//currB.setLayoutParams(params);

        	currB.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg) {
					Button btn = (Button) arg;
					
					String cat = (String) btn.getText();
					
					Intent move = new Intent(MainActivity.this, ExpenseActivity.class);
					move.putExtra("Category", cat.replace(" ", "_"));
					startActivity(move);
					
				}
        		
        		
        	});*/
        	
        }
        
    
    class Category {
    	public String name="";
    	Category(String name) {
    		this.name=name;
    	}
    	@Override
    	public String toString() {
    		return this.name;
    	}
    }

    static class ViewHolder {
		public TextView text;
	}
    class CategoryListAdapter  extends ArrayAdapter <Category> {

		private Context context;
		private Category[] categories;

		public CategoryListAdapter(Context context, Category [] items) {
			super(context, R.layout.category_list_item, items);
			this.context = context;
			this.categories = items;
			
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View rowView = convertView;
			
			if (rowView == null) {
				
				LayoutInflater inflater =  (LayoutInflater)context.getSystemService
					      (Context.LAYOUT_INFLATER_SERVICE);
				rowView = inflater.inflate(R.layout.category_list_item,null);
				ViewHolder viewholder = new ViewHolder();
				viewholder.text = (TextView) rowView.findViewById(R.id.category_list_item_label);
				rowView.setTag(viewholder);
			}
			
			ViewHolder holder = (ViewHolder) rowView.getTag();
			String s = categories[position].toString();
			holder.text.setText(s);
			
			return rowView;
			
		}
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.addSubMenu("Add Category");
		return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	int id = item.getItemId();
    	if (id == 0) {
    		Intent move = new Intent(MainActivity.this, AddCategoryActivity.class);
			startActivity(move);
    	}
    	return true;
    }
}