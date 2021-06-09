package com.ssalabura.meltee.ui.friends;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ssalabura.meltee.R;
import com.ssalabura.meltee.database.MelteeRealm;
import com.ssalabura.meltee.util.BitmapTools;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsFragment extends Fragment implements NewFriendDialogFragment.NewFriendDialogListener {
    View root;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_friends, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);

        CircleImageView circleImageView = root.findViewById(R.id.profilePicture);
        Bitmap profilePicture = MelteeRealm.getProfilePicture();
        if(profilePicture != null) {
            circleImageView.setImageBitmap(profilePicture);
        }

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getData() == null) return;
                    Uri uri = result.getData().getData();
                    try {
                        Bitmap bitmap = BitmapTools.toProfilePicture(
                                ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContext().getContentResolver(), uri)));
                        circleImageView.setImageBitmap(bitmap);
                        new Thread(() -> MelteeRealm.updateProfile(bitmap)).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        circleImageView.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            activityResultLauncher.launch(gallery);
        });

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
        if(username == null ||
                username.contains("|") ||
                username.trim().length() <= 3 ||
                username.trim().length() > 20) {
            Toast.makeText(getContext(), R.string.invalid_username, Toast.LENGTH_SHORT).show();
        } else {
            MelteeRealm.insertFriend(username);
            recyclerView.setAdapter(new FriendViewAdapter(getContext(), this, MelteeRealm.getFriends()));
        }
    }
}