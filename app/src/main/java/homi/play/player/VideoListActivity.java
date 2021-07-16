package homi.play.player;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import homi.play.player.adapters.FilesAdapter;
import homi.play.player.models.VideoModel;
import homi.play.player.ui.home.HomeFragment;
import static homi.play.player.MainActivity.videos;


public class VideoListActivity extends AppCompatActivity {
    int position = 0;

    RecyclerView RV;
    FilesAdapter VideoAdapter;
    ImageButton back;
    TextView foldName;
    ImageButton imageButton;
    public static ArrayList<VideoModel> file = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        getSupportActionBar().hide();
        file.clear();
        RV = findViewById(R.id.RV);
        back = findViewById(R.id.back);
        foldName = findViewById(R.id.foldName);
        imageButton = findViewById(R.id.imageButton);
        position = getIntent().getIntExtra("position", 0);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        RV.setLayoutManager(manager);
        RV.setHasFixedSize(true);
        getVideos();
        VideoAdapter = new FilesAdapter(getApplicationContext(), file, null, 0, 1);
        RV.setAdapter(VideoAdapter);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoListActivity.this, SearchActivity.class);
                intent.putExtra("pos", 1);
                startActivity(intent);
            }
        });

        foldName.setText(HomeFragment.fileInfoArrayList.get(position).getName());


    }

    public void getVideos() {
        String name = getIntent().getStringExtra("name");
        for (int i = 0; i < videos.size(); i++) {
            if(videos.get(i).getFileName().equals(name)){
                file.add(videos.get(i));
            }
        }
    }

}
