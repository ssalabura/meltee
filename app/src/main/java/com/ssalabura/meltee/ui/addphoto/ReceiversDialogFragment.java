package com.ssalabura.meltee.ui.addphoto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssalabura.meltee.R;
import com.ssalabura.meltee.database.MelteeRealm;

import java.util.List;

public class ReceiversDialogFragment extends DialogFragment {
    public interface ReceiversDialogListener {
        void onDialogPositiveClick(List<String> receivers);
    }

    ReceiversDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ReceiversDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString()
                    + " must implement ReceiversDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_receivers, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        ClickableFriendViewAdapter adapter = new ClickableFriendViewAdapter(getContext(), MelteeRealm.getFriends());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        builder.setView(view)
                .setTitle(getString(R.string.dialog_receiver))
                .setPositiveButton("OK", (dialog, id) -> {
                    listener.onDialogPositiveClick(adapter.getSelected());
                });

        return builder.create();
    }
}
