package com.cfp.metpollen.view.adapters;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import com.cfp.metpollen.R;
import com.cfp.metpollen.view.customViews.ExpandableStickyHeaderRecyclerView.SectioningAdapter;

import java.util.ArrayList;

/**
 * HourlyHeaderRecyclerAdapter, just shows demo data
 */
public class HourlyHeaderRecyclerAdapter extends SectioningAdapter {

	static final String TAG = HourlyHeaderRecyclerAdapter.class.getSimpleName();
	static final boolean USE_DEBUG_APPEARANCE = false;

	private class Section {
		int index;
		int copyCount;
		String header;
		String footer;
		ArrayList<String> items = new ArrayList<>();

		public Section duplicate() {
			Section c = new Section();
			c.index = this.index;
			c.copyCount = this.copyCount + 1;
			c.header = c.index + " copy " + c.copyCount;
			c.footer = this.footer;
			for (String i : this.items) {
				c.items.add(i + " copy " + c.copyCount);
			}

			return c;
		}

		public void duplicateItem(int item) {
			String itemCopy = items.get(item) + " copy";
			items.add(item + 1, itemCopy);
		}

	}

	public class ItemViewHolder extends SectioningAdapter.ItemViewHolder implements View.OnClickListener {
		TextView textView;
		TextView adapterPositionTextView;
		ImageButton cloneButton;
		ImageButton deleteButton;

		public ItemViewHolder(View itemView) {
			super(itemView);
			textView = (TextView) itemView.findViewById(R.id.time);
//			adapterPositionTextView = (TextView) itemView.findViewById(R.id.name);
/*
			cloneButton = (ImageButton) itemView.findViewById(R.id.cloneButton);
			cloneButton.setOnClickListener(this);

			deleteButton = (ImageButton) itemView.findViewById(R.id.deleteButton);
			deleteButton.setOnClickListener(this);*/

			/*if (!HourlyHeaderRecyclerAdapter.this.showModificationControls) {
				cloneButton.setVisibility(View.GONE);
				deleteButton.setVisibility(View.GONE);
			}

			if (!HourlyHeaderRecyclerAdapter.this.showAdapterPositions) {
				adapterPositionTextView.setVisibility(View.GONE);
			}*/
		}

		@Override
		public void onClick(View v) {
			int adapterPosition = getAdapterPosition();
			final int section = HourlyHeaderRecyclerAdapter.this.getSectionForAdapterPosition(adapterPosition);
			final int item = HourlyHeaderRecyclerAdapter.this.getPositionOfItemInSection(section, adapterPosition);
			if (v == cloneButton) {
				HourlyHeaderRecyclerAdapter.this.onCloneItem(section, item);
			} else if (v == deleteButton) {
				HourlyHeaderRecyclerAdapter.this.onDeleteItem(section, item);
			}
		}
	}

	public class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder implements View.OnClickListener {
		TextView textView;
		TextView adapterPositionTextView;
		ImageButton cloneButton;
		ImageButton deleteButton;
		ImageButton collapseButton;

		public HeaderViewHolder(View itemView) {
			super(itemView);
			textView = (TextView) itemView.findViewById(R.id.textview_section_header);
//			adapterPositionTextView = (TextView) itemView.findViewById(R.id.textview_section_header);

	/*		cloneButton = (ImageButton) itemView.findViewById(R.id.cloneButton);
			cloneButton.setOnClickListener(this);

			deleteButton = (ImageButton) itemView.findViewById(R.id.deleteButton);
			deleteButton.setOnClickListener(this);

			collapseButton = (ImageButton) itemView.findViewById(R.id.collapseButton);
			collapseButton.setOnClickListener(this);
*/
			/*if (!HourlyHeaderRecyclerAdapter.this.showModificationControls) {
				cloneButton.setVisibility(View.GONE);
				deleteButton.setVisibility(View.GONE);
			}

			if (!HourlyHeaderRecyclerAdapter.this.showCollapsingSectionControls) {
				collapseButton.setVisibility(View.GONE);
			}

			if (!HourlyHeaderRecyclerAdapter.this.showAdapterPositions) {
				cloneButton.setVisibility(View.INVISIBLE);
				deleteButton.setVisibility(View.INVISIBLE);
				adapterPositionTextView.setVisibility(View.INVISIBLE);
			}*/
		}

		void updateSectionCollapseToggle(boolean sectionIsCollapsed) {
			/*@DrawableRes int id = sectionIsCollapsed
					? R.drawable.ic_expand_more_black_24dp
					: R.drawable.ic_expand_less_black_24dp;

			collapseButton.setImageDrawable(ContextCompat.getDrawable(collapseButton.getContext(), id));*/
		}

		@Override
		public void onClick(View v) {
			int position = getAdapterPosition();
			final int section = HourlyHeaderRecyclerAdapter.this.getSectionForAdapterPosition(position);
			if (v == cloneButton) {
				HourlyHeaderRecyclerAdapter.this.onCloneSection(section);
			} else if (v == deleteButton) {
				HourlyHeaderRecyclerAdapter.this.onDeleteSection(section);
			} else if (v == collapseButton) {
				HourlyHeaderRecyclerAdapter.this.onToggleSectionCollapse(section);
				updateSectionCollapseToggle(HourlyHeaderRecyclerAdapter.this.isSectionCollapsed(section));
			}
		}
	}

	public class FooterViewHolder extends SectioningAdapter.FooterViewHolder {
//		TextView textView;
//		TextView adapterPositionTextView;

		public FooterViewHolder(View itemView) {
			super(itemView);
//			textView = (TextView) itemView.findViewById(R.id.textview_section_header);
//			adapterPositionTextView = (TextView) itemView.findViewById(R.id.textview_section_header);

			if (!HourlyHeaderRecyclerAdapter.this.showAdapterPositions) {
//				adapterPositionTextView.setVisibility(View.GONE);
			}
		}
	}


	ArrayList<Section> sections = new ArrayList<>();
	boolean showModificationControls;
	boolean showCollapsingSectionControls;
	boolean showAdapterPositions;
	boolean hasFooters;

	public HourlyHeaderRecyclerAdapter(int numSections, int numItemsPerSection, boolean hasFooters, boolean showModificationControls, boolean showCollapsingSectionControls, boolean showAdapterPositions) {
		this.showModificationControls = showModificationControls;
		this.showCollapsingSectionControls = showCollapsingSectionControls;
		this.showAdapterPositions = showAdapterPositions;
		this.hasFooters = hasFooters;

		for (int i = 0; i < numSections; i++) {
			appendSection(i, numItemsPerSection);
		}
	}

	void appendSection(int index, int itemCount) {
		Section section = new Section();
		section.index = index;
		section.copyCount = 0;
		section.header = Integer.toString(index);

		if (this.hasFooters) {
			section.footer = "End of section " + index;
		}

		for (int j = 0; j < itemCount; j++) {
			section.items.add(index + "/" + j);
		}

		sections.add(section);
	}

	void onToggleSectionCollapse(int sectionIndex) {
		Log.d(TAG, "onToggleSectionCollapse() called with: " + "sectionIndex = [" + sectionIndex + "]");
		setSectionIsCollapsed(sectionIndex, !isSectionCollapsed(sectionIndex));
	}

	void onDeleteSection(int sectionIndex) {
		Log.d(TAG, "onDeleteSection() called with: " + "sectionIndex = [" + sectionIndex + "]");
		sections.remove(sectionIndex);
		notifySectionRemoved(sectionIndex);
	}

	void onCloneSection(int sectionIndex) {
		Log.d(TAG, "onCloneSection() called with: " + "sectionIndex = [" + sectionIndex + "]");

		Section s = sections.get(sectionIndex);
		Section d = s.duplicate();
		sections.add(sectionIndex + 1, d);
		notifySectionInserted(sectionIndex + 1);
	}

