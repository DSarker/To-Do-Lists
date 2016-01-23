package com.example.android.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String MAIN_ARRAY_TAG = "mainArray";
    public static final String SUB_ARRAY_TAG = "subArray";
    public static final String INT_ARRAY_TAG = "intArray";
    public static final String POSITION_TAG = "position";
    public static final String LIST_TITLE_TAG = "title";

    ListView mMainListView;
    ArrayList<String> mMainArrayList = new ArrayList<>();
    ArrayAdapter<ArrayList<String>> mArrayAdapter;
    ArrayList<ArrayList<String>> mInceptionList;
    ArrayList<ArrayList<Integer>> mIntegerList;
    FloatingActionButton mAddListButton;
    AlertDialog.Builder mNewListAlertBuilder;
    AlertDialog mDialog;
    EditText mEditText;

    private int mPositionToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize ListView
        mMainListView = (ListView) findViewById(R.id.lists_listview);
        mInceptionList = new ArrayList<>();
        mIntegerList = new ArrayList<>();

        mMainArrayList = new ArrayList<>();

        // Initialize ArrayAdapter
        mArrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, mMainArrayList);

        // Set adapter to list view
        mMainListView.setAdapter(mArrayAdapter);

        // Set Alert Dialog Builder
        mNewListAlertBuilder = new AlertDialog.Builder(MainActivity.this);
        mNewListAlertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mMainArrayList.add(mEditText.getText().toString());
                mInceptionList.add(new ArrayList<String>());
                mIntegerList.add(new ArrayList<Integer>());
                mArrayAdapter.notifyDataSetChanged();
                mEditText.setText("");
            }
        });
        mNewListAlertBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                mEditText.setText("");
                mDialog.cancel();
            }
        });
        mNewListAlertBuilder.setTitle("Create New List");

        // EditText for Alert
        mEditText = new EditText(MainActivity.this);
        mEditText.setHint("Name of list");
        mNewListAlertBuilder.setView(mEditText);

        // Create dialog
        mDialog = mNewListAlertBuilder.create();

        // Floating button action
        mAddListButton = (FloatingActionButton) findViewById(R.id.addlist_floatingbutton);
        mAddListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
            }
        });

        // ItemClick action
        mMainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent subListIntent = new Intent(MainActivity.this, SubList.class);
                subListIntent.putExtra(POSITION_TAG, position);
                subListIntent.putExtra(LIST_TITLE_TAG, mMainArrayList.get(position));
                subListIntent.putStringArrayListExtra(SUB_ARRAY_TAG, mInceptionList.get(position));
                subListIntent.putIntegerArrayListExtra(INT_ARRAY_TAG, mIntegerList.get(position));
                startActivityForResult(subListIntent, 0);
            }
        });

        // ItemLongClick action
        mMainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mPositionToDelete = position;
                MainActivity.this.showPopUpView(view);
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
                    mMainArrayList.remove(mPositionToDelete);
                    mInceptionList.remove(mPositionToDelete);
                    mArrayAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
        MenuInflater inflator = popUp.getMenuInflater();
        inflator.inflate(R.menu.popup, popUp.getMenu());
        popUp.show();
    }

    // Receiving the updated array list
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            int position = data.getIntExtra(POSITION_TAG, -1);
            ArrayList<String> updatedSubList = data.getStringArrayListExtra(MAIN_ARRAY_TAG);
            ArrayList<Integer> updatedIntSubList = data.getIntegerArrayListExtra(INT_ARRAY_TAG);

            mInceptionList.get(position).clear();
            mInceptionList.get(position).addAll(updatedSubList);

            mIntegerList.get(position).clear();
            mIntegerList.get(position).addAll(updatedIntSubList);
        }
    }
}



















