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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

/**
 * @author Andrii Abramov on 11/26/16.
 */
public class SelectServerDialog extends DialogFragment {

    private static final String TAG = SelectServerDialog.class.getName();

    @BindView(R.id.dropdownServers)
    Spinner dropdownServers;

    @BindView(R.id.btnAddServer)
    ImageButton btnAddServer;

    private RetrofitConfiguration configuration;
    private ServersRepository serversRepository;

    public static SelectServerDialog newInstance() {
        SelectServerDialog changeServerDialog = new SelectServerDialog();
        TodoApplication application = TodoApplication.getApplication();

        changeServerDialog.configuration = application.getRetrofitConfiguration();
        changeServerDialog.serversRepository = application.getTodoDatabase().getRepository(ServersRepository.class);
        return changeServerDialog;
    }

    private DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            ServerAddress serverAddress = (ServerAddress) dropdownServers.getSelectedItem();
            configuration.setServerAddress(serverAddress);
            Toast.makeText(getActivity(), "Changed server to: " + serverAddress.getAsString(), LENGTH_LONG).show();
        }
    };

    private DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createDialog(initView());
    }

    private View initView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_select_server, null);

        ButterKnife.bind(this, view);

        List<ServerAddress> servers = serversRepository.findAll();

        ArrayAdapter<ServerAddress> serverAdapter;
        serverAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, servers);

        serverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdownServers.setAdapter(serverAdapter);
        dropdownServers.setSelection(serverAdapter.getPosition(configuration.getServerAddress()));

        return view;
    }

    private AlertDialog createDialog(View dialogView) {
        return new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_menu_compass)
                .setTitle("Select server")
                .setPositiveButton(android.R.string.ok, positiveListener)
                .setNegativeButton(android.R.string.cancel, negativeListener)
                .setView(dialogView)
                .create();
    }


}