	void onDeleteItem(int sectionIndex, int itemIndex) {
		Log.d(TAG, "onDeleteItem() called with: " + "sectionIndex = [" + sectionIndex + "], itemIndex = [" + itemIndex + "]");
		Section s = sections.get(sectionIndex);
		s.items.remove(itemIndex);
		notifySectionItemRemoved(sectionIndex, itemIndex);
	}

	void onCloneItem(int sectionIndex, int itemIndex) {
		Log.d(TAG, "onCloneItem() called with: " + "sectionIndex = [" + sectionIndex + "], itemIndex = [" + itemIndex + "]");
		Section s = sections.get(sectionIndex);
		s.duplicateItem(itemIndex);
		notifySectionItemInserted(sectionIndex, itemIndex + 1);
	}

	@Override
	public int getNumberOfSections() {
		return sections.size();
	}

	@Override
	public int getNumberOfItemsInSection(int sectionIndex) {
		return sections.get(sectionIndex).items.size();
	}

	@Override
	public boolean doesSectionHaveHeader(int sectionIndex) {
		return !TextUtils.isEmpty(sections.get(sectionIndex).header);
	}

	@Override
	public boolean doesSectionHaveFooter(int sectionIndex) {
		return !TextUtils.isEmpty(sections.get(sectionIndex).footer);
	}

	@Override
	public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View v = inflater.inflate(R.layout.recycler_item_hourly, parent, false);
		return new ItemViewHolder(v);
	}

	@Override
	public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View v = inflater.inflate(R.layout.recycler_section_header_hourly, parent, false);
		return new HeaderViewHolder(v);
	}

	@Override
	public FooterViewHolder onCreateFooterViewHolder(ViewGroup parent, int footerType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View v = inflater.inflate(R.layout.recycler_section_footer_hourly, parent, false);
		return new FooterViewHolder(v);
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemType) {
		Section s = sections.get(sectionIndex);
		ItemViewHolder ivh = (ItemViewHolder) viewHolder;
		ivh.textView.setText(s.items.get(itemIndex));
//		ivh.adapterPositionTextView.setText(Integer.toString(getAdapterPositionForSectionItem(sectionIndex, itemIndex)));
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerType) {
		Section s = sections.get(sectionIndex);
		HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;
//		hvh.adapterPositionTextView.setText(Integer.toString(getAdapterPositionForSectionHeader(sectionIndex)));

		if (USE_DEBUG_APPEARANCE) {
			hvh.textView.setText(pad(sectionIndex * 2) + s.header);
			viewHolder.itemView.setBackgroundColor(0x55FF9999);
		} else {
			hvh.textView.setText(s.header);
		}

/*
		if (sectionIndex>0)
		{
			hvh.itemView.setVisibility(View.GONE);
		}
		else
		{
			hvh.itemView.setVisibility(View.VISIBLE);
		}
*/
		hvh.updateSectionCollapseToggle(isSectionCollapsed(sectionIndex));
	}

	@Override
	public void onBindGhostHeaderViewHolder(SectioningAdapter.GhostHeaderViewHolder viewHolder, int sectionIndex) {
		if (USE_DEBUG_APPEARANCE) {
			viewHolder.itemView.setBackgroundColor(0xFF9999FF);
		}
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onBindFooterViewHolder(SectioningAdapter.FooterViewHolder viewHolder, int sectionIndex, int footerType) {
		Section s = sections.get(sectionIndex);
		FooterViewHolder fvh = (FooterViewHolder) viewHolder;
//		fvh.textView.setText(s.footer);
//		fvh.adapterPositionTextView.setText(Integer.toString(getAdapterPositionForSectionFooter(sectionIndex)));
	}

	private String pad(int spaces) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < spaces; i++) {
			b.append(' ');
		}
		return b.toString();
	}

}
