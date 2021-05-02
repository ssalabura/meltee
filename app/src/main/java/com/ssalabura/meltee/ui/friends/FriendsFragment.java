package com.ssalabura.meltee.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssalabura.meltee.R;
import com.ssalabura.meltee.database.MelteeRealm;

public class FriendsFragment extends Fragment {
    View root;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_friends, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);

        recyclerView.setAdapter(new FriendViewAdapter(getContext(), this, MelteeRealm.getFriends()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        return root;
    }

    public void addFriend(String friend) {
        MelteeRealm.insertFriend(friend);
        recyclerView.setAdapter(new FriendViewAdapter(getContext(), this, MelteeRealm.getFriends()));
    }

    public void removeFriend(String friend) {
        MelteeRealm.removeFriend(friend);
        recyclerView.setAdapter(new FriendViewAdapter(getContext(), this, MelteeRealm.getFriends()));
    }
}