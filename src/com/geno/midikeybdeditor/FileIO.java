package com.geno.midikeybdeditor;

import android.app.*;
import android.content.*;
import android.widget.*;
import java.io.*;
import java.nio.*;

public class FileIO
{
	public static byte[] b;

	public static final byte[] open(final Context c)
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
					catch(FileNotFoundException e)
					{
						Toast.makeText(c,R.string.filenotfound,Toast.LENGTH_SHORT).show();
					}
					b = new byte[(int)f.length()];
					try
					{
						i.read(b);
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
		return b;
	}
}
