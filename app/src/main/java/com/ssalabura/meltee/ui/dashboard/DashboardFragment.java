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
import com.ssalabura.meltee.ui.database.AppDatabase;
import com.ssalabura.meltee.ui.database.PhotoCard;
import com.ssalabura.meltee.ui.database.PhotoCardDao;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);

        swipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            //TODO: read from online database and write to local
            new Thread(() -> refreshPhotoCards(root)).start();
        });

        recyclerView.setAdapter(new RecyclerViewAdapter(getContext(), new ArrayList<>()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        new Thread(() -> refreshPhotoCards(root)).start();
        return root;
    }

    private void refreshPhotoCards(View root) {
        AppDatabase db = AppDatabase.getInstance(getContext());
        PhotoCardDao photoCardDao = db.photoCardDao();
        List<PhotoCard> photoCardList = photoCardDao.getAll();
        TextView empty = root.findViewById(R.id.textView_empty);
        if(photoCardList.size() > 0) {
            empty.setVisibility(View.INVISIBLE);
        } else {
            empty.setVisibility(View.VISIBLE);
        }
        getActivity().runOnUiThread(() -> {
            recyclerView.setAdapter(new RecyclerViewAdapter(getContext(), photoCardList));
            swipeRefreshLayout.setRefreshing(false);
        });
    }
}