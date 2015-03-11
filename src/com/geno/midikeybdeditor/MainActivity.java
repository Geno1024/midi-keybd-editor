package com.geno.midikeybdeditor;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.nio.*;
import java.util.*;

public class MainActivity extends Activity
{
	public TextView expl,src,detail,addevent;
	public byte[] music;
	public String[] expla,selectevent;
	public String note,remain,use,event;
	public StringBuffer sb;
	public ByteBuffer midi;
	public static int notelength;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	//	Get string res
		note = getString(R.string.note);
		remain = getString(R.string.remain);
		use = getString(R.string.nowuse);
		event = getString(R.string.event);
		selectevent = new String[]{getString(R.string.eventid8)};

	//	Get layout id
		expl = (TextView)findViewById(R.id.explanation);
		src = (TextView)findViewById(R.id.srccode);
		detail = (TextView)findViewById(R.id.detail);
		addevent = (Button)findViewById(R.id.addevent);

	//	Get byte buffer for editing
		sb = new StringBuffer();
		midi = ByteBuffer.allocate(1048576);
		byte[] head = {0x4D,0x54,0x68,0x64,0x0,0x0,0x0,0x6};
		midi.put(head);
		detail.setText(remain+midi.remaining()+use+midi.position());

	//	Edit widget
		addevent.setOnClickListener
		(new OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this).setTitle(event).setItems
					(selectevent, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								eventchk(p2);
							}
						}
					);
					ad.show();
				}
			}
		);
    }

	void eventchk(int eventid)
	{
		
	}

	int noteid()
	{
		int a = 0;
		AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this).setTitle(note).setItems
		(new String[]{"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"}, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					
				}
			}
		);
		ad.show();
		return a;
	}
}

