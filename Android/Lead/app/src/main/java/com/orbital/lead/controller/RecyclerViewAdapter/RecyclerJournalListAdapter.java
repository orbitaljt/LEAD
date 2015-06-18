package com.orbital.lead.controller.RecyclerViewAdapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orbital.lead.R;
import com.orbital.lead.controller.MainActivity;

import java.util.ArrayList;


/**
 * Created by joseph on 16/6/2015.
 */
public class RecyclerJournalListAdapter extends RecyclerView.Adapter<RecyclerJournalListAdapter.ListContentHolder>{
    private OnItemClickListener mItemClickListener;

    private ArrayList<String> arrayTitle;

    public class ListContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView mCardParent;
        private TextView mTextTitle;

        public ListContentHolder(View v){
            super(v);
            this.mCardParent = (CardView) v.findViewById(R.id.card_view_journal);
            this.mTextTitle = (TextView) v.findViewById(R.id.text_view_title);


        }

        public void setCardTextTitle(String val){
            this.mTextTitle.setText(val);
        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener != null){
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

    }

    public RecyclerJournalListAdapter(MainActivity activity, ArrayList<String> arr){
        this.arrayTitle = new ArrayList<String>(arr);
    }

    @Override
    public RecyclerJournalListAdapter.ListContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_row_user_journal_list_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ListContentHolder vh = new ListContentHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ListContentHolder holder, int position) {
        holder.setCardTextTitle(this.arrayTitle.get(position));
    }

    @Override
    public int getItemCount() {
        return this.arrayTitle.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener listener){
        this.mItemClickListener = listener;
    }


}
