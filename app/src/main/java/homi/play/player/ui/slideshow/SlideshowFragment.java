package homi.play.player.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import homi.play.player.MainActivity;
import homi.play.player.R;
import homi.play.player.VideoActivity;


public class SlideshowFragment extends Fragment {

    EditText url;
    Button open;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        MainActivity.zagolov.setText(getString(R.string.menu_video));

        url = root.findViewById(R.id.url);
        open = root.findViewById(R.id.open);

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!url.getText().toString().equals("")) {
                    Intent intent = new Intent(getContext(), VideoActivity.class);
                    intent.putExtra("url", url.getText().toString());
                    startActivity(intent);
                }
            }
        });

        url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(url.getText().toString().equals("")){
                    open.setBackgroundResource(R.drawable.back3);
                }
                else{
                    open.setBackgroundResource(R.drawable.back2);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(url.getText().toString().equals("")){
                    open.setBackgroundResource(R.drawable.back3);
                }
                else{
                    open.setBackgroundResource(R.drawable.back2);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(url.getText().toString().equals("")){
                    open.setBackgroundResource(R.drawable.back3);
                }
                else{
                    open.setBackgroundResource(R.drawable.back2);
                }
            }
        });

        return root;
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
}
