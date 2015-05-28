package com.geno.midikeybdeditor;

import android.app.*;
import android.content.*;
import android.widget.*;
import java.io.*;
import android.view.View.*;

public class FileIO extends Activity
{
	public byte[] r;

	public byte[] open(Context co)
	{
		final EditText t = new EditText(co);
		AlertDialog.Builder ad = new AlertDialog.Builder(co)
		.setTitle(R.string.open)
		.setView(t)
		.setPositiveButton
		(android.R.string.ok,
			new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					r=openFileIntoByte(t.getText().toString());
				}
			}
		)
		.setNegativeButton(android.R.string.cancel,null);
		ad.show();
		return r;
	}

	public static byte[] openFileIntoByte(String fileName)
	{
		byte[] out = null;
		FileInputStream f = null;
		try
		{
			f = new FileInputStream(fileName);
			out = new byte[f.available()];
			f.read(out);
			f.close();
		}
		catch(Exception e)
		{}
		return out;
	}
}

