package com.example.android.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

public class SubList extends AppCompatActivity {

    ListView mSubListView;
    ArrayList<String> mSubListArray;
    ArrayList<Integer> mIntegerArray;
    ArrayAdapter<String> mArrayAdapter;
    FloatingActionButton mAddTaskButton, mBackButton;
    AlertDialog.Builder mNewTaskAlertBuilder;
    AlertDialog mDialog;
    EditText mEditText;
    TextView mTextView;

    // Keep track of position to delete through popup menu
    private int mPositionToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextView = (TextView) findViewById(R.id.textview);
        mTextView.setText(getIntent().getStringExtra(MainActivity.LIST_TITLE_TAG));

        mSubListView = (ListView) findViewById(R.id.listview);
        mSubListArray = new ArrayList<>();
        mIntegerArray = new ArrayList<>();
        mIntegerArray = getIntent().getIntegerArrayListExtra(MainActivity.INT_ARRAY_TAG);
        mSubListArray = getIntent().getStringArrayListExtra(MainActivity.SUB_ARRAY_TAG);
        mArrayAdapter = new ArrayAdapter(SubList.this, android.R.layout.simple_list_item_1, mSubListArray);
        mSubListView.setAdapter(mArrayAdapter);

        // Set Alert Dialog Builder
        mNewTaskAlertBuilder = new AlertDialog.Builder(SubList.this);
        mNewTaskAlertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mIntegerArray.add(0);
                mSubListArray.add(mEditText.getText().toString());
                mArrayAdapter.notifyDataSetChanged();
                mEditText.setText("");
            }
        });
        mNewTaskAlertBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                mEditText.setText("");
                mDialog.cancel();
            }
        });
        mNewTaskAlertBuilder.setTitle("Create New Task");

        // EditText for Alert
        mEditText = new EditText(SubList.this);
        mEditText.setHint("Name of task");
        mNewTaskAlertBuilder.setView(mEditText);

        mDialog = mNewTaskAlertBuilder.create();

        // Floating button action
        mAddTaskButton = (FloatingActionButton) findViewById(R.id.addtask_floatingbutton);
        mAddTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
            }
        });

        mBackButton = (FloatingActionButton) findViewById(R.id.back_floatingbutton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getIntent().getIntExtra(MainActivity.POSITION_TAG, -1);
                Intent returningIntent = new Intent();
                returningIntent.putExtra(MainActivity.POSITION_TAG, position);
                returningIntent.putStringArrayListExtra(MainActivity.MAIN_ARRAY_TAG, mSubListArray);
                returningIntent.putIntegerArrayListExtra(MainActivity.INT_ARRAY_TAG, mIntegerArray);
                setResult(RESULT_OK, returningIntent);
                finish();
            }
        });

        mSubListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mIntegerArray.get(position) == 0) {
                    ((TextView) view).setPaintFlags(((TextView) view).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    mIntegerArray.set(position, 1);
                } else {
                    ((TextView) view).setPaintFlags(((TextView) view).getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    mIntegerArray.set(position, 0);
                }
            }
        });

        mSubListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mPositionToDelete = position;
                SubList.this.showPopUpView(view);
                return true;
            }
        });
    }

    // Popup Menu
    public void showPopUpView(View view) {
        PopupMenu popUp = new PopupMenu(this, view);
        popUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.popup_action) {
                    mSubListArray.remove(mPositionToDelete);
                    mArrayAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
        MenuInflater inflator = popUp.getMenuInflater();
        inflator.inflate(R.menu.popup, popUp.getMenu());
        popUp.show();
    }
}

//    private void striker() {
//        int i = 0;
//        for (int num : mIntegerArray) {
//            if (num == 0) {
//                ((TextView) mSubListView.getSelectedView().findViewById(i)).setPaintFlags(((TextView) mSubListView.getSelectedView().findViewById(i)).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            } else
//                ((TextView) mSubListView.getSelectedView().findViewById(i)).setPaintFlags(((TextView) mSubListView.getSelectedView().findViewById(i)).getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
//            i++;
//        }
//
//    }
