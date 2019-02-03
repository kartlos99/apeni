package com.example.kdiakonidze.beerapeni.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.kdiakonidze.beerapeni.R;

/**
 * Created by k.diakonidze on 19.06.2018.
 */

public class ChooseColorDialog extends AppCompatDialogFragment {

    private ImageView imgColor;
    private SeekBar seekBar_R, seekBar_G, seekBar_B;
    int posR = 0, posG = 0, posB = 0;

    ColorDialogListener listener;

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            switch (seekBar.getId()) {
                case R.id.sbar_r:
                    posR = i;
                    break;
                case R.id.sbar_g:
                    posG = i;
                    break;
                case R.id.sbar_b:
                    posB = i;
                    break;
            }

            setColorToView();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    void setColorToView() {
        imgColor.setBackgroundColor(Color.rgb(posR, posG, posB));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewDlg = inflater.inflate(R.layout.choose_color, null);

        imgColor = viewDlg.findViewById(R.id.img_show_color);
        seekBar_R = viewDlg.findViewById(R.id.sbar_r);
        seekBar_G = viewDlg.findViewById(R.id.sbar_g);
        seekBar_B = viewDlg.findViewById(R.id.sbar_b);

        seekBar_R.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBar_G.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBar_B.setOnSeekBarChangeListener(seekBarChangeListener);

        int initialColor = Integer.valueOf(getTag());
        posR = Color.red(initialColor);
        posG = Color.green(initialColor);
        posB = Color.blue(initialColor);
        seekBar_R.setProgress(posR);
        seekBar_G.setProgress(posG);
        seekBar_B.setProgress(posB);
        setColorToView();

        builder.setView(viewDlg)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        listener.myColor(Color.rgb(posR, posG, posB));
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ColorDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement ColorDialogListener interface");
        }
    }

    public interface ColorDialogListener {
        void myColor(int color);
    }
}