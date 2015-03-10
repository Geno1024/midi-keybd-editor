package com.geno.midikeybdeditor;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.nio.*;

public class MainActivity extends Activity
{
	public Button c,d,e,f,g,a,b,cr,dr,fr,gr,ar;
	public TextView expl,src,detail;
	public byte[] music;
	public String[] expla,timelength;
	public String n64,n32,n16,n8,n4,n2,n1,note,remain,use;
	public StringBuffer sb;
	public ByteBuffer midi;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	//	Get string res
		n64 = getString(R.string.n64);
		n32 = getString(R.string.n32);
		n16 = getString(R.string.n16);
		n8 = getString(R.string.n8);
		n4 = getString(R.string.n4);
		n2 = getString(R.string.n2);
		n1 = getString(R.string.n1);
		note = getString(R.string.note);
		remain = getString(R.string.remain);
		use = getString(R.string.nowuse);
		
		timelength = new String[] {n64,n32,n16,n8,n4,n2,n1};
	//	Get layout id
		c = (Button)findViewById(R.id.c);
		d = (Button)findViewById(R.id.d);
		e = (Button)findViewById(R.id.e);
		f = (Button)findViewById(R.id.f);
		g = (Button)findViewById(R.id.g);
		a = (Button)findViewById(R.id.a);
		b = (Button)findViewById(R.id.b);
		cr = (Button)findViewById(R.id.cr);
		dr = (Button)findViewById(R.id.dr);
		fr = (Button)findViewById(R.id.fr);
		gr = (Button)findViewById(R.id.gr);
		ar = (Button)findViewById(R.id.ar);
		expl = (TextView)findViewById(R.id.explanation);
		src = (TextView)findViewById(R.id.srccode);
		detail = (TextView)findViewById(R.id.detail);
	//	Get byte buffer for editing
		sb = new StringBuffer();
		midi = ByteBuffer.allocate(1048576);
		byte[] head = {0x4D,0x54,0x68,0x64,0x0,0x0};
		midi.put(head);
		detail.setText(remain+midi.remaining()+use+midi.position());
	//	Edit widget
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
								byte a=90;
								byte b=40;
								mainloop(a,b);
							}
						}
					);
					ad.show();
				}
			}
		);
    }

	void mainloop(byte note,byte length)
	{
		midi.put(note);
		midi.put(length);
		detail.setText(remain+midi.remaining()+use+midi.position());
	}
}

