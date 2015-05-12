package com.geno.midikeybdeditor;

import android.os.*;
import android.app.*;
import android.view.*;
import android.widget.*;
import android.database.*;

public class Help extends Activity
{
	public String[] helpTitle;
	public String[] helpInnerText;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		for(int i = 0;i < Progress.helpTitleCount;i++)
		{
			helpTitle[i]=getString(R.string.help_1)+i;
		}
		
		LinearLayout main=new LinearLayout(this);

		ExpandableListView e=new ExpandableListView(this);
		ExpandableListAdapter a=new ExpandableListAdapter()
		{
			@Override
			public void registerDataSetObserver(DataSetObserver p1)
			{
			}

			@Override
			public void unregisterDataSetObserver(DataSetObserver p1)
			{
			}

			@Override
			public int getGroupCount()
			{
				return 0;
			}

			@Override
			public int getChildrenCount(int p1)
			{
				return 0;
			}

			@Override
			public Object getGroup(int p1)
			{
				return null;
			}

			@Override
			public Object getChild(int p1, int p2)
			{
				return null;
			}

			@Override
			public long getGroupId(int p1)
			{
				return 0;
			}

			@Override
			public long getChildId(int p1, int p2)
			{
				return 0;
			}

			@Override
			public boolean hasStableIds()
			{
				return false;
			}

			@Override
			public View getGroupView(int p1, boolean p2, View p3, ViewGroup p4)
			{
				return null;
			}

			@Override
			public View getChildView(int p1, int p2, boolean p3, View p4, ViewGroup p5)
			{
				return null;
			}

			@Override
			public boolean isChildSelectable(int p1, int p2)
			{
				return false;
			}

			@Override
			public boolean areAllItemsEnabled()
			{
				return false;
			}

			@Override
			public boolean isEmpty()
			{
				return false;
			}

			@Override
			public void onGroupExpanded(int p1)
			{
			}

			@Override
			public void onGroupCollapsed(int p1)
			{
			}

			@Override
			public long getCombinedChildId(long p1, long p2)
			{
				return 0;
			}

			@Override
			public long getCombinedGroupId(long p1)
			{
				return 0;
			}
		};

		e.setAdapter(a);
		main.addView(e);
		super.onCreate(savedInstanceState);
		setContentView(main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return super.onOptionsItemSelected(item);
	}

}

