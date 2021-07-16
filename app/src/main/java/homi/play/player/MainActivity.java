package homi.play.player;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import homi.play.player.models.VideoModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static List<VideoModel> videos = new ArrayList<>();
    private boolean permission;
    private static int REQUEST_PERMISSION = 1;
    public static TextView zagolov;
    Menu m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        zagolov = findViewById(R.id.Zagolovok);
        zagolov.setText(getResources().getString(R.string.menu_folders));
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_folders, R.id.nav_video, R.id.nav_info)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        permissionForVideo();

    }


    private void getVideos(){
        if(videos.size() == 0) {
            final int name, duration, data, id, thum, fileName, type;
            String path = null;
            Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            final Cursor videoCursor = getApplicationContext().getContentResolver().query(videoUri, null, null, null, null);
            if (videoCursor != null && videoCursor.moveToFirst()) {
                name = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                fileName = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
                duration = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
                type = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE);
                data = videoCursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                id = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                thum = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);

                do {
                    path = videoCursor.getColumnName(data);

                    VideoModel videoModel = new VideoModel();
                    videoModel.setPath(path);
                    videoModel.setThumb(videoCursor.getString(thum));
                    videoModel.setDuration(getDuration(videoCursor.getString(duration)));
                    videoModel.setName(videoCursor.getString(name));
                    videoModel.setFileName(videoCursor.getString(fileName));
                    videoModel.setType(videoCursor.getString(type));

                    videos.add(videoModel);


                } while (videoCursor.moveToNext());
            }
        }
    }

    public String getDuration(String videoFile) {
        String min_result = "";
        String sec_result = "";
        String hrs_result = "";
        try {
            int duration = Integer.parseInt(videoFile);
            int sec = (duration / 1000) % 60;
            if(sec < 10){
                sec_result = "0"+String.valueOf(sec);
            }
            else{
                sec_result = String.valueOf(sec);
            }
            int min = (duration / (1000 * 60)) % 60;
            if(min < 10){
                min_result = "0"+String.valueOf(min);
            }
            else{
                min_result = String.valueOf(min);
            }
            int hrs = duration / (1000 * 60 * 60);
            hrs_result = String.valueOf(hrs);
            if(hrs < 10){
                return "0"+hrs+":"+min_result+":"+sec_result;
            }
            else{
                return hrs_result+":"+min_result+":"+sec_result;
            }
        }catch (Exception e){
            return "0";
        }
    }


    private void permissionForVideo() {
        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION);
            }
        } else {
            permission = true;
            getVideos();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                permission = true;
                recreate();
                getVideos();

            } else {
                Toast.makeText(this, ""+getResources().getString(R.string.permiss), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        m = menu;
        loadLocale();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.Language:
                showLanguageDialog();
                break;

            case R.id.search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showLanguageDialog() {
            String[] listItems = {getString(R.string.rus), getString(R.string.angl)};

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle(getString(R.string.lang));
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    setLocate("ru");
                    recreate();
                }
                if(which == 1){
                    setLocate("en");
                    recreate();
                }
                dialog.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocate(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_lang", lang);
        editor.apply();
        if(lang.equals("en")){
            m.findItem(R.id.Language).setTitle("EN ");
        }
        else if(lang.equals("ru")){
            m.findItem(R.id.Language).setTitle("RU ");
        }
    }
    public String getLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang", "");
        return language;
    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang", "");

        if(language.equals("")){
            language = String.valueOf(Locale.getDefault());
            language = language.substring(0,2);
        }
        setLocate(language);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
