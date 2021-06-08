package com.ssalabura.meltee.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ssalabura.meltee.R;
import com.ssalabura.meltee.database.MelteeRealm;
import com.ssalabura.meltee.database.PhotoCard;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    View root;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);

        swipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Thread(this::refreshPhotoCards).start();
        });

        MelteeRealm.startListener(getContext());

        recyclerView.setAdapter(new PhotoCardViewAdapter(getContext(), new ArrayList<>()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        new Thread(this::refreshPhotoCards).start();
        return root;
    }

    private void refreshPhotoCards() {
        List<PhotoCard> photoCardList = MelteeRealm.getPhotos();
        TextView empty = root.findViewById(R.id.textView_empty);
        if(photoCardList.size() > 0) {
            empty.setVisibility(View.INVISIBLE);
        } else {
            empty.setVisibility(View.VISIBLE);
        }
        getActivity().runOnUiThread(() -> {
            recyclerView.setAdapter(new PhotoCardViewAdapter(getContext(), photoCardList));
            swipeRefreshLayout.setRefreshing(false);
        });
    }
}