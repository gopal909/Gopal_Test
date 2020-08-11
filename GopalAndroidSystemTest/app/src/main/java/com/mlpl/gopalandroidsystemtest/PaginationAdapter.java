package com.mlpl.gopalandroidsystemtest;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mlpl.gopalandroidsystemtest.api.model.PicsSum;
import java.util.LinkedList;
import java.util.List;

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<PicsSum> picsSumList;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;

    public PaginationAdapter(Context context) {
        this.context = context;
        picsSumList = new LinkedList<>();
    }

    public void setMovieList(List<PicsSum> movieList) {
        this.picsSumList = movieList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.item_list, parent, false);
                viewHolder = new MovieViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        PicsSum picsSum = picsSumList.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
                movieViewHolder.txtTitle.setText(picsSum.getAuthor());
                Glide.with(context).load(picsSum.getDownloadUrl()).into(movieViewHolder.imageView);
                break;

            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return picsSumList == null ? 0 : picsSumList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == picsSumList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new PicsSum());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = picsSumList.size() - 1;
        PicsSum result = getItem(position);

        if (result != null) {
            picsSumList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(PicsSum picsSum) {
        picsSumList.add(picsSum);
        notifyItemInserted(picsSumList.size() - 1);
    }

    public void addAll(List<PicsSum> picsSums) {
        for (PicsSum result : picsSums) {
            add(result);
        }
    }

    public PicsSum getItem(int position) {
        return picsSumList.get(position);
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle;
        private ImageView imageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.title);
            imageView = (ImageView) itemView.findViewById(R.id.list_image);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);

        }
    }
}
