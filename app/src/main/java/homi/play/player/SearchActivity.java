package homi.play.player;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import homi.play.player.adapters.FilesAdapter;
import homi.play.player.models.VideoModel;

import static homi.play.player.MainActivity.videos;

public class SearchActivity extends AppCompatActivity {

    RecyclerView filesRV;
    EditText ETFiles;
    FilesAdapter adapter;
    ImageButton backB;
    public static List<VideoModel> UserF = new ArrayList<>();
    TextView mess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
        filesRV = findViewById(R.id.FilesRV);
        mess = findViewById(R.id.mess);
        backB = findViewById(R.id.backB);
        mess.setText(getResources().getString(R.string.f));
        filesRV.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        filesRV.setLayoutManager(manager);
        ETFiles = findViewById(R.id.search_files);

        ETFiles.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UserF.clear();
                String UsInput = s.toString().toLowerCase();
                if (s.length() < 3) {
                    UserF.clear();
                    mess.setText(getResources().getString(R.string.f));
                    mess.setVisibility(View.VISIBLE);
                    adapter = new FilesAdapter(getApplicationContext(), UserF, null, 0, 0);
                    filesRV.setAdapter(adapter);
                } else {
                    int i = 0;
                    for (VideoModel value : videos) {
                        try {
                            if (value.getName().toLowerCase().contains(UsInput)) {
                                mess.setVisibility(View.INVISIBLE);
                                UserF.add(value);
                                i++;
                            }
                        }catch (Exception e){

                        }
                    }
                    if(UserF.size() == 0){
                        mess.setText(getResources().getString(R.string.ff));
                        mess.setVisibility(View.VISIBLE);
                    }
                    adapter = new FilesAdapter(getApplicationContext(), UserF, null, 0, 0);
                    filesRV.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}

