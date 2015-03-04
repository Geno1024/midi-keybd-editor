package com.geno.midikeybdeditor;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity
{
	public Button c,d,e,f,g,a,b,cr,dr,fr,gr,ar;
	public TextView expl,src;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
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
    }
}
