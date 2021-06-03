package com.ssalabura.meltee.ui.addphoto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ssalabura.meltee.R;

import java.util.ArrayList;

public class AdditionalInfoDialogFragment extends DialogFragment {

    public interface AdditionalInfoDialogListener {
        void onDialogPositiveClick(String message, String location);
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
        EditText view_message = view.findViewById(R.id.dialog_message);
        Spinner view_location = view.findViewById(R.id.dialog_location);

        view_message.setText(getArguments().getString("message"));
        ArrayList<String> locationList = getArguments().getStringArrayList("location_list");

        view_location.setAdapter(
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, locationList)
        );
        String savedLocation = getArguments().getString("location");
        for(int i=0; i<locationList.size(); i++) {
            if(locationList.get(i).equals(savedLocation)) view_location.setSelection(i);
        }

        builder.setView(view)
                .setTitle(getString(R.string.additional_info))
                .setPositiveButton("OK", (dialog, id) -> {
                    String message = view_message.getText().toString();
                    String location = view_location.getSelectedItem().toString();
                    if(location.equals(getString(R.string.hint_empty))) {
                        location = null;
                    }
                    listener.onDialogPositiveClick(message, location);
                });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }
}
