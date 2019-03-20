package com.example.quartermaster;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsDialogFragment extends DialogFragment {

    public interface SettingsDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    SettingsDialogListener listener;
    String cityID;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (SettingsDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_settings_dialog, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle("Settings");
        builder.setView(view);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText cityCodeBox = (EditText)view.findViewById(R.id.editText);
                cityID = cityCodeBox.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("cityID", cityID);
                SettingsDialogFragment.this.setArguments(bundle);
                listener.onDialogPositiveClick(SettingsDialogFragment.this);
            }
        });
        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity().getApplicationContext(), "Changes Discarded", Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }
}