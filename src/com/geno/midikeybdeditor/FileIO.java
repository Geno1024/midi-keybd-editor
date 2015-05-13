package com.geno.midikeybdeditor;

import android.app.*;
import android.content.*;
import android.widget.*;
import java.io.*;

public class FileIO extends Activity
{

	private static byte[] out;
	public static byte[] open(final Context c)
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
					FileInputStream f = null;
					try
					{
						f = new FileInputStream(t.getText().toString());
						final byte[] out = new byte[f.available()];
						f.read(out);
						f.close();
					}
					catch (Exception e)
					{}
					
					/*
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
					{}*/
					Toast.makeText(c,R.string.finish,Toast.LENGTH_SHORT).show();
				}
			}
		)
		.setNegativeButton(android.R.string.cancel,null);
		ad.show();
		return out;
	}
	/*public static byte[] open(String fileName) {
		try
		{
			FileInputStream in = new FileInputStream(fileName);
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			in.close();
			byte[] content = EncodingUtils.getString(buffer, "UTF-8");
			return content;
        }
		catch (Exception e)
		{
			com.geno.tools.Debug.toast((R.string.filenotfound),FileIO.this);
			return new byte[]{};
        }
    }*/
}

