package co.startupbingo.startupbingo.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import co.startupbingo.startupbingo.R;
import co.startupbingo.startupbingo.model.ClearWord;
import co.startupbingo.startupbingo.model.Word;
import rx.Observable;

public class GameGridRecyclerAdapter extends RecyclerView.Adapter<GameGridRecyclerAdapter.ViewHolder> {

    public ArrayList<Word> wordArrayList = new ArrayList<>(25);
    public Context mContext;
    public GameGridRecyclerEvents mListener;

    public GameGridRecyclerAdapter(Context context) {
        mContext = context;
    }

    public void setOnEventsListener(GameGridRecyclerEvents listener){
        mListener = listener;
    }

    public boolean addWord(Word newWord) {
        if (newWord instanceof ClearWord){
            clearWords();
            return false;
        } else {
            wordArrayList.add(newWord);
            notifyItemInserted(getItemCount());
            return true;
        }
    }

    private void clearWords() {
        int length = wordArrayList.size();
        wordArrayList.clear();
        notifyItemRangeRemoved(0,length);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_grid_template,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Word currentWord = wordArrayList.get(position);
        holder.tileLayout.setBackgroundColor(currentWord.isChecked?
                        ContextCompat.getColor(mContext,R.color.colorAccent):
                        ContextCompat.getColor(mContext,R.color.colorPrimaryDark));
        holder.tileTextView.setTextColor(currentWord.isChecked?
                ContextCompat.getColor(mContext,android.R.color.primary_text_light):
                ContextCompat.getColor(mContext,android.R.color.primary_text_dark)
        );
        holder.tileTextView.setText(currentWord.getTileString());
        holder.tileClickLayout.setOnClickListener(v-> {
            if (mListener!=null) {
                mListener.onClickTile(currentWord,position);
                currentWord.isChecked= !currentWord.isChecked;
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordArrayList.size();
    }

    public interface GameGridRecyclerEvents {
        void onClickTile(Word selectedWord, int pos);
        void onLongClickTile(Word selectedTile, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout tileLayout;
        public RelativeLayout tileClickLayout;
        public TextView tileTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            tileLayout = (RelativeLayout)itemView.findViewById(R.id.game_tile_base_layout);
            tileClickLayout = (RelativeLayout)itemView.findViewById(R.id.game_tile_click_layout);
            tileTextView = (TextView)itemView.findViewById(R.id.game_tile_text_view);
        }
    }


}
