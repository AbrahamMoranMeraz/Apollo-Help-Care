package com.mim.apollo.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mim.apollo.databinding.FragmentHomeBinding;
import com.mim.apollo.ui.slideshow.SlideshowFragment;
import com.mim.apollo.util.CustomDialog;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private OnFragmentInteraction mListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        final Button buttonHigh = binding.buttonHospital;
        buttonHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               if (mListener != null) {
                    mListener.setTipoAlerta(1);
                    mListener.navigateCounter();
                }
               // CustomDialog dlg= new CustomDialog();
                //dlg.show(HomeFragment.this.getChildFragmentManager(),null);
            }
        });

        final Button buttonLow = binding.buttonContacts;
        buttonLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mListener != null) {
                    mListener.setTipoAlerta(2);
                    mListener.navigateCounter();
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteraction) {
            mListener = (OnFragmentInteraction) context;
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

    public interface OnFragmentInteraction {
        public void navigateCounter();
        public void setTipoAlerta(int alerta);
    }
}