package com.example.yourservices.ui;

import android.view.LayoutInflater;

import com.example.yourservices.R;
import com.example.yourservices.core.News;

import java.util.List;

public class NewsListAdapter extends AlternatingColorListAdapter<News> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public NewsListAdapter(final LayoutInflater inflater, final List<News> items,
                           final boolean selectable) {
        super(R.layout.discount_list_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public NewsListAdapter(final LayoutInflater inflater, final List<News> items) {
        super(R.layout.discount_list_item, inflater, items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.tv_title, R.id.tv_summary,
                R.id.tv_distance};
    }

    @Override
    protected void update(final int position, final News item) {
        super.update(position, item);

        setText(0, item.getTitle());
        setText(1, item.getContent());
        //setNumber(R.id.tv_date, item.getCreatedAt());
    }
}
