package com.chengte99.itstar19.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.chengte99.itstar19.R;

public class Fragment2 extends Fragment {

    private static final String TAG = Fragment2.class.getSimpleName();
    private static Fragment2 instance;
    private Button btnTo01;

    public static Fragment2 getInstance() {
        if (instance == null) {
            instance = new Fragment2();
        }
        return instance;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View rootView = getLayoutInflater().inflate(R.layout.fragment_2, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
        btnTo01 = getView().findViewById(R.id.btn_to_01);
        btnTo01.setText("Change to Fragment1");
        btnTo01.setOnClickListener(btnChangeFragment_listener);
    }

    Button.OnClickListener btnChangeFragment_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            if (!Fragment1.getInstance().isAdded()) {
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
//                        .remove(Fragment2.getInstance())
                        .hide(Fragment2.getInstance())
                        .add(R.id.container, Fragment1.getInstance(), Fragment2.class.getSimpleName())
                        .addToBackStack(null)
                        .commit();
            }else {
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .hide(Fragment2.getInstance())
//                        .remove(Fragment2.getInstance())
                        .show(Fragment1.getInstance())
                        .addToBackStack(null)
                        .commit();
            }
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        }else {

        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: ");
        super.onDetach();
    }
}
