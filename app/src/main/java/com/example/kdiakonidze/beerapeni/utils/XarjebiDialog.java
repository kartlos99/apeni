package com.example.kdiakonidze.beerapeni.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.kdiakonidze.beerapeni.R;

public class XarjebiDialog extends AppCompatDialogFragment {

    private EditText eAmount, eComment;
    private xarjListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.xarjebi_dialog, null);

        builder.setView(view)
                .setTitle(getString(R.string.add_xarji))
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String comment = eComment.getText().toString();
                        float amount = Float.valueOf(eAmount.getText().toString());

                        listener.applayChanges(comment, amount);
                    }
                });

        eAmount = view.findViewById(R.id.e_xarj_amount);
        eComment = view.findViewById(R.id.e_xarj_comment);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (xarjListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "implementacia araa gaketebuli");
        }
    }

    public interface xarjListener {
        void applayChanges(String comment, Float amount);
    }
}
