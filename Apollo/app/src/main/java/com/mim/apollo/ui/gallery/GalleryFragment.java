package com.mim.apollo.ui.gallery;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mim.apollo.databinding.FragmentGalleryBinding;
import com.mim.apollo.ui.home.HomeFragment;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private Handler handler = new Handler();
    private OnFragmentInteraction mListener;
    private boolean should=true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button btn=binding.button;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                should=false;
            }
        });

        final TextView textView = binding.labelContador;
        //galleryViewModel.getCounter().observe(getViewLifecycleOwner(), textView::setText);

        new Thread(new Runnable() {
            @Override
            public void run() {

                int counter = 6;
                while (counter > 0&&should) {
                    counter--;
                    int finalCounter = counter;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            galleryViewModel.setCounter(finalCounter);
                            textView.setText(String.valueOf(finalCounter));
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                      if(should) {
                          if (mListener != null) {

                              mListener.gotoActivatedAlarm();
                          }
                      }else{
                          if (mListener != null) {

                              mListener.startPage();
                          }
                      }
                    }
                });
            }
        }).start();

        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof GalleryFragment.OnFragmentInteraction) {
            mListener = (GalleryFragment.OnFragmentInteraction) context;
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface OnFragmentInteraction {
         public void  gotoActivatedAlarm();

        public void startPage();

    }
}