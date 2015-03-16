package com.geno.midikeybdeditor;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.nio.*;
import android.text.*;
import java.io.*;

public class MainActivity extends Activity
{
	public TextView expl,src,detail,addevent;
	public String[] expla,selectevent;
	public String note,remain,use,event,confirm,cancel,velocity;
	public StringBuffer sb;
	public ByteBuffer midi;
	public static int notelength;
	
	//	These values are for functions
	public boolean justopen;
	public byte eventvalue,notevalue,notelengthvalue;
	public byte[] eventdefinedvalue = {ubtosb(0x80),ubtosb(0x90),ubtosb(0xA0),ubtosb(0xB0),ubtosb(0xC0),ubtosb(0xD0),ubtosb(0xE0)};
	public ByteBuffer eventnotebuffer;
	public String[] notedefinedname = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
	public String[] trackno = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
	public String[] note12 = {"8x -5","8x -4","8x -3","8x -2","8x -1","8x ±0","8x +1","8x +2","8x +3","8x +4","8x +5"};

	/*	This integer is an important flag in this program
	*	for checking if this nesting functions below
	*	is triggered by editing an event or creating an event
	*	former 0 latter 1
	*	it will be all 1 before editing function completed
	*	later will be changed into either 0 or 1
	*	it maybe 2 or more later
	*	when function increases
	*	so I didn't make it Boolean
	*/
	public int flag;

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
		selectevent = new String[]{getString(R.string.eventid8),getString(R.string.eventid9),getString(R.string.eventidA),getString(R.string.eventidB),getString(R.string.eventidC),getString(R.string.eventidD),getString(R.string.eventidE)};

	//	Get layout id
		expl = (TextView)findViewById(R.id.explanation);
		src = (TextView)findViewById(R.id.srccode);
		detail = (TextView)findViewById(R.id.detail);
		addevent = (Button)findViewById(R.id.addevent);

	//	Init variables
		justopen = true;

	//	Get byte buffer for editing
		sb = new StringBuffer();
		midi = ByteBuffer.allocate(1048576);
		eventnotebuffer = ByteBuffer.allocate(3);
		byte[] head = {0x4D,0x54,0x68,0x64,0x0,0x0,0x0,0x6};
		midi.put(head);

	//	Init display
		detail.setText(remain+midi.remaining()+" "+use+midi.position());
		update();
		WindowManager m=(WindowManager) getSystemService(WINDOW_SERVICE);
		expl.setWidth(m.getDefaultDisplay().getWidth()/2);
		src.setWidth(m.getDefaultDisplay().getWidth()/2);
		justopen = false;

		save();
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
								flag=1;
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
		if(!justopen)
		{
			addevent();
		}
		detail.setText(remain+midi.remaining()+use+midi.position());
		src.setText(printbyte(midi));
	}

	void addevent()
	{
		eventnotebuffer.position(0);
		midi.put(eventnotebuffer);
	}

	void eventchk(final int eventid)
	{
		eventnotebuffer.position(0);
		AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
		.setTitle(R.string.track)
		.setItems
		(trackno,new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					eventnotebuffer.put((byte)(eventdefinedvalue[eventid]+p2));
					noteid();
				}
			}
		);
		ad.show();
	}

	void noteid()
	{
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
						.setTitle(R.string.note12)
						.setItems
							(note12,new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface p1,int p2)
									{
										notevalue = ubtosb(notevalue + ( p2 - 5 ) * 12);
										eventnotebuffer.put(notevalue);
										if(flag==1)
											velocity();
									}
								}
							);
						ad.show();
					}
				}
			);
		ad.show();
	}

	void velocity()
	{
		final EditText t = new EditText(MainActivity.this);
		t.setInputType(InputType.TYPE_CLASS_NUMBER);
		t.setHint(R.string.velocityintro);
		t.setText("64");
		AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
		.setTitle(R.string.velocity)
		.setView(t)
		.setPositiveButton
		(R.string.confirm,new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					try
					{
						eventnotebuffer.put((byte)Integer.parseInt(t.getText().toString()));
					}
					catch(Exception e)
					{}
					update();
				}
			}
		);
		ad.show();
	}

	byte ubtosb(int unsigned)
	{
		//	Unsigned byte to signed byte
		return unsigned < 128 ? (byte)unsigned : (byte)(unsigned-256);
	}

	/*	This function is now very simple
	*	but later it will have its responsibility
	*	of DECODING A MIDI
	*/
	String printbyte(ByteBuffer b)
	{
		String res = "";
		int i;
		for(i=0;i<b.position();i++)
		{
			String buf = Integer.toHexString(b.get(i)).toUpperCase();
			if(buf.length()<2)
				buf="0"+buf;
			if(buf.contains("FFFFFF")==false)
				res=res+buf;
			else
				res=res+buf.substring(6,8);
			res=res+" ";
		}
		return res;
	}

	void save()
	{
		final EditText t = new EditText(MainActivity.this);
		AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
		.setTitle("Save")
		.setView(t)
		.setPositiveButton
		(R.string.confirm,new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					File f = new File(t.getText().toString());
					OutputStream o = null;
					try
					{
						o = new BufferedOutputStream(new FileOutputStream(f));
					}
					catch (FileNotFoundException e)
					{}
					ByteBuffer b = null;
					b = ByteBuffer.allocate(midi.position());
					try
					{
						o.write(b.array());
						if(o!=null)
						{
							o.close();
						}
					}
					catch (IOException e)
					{}
					Toast.makeText(MainActivity.this,"Finish!",Toast.LENGTH_SHORT).show();
				}
			}
		).setNegativeButton
		(R.string.cancel,new DialogInterface.OnClickListener()
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

