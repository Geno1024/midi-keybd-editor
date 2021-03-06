package com.geno.midikeybdeditor;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;
import java.nio.*;

public class MainActivity extends Activity
{
	public TextView expl,src,detail,addevent,addmetaevent;
	public String[] expla,selectevent;
	public String remain,use;
	public StringBuffer sb;
	public ByteBuffer midi;
	public EditText trackn;

	//	These values are for functions
	public byte eventvalue,notevalue,notelengthvalue;
	public byte[] eventdefinedvalue;
	public ByteBuffer eventnotebuffer;
	public String[] insfaminame,metaevents;
	public String[][] insname;
	public String innerExpl,innerSrc;
	public String diffExpl,diffSrc;
	public float progressInInit;

	/*	New a thread for analysing MIDI opcode
	*	Analysing in UI Thread
	*	WILL CAUSE ANR
	*	So I am forced to new another thread
	*	to analyze
	*	and a Handler to update UI
	*	because view hierarchy
	*/
	public ByteBuffer eventBuf;
	public String eventBuf2str;
	public StringBuffer eventnameBuf;
	public String eventnameBuf2str;
	//	If input an event at middle	not EOF
	public int eventPosition;

	public Handler analyze = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
				case Progress.updateStatus.initSrc:
					src.setText(innerSrc);
					break;
				case Progress.updateStatus.initExpl:
					expl.setText(innerExpl);
					break;
				case Progress.updateStatus.addSrc:
					src.setText(src.getText().toString()+eventBuf2str);
					break;
				case Progress.updateStatus.addExpl:
					expl.setText(expl.getText().toString()+eventnameBuf2str);
					break;
			}
		}
	};

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
		com.geno.tools.Debug.compileCount(this);

	//	Get string res
		remain = getString(R.string.remain);
		use = getString(R.string.nowuse);
		selectevent = new String[Progress.eventCount];
		insfaminame = new String[Progress.instFamilyCount];
		insname = new String[Progress.instFamilyCount][Progress.instCountPerFami];
		metaevents = new String[Progress.metaEventStatus.length];
		/*	Note that in R.java
		*	the string is sorted by name
		*	so we can getstring in this
		*	AMAZING way!
		*/
		for(int i = 0;i < Progress.eventCount;i++)
			selectevent[i]=getString(R.string.eventid8+i);
		for(int i = 0;i < Progress.instFamilyCount;i++)
			insfaminame[i]=getString(R.string.fami1+i);
		for(int i = 0;i < Progress.instFamilyCount;i++)
			for(int j = 0;j < Progress.instCountPerFami;j++)
				insname[i][j]=getString(R.string.inst00+i*Progress.instCountPerFami+j);
		for(int i = 0;i < Progress.metaEventStatus.length; i++)
			metaevents[i]=getString(R.string.meta00+i);

		eventdefinedvalue = new byte[] {ubtosb(0x80),ubtosb(0x90),ubtosb(0xA0),ubtosb(0xB0),ubtosb(0xC0),ubtosb(0xD0),ubtosb(0xE0)};
	//	Get layout id
		expl = (TextView)findViewById(R.id.explanation);
		src = (TextView)findViewById(R.id.srccode);
		detail = (TextView)findViewById(R.id.detail);
		addevent = (Button)findViewById(R.id.addevent);
		addmetaevent = (Button)findViewById(R.id.addmetaevent);

	//	Get byte buffer for editing
		sb = new StringBuffer();
		midi = ByteBuffer.allocate(1048576);
		eventnotebuffer = ByteBuffer.allocate(3);
		midi.put(Progress.midiHeader);

	//	Init display
		detail.setText(remain+midi.remaining()+" "+use+midi.position());
		src.setTypeface(Typeface.MONOSPACE);
		init();
		expl.setWidth(((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getWidth()/2);
		src.setWidth(((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getWidth()/2);

	//	The func below are being tested
		flag=1;
		midi.position(0);
		//toast(i.length+"");
		init();
		com.geno.midikeybdeditor.FileIO f=new com.geno.midikeybdeditor.FileIO();
		midi.put(f.openFileIntoByte("/sdcard/12[1].mid"));

	//	Edit widget
		addevent.setOnClickListener
		(new OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this)
					.setTitle(R.string.event)
					.setItems
					(selectevent, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface p1, final int p2)
							{
								flag=1;
								eventnotebuffer.position(0);
								AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
								.setTitle(R.string.track)
								.setItems
								(Progress.trackno,new DialogInterface.OnClickListener()
									{
										@Override
										public void onClick(DialogInterface p3, int p4)
										{
											eventnotebuffer.put((byte)(eventdefinedvalue[p2]+p4));
											switch(p2)
											{
												case 0x8-0x8:noteid();break;
												case 0x9-0x8:noteid();break;
												case 0xA-0x8:noteid();break;
												case 0xB-0x8:ctrlchg();break;
												case 0xC-0x8:prgmchg();break;
											}
										}
									}
								);
								ad.show();
							}
						}
					);
					ad.show();
				}
			}
		);

		addmetaevent.setOnClickListener
		(new OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
					.setTitle(R.string.metaevent)
					.setItems(metaevents,new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								switch(Progress.metaEventStatus[p2])
								{
									case 0x01:getMessage(0x01);break;
									case 0x02:getMessage(0x02);break;
									case 0x03:getMessage(0x03);break;
									case 0x04:getMessage(0x04);break;
									case 0x05:getMessage(0x05);break;
									case 0x06:getMessage(0x06);break;
									case 0x07:getMessage(0x07);break;
									case 0x2f:midi.put(new byte[]{ubtosb(0xff),0x2f,0});update();break;
								}
							}
						}
					);
					ad.show();
				}
			}
		);
	}

	//Menus

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.about:
				com.geno.tools.About.about("",MainActivity.this);
				break;
			case R.id.open:
				open();
				break;
			case R.id.save:
				save();
				break;
			case R.id.help:
				startActivity(new Intent(MainActivity.this,Help.class));
				break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	//Many necessary functions

	void init()
	{
		detail.setText(remain+midi.remaining()+" "+use+midi.position());
		expl.setText(R.string.analyzing);
		Thread initThread = new Thread() 
		{
			@Override
			public void run() 
			{
				innerSrc=printbyte(midi);
				analyze.sendEmptyMessage(Progress.updateStatus.initSrc);
			}
		};
		initThread.start();
	}

	void update()
	{
		detail.setText(remain+midi.remaining()+" "+use+midi.position());
		expl.setText(R.string.analyzing);
		Thread initThread = new Thread() 
		{
			@Override
			public void run() 
			{
				innerSrc=printbyte(midi);
				analyze.sendEmptyMessage(Progress.updateStatus.addSrc);
			}
		};
		initThread.start();
	}

	void addevent()
	{
		eventnotebuffer.position(0);
		midi.put(eventnotebuffer);
	}

	void trackcnt()
	{
		AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
		.setTitle(R.string.trackcnt)
		.setItems(new String[]{getString(R.string.monotrack),getString(R.string.syncmultitrack),getString(R.string.asyncmultitrack)},new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					ByteBuffer b;
					b = ByteBuffer.allocate(2);
					b.position(1);
					b.put((byte)p2);
					b.position(0);
					midi.put(b);
					update();
					trackno();
				}
			}
		);
		ad.show();
	}

	void trackno()
	{ 
		View v = LayoutInflater.from(this).inflate(R.layout.trackno, null);
		trackn = (EditText)v.findViewById(R.id.trackcount);
		AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
		.setTitle(R.string.trackcnt)
		.setView(v)
		.setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					ByteBuffer b;
					b = ByteBuffer.allocate(2);
					b.position(1);
					b.put((byte)Integer.parseInt(trackn.getText().toString()));
					b.position(0);
					midi.put(b);
					update();
				}
			}
		)
		.setNegativeButton(android.R.string.cancel,null);
		ad.show();
	}

	void deltatime()
	{
		
	}

	//Test String
	/*dguihghcidus8socid7dofie8dis7eiv6sueodofodirksusm jsusjsirieidurud8dkvuskci
	ovidodiguakdifiskcusodidure8e8?idieicieicuwuvcifoc8rpg9rpg9rpfofof9govofidppvofp co ocodjwkdkvi o lcl icorodld*/

	void getMessage(final int metaEventId)
	{
		View v = LayoutInflater.from(this).inflate(R.layout.textmessage, null);
		final EditText t = (EditText)v.findViewById(R.id.innertext);
		AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
		.setTitle(getString(R.string.meta00+metaEventId).substring(3))
		.setView(v)
		.setPositiveButton
		(android.R.string.ok,new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					String msg = t.getText().toString();
					char[] c = msg.toCharArray();
					ByteBuffer b,charbuf;
					b = ByteBuffer.allocate(c.length);
					charbuf = ByteBuffer.allocate(2);
					for(int i = 0;i<c.length;i++)
					{
						charbuf.putChar(c[i]);
						charbuf.position(1);
						b.put(charbuf);
						charbuf.position(0);
					}
					b.position(0);
					midi.put(new byte[]{ubtosb(0xFF),ubtosb(metaEventId)});
					midi.put(int2byte(hex2dec(com.geno.tools.Math.variableLengthFormat(c.length))));
					midi.put(b);
					update();
				}
			}
		)
		.setNegativeButton(android.R.string.cancel,null);
		ad.show();
	}

	/*	Then eventnotebuffer has ONE byte
	*	saying the event id (8 - E) in high 8 bits
	*	and the track number (0 - E) in low 8 bits
	*/
	
	//	Event start
	//	8x, 9x, Ax needed
	void noteid()
	{
		AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
		.setTitle(R.string.note)
		.setItems
		(Progress.notename,new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					notevalue = (byte) (0x3c + p2);
					AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
					.setTitle(R.string.note12)
					.setItems
						(Progress.note12,new DialogInterface.OnClickListener()
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
		(android.R.string.ok,new DialogInterface.OnClickListener()
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
					if(flag==1)
					{
						eventnotebuffer.position(0);
						midi.put(eventnotebuffer);
					}
					update();
				}
			}
		);
		ad.show();
	}

	//Bx needed
	void ctrlchg()
	{
		final EditText t = new EditText(MainActivity.this);
		t.setHint(R.string.eventidB_sta1);
		AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
		.setTitle(getString(R.string.eventidB).substring(3))
		.setView(t)
		.setPositiveButton
		(android.R.string.ok,new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					try
					{
						eventnotebuffer.put(ubtosb(Integer.parseInt(t.getText().toString())));
					}
					catch(Exception e)
					{
						Toast.makeText(MainActivity.this,R.string.illegalnumfmt,Toast.LENGTH_SHORT).show();
						ctrlchg();
						return;
					}
					if(flag == 1)
						ctrlid();
				}
			}
		)
		.setNegativeButton(android.R.string.cancel,null);
		ad.show();
	}

	void ctrlid()
	{
		final EditText e = new EditText(MainActivity.this);
		AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
		.setTitle(R.string.eventidB_sta1)
		.setView(e)
		.setPositiveButton
		(android.R.string.ok,new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					try
					{
						eventnotebuffer.put(ubtosb(Integer.parseInt(e.getText().toString())));
					}
					catch(Exception e)
					{
						Toast.makeText(MainActivity.this,R.string.illegalnumfmt,Toast.LENGTH_SHORT).show();
						ctrlid();
						return;
					}
					if(flag == 1)
					{
						eventnotebuffer.position(0);
						midi.put(eventnotebuffer);
					}
					update();
				}
			}
		)
		.setNegativeButton(android.R.string.cancel,null);
		ad.show();
	}

	//	Cx needed
	void prgmchg()
	{
		AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
		.setTitle(getString(R.string.eventidC).substring(3))
		.setItems
		(insfaminame,new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					String a = insfaminame[p2];
					final int s = p2;
					AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
					.setTitle(String.copyValueOf(a.toCharArray(),7,a.length()-7))
					.setItems
					(insname[p2],new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								eventnotebuffer.put((byte)(s*8+p2));
								if(flag == 1)
								{
									eventnotebuffer.position(0);
									midi.put(eventnotebuffer);
									midi.position(midi.position()-1);
								}
								update();
							}
						}
					);
					ad.show();
				}
			}
		);
		ad.show();
	}

	//	SysEx Event
	//	This name right?

	void sysEx()
	{
		AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
		.setItems
		(new String[]{"0xF8","0xFA","0xFB","0xFC"},
		new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Implement this method
				}
			}
		);
		ad.show();
	}

	//	Event end

	//	Calc function
	byte ubtosb(int unsigned)
	{
		//	Unsigned byte to signed byte
		return unsigned < 128 ? (byte)unsigned : (byte)(unsigned-256);
	}

	byte[] int2byte(int hexint)
	{
		String out = Integer.toHexString(hexint);
		byte[] a ;
		odd2even(out);
		a = new byte[out.length()];
		for(int i = 0;i < out.length()/2;i++)
		{
			a[i]=(byte)Integer.parseInt(out.substring(2*i,2*i+1));
		}
		return a;
	}

	String odd2even(String oddLengthString)
	{
		if(oddLengthString.length()%2!=0)
			oddLengthString="0"+oddLengthString;
		return oddLengthString;
	}

	int bin2hex(String binary)
	{
		int out = 0;
		for(int i = 0;i < binary.length();i++)
		{
			out = Integer.parseInt(String.valueOf(binary.charAt(i)))*(int)(Math.pow((double)2,(double)(binary.length()-i-1)))+out;
		}
		return out;
	}

	int hex2dec(String hexString)
	{
		int out = 0;
		for(int i = 0;i<hexString.length();i++)
		{
			switch(hexString.charAt(i))
			{
				case 'a':
					out=(int)(out+Math.pow(16,hexString.length()-i-1)*10);
					break;
				case 'b':
					out=(int)(out+Math.pow(16,hexString.length()-i-1)*11);
					break;
				case 'c':
					out=(int)(out+Math.pow(16,hexString.length()-i-1)*12);
					break;
				case 'd':
					out=(int)(out+Math.pow(16,hexString.length()-i-1)*13);
					break;
				case 'e':
					out=(int)(out+Math.pow(16,hexString.length()-i-1)*14);
					break;
				case 'f':
					out=(int)(out+Math.pow(16,hexString.length()-i-1)*15);
					break;
				default:
					out=(int)(out+Math.pow(16,hexString.length()-i-1)*Integer.parseInt(""+hexString.charAt(i)));
					break;
			}
		}
		return out;
	}

	/*	This function is now very simple
	*	but later it will have its responsibility
	*	of DECODING A MIDI
	*/
	//	Will this be a new thread?
	String printbyte(ByteBuffer b)
	{
		String res = "";
		int i;
		for(i=0;i<b.position();i++)
		{
			String buf = Integer.toHexString(b.get(i)&0xFF).toUpperCase();
			if(buf.length()<2)
				buf="0"+buf;
			res=res+buf+" ";
		}
		return res;
	}

	Thread printByte = new Thread() 
	{
		@Override
		public void run() 
		{
			String res = "";
			for(int i=0;i<midi.position();i++)
			{
				String buf = Integer.toHexString(midi.get(i)&0xFF).toUpperCase();
				if(buf.length()<2)
					buf="0"+buf;
				res=res+buf+" ";
			}
		}
	};

	String printbytearr(byte[] input)
	{
		String output = "";
		for(int i = 0;i<input.length;i++)
		output=output+Byte.toString(input[i]);
		return output;
	}

	//Debugger function
	void toast(String innerText)
	{
		Toast.makeText(MainActivity.this,innerText,Toast.LENGTH_SHORT).show();
	}

	String printarr(String[] src)
	{
		String res="";
		for(int i = 0;i<src.length;i++)
		{
			res=res+src[i];
		}
		return res;
	}

	//File function
	void save()
	{
		final EditText t = new EditText(MainActivity.this);
		AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
		.setTitle(R.string.save)
		.setView(t)
		.setPositiveButton
		(android.R.string.ok,new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					OutputStream o = null;
					try
					{
						o = new BufferedOutputStream(new FileOutputStream(new File(t.getText().toString())));
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
					Toast.makeText(MainActivity.this,R.string.finish,Toast.LENGTH_SHORT).show();
				}
			}
		)
		.setNegativeButton(android.R.string.cancel,null);
		ad.show();
	}

	void open()
	{
		final EditText t = new EditText(MainActivity.this);
		AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this)
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
						Toast.makeText(MainActivity.this,R.string.filenotfound,Toast.LENGTH_SHORT).show();
					}
					byte[] midichk = null;
					try
					{
						i.read(midichk,0,8);
						i.close();
					}
					catch (Exception e)
					{}
					if(midichk!=new byte[]{0x4D,0x54,0x68,0x64,0,0,0,6})
					{
						Toast.makeText(MainActivity.this,R.string.notamidi,Toast.LENGTH_SHORT).show();
					}
					ByteBuffer b=null;
					b = ByteBuffer.allocate((int)f.length());
					try
					{
						i.read(b.array());
						i.close();
					}
					catch (Exception e)
					{}
					midi.position(0);
					midi.put(b);
					init();
					Toast.makeText(MainActivity.this,R.string.finish,Toast.LENGTH_SHORT).show();
				}
			}
		)
		.setNegativeButton(android.R.string.cancel,null);
		ad.show();
	}
}

