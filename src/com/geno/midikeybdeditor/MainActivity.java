package com.geno.midikeybdeditor;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class MainActivity extends Activity
{
	public Button c,d,e,f,g,a,b,cr,dr,fr,gr,ar;
	public TextView expl,src;
	public byte[] music;
	public String[] expla,timelength;
	public String n64,n32,n16,n8,n4,n2,n1,note;
	public int len;
	public StringBuffer sb;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		n64=getString(R.string.n64);
		n32=getString(R.string.n32);
		n16=getString(R.string.n16);
		n8=getString(R.string.n8);
		n4=getString(R.string.n4);
		n2=getString(R.string.n2);
		n1=getString(R.string.n1);
		note=getString(R.string.note);
		timelength=new String[] {n64,n32,n16,n8,n4,n2,n1};
		sb=new StringBuffer();
		c=(Button)findViewById(R.id.c);
		d=(Button)findViewById(R.id.d);
		e=(Button)findViewById(R.id.e);
		f=(Button)findViewById(R.id.f);
		g=(Button)findViewById(R.id.g);
		a=(Button)findViewById(R.id.a);
		b=(Button)findViewById(R.id.b);
		cr=(Button)findViewById(R.id.cr);
		dr=(Button)findViewById(R.id.dr);
		fr=(Button)findViewById(R.id.fr);
		gr=(Button)findViewById(R.id.gr);
		ar=(Button)findViewById(R.id.ar);
		expl=(TextView)findViewById(R.id.explanation);
		src=(TextView)findViewById(R.id.srccode);
		init();
		mainloop(0x4e556864);	//"MThd"
		c.setOnClickListener
		(new OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this).setTitle(note).setItems
					(timelength, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								
							}
						}
					);
					ad.show();
				}
			}
		);
    }

	void init()
	{
		len=0;
	}

	void mainloop(int i)
	{
		sb.append(Integer.toHexString(i).toUpperCase());
		expl.setText(sb.toString());
	}

	void save()
	{
		
	}
}

