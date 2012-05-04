package com.expensessender;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ExpenseActivity extends Activity {
	
	static String phoneNumber= "09212356239";
	static String currentCat;
	private static String preferencesContext = "Category";
			
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_entry);
        Bundle extras = getIntent().getExtras();
        ExpenseActivity.currentCat = extras.getString("Category");
        
   
        TextView label = (TextView) findViewById(R.id.expense_name);
        label.setText(ExpenseActivity.currentCat);
        label.setTextSize(40);
        
        EditText textEdit = (EditText) findViewById(R.id.expense);
        textEdit.setRawInputType(Configuration.KEYBOARD_12KEY);

        textEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                	Activity activity = (Activity) v.getContext();
                    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        
        textEdit.requestFocus();
        
        Button btn = (Button)findViewById(R.id.save_expense);
        
        btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				String category = ExpenseActivity.currentCat;
				EditText expense = (EditText) findViewById(R.id.expense);
				String exp =  expense.getText().toString();
				sendExpense(category, exp);
				CategoryManager manager = new CategoryManager(getBaseContext());
				manager.open();
				manager.incrementUsage(category);
				manager.close();
				Intent move = new Intent(ExpenseActivity.this, MainActivity.class);
				startActivity(move);
			}
        	
        });
        
        
	}
	private void sendSMS(String phoneNumber, String message)
    {        
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
 
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SENT), 0);
 
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
            new Intent(DELIVERED), 0);
 
        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off", 
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }

		
        }, new IntentFilter(SENT));
 
        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;                        
                }
            }
        }, new IntentFilter(DELIVERED));        
 
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
    }
	
	public void sendExpense(String categoryName, String expense)
	{
		if (expense.trim().length() ==0 )
			return;
		  String message = "CASH "+expense+" "+categoryName;
		  this.sendSMS(ExpenseActivity.phoneNumber, message);
	}   

}
