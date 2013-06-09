//  Log.d("TAG", "User name: " + value + " " + input.getId() + " " + etDir.getId());


package com.hister.changeformat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends Activity {
    EditText etDir;
    Button btnLock;
    String nameFolder;
    int pass;
    int enteredPass;
    boolean dialogPassIsOpen;
    public AlertDialog d;
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);
        setElementsId();

        if (icicle != null){
            dialogPassIsOpen = icicle.getBoolean("dialogPassIsOpen", false);
            if (dialogPassIsOpen) {
                btnLockClicked();
                input.setText(icicle.getString("etInput"));
            }
        }

        SharedPreferences setting = getSharedPreferences("Setting", 0);
        SharedPreferences.Editor editorSetting = setting.edit();
        etDir.setText(setting.getString("etDir", ""));
        nameFolder = etDir.getText().toString();
        if (nameFolder.equals("")) {
            btnLock.setEnabled(false);
        }

        check();

//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(etDir, InputMethodManager.SHOW_IMPLICIT);

        etDir.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etDir.getText().toString().equals("")) {
                    btnLock.setEnabled(false);
                }
                else {
                    btnLock.setEnabled(true);
                    check();
                }

            }
        });

        etDir.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    void setElementsId() {
        etDir = (EditText) findViewById(R.id.etDir);
        btnLock = (Button) findViewById(R.id.btnLock);
        pass = 2304;
    }


    private void check() {
        boolean isLock = false;
        nameFolder = etDir.getText().toString();
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard, "/" + nameFolder);
        if ((!dir.exists() && !dir.isDirectory()) || nameFolder.equals("")) {
            return;
        }
        for (File f : dir.listFiles()) {
            if (f.isFile()) {
                String name = f.getAbsolutePath();
                name = name.substring(name.length() - 4, name.length());

                if (name.equals(".lck")) {
                    isLock = true;
                } else {
                    isLock = false;
                }
                break;
            }
        }

        if (isLock) {
            btnLock.setText(R.string.unlock);
        } else {
            btnLock.setText(R.string.lock);
        }
    }

    private void lock() {
        boolean wasSuccessful = false;
        nameFolder = etDir.getText().toString();
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard, "/" + nameFolder);
        if ((!dir.exists() && !dir.isDirectory()) || nameFolder.equals("")) {
                Toast.makeText(MainActivity.this, "Folder doesn't exist", Toast.LENGTH_LONG).show();
            return;
        }

        for (File f : dir.listFiles()) {
            if (f.isFile()) {
                String name = f.getAbsolutePath();

                if (f.renameTo(new File(name + ".lck"))) {
                    wasSuccessful = true;
                    //names.append("Renamed.");
                } else {
                    wasSuccessful = false;
                    //names.append("Didn't Rename.");
                }
            }
        }


        if (wasSuccessful) {
            btnLock.setText(R.string.unlock);
            Toast.makeText(MainActivity.this, "Successfully locked.", Toast.LENGTH_LONG).show();
        } else {
            btnLock.setText(R.string.lock);
            Toast.makeText(MainActivity.this, "Unable to lock.", Toast.LENGTH_LONG).show();
        }

    }

    private void unLock() {

        boolean wasSuccessful = false;
        nameFolder = etDir.getText().toString();
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard, "/he");
        if ((!dir.exists() && !dir.isDirectory()) || nameFolder.equals("")) {
            Toast.makeText(MainActivity.this, "Folder doesn't exist", Toast.LENGTH_LONG).show();
            return;
        }
        for (File f : dir.listFiles()) {
            if (f.isFile()) {
                String name = f.getAbsolutePath();
                name = name.substring(0, name.length() - 4);

                if (f.renameTo(new File(name))) {
                    wasSuccessful = true;
                    //names.append("Renamed.");
                } else {
                    wasSuccessful = false;
                    //names.append("Didn't Rename.");
                }
            }
        }


        if (wasSuccessful) {
            btnLock.setText(R.string.lock);
            Toast.makeText(MainActivity.this, "Successfully Unlocked.", Toast.LENGTH_LONG).show();
        } else {
            btnLock.setText(R.string.unlock);
            Toast.makeText(MainActivity.this, "Unable to Unlock.", Toast.LENGTH_LONG).show();
        }
    }

    public EditText input;
    public void btn_Lock(View view) {
        btnLockClicked();
    }

    public void btnLockClicked() {

        input = new EditText(this);
        input.setId(1);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());

        d = new AlertDialog.Builder(this)
                .setTitle("Change Format")
                .setMessage("Enter the password")
                .setView(input)
                .setPositiveButton("Ok",
                        new Dialog.OnClickListener() {
                            public void onClick(DialogInterface d, int which) {
                            }
                        })
                .setNegativeButton("Cancel",
                        new Dialog.OnClickListener() {
                            public void onClick(DialogInterface d, int which) {
                            }
                        })
                .create();
        d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//                }
//            }
//        });
        d.show();

        Button theButton = d.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(d));
    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;
        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }
        @Override
        public void onClick(View v) {
            String value = input.getText().toString();
            if (value.equals("")) {
                Toast.makeText(MainActivity.this, "You didn't type anything", Toast.LENGTH_LONG).show();
            }
            else {
                enteredPass = Integer.parseInt(input.getText().toString());
                if (operationWasSuccess()) {
                    dialog.dismiss();
                }
            }
        }
    }

    boolean operationWasSuccess() {
        if (pass == enteredPass) {
            nameFolder = etDir.getText().toString();
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard, "/" + nameFolder);

            if (!dir.exists()) {
                Toast.makeText(MainActivity.this, "Folder doesn't exist.", Toast.LENGTH_LONG).show();
                return false;
            } else {
                String str = btnLock.getText().toString();
                if (str.equals(getResources().getString(R.string.lock))) {
                    lock();
                } else {
                    unLock();
                }
                return true;
            }
        }
        else {
            Toast.makeText(MainActivity.this, "PassWord is wrong", Toast.LENGTH_SHORT).show();
            input.setText("");
            return false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences setting = getSharedPreferences("Setting", 0);
        SharedPreferences.Editor editorSetting = setting.edit();

        editorSetting.putString("etDir", etDir.getText().toString());

        editorSetting.commit();
    }


    protected void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);
        if (d.isShowing()) {
            icicle.putBoolean("dialogPassIsOpen", true);
            icicle.putString("etInput", input.getText().toString());
        }
        else {
            icicle.putBoolean("dialogPassIsOpen", false);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



}