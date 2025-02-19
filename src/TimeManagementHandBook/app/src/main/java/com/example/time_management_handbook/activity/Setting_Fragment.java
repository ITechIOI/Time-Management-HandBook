package com.example.time_management_handbook.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.time_management_handbook.R;
import com.example.time_management_handbook.adapter.RatingDAO;
import com.example.time_management_handbook.model.MyForegroundService;
import com.example.time_management_handbook.model.RatingDTO;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Setting_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Setting_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private float rate;
    private static String checkNotificationStatusText = "On";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Setting_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Setting_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Setting_Fragment newInstance(String param1, String param2) {
        Setting_Fragment fragment = new Setting_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_, container, false);
        TextView checkNotificationStatus = view.findViewById(R.id.checkNotificationStatus);
        RelativeLayout notificationLayout = view.findViewById(R.id.notification_layout);
        RelativeLayout ratingLayout = view.findViewById(R.id.ratingLayout);
        ImageButton nextToRatingBtn = view.findViewById(R.id.nextToRatingBtn);
        ImageButton about = view.findViewById(R.id.about);

        checkNotificationStatus.setText(checkNotificationStatusText);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mit= new Intent(getActivity(), AboutZEIT_Activity.class);
                startActivity(mit);
            }
        });

        notificationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("On".equals(checkNotificationStatus.getText())) {
                    checkNotificationStatusText = "Off";
                    checkNotificationStatus.setText(checkNotificationStatusText);
                    stopNotifications();
                } else {
                    checkNotificationStatusText = "On";
                    checkNotificationStatus.setText(checkNotificationStatusText);
                    restartNotifications();
                }
            }
        });

        ratingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog ratingDialog = new Dialog(getActivity());
                RatingBar ratingBar;
                Button buttonOk, buttonCancel;
                ratingDialog.setContentView(R.layout.rating_dialog);
                ratingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT );
                ratingDialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.custom_itemdialog));
                ratingBar = ratingDialog.findViewById(R.id.ratingbar);
                buttonCancel = ratingDialog.findViewById(R.id.ratingCancel_button);

                Log.d("Click image button", "yeah");
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ratingDialog.dismiss();
                    }
                });
                buttonOk = ratingDialog.findViewById(R.id.ratingSend_button);
                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rate = ratingBar.getRating();
                        int rowEffectInsertNewRating = RatingDAO.getInstance().InsertNewRating( (int)rate);
                        if (rowEffectInsertNewRating > 0) {
                            Toast.makeText(getContext(), "Great feedback!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Feedback: ", "failed");
                        }
                        ratingDialog.dismiss();
                    }
                });
                ratingDialog.show();
            }
        });

        nextToRatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog ratingDialog = new Dialog(getActivity());
                RatingBar ratingBar;
                Button buttonOk, buttonCancel;
                ratingDialog.setContentView(R.layout.rating_dialog);
                ratingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT );
                ratingDialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.custom_itemdialog));
                ratingBar = ratingDialog.findViewById(R.id.ratingbar);
                buttonCancel = ratingDialog.findViewById(R.id.ratingCancel_button);

                Log.d("Click image button", "yeah");
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ratingDialog.dismiss();
                    }
                });
                buttonOk = ratingDialog.findViewById(R.id.ratingSend_button);
                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rate = ratingBar.getRating();
                        int rowEffectInsertNewRating = RatingDAO.getInstance().InsertNewRating( (int)rate);
                        if (rowEffectInsertNewRating > 0) {
                            Toast.makeText(getContext(), "Great feedback!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Feedback: ", "failed");
                        }
                        ratingDialog.dismiss();
                    }
                });
                ratingDialog.show();
            }
        });

        return view;
    }

    // Turn off receiving notification feature
    public void stopNotifications() {
        Intent intent = new Intent("stop_service");
        getActivity().sendBroadcast(intent);
    }

    // Turn on receiving notification feature
    public void restartNotifications() {
        Intent intent = new Intent(getActivity(), MyForegroundService.class);
        getActivity().startService(intent);
    }
}