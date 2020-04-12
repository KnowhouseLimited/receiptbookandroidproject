package com.knowhouse.thereceiptbook.VerficationDialogFragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.knowhouse.thereceiptbook.R;

import java.util.Objects;

public class VerificationDialogFragment extends DialogFragment {

    private TextInputEditText verificationInput;
    private Button verifiedButton;
    public static String verifiedCode;


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //Create dialog
        AlertDialog.Builder builder =
                new AlertDialog.Builder(requireActivity());
        View verificationDialogView = requireActivity().getLayoutInflater().inflate(
                R.layout.fragment_verification,null);

        builder.setView(verificationDialogView);
        builder.setTitle(R.string.verfication_dialog_title);

        verificationInput = getActivity().findViewById(R.id.verification_edit_text);
        verifiedButton = getActivity().findViewById(R.id.verified_button);

        //add the verified button
        builder.setPositiveButton(R.string.verify,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        verifiedCode = verificationInput.getText().toString();
                    }
                });

        return builder.create();
    }
}
