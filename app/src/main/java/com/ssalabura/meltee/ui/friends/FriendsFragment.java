package com.ssalabura.meltee.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ssalabura.meltee.R;
import com.ssalabura.meltee.database.MelteeRealm;
import com.ssalabura.meltee.ui.addphoto.ReceiversDialogFragment;

public class FriendsFragment extends Fragment implements NewFriendDialogFragment.NewFriendDialogListener {
    View root;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_friends, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);

        FloatingActionButton floatingActionButton = root.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(v -> {
            DialogFragment dialogFragment = new NewFriendDialogFragment();
            dialogFragment.setTargetFragment(this, 22);
            dialogFragment.show(getParentFragmentManager(), "NewFriendDialog");
        });

        recyclerView.setAdapter(new FriendViewAdapter(getContext(), this, MelteeRealm.getFriends()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        return root;
    }

    public void removeFriend(String username) {
        MelteeRealm.removeFriend(username);
        recyclerView.setAdapter(new FriendViewAdapter(getContext(), this, MelteeRealm.getFriends()));
    }

    @Override
    public void onDialogPositiveClick(String username) {
        MelteeRealm.insertFriend(username);
        recyclerView.setAdapter(new FriendViewAdapter(getContext(), this, MelteeRealm.getFriends()));
    }
}