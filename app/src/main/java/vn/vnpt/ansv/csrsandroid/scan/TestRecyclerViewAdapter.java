package vn.vnpt.ansv.csrsandroid.scan;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import vn.vnpt.ansv.csrsandroid.R;

/**
 * Created by ANSV on 11/4/2017.
 */

public class TestRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Object> contents;
    enum TypeCell {
        TYPE_HEADER(0),
        TYPE_DETAIL(1);
        public final int value;
        TypeCell(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
//    static final int TYPE_HEADER = 0;
//    static final int TYPE_CELL = 1;

    public TestRecyclerViewAdapter(List<Object> contents) {
        this.contents = contents;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TypeCell.TYPE_HEADER.getValue();
            default:
                return TypeCell.TYPE_DETAIL.getValue();
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case 0: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_big, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            }
            case 1: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_small, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            }
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                break;
            case 1:
                break;
        }
    }
}