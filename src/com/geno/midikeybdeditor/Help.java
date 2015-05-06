package com.geno.midikeybdeditor;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class Help extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		LinearLayout main=new LinearLayout(this);

		ExpandableListView e=new ExpandableListView(this);

		main.addView(e);
		super.onCreate(savedInstanceState);
		setContentView(main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return super.onOptionsItemSelected(item);
	}

}
