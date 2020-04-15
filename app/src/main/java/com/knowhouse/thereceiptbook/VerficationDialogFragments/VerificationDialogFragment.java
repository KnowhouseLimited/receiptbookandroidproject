package com.knowhouse.thereceiptbook.VerficationDialogFragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.knowhouse.thereceiptbook.R;

import java.util.Objects;

public class VerificationDialogFragment extends DialogFragment{

    private EditText verificationInput;

    public VerificationDialogFragment() {
    }

    public interface VerificationDialogListener{
        void onFinishedVerificationDialog(String inputText);
    }


    public static VerificationDialogFragment newInstance (String title){
        VerificationDialogFragment fragment = new VerificationDialogFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //Create dialog
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity(),R.style.AppDialogTheme);
        View verificationDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_verification,null);

        builder.setView(verificationDialogView);
        builder.setTitle(R.string.verfication_dialog_title);

        verificationInput = verificationDialogView.findViewById(R.id.verification_edit_text);

        //add the verified button
        builder.setPositiveButton("Ok",
                (dialog, which) -> {
                    VerificationDialogListener listener = (VerificationDialogListener) getActivity();
                    assert listener != null;
                    listener.onFinishedVerificationDialog(Objects.requireNonNull(verificationInput.getText()).toString());
                    dismiss();
                });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            if(dialog != null && Objects.requireNonNull(getDialog()).isShowing()){
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

}
