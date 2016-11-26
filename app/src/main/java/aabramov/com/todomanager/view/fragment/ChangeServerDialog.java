package aabramov.com.todomanager.view.fragment;

import aabramov.com.todomanager.R;
import aabramov.com.todomanager.TodoApplication;
import aabramov.com.todomanager.configuration.RetrofitConfiguration;
import aabramov.com.todomanager.model.ServerAddress;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * @author Andrii Abramov on 11/26/16.
 */

public class ChangeServerDialog extends DialogFragment {

    private String hostName;
    private String protocol;
    private int port;

    private Spinner dropdownProtocol;
    private EditText etHostname;
    private EditText etPort;

    private ArrayAdapter<CharSequence> adapter;

    private RetrofitConfiguration configuration;

    public static ChangeServerDialog newInstance() {
        ChangeServerDialog changeServerDialog = new ChangeServerDialog();
        changeServerDialog.configuration = TodoApplication.getApplication().getRetrofitConfiguration();
        return changeServerDialog;
    }

    private DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            ServerAddress serverAddress = parseServerAddress();
            configuration.setServerAddress(serverAddress);
            Toast.makeText(getActivity(), "Changed server data to: " + serverAddress.getAsString(), Toast.LENGTH_LONG).show();
        }
    };

    private ServerAddress parseServerAddress() {
        hostName = etHostname.getText().toString();
        protocol = dropdownProtocol.getSelectedItem().toString();
        port = Integer.valueOf(etPort.getText().toString());
        return new ServerAddress(protocol, hostName, port);
    }

    private DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    };


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_menu_edit)
                .setTitle("Change server configuration")
                .setPositiveButton(android.R.string.ok, positiveListener)
                .setNegativeButton(android.R.string.cancel, negativeListener)
                .setView(initView())
                .create();

        return dialog;
    }

    private View initView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_change_server, null);

        setUpData();

        etHostname = (EditText) view.findViewById(R.id.etHostname);
        etPort = (EditText) view.findViewById(R.id.etPort);
        dropdownProtocol = (Spinner) view.findViewById(R.id.dropdownProtocol);

        etHostname.setText(hostName);
        etPort.setText(String.valueOf(port));

        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.protocols, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdownProtocol.setAdapter(adapter);
        dropdownProtocol.setSelection(adapter.getPosition(protocol));

        return view;
    }

    private void setUpData() {
        ServerAddress address = configuration.getServerAddress();

        hostName = address.getHost();
        protocol = address.getProtocol();
        port = address.getPort();
    }


}
