package com.geno.midikeybdeditor;

import android.os.*;
import android.app.*;
import android.view.*;
import android.widget.*;
import android.database.*;

public class Help extends Activity
{
	public String[] helpTitle;
	public String[][] helpText;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		helpTitle = new String[Progress.helpTitleCount];
		helpText = new String[Progress.helpTitleCount][Progress.helpTextMax];

		for(int i = 0;i < Progress.helpTitleCount;i++)
			helpTitle[i]=(i + 1) + ". " + getString(R.string.help_1+i);

		for(int i = 0;i < Progress.helpTitleCount;i++)
			for(int j = 0; j < Progress.helpTextCount[i];j++)
				helpText[i][j]=getString(R.string.helpans_1_1+com.geno.tools.Math.sumInArray(Progress.helpTextCount,i)+j);

		LinearLayout main=new LinearLayout(this);

		ExpandableListView e=new ExpandableListView(this);
		ExpandableListAdapter a=new ExpandableListAdapter()
		{
			@Override
			public void registerDataSetObserver(DataSetObserver observer)
			{
			}

			@Override
			public void unregisterDataSetObserver(DataSetObserver observer)
			{
			}

			@Override
			public int getGroupCount()
			{
				return Progress.helpTitleCount;
			}

			@Override
			public int getChildrenCount(int groupPosition)
			{
				return Progress.helpTextCount[groupPosition];
			}

			@Override
			public Object getGroup(int groupPosition)
			{
				return helpTitle[groupPosition];
			}

			@Override
			public Object getChild(int groupPosition, int childPosition)
			{
				return helpText[groupPosition][childPosition];
			}

			@Override
			public long getGroupId(int groupPosition)
			{
				return 0x7e000000 + groupPosition * 0x00010000;
			}

			@Override
			public long getChildId(int groupPosition, int childPosition)
			{
				return 0x7e000000 + groupPosition * 0x00010000 + childPosition + 1;
			}

			@Override
			public boolean hasStableIds()
			{
				return true;
			}

			@Override
			public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
			{
				LinearLayout l = new LinearLayout(Help.this);
				TextView t = new TextView(Help.this);
				t.setText(getGroup(groupPosition).toString());
				t.setTextSize(20);
				t.setPadding(50,10,0,10);
				l.addView(t,((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getWidth(),LinearLayout.LayoutParams.WRAP_CONTENT);
				return l;
			}

			@Override
			public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
			{
				LinearLayout l = new LinearLayout(Help.this);
				TextView t = new TextView(Help.this);
				t.setText(getChild(groupPosition,childPosition).toString());
				t.setPadding(100,10,0,10);
				l.addView(t);
				return l;
			}

			@Override
			public boolean isChildSelectable(int groupPosition, int childPosition)
			{
				return true;
			}

			@Override
			public boolean areAllItemsEnabled()
			{
				return true;
			}

			@Override
			public boolean isEmpty()
			{
				return false;
			}

			@Override
			public void onGroupExpanded(int groupPosition)
			{
			}

			@Override
			public void onGroupCollapsed(int groupPosition)
			{
			}

			@Override
			public long getCombinedChildId(long groupId, long childId)
			{
				return 0;
			}

			@Override
			public long getCombinedGroupId(long groupId)
			{
				return 0;
			}
		};

		e.setAdapter(a);
		main.addView(e);
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

