package my.kylogger.johnmelodyme.IOT.bluetooth_hc_05;
/**
 * Copyright 2020 © John Melody Melissa
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @Author : John Melody Melissa
 * @Copyright: John Melody Melissa & Tan Sin Dee © Copyright 2020
 * @INPIREDBYGF: Cindy Tan <3
 * @Class: BluetoothActivity.class
 *
 */
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ligl.android.widget.iosdialog.IOSDialog;

import java.util.Set;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class BluetoothActivity extends AppCompatActivity {
    public static final String TAG = "Bluetooth";
    public static final UUID BLUETOOTH_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public final static int REQUEST_ENABLE_BLUETOOTH = 0x1;
    public final static int MESSAGE_READ = 0x2;
    public final static int CONNECTING_STATUS = 0x3;
    public final static int TOAST_DURATION = Toast.LENGTH_SHORT;
    public static BluetoothAdapter bluetoothAdapter;
    public static Set<BluetoothDevice> pairedDevices;
    public static Handler staticHandler;
    private ArrayAdapter<String> btAdapter;
    private TextView RX, Status;
    private Button ShowPairedDevice;
    private ListView listViewPairedDevices;
    private ProgressDialog progressDialog;

    // TODO DeclarationInit()
    public void DeclarationInit(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        progressDialog = new ProgressDialog(BluetoothActivity.this);
        RX = findViewById(R.id.RX);
        Status = findViewById(R.id.Status);
        ShowPairedDevice = findViewById(R.id.ShowPairedDevice);
        btAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listViewPairedDevices = findViewById(R.id.lv_devices);
        listViewPairedDevices.setAdapter(btAdapter);
        progressDialog.setMessage(getResources().getString(R.string.EnablingBluetooth));
    }

    @Override
    // TODO onCreate()
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: " + BluetoothActivity.class.getSimpleName());
        DeclarationInit();
        CheckBluetoothInit();
    }

    // TODO CheckBluetoothInit()
    public void CheckBluetoothInit() {
        if (!(bluetoothAdapter == null)){
            if (bluetoothAdapter.isEnabled()){
//                    progressDialog.show();
//                    bluetoothAdapter.enable();
//                    findViewById(R.id.bluetoothonoff).setBackgroundResource(R.drawable.ic_bluetooth_connected_black_24dp);
//                    progressDialog.dismiss();
                Toasty.success(getApplicationContext(),
                        getResources().getString(R.string.alreadyOn),
                        TOAST_DURATION, true)
                        .show();
                Log.d(TAG, "$user " + getResources().getString(R.string.alreadyOn));
            } else {
                Toasty.error(getApplicationContext(),
                        getResources().getString(R.string.pleaseena),
                        TOAST_DURATION)
                        .show();
                Log.d(TAG, "$user needs to " + getResources().getString(R.string.pleaseena));
            }
        }
    }

    @Override
    // TODO  onCreateOptionsMenu()
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    // TODO onOptionsItemSelected()
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.about) {
            new IOSDialog.Builder(BluetoothActivity.this)
                    .setTitle("About")
                    .setMessage(getResources().getString(R.string.aboutdev))
                    .setPositiveButton("Ok", null)
                    .show();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}