package com.mlpl.gopalandroidsystemtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.mlpl.gopalandroidsystemtest.api.ClientApi;
import com.mlpl.gopalandroidsystemtest.api.model.PicsSum;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    List<PicsSum> picsSumList;
    RecyclerView recyclerView_PicSum;
    LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 20;
    private int currentPage = PAGE_START;
    PaginationAdapter paginationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picsSumList = new ArrayList<>();
        progressBar = findViewById(R.id.progressbar);
        recyclerView_PicSum = (RecyclerView) findViewById(R.id.recyclerView_PicSum);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView_PicSum.setLayoutManager(linearLayoutManager);
        recyclerView_PicSum.setItemAnimator(new DefaultItemAnimator());
        paginationAdapter = new PaginationAdapter(this);
        recyclerView_PicSum.setAdapter(paginationAdapter);

        recyclerView_PicSum.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadNextPage();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        loadFirstPage();


    }

    private void loadFirstPage() {

        ClientApi.getService().PICS_SUM_CALL(PAGE_START,20).enqueue(new Callback<List<PicsSum>>() {
            @Override
            public void onResponse(Call<List<PicsSum>> call, Response<List<PicsSum>> response) {
                List<PicsSum> results = response.body();
                progressBar.setVisibility(View.GONE);
                paginationAdapter.addAll(results);

                if (currentPage <= TOTAL_PAGES) paginationAdapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<List<PicsSum>> call, Throwable t) {

            }

        });
    }

    private void loadNextPage(){

        ClientApi.getService().PICS_SUM_CALL(currentPage,20).enqueue(new Callback<List<PicsSum>>() {
            @Override
            public void onResponse(Call<List<PicsSum>> call, Response<List<PicsSum>> response) {
                paginationAdapter.removeLoadingFooter();
                isLoading = false;

                List<PicsSum> results = response.body();
                paginationAdapter.addAll(results);

                if (currentPage != TOTAL_PAGES) paginationAdapter.addLoadingFooter();
                else isLastPage = true;

            }

            @Override
            public void onFailure(Call<List<PicsSum>> call, Throwable t) {

            }
        });
    }
}
