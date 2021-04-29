package com.ssalabura.meltee.ui.addphoto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ssalabura.meltee.R;

import java.util.ArrayList;
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
        builder.setView(view)
                .setPositiveButton("OK", (dialog, id) -> {
                    List<String> receivers = new ArrayList<>();
                    receivers.add(((EditText)view.findViewById(R.id.dialog_receiver)).getText().toString());
                    receivers.add("test_hardcoded");
                    listener.onDialogPositiveClick(receivers);
                });

        return builder.create();
    }
}
