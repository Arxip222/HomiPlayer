package homi.play.player.ui.home;


import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import homi.play.player.MainActivity;
import homi.play.player.R;
import homi.play.player.adapters.FilesAdapter;
import homi.play.player.models.FileModel;

import static homi.play.player.MainActivity.videos;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private FilesAdapter filesAdapter;
    TextView mass1;
    public static ArrayList<FileModel> fileInfoArrayList = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        MainActivity.zagolov.setText(getString(R.string.menu_folders));
        recyclerView = view.findViewById(R.id.VideoRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        mass1 = view.findViewById(R.id.mess1);
        recyclerView.setLayoutManager(linearLayoutManager);
        getFile();
        filesAdapter = new FilesAdapter(getContext(), null, fileInfoArrayList, 1, -1);
        recyclerView.setAdapter(filesAdapter);

        return view;
    }


    private void getFile() {
        fileInfoArrayList.clear();
        for (int i = 0; i < videos.size(); i++) {
            if(getBool(videos.get(i).getFileName())){
                FileModel fileModel = new FileModel();
                fileModel.setCountVid(1);
                fileModel.setName(videos.get(i).getFileName());
                fileInfoArrayList.add(fileModel);
            }
            else{
                for (int j = 0; j < fileInfoArrayList.size() ; j++) {
                    if(fileInfoArrayList.get(j).getName().equals(videos.get(i).getFileName())) {
                        int count = fileInfoArrayList.get(j).getCountVid();
                        fileInfoArrayList.get(j).setCountVid(++count);
                    }
                }
            }
        }


        filesAdapter = new FilesAdapter(getContext(),null, fileInfoArrayList, 1, -1);
        recyclerView.setAdapter(filesAdapter);
        if(fileInfoArrayList.size()==0){
            mass1.setVisibility(View.VISIBLE);
        }
        else{
            mass1.setVisibility(View.INVISIBLE);
        }
    }

    private boolean getBool(String fileName) {
        boolean bool = true;
        if (fileInfoArrayList != null && fileInfoArrayList.size() > 0) {
            for (int j = 0; j < fileInfoArrayList.size(); j++) {
                if (fileInfoArrayList.get(j).getName().equals(fileName)) {
                    bool = false;
                }
            }
        }
        else{
            return true;
        }
        return bool;
    }
}