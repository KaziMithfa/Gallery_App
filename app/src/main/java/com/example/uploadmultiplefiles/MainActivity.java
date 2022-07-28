package com.example.uploadmultiplefiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.uploadmultiplefiles.Model.ImageModel;
import com.example.uploadmultiplefiles.api.ApiUtilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recylerView;
    private ArrayList<ImageModel> arrayList;
    private GridLayoutManager gridLayoutManager;
    private ImageAdapter imageAdapter;
    private int page = 1;


    private int pagesize = 30;
    private ProgressDialog loadingBar;
    private boolean isLoading;
    private boolean isLastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recylerView = findViewById(R.id.recylerViewId);

        arrayList = new ArrayList<ImageModel>();
        imageAdapter = new ImageAdapter(this,arrayList);
        gridLayoutManager = new GridLayoutManager(this,3);
        recylerView.setLayoutManager(gridLayoutManager);
        recylerView.setHasFixedSize(true);
        recylerView.setAdapter(imageAdapter);

        loadingBar = new ProgressDialog(this);
        loadingBar.setMessage("Loading....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        getData();




        recylerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItem = gridLayoutManager.getChildCount();
                int totalItem = gridLayoutManager.getItemCount();
                int firstVisibleItempos = gridLayoutManager.findFirstVisibleItemPosition();


                if(!isLoading && !isLastPage){
                    if((visibleItem+firstVisibleItempos >=totalItem )
                        && firstVisibleItempos >=0
                        && totalItem >= pagesize)
                    {

                        page++;
                        getData();
                    }
                }
            }
        });



    }

    private void getData(){

        isLoading = true;

       ApiUtilities.getApiInterface().getImages(page,30)
               .enqueue(new Callback<List<ImageModel>>() {
                   @Override
                   public void onResponse(Call<List<ImageModel>> call, Response<List<ImageModel>> response) {
                       if(response.isSuccessful()){
                           arrayList.addAll(response.body());
                           imageAdapter.notifyDataSetChanged();






                       }

                       else{
                           Toast.makeText(MainActivity.this,response.code()+"", Toast.LENGTH_SHORT).show();
                       }




                       isLoading = false;
                       loadingBar.dismiss();

                       if(arrayList.size()>0){
                           isLastPage = arrayList.size()<pagesize;
                       }

                       else{
                           isLastPage = true;
                       }





                   }

                   @Override
                   public void onFailure(Call<List<ImageModel>> call, Throwable t) {
                       loadingBar.dismiss();

                       Toast.makeText(MainActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();



                   }
               });


       }




    }
