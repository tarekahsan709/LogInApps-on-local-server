package com.example.loginapp;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	EditText ed1,ed2;
	Button btn;
	ProgressBar pb;
	TextView output;
	
	List<MyTask> task;


	public String userName;
	public String password;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		output=(TextView) findViewById(R.id.textView1);
		output.setMovementMethod(new ScrollingMovementMethod());
		ed1=(EditText) findViewById(R.id.editText1);
		ed2=(EditText) findViewById(R.id.editText2);
		pb=(ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);
		task=new ArrayList<>();
		btn=(Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				userName=ed1.getText().toString();
				password=ed2.getText().toString();
				if(isOnline()){
					requestData("http://10.0.3.2:7777/BBD/androidController");
					//requestData("http://10.0.3.2:7777/android.php");

				}
				else{
					Toast.makeText(MainActivity.this,"No internet connection availabe",Toast.LENGTH_LONG).show();
				}
			}
		});

	}
	protected boolean isOnline() {
		ConnectivityManager cm=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo=cm.getActiveNetworkInfo();
		if(netInfo!=null && netInfo.isConnectedOrConnecting()){
			return true;
		}
		else{
			return false;
		}
	}

	protected void requestData(String uri) {
		DataPackage dp=new DataPackage();	
		dp.setMethod("POST");
		dp.setUri(uri);	
		dp.setParam("name",userName);
		dp.setParam("password",password);
		
		MyTask backTask=new MyTask();
		backTask.execute(dp);

	}
	protected void updateDisplay(String result) {
		output.append(result + "\n");
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

	private class MyTask extends AsyncTask<DataPackage, String, String>{

		@Override
		protected void onPreExecute() {
			if(task.size()==0){
				pb.setVisibility(View.VISIBLE);	
			}
			task.add(this);
		}
		@Override
		protected String doInBackground(DataPackage... params) {
			String content=HttpManager.getData(params[0]);
			return content;
		}
		@Override
		protected void onPostExecute(String result) {
			task.remove(this);
			if (task.size() == 0) {
				pb.setVisibility(View.INVISIBLE);
			}
			
			updateDisplay(result);
		}

	}




}
