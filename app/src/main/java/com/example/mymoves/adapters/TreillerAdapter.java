package com.example.mymoves.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoves.R;
import com.example.mymoves.data.Treiller;

import java.util.ArrayList;

public class TreillerAdapter extends RecyclerView.Adapter<TreillerAdapter.TreillerHolder> {

    private onClickLTreillerListener onClickLTreillerListener;
    private ArrayList<Treiller> treillers;

    @NonNull
    @Override
    public TreillerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.treiller_item,parent,false);
        return new TreillerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TreillerHolder holder, int position) {
            Treiller treiller = treillers.get(position);
            holder.name.setText(treiller.getName());

    }

    @Override
    public int getItemCount() {
        return treillers.size();
    }

    public interface onClickLTreillerListener{
        void onTreillerClick(String url);
    }

    class TreillerHolder extends RecyclerView.ViewHolder{

        private TextView name;

        public TreillerHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.texxtViewNameVideo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickLTreillerListener != null){
                        onClickLTreillerListener.onTreillerClick(treillers.get(getAdapterPosition()).getKey());
                    }
                }
            });
        }
    }

    public void setTreillers(ArrayList<Treiller> treillers) {
        this.treillers = treillers;
        notifyDataSetChanged();
    }

    public void setOnClickLTreillerListener(TreillerAdapter.onClickLTreillerListener onClickLTreillerListener) {
        this.onClickLTreillerListener = onClickLTreillerListener;
    }
}
