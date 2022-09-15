package com.mim.apollo.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.mim.apollo.R;
import com.mim.apollo.ui.home.HomeFragment;

import org.xmlpull.v1.XmlPullParser;

public class CustomDialog extends DialogFragment {

    private OnFragmentInteraction mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view =inflater.inflate(R.layout.dialog_input_layout, null);
        EditText input=view.findViewById(R.id.mensajito);
        Button cancel=view.findViewById(R.id.cancel_dlg);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CustomDialog.this!=null)
                CustomDialog.this.dismiss();
            }
        });

        Button accept=view.findViewById(R.id.send_dlg);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener!=null){
                    if(input.getText().length()>0) {
                        mListener.sendMensaje(input.getText().toString());
                        if(CustomDialog.this!=null) {
                            CustomDialog.this.dismiss();
                        }
                    }
                }
            }
        });


        builder.setView(view);


        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragment.OnFragmentInteraction) {
            mListener = (CustomDialog.OnFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteraction{
        public void sendMensaje(String content);

    }
}
