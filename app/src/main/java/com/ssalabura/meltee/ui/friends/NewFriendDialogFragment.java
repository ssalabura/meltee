package com.ssalabura.meltee.ui.friends;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ssalabura.meltee.R;

public class NewFriendDialogFragment extends DialogFragment {
    public interface NewFriendDialogListener {
        void onDialogPositiveClick(String username);
    }

    NewFriendDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (NewFriendDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString()
                    + " must implement NewFriendDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_new_friend, null);
        builder.setView(view)
                .setTitle(getString(R.string.username))
                .setPositiveButton("OK", (dialog, id) -> {
                    listener.onDialogPositiveClick(((EditText)view.findViewById(R.id.dialog_new_friend_username)).getText().toString());
                });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }
}
