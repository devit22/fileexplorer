package com.example.filemanagerapp.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanagerapp.FileOpener;
import com.example.filemanagerapp.FileRecyclerAdapter;
import com.example.filemanagerapp.OnFileSelectedListener;
import com.example.filemanagerapp.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class InternFagment extends Fragment implements OnFileSelectedListener {

private RecyclerView recyclerView;
private List<File> fileList;
private ImageView imageView;
private TextView tv_pathholder;
String data;
private File storage;
FileRecyclerAdapter fileRecyclerAdapter;
String[] items = {"Details","Rename","Delete","Share"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intern_fagment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        connectxml(view);
String internalStorage = System.getenv("EXTERNAL_STORAGE");
storage = new File(internalStorage);

try {
  data = getArguments().getString("path");
  File file = new File(data);
  storage = file;
}catch(Exception e){
    e.printStackTrace();
}
tv_pathholder.setText(storage.getAbsolutePath());
        displayfiels();
    }


    public ArrayList<File> findfiles(File file){
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();
        for(File singlfiel: files){
            if(singlfiel.isDirectory() && !singlfiel.isHidden()){
                arrayList.add(singlfiel);
            }
        }

        for(File singleFile: files){
            if(singleFile.getName().toLowerCase().endsWith(".jpeg")  ||
                    singleFile.getName().toLowerCase().endsWith(".jpg") ||
                    singleFile.getName().toLowerCase().endsWith(".png") ||
                    singleFile.getName().toLowerCase().endsWith(".mp3")  ||
                    singleFile.getName().toLowerCase().endsWith(".wav") ||
                    singleFile.getName().toLowerCase().endsWith(".mp4") ||
                    singleFile.getName().toLowerCase().endsWith(".pdf") ||
                    singleFile.getName().toLowerCase().endsWith(".doc") ||
                    singleFile.getName().toLowerCase().endsWith(".apk")

            ){

                arrayList.add(singleFile);
            }
        }
        return  arrayList;
    }
    private void displayfiels() {
recyclerView.setHasFixedSize(true);
recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
fileList = new ArrayList<>();
fileList.addAll(findfiles(storage));
fileRecyclerAdapter = new FileRecyclerAdapter(getContext(), (ArrayList<File>) fileList,this);
recyclerView.setAdapter(fileRecyclerAdapter);
    }

    private void connectxml(View view) {
        recyclerView = view.findViewById(R.id.recycelr_view);
        tv_pathholder = view.findViewById(R.id.tv_pathHolder);
        imageView = view.findViewById(R.id.image_back);
    }

    @Override
    public void onFileClicked(File file) {
        if(file.isDirectory()){
            Bundle bundle = new Bundle();
            bundle.putString("path",file.getAbsolutePath());
            InternFagment internFagment = new InternFagment();
            internFagment.setArguments(bundle);
            getParentFragmentManager().beginTransaction().replace(R.id.fragement_container,internFagment).addToBackStack(null).commit();
        }else{
            try {
                FileOpener.openfile(getContext(),file);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFileLongClicked(File file) {

    }
}