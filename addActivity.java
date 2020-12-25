package com.example.ws_new;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

        import android.Manifest;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Environment;
        import android.os.Handler;
        import android.util.Log;
        import android.widget.Toast;

        import com.karumi.dexter.Dexter;
        import com.karumi.dexter.MultiplePermissionsReport;
        import com.karumi.dexter.PermissionToken;
        import com.karumi.dexter.listener.PermissionRequest;
        import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

        import java.io.File;
        import java.util.ArrayList;
        import java.util.List;

public class addActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    StoryAdapter storyAdapter;
    File[] files;
    ArrayList<Object> filesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initViews();
        setUpRefreshLayout();


    }

    private void initViews(){
        recyclerView = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipeRecyclerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(true);
                setUpRefreshLayout();

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(addActivity.this, "Refresh!", Toast.LENGTH_SHORT).show();

                    }
                },2000);


            }
        });


    }

    private void setUpRefreshLayout(){

        filesList.clear();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        storyAdapter = new StoryAdapter(addActivity.this,getData());
        recyclerView.setAdapter(storyAdapter);
        storyAdapter.notifyDataSetChanged();

    }

    private ArrayList<Object> getData(){

        StoryModel f;
        //String targetPath = Environment.getExternalStorageDirectory().toString() + Constant.FOLDER_NAME + "Media/.Statuses";
        if (Environment.getExternalStorageState() != null) {
            String targetPath = Environment.getExternalStorageDirectory().toString() + Constant.FOLDER_NAME + "Media/.Statuses";
            Log.d("Dexter",targetPath);


            File targetDirector = new File(targetPath);
            if (targetDirector.exists()) {
                files = targetDirector.listFiles();
                if (files.length != 0) {
                    for (int i = 0; i < files.length; i++) {
                        File file = files[i];
                        f = new StoryModel();
                        f.setUri(Uri.fromFile(file));
                        f.setPath(files[i].getAbsolutePath());
                        f.setFilename(file.getName());

                        if (!f.getUri().toString().endsWith(".nomedia")) {

                            filesList.add(f);

                        }
                    }
                }
            }
        }
        else
        {
            Toast.makeText(this, "no file exist", Toast.LENGTH_SHORT).show();
        }
        return filesList;
    }


}