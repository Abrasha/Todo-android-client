package aabramov.com.todomanager.view.fragment;

import aabramov.com.todomanager.R;
import aabramov.com.todomanager.TodoApplication;
import aabramov.com.todomanager.configuration.RetrofitConfiguration;
import aabramov.com.todomanager.model.ServerAddress;
import aabramov.com.todomanager.persistence.data.ServersRepository;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.Toast.LENGTH_LONG;

/**
 * @author Andrii Abramov on 11/26/16.
 */
public class AddServerDialog extends DialogFragment {

    private static final String TAG = AddServerDialog.class.getName();
    @BindView(R.id.dropdownProtocol)
    Spinner dropdownProtocol;
    @BindView(R.id.etHostname)
    EditText etHostname;
    @BindView(R.id.etPort)
    EditText etPort;
    private String hostName;
    private String protocol;
    private int port;
    private RetrofitConfiguration configuration;
    private ServersRepository serversRepository;
    private DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            ServerAddress serverAddress = parseServerAddress();
            serversRepository.insert(serverAddress);
            Log.d(TAG, "onClick: Added new server: " + serverAddress.getAsString());
            Toast.makeText(getActivity(), "Added new server: " + serverAddress.getAsString(), LENGTH_LONG).show();
        }
    };
    private DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    };

    public static AddServerDialog newInstance() {
        Log.d(TAG, "newInstance: creating AddServerDialog instance");
        AddServerDialog dialog = new AddServerDialog();
        TodoApplication application = TodoApplication.getApplication();
        dialog.configuration = application.getRetrofitConfiguration();
        dialog.serversRepository = application.getTodoDatabase().getRepository(ServersRepository.class);
        return dialog;
    }

    private ServerAddress parseServerAddress() {
        hostName = etHostname.getText().toString();
        protocol = dropdownProtocol.getSelectedItem().toString();
        port = Integer.valueOf(etPort.getText().toString());
        return new ServerAddress(protocol, hostName, port);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        setUpData();

        return createDialog(initView());
    }

    private AlertDialog createDialog(View dialogView) {
        return new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_menu_edit)
                .setTitle("Add application server")
                .setPositiveButton(android.R.string.ok, positiveListener)
                .setNegativeButton(android.R.string.cancel, negativeListener)
                .setView(dialogView)
                .create();
    }

    private View initView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_change_server, null, false);

        ButterKnife.bind(this, view);

        etHostname.setText(hostName);
        etPort.setText(String.valueOf(port));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.protocols, android.R.layout.simple_spinner_item);

        dropdownProtocol.setAdapter(adapter);
        dropdownProtocol.setSelection(adapter.getPosition(protocol));

        return view;
    }

    private void setUpData() {
        ServerAddress address = configuration.getServerAddress();

        hostName = address.getHostname();
        protocol = address.getProtocol();
        port = address.getPort();
    }


}
