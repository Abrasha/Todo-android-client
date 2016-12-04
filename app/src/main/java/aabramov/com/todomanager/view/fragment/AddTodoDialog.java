package aabramov.com.todomanager.view.fragment;

import aabramov.com.todomanager.R;
import aabramov.com.todomanager.TodoApplication;
import aabramov.com.todomanager.model.Priority;
import aabramov.com.todomanager.model.Status;
import aabramov.com.todomanager.model.Todo;
import aabramov.com.todomanager.service.TodoService;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Andrii Abramov on 12/1/16.
 */
public class AddTodoDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private OnDismissListener onDismissListener;

    @BindView(R.id.etTitle)
    EditText etTitle;

    @BindView(R.id.etBody)
    EditText etBody;

    @BindView(R.id.etTags)
    EditText etTags;

    @BindView(R.id.dropdownPriority)
    Spinner dropdownPriority;

    @BindView(R.id.btnSetDate)
    Button btnSetDate;

    @BindView(R.id.btnSetTime)
    Button btnSetTime;

    private final Date todoDate = new Date();
    private Calendar calendar = Calendar.getInstance();
    private boolean todoAdded = false;

    public interface OnDismissListener {
        void onDismiss(Todo added);
    }

    public static AddTodoDialog newInstance(OnDismissListener listener) {
        AddTodoDialog result = new AddTodoDialog();
        result.onDismissListener = listener;
        result.calendar.setTime(result.todoDate);
        return result;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = initView();

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Add todo")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        todoAdded = true;
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        todoAdded = false;
                    }
                })
                .create();
    }

    private Todo parseTodo() {
        Todo result = new Todo();

        result.setTitle(etTitle.getText().toString());
        result.setBody(etBody.getText().toString());
        result.setWhen(calendar.getTime());
        result.setPriority(((Priority) dropdownPriority.getSelectedItem()));
        result.setStatus(Status.DEFAULT);
        String[] tags = etTags.getText().toString().split(" ");
        result.setTags(Arrays.asList(tags));

        return result;
    }

    private View initView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_todo_details, null);
        ButterKnife.bind(this, view);

        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSetDateDialog();
            }
        });

        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSetTimeDialog();
            }
        });

        ArrayAdapter<Priority> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Priority.values());
        dropdownPriority.setAdapter(adapter);

        return view;
    }

    private void showSetTimeDialog() {
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(getActivity(), AddTodoDialog.this, hour, minute, true);
        dialog.setTitle("Pick time");
        dialog.show();
    }

    private void showSetDateDialog() {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), AddTodoDialog.this, year, month, day);
        dialog.setTitle("Pick date");
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null && todoAdded) {
            onDismissListener.onDismiss(parseTodo());
        }
    }
}
