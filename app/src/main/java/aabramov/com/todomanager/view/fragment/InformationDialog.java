package aabramov.com.todomanager.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

/**
 * @author Andrii Abramov on 12/3/16.
 */
public class InformationDialog extends DialogFragment {

    private static final String TAG = InformationDialog.class.getName();

    private String title;
    private String message;

    public static void show(String title, String message, FragmentManager manager) {
        InformationDialog dialog = new InformationDialog();
        dialog.message = message;
        dialog.title = title;
        dialog.show(manager, TAG);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
    }
}
