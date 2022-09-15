package com.mim.apollo.ui.slideshow;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mim.apollo.databinding.FragmentSlideshowBinding;
import com.mim.apollo.service.AlertaAPI;
import com.mim.apollo.ui.home.HomeFragment;
import com.mim.apollo.ui.home.HomeViewModel;
import com.mim.apollo.util.CustomDialog;
import com.mim.apollo.util.FileDB;

import java.io.IOException;

import retrofit2.Response;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private OnFragmentInteraction mListener;
    private Handler handle = new Handler();
    private boolean control = true;
    private String data = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button btnMsg = binding.msgBtn;
        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog dlg = new CustomDialog();
                dlg.show(SlideshowFragment.this.getChildFragmentManager(), null);
            }
        });

        Button btnCall= binding.callBtn;;
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener!=null){
                    mListener.makePhoneCall();
                }
            }
        });

        TextView holder=binding.holder;
        holder.setText(mListener.retrieveSecret());


        TextView textView = binding.fieldEstatus;
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (control) {
                    if (mListener != null) {
                        data = mListener.retrieveSecret();
                        if (data == null) {
                            control = false;
                            Log.d("myapp","1");
                            return;
                        }

                        if (data != null) {
                            try {
                                Response<String> res = AlertaAPI.Factory.getInstance().retrieveStatus(data).execute();
                                if (res.isSuccessful()) {
                                    handle.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("myapp", "leyendo...");
                                            textView.setText(res.body());
                                            if (res.body().equals("Close")) {
                                                control = false;
                                                if (SlideshowFragment.this.getContext() != null){
                                                    FileDB.writeToSettingsFile("", SlideshowFragment.this.getContext().getFilesDir().getAbsolutePath());
                                                   if(mListener!=null){
                                                       mListener.startPage();
                                                   }
                                                }
                                            }
                                        }
                                    });
                                }else{
                                    Log.d("myapp","error");
                                }
                            } catch (IOException e) {
                                Log.d("myapp","3");
                                e.printStackTrace();
                            }
                        }else{
                            Log.d("myapp","2");
                        }

                    } else {
                        Log.d("myapp","4");
                        control = false;
                    }

                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
        //homeViewModel.getTipoAlerta().observe(getViewLifecycleOwner(), textView::setText);
        //textView.setText(String.valueOf(mListener.getTipoAlerta()));

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
        if (context instanceof SlideshowFragment.OnFragmentInteraction) {
            mListener = (SlideshowFragment.OnFragmentInteraction) context;
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

        public int getTipoAlerta();

        public boolean sendAlert();

        public String retrieveSecret();
        public void startPage();
        public void makePhoneCall();
    }
}