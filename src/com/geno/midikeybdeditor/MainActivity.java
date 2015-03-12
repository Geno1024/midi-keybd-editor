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
	public String note,remain,use,event,confirm,cancel;
	public StringBuffer sb;
	public ByteBuffer midi;
	public static int notelength;
	
	//	These value are for functions
	public byte notevalue;
	public String[] notedefinedname = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
	public String[] note12 = {};

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
		confirm = getString(R.string.confirm);
		cancel = getString(R.string.cancel);
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
								noteid();
								if(notevalue != -1)
								{
									midi.put(notevalue);
								}
								update();
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

	void update()
	{
		detail.setText(remain+midi.remaining()+use+midi.position());
	}

	byte noteid()
	{
		notevalue = -1;
		AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this)
		.setTitle(note)
		.setItems
			(notedefinedname,new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						notevalue = (byte) (0x3c + p2);
					}
				}
			)
/*		.setPositiveButton
			(confirm,new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface p1, int p2)
					{}
				}
			)
		.setNegativeButton
			(cancel,new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						notevalue = -1;
					}
				}
			)*/;
		ad.show();
		Toast.makeText(MainActivity.this,notevalue+"",Toast.LENGTH_SHORT).show();
		return notevalue;
	}
}

