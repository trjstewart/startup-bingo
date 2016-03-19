package co.startupbingo.startupbingo.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import co.startupbingo.startupbingo.R;
import co.startupbingo.startupbingo.model.Word;

public class GameGridRecyclerAdapter extends RecyclerView.Adapter<GameGridRecyclerAdapter.ViewHolder> {

    public ArrayList<Word> wordArrayList = new ArrayList<>(25);
    public Context mContext;
    public GameGridRecyclerEvents mListener;

    public GameGridRecyclerAdapter(Context context) {
        mContext = context;
        if (mContext instanceof GameGridRecyclerEvents){
            mListener = (GameGridRecyclerEvents)mContext;
        } else {
            throw new RuntimeException("RuntimeError: Thing must implement "+GameGridRecyclerEvents.class.getName());
        }
    }

    public boolean addWord(Word newWord){
        return wordArrayList!=null&&wordArrayList.add(newWord);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_grid_template,parent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Word currentWord = wordArrayList.get(position);
        holder.tileLayout.setBackgroundColor(currentWord.isChecked?
                        ContextCompat.getColor(mContext,R.color.colorAccent):
                        ContextCompat.getColor(mContext,R.color.transparent));
        holder.tileTextView.setText(currentWord.getTileString());
    }

    @Override
    public int getItemCount() {
        return wordArrayList.size();
    }

    public interface GameGridRecyclerEvents {
        void onClickTile(Word selectedWord);
        void onLongClickTile(Word selectedTile);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public SquareGameTileLayout tileLayout;
        public RelativeLayout tileClickLayout;
        public TextView tileTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            tileLayout = (SquareGameTileLayout)itemView.findViewById(R.id.game_tile_base_layout);
            tileClickLayout = (RelativeLayout)itemView.findViewById(R.id.game_tile_click_layout);
            tileTextView = (TextView)itemView.findViewById(R.id.game_tile_text_view);
        }
    }


}
