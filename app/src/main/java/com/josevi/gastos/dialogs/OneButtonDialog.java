package com.josevi.gastos.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.josevi.gastos.R;

public class OneButtonDialog extends Dialog {

    private TextView messageView;
    private Button button;

    Activity activity;

    public OneButtonDialog(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_one_button);
        messageView = findViewById(R.id.dialog_one_button_message);
        button = findViewById(R.id.dialog_one_button_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setMessage(String message){
        messageView.setText(message);
    }

    public void setButton(String buttonText, View.OnClickListener onClickListener){
        button.setText(buttonText);
        button.setOnClickListener(onClickListener);
    }

    public void setButtonColor(int color){
        button.setBackgroundColor(color);
    }

    public void setButtonListener(View.OnClickListener onClickListener){
        button.setOnClickListener(onClickListener);
    }
}
