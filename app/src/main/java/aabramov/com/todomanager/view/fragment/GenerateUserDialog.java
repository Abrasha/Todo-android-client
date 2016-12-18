package aabramov.com.todomanager.view.fragment;

import aabramov.com.todomanager.R;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Andrii Abramov on 12/18/16.
 */

public class GenerateUserDialog extends DialogFragment {

    @BindView(R.id.etUsersCount)
    EditText etUsersCount;

    private OnUsersGeneratedListener onDismissListener;
    private boolean usersShouldBeGenerated = false;
    private int usersCount = 0;

    public interface OnUsersGeneratedListener {
        void onUsersGenerated(int count);
    }

    public static GenerateUserDialog newInstance(OnUsersGeneratedListener listener) {
        GenerateUserDialog result = new GenerateUserDialog();
        result.onDismissListener = listener;
        return result;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = initView();

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Generate users")
                .setPositiveButton("Generate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        usersShouldBeGenerated = true;
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        usersShouldBeGenerated = false;
                    }
                })
                .create();
    }

    private View initView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_generate_users, null);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null && usersShouldBeGenerated) {
            int parseUsersCount = parseUsersCount();
            onDismissListener.onUsersGenerated(parseUsersCount);
        }
    }

    private int parseUsersCount() {
        return Integer.parseInt(etUsersCount.getText().toString());
    }


}
