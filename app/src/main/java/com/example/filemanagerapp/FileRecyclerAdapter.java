package com.example.filemanagerapp;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class FileRecyclerAdapter extends RecyclerView.Adapter<FileRecyclerAdapter.MyViewHolder> {
 Context context;
 ArrayList<File>    filelist;
private OnFileSelectedListener onFileSelectedListener;
    public FileRecyclerAdapter(Context context, ArrayList<File> filelist,OnFileSelectedListener onFileSelectedListener) {
        this.context = context;
        this.filelist = filelist;
        this.onFileSelectedListener = onFileSelectedListener;
    }

    @NonNull
    @Override
    public FileRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerformate,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FileRecyclerAdapter.MyViewHolder holder, int position) {

        holder.tvName.setText(filelist.get(position).getName());
        holder.tvName.setSelected(true);
        int items = 0;
        if(filelist.get(position).isDirectory()){
            File[] files = filelist.get(position).listFiles();
            for(File singlefile: files){
                if(!singlefile.isHidden()){
                    items +=1;

                }
            }
            holder.tvSize.setText(String.valueOf(items)+" files");
        }else{
            holder.tvSize.setText(Formatter.formatShortFileSize(context,filelist.get(position).length()));
        }

//        if(filelist.get(position).getName().toLowerCase().endsWith(".jpeg")){
//            holder.foldericon.setImageResource(R.drawable.ic_launcher_background);
//        }else if(filelist.get(position).getName().toLowerCase().endsWith(".jpg"))
//        {
//
//        }

        holder.layoutcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFileSelectedListener.onFileClicked(filelist.get(position));

            }
        });

        holder.layoutcard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onFileSelectedListener.onFileLongClicked(filelist.get(position));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return filelist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvSize;
        ImageView foldericon;
        CardView layoutcard;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_filename);
            tvSize = itemView.findViewById(R.id.tv_filesize);
            foldericon = itemView.findViewById(R.id.folder_icon);
            layoutcard = itemView.findViewById(R.id.format_card);
        }
    }
}
