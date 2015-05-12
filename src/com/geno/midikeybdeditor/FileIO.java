package com.geno.midikeybdeditor;

import android.app.*;
import android.content.*;
import android.widget.*;
import java.io.*;

public class FileIO
{
	public static void open(final Context c, final byte[] toWrite)
	{
		final EditText t = new EditText(c);
		AlertDialog.Builder ad = new AlertDialog.Builder(c)
		.setTitle(R.string.open)
		.setView(t)
		.setPositiveButton
		(android.R.string.ok,new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					File f = new File(t.getText().toString());
					InputStream i = null;
					try
					{
						i = new BufferedInputStream(new FileInputStream(f));
					}
					catch(Exception e)
					{
						Toast.makeText(c,R.string.filenotfound,Toast.LENGTH_SHORT).show();
						return;
					}
					try
					{
						i.read(toWrite);
						i.close();
					}
					catch (Exception e)
					{}
					Toast.makeText(c,R.string.finish,Toast.LENGTH_SHORT).show();
				}
			}
		)
		.setNegativeButton(android.R.string.cancel,null);
		ad.show();
	}
}

