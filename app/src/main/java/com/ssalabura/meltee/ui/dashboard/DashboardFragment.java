package com.ssalabura.meltee.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssalabura.meltee.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        List<PhotoCard> photoCardList = getPhotoCards();
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        if(photoCardList.size() > 0) {
            root.findViewById(R.id.textView_empty).setVisibility(View.INVISIBLE);
        }
        recyclerView.setAdapter(new RecyclerViewAdapter(getContext(), photoCardList));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        return root;
    }

    private List<PhotoCard> getPhotoCards() {
        List<PhotoCard> photoCards = new ArrayList<>();
        for(File f : getContext().getExternalMediaDirs()[0].listFiles()) {
            photoCards.add(new PhotoCard(f.getAbsolutePath(), f.getName()));
        }
        return photoCards;
    }
}