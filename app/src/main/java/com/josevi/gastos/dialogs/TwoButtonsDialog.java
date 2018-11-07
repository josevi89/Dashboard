package com.josevi.gastos.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.josevi.gastos.R;

public class TwoButtonsDialog extends Dialog {

    private TextView messageView;
    private Button leftButton, rightButton;

    Activity activity;

    public TwoButtonsDialog(@NonNull Activity activity, int color) {
        super(activity);
        this.activity = activity;
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        switch (color) {
            case R.color.blue_app:
                setContentView(R.layout.dialog_two_buttons_blue);
                break;
            case R.color.red_app:
                setContentView(R.layout.dialog_two_buttons_red);
                break;
            default:
                setContentView(R.layout.dialog_two_buttons_blue);
                break;

        }
        messageView = findViewById(R.id.dialog_two_buttons_message);
        leftButton = findViewById(R.id.dialog_two_buttons_left_button);
        rightButton = findViewById(R.id.dialog_two_buttons_right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setMessage(String message){
        messageView.setText(message);
    }

    public void setLeftButton(String buttonText, View.OnClickListener onClickListener){
        leftButton.setText(buttonText);
        leftButton.setOnClickListener(onClickListener);
    }

    public void setLeftButtonListener(View.OnClickListener onClickListener){
        leftButton.setOnClickListener(onClickListener);
    }

    public void setRightButton(String buttonText, View.OnClickListener onClickListener){
        rightButton.setText(buttonText);
        rightButton.setOnClickListener(onClickListener);
    }

    public void setRightButtonListener(View.OnClickListener onClickListener){
        rightButton.setOnClickListener(onClickListener);
    }


}
