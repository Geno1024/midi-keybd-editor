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
	public String[] expla,selectevent;
	public String note,remain,use,event,confirm,cancel,velocity;
	public StringBuffer sb;
	public ByteBuffer midi;
	public static int notelength;
	
	//	These value are for functions
	public byte eventvalue,notevalue,notelengthvalue;
	public byte[] eventdefinedvalue = {ubtosb(0x80),ubtosb(0x90)};
	public ByteBuffer eventnotebuffer;
	public String[] notedefinedname = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
	public String[] note12 = {"8x -5","8x -4","8x -3","8x -2","8x -1","8x ±0","8x +1","8x +2","8x +3","8x +4","8x +5"};

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
		velocity = getString(R.string.velocity);
		selectevent = new String[]{getString(R.string.eventid8),getString(R.string.eventid9)};

	//	Get layout id
		expl = (TextView)findViewById(R.id.explanation);
		src = (TextView)findViewById(R.id.srccode);
		detail = (TextView)findViewById(R.id.detail);
		addevent = (Button)findViewById(R.id.addevent);

	//	Get byte buffer for editing
		sb = new StringBuffer();
		midi = ByteBuffer.allocate(1048576);
		eventnotebuffer = ByteBuffer.allocate(3);
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

	//Many necessary functions
	void update()
	{
		midi.put(eventnotebuffer);
		detail.setText(remain+midi.remaining()+use+midi.position());
	}

	void eventchk(int eventid)
	{
		final EditText t = new EditText(MainActivity.this);
		AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
		.setTitle(R.string.track)
		.setView(t)
		.setPositiveButton
		(R.string.confirm,new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					noteid();
				}
			}
		);
		ad.show();
		eventnotebuffer.put(eventdefinedvalue[eventid]);
	}

	void noteid()
	{
		notevalue = -1;
		AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
		.setTitle(R.string.note)
		.setItems
			(notedefinedname,new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						notevalue = (byte) (0x3c + p2);
						AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
						.setTitle("")
						.setItems
							(note12,new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface p1,int p2)
									{
										notevalue = ubtosb(notevalue + ( p2 - 5 ) * 12);
										if(notevalue!=-1)
											eventnotebuffer.put(notevalue);
									}
								}
							);
						ad.show();
						Toast.makeText(MainActivity.this,notevalue+"",Toast.LENGTH_SHORT).show();
						update();
					//	notelength();
					}
				}
			);
		ad.show();
	}

	void notelength()
	{
		AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this).setTitle(R.string.velocity);
		ad.show();
	}

	byte ubtosb(int unsigned)
	{
		//	Unsigned byte to signed byte
		return unsigned < 128 ? (byte)unsigned : (byte)(unsigned-256);
	}
}

