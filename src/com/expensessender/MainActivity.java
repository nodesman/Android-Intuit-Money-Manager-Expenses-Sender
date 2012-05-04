package com.expensessender;

import com.expensessender.MainActivity.Category;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	private Category [] categories ;
	private ListView view;
	private CategoryListAdapter adapter;
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

        CategoryManager man = new CategoryManager(getBaseContext());
        man.open();
        
        String [] categoryList = man.getCategories();
        
        categories = new Category[categoryList.length];
        
        for (int iter=0;iter < categoryList.length;iter++) {
        	categories[iter] = new Category(categoryList[iter]);
        }
        
        
        Button currB = null;
        
        view = (ListView) findViewById(R.id.category_listview);
        
        
        adapter = new CategoryListAdapter(getBaseContext(), categories);
        
        CategoryItemClickListener listener = new CategoryItemClickListener();
        view.setAdapter(adapter);
        view.setOnItemClickListener(listener);
        
        CategoryItemLongClickListener longlistener = new CategoryItemLongClickListener();
        view.setOnItemLongClickListener(longlistener);
        registerForContextMenu(view);
       
        	
     }
    @Override
    public void onResume() {
    	
    	this.reloadListAdapter();
    	super.onResume();
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
      AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
      //int menuItemIndex = item.getItemId();

      String listItemName = categories[info.position].toString();
      CategoryManager manager = new CategoryManager(getBaseContext());
      manager.open();
      manager.deleteCategory(listItemName);
      this.reloadListAdapter();
      
      
      return true;
    }
    
    public void reloadListAdapter() {
    	CategoryManager man = new CategoryManager(getBaseContext());
        man.open();
        
        String [] categoryList = man.getCategories();
        
        man.close();
        Category [] categories;
        
        categories = new Category[categoryList.length];
        
        for (int iter=0;iter < categoryList.length;iter++) {
        	categories[iter] = new Category(categoryList[iter]);
        }
        

    	CategoryListAdapter adapter = new CategoryListAdapter(getBaseContext(), categories);
        view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	
    	super.onCreateContextMenu(menu, v, menuInfo);
    	
    	MenuInflater inf = getMenuInflater();
    	inf.inflate(R.menu.delete_cat, menu);

    }
    
    
    class CategoryItemLongClickListener implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(50);
			
			return false;
		}
    	
    }
        
    class CategoryItemClickListener implements OnItemClickListener {
    	CategoryItemClickListener() {
    		
    	}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			TextView cat =  (TextView) view.findViewById(R.id.category_list_item_label);
			String label = cat.getText().toString();
			
			Category cat1 = (Category) arg0.getItemAtPosition((int) arg3);
			
			Intent move = new Intent(MainActivity.this, ExpenseActivity.class);
			move.putExtra("Category", cat1.toString().replace(" ", "_"));
			startActivity(move);
		}
    	
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
		
		private Category [] getCategoriesObjects() {
			CategoryManager man = new CategoryManager(getBaseContext());
	        man.open();
	        
	        String [] categoryList = man.getCategories();
	        man.close();
	        
	        categories = new Category[categoryList.length];
	        
	        for (int iter=0;iter < categoryList.length;iter++) {
	        	categories[iter] = new Category(categoryList[iter]);
	        }
	        return categories;
		}
		

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
			
			Log.e("GetView","Getting view for position"+position);
			
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