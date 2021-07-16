package homi.play.player.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import homi.play.player.R;
import homi.play.player.VideoActivity;
import homi.play.player.VideoListActivity;
import homi.play.player.models.FileModel;
import homi.play.player.models.VideoModel;

import static homi.play.player.ImageHelper.getRoundedCornerBitmap;


public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.FileHolder> {

    private static final int video = 0;
    private static final int file = 1;
    private Context cont;
    private List<VideoModel> infoList;
    private List<FileModel> fileList;
    public int type, t;

    public FilesAdapter(Context cont, List<VideoModel> infoList, List<FileModel> fileList, int type, int t) {
        this.cont = cont;
        this.infoList = infoList;
        this.type = type;
        this.fileList = fileList;
        this.t = t;
    }

    @NonNull
    @Override
    public FileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == file) {
            View view = LayoutInflater.from(cont).inflate(R.layout.raw, parent, false);
            return new FileHolder(view);
        }
        else{
            View view = LayoutInflater.from(cont).inflate(R.layout.video, parent, false);
            return new FileHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final FileHolder holder, final int position) {


        if(getItemViewType(position) == file) {

            String fileName = fileList.get(position).getName();
            long VideoCount = fileList.get(position).getCountVid();
            holder.fileName.setText(fileName);
            holder.VideoCount.setText(VideoCount + " " + cont.getResources().getString(R.string.video));

            holder.IntentLin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(cont, VideoListActivity.class);
                    intent.putExtra("position", holder.getAdapterPosition());
                    intent.putExtra("name", fileList.get(position).getName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    cont.startActivity(intent);
                }
            });

        }

        else{
            Bitmap bitmap = BitmapFactory.decodeFile(infoList.get(position).getThumb());
            Glide.with(cont)
                    .load(infoList.get(position).getThumb())
                    .skipMemoryCache(false)
                    .centerCrop()
                    .into(holder.background);
            holder.name.setText(infoList.get(position).getName());
            holder.TimeCount.setText(infoList.get(position).getDuration());
            String type = infoList.get(position).getType();
            type = type.replace("video/","");
            holder.type.setText(type);

            holder.lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(cont, VideoActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("t", t);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    cont.startActivity(intent);
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        try {
            return infoList.size();
        }catch (Exception e){
            return fileList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(type==file){
            return file;
        }
        else{
            return video;
        }
    }


    static class FileHolder extends RecyclerView.ViewHolder{

        TextView fileName, VideoCount;
        LinearLayout IntentLin;
        TextView name, TimeCount, type;
        ImageView background;
        LinearLayout lin;

        FileHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.name);
            VideoCount = itemView.findViewById(R.id.count);
            IntentLin = itemView.findViewById(R.id.intentLin);
            lin = itemView.findViewById(R.id.lin);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            TimeCount = itemView.findViewById(R.id.TimeCount);
            background = itemView.findViewById(R.id.background);
        }
    }
}

