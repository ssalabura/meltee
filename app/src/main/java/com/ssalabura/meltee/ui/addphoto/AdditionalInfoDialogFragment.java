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

public class AdditionalInfoDialogFragment extends DialogFragment {

    public interface AdditionalInfoDialogListener {
        void onDialogPositiveClick(String message);
    }

    AdditionalInfoDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AdditionalInfoDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString()
                    + " must implement AdditionalInfoDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_additional_info, null);
        ((EditText)view.findViewById(R.id.dialog_message)).setText(getArguments().getString("message"));
        builder.setView(view)
                .setPositiveButton("OK", (dialog, id) -> {
                    listener.onDialogPositiveClick(((EditText)view.findViewById(R.id.dialog_message)).getText().toString());
                });

        return builder.create();
    }
}
