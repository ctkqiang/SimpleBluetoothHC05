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
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.ligl.android.widget.iosdialog.IOSDialog;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.UUID;
import es.dmoral.toasty.Toasty;
import my.kylogger.johnmelodyme.IOT.bluetooth_hc_05.Helper.ConnectedThread;

public class BluetoothActivity extends AppCompatActivity {
    public static final String TAG = "Bluetooth";
    public static final UUID BLUETOOTH_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public final static int REQUEST_ENABLE_BLUETOOTH = 0x1;
    public final static int MESSAGE_READ = 0x2;
    public final static int CONNECTING_STATUS = 0x3;
    public final static int TOAST_DURATION = Toast.LENGTH_SHORT;
    public static BluetoothAdapter bluetoothAdapter;
    public static BluetoothSocket bluetoothSocket = null;
    public static Set<BluetoothDevice> pairedDevices;
    public static Handler staticHandler;
    public ConnectedThread connectedThread;
    private ArrayAdapter<String> btAdapter;
    private TextView RX, Status;
    private Button ShowPairedDevice;
    private ListView listViewPairedDevices;
    private ProgressDialog progressDialog;
    private Handler handler;

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
        listViewPairedDevices.setOnItemClickListener(listViewPairedDevicesClickListener);
        progressDialog.setMessage(getResources().getString(R.string.EnablingBluetooth));
    }

    @SuppressLint("HandlerLeak")
    @Override
    // TODO onCreate()
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: " + BluetoothActivity.class.getSimpleName());
        DeclarationInit();
        CheckBluetoothInit();

        // TODO handler();
        handler = new Handler(){
            @SuppressLint({"HandlerLeak", "SetTextI18n"})
            @Override
            public void handleMessage(android.os.Message msg) {
                if(msg.what  == MESSAGE_READ) {
                    String ReadMessage = null;
                    try {
                        ReadMessage = new String((byte []) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Handler", e);
                    }
                    RX.setText(ReadMessage);
                }

                if (msg.what == CONNECTING_STATUS) {
                    if (msg.arg1 == 1) {
                        Status.setText(getResources().getString(R.string.connectedto) +" "+(String) (msg.obj));
                    } else {
                        Status.setText(getResources().getString(R.string.ConnectionFailed));
                    }
                }
            }
        };
    }

    // TODO CheckBluetoothInit()
    public void CheckBluetoothInit() {
        if (!(bluetoothAdapter == null)){
            if (bluetoothAdapter.isEnabled()){
                Toasty.success(getApplicationContext(),
                        getResources().getString(R.string.alreadyOn),
                        TOAST_DURATION, true)
                        .show();
                Log.d(TAG, "$user " + getResources().getString(R.string.alreadyOn));
                InitMenu();
            } else {
                Toasty.error(getApplicationContext(),
                        getResources().getString(R.string.pleaseena),
                        TOAST_DURATION)
                        .show();
                Log.d(TAG, "$user needs to " + getResources().getString(R.string.pleaseena));
                Intent enableBtIntent;
                enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
            }
        } else {
            Toasty.error(getApplicationContext(),
                    getResources().getString(R.string.notSupported),
                    TOAST_DURATION)
                    .show();
            Log.e(TAG, "$user device " + getResources().getString(R.string.notSupported));
        }
    }

    // TODO InitMenu()
    public void InitMenu() {
        // TODO ShowPairedDevice.setOnClickListener()
        ShowPairedDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                } else {
                    if (bluetoothAdapter.isEnabled()){
                        btAdapter.clear();
                        bluetoothAdapter.startDiscovery();
                        registerReceiver(BR, new IntentFilter(BluetoothDevice.ACTION_FOUND));
                    } else {
                        System.out.println(getResources().getString(R.string.pleaseena));
                    }
                }

                Set<BluetoothDevice>pairedDevice = bluetoothAdapter.getBondedDevices();
                if (pairedDevice.size() > 0) {
                    for (BluetoothDevice device : pairedDevice) {
                        String deviceName = device.getName();
                        String deviceHardwareAddress = device.getAddress(); // MAC address
                        btAdapter.add(deviceName + "\n" + deviceHardwareAddress);
                    }
                    listViewPairedDevices.setAdapter(btAdapter);
                }
            }
        });

        // TODO listViewPairedDevices.setOnItemClickListener()
        listViewPairedDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    // TODO BroadCastReceiver()
    final BroadcastReceiver BR = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String ACTION;
            ACTION = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(ACTION)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_NAME);
                btAdapter.add(device.getName() + "\n" + device.getAddress());
                btAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    // TODO onDestroy
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(BR);
    }

    @SuppressLint("SetTextI18n")
    @Override
    // TODO onStart
    public void onStart(){
        super.onStart();
        Status.setText("<Bluetooth Status>");
        RX.setText("<Read Buffer>");
    }

    // TODO handleMessage()
    @SuppressLint("SetTextI18n")
    public void handleMessage(android.os.Message msg){
        if (msg.what == MESSAGE_READ){
            String ReadMessage = null;
            try {
                ReadMessage = new String((byte[]) msg.obj, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "handleMessage: ", e);
            }
            RX.setText("RX" + ReadMessage);
        }

        if (msg.what == CONNECTING_STATUS){
            if (msg.arg1 == 1){
                Status.setText("Bluetooth Status: " + "Connected To" + (String) (msg.obj) );
            } else {
                Status.setText("Connection Failed \uD83D\uDE41 ");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.about) {
            new IOSDialog.Builder(BluetoothActivity.this)
                    .setTitle("About")
                    .setMessage(getResources().getString(R.string.aboutdev))
                    .setPositiveButton("Ok", null)
                    .show();
            return false;
        }

        if (menuItem.getItemId() == R.id.arduinocode){
            Intent ToArduino;
            ToArduino = new Intent(BluetoothActivity.this, ArduinoActivity.class);
            startActivity(ToArduino);
            return false;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    // TODO listViewPairedDevicesClickListener()
    private AdapterView.OnItemClickListener listViewPairedDevicesClickListener = new AdapterView.OnItemClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (!(bluetoothAdapter.isEnabled())) {
                Toasty.error(getApplicationContext(),
                        getResources().getString(R.string.pleaseena),
                        TOAST_DURATION)
                        .show();
                Log.d(TAG, "$user needs to " + getResources().getString(R.string.pleaseena));
            }
            String information;
            final String Address, Name;
            information = ((TextView) view).getText().toString();
            Address = information.substring(information.length() - 17);
            Name = information.substring(0, information.length() - 17);
            Status.setText(getResources().getString(R.string.connect));

            new Thread() {
              public void run(){
                  boolean fail = false;
                  BluetoothDevice device;
                  device = bluetoothAdapter.getRemoteDevice(Address);
                  try {
                      bluetoothSocket = createBluetoothSocket(device);
                  } catch (Exception e) {
                      fail = true;
                      Toasty.error(getApplicationContext(),
                              getResources().getString(R.string.failedSocket),
                              TOAST_DURATION, true)
                              .show();
                      Log.e(TAG, getResources().getString(R.string.failedSocket) + e);
                  }
                  try {
                      bluetoothSocket.connect();
                  } catch (Exception e) {
                      try {
                          fail = true;
                          bluetoothSocket.close();
                          handler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
                      } catch (Exception ex) {
                          Log.e(TAG, "run: " + e );
                      }
                  }

                  if (!fail) {
                      connectedThread = new ConnectedThread(bluetoothSocket);
                      connectedThread.start();
                      handler.obtainMessage(CONNECTING_STATUS, -1, -1, Name).sendToTarget();
                  }
              }
            }.start();
        }
    };

    // TODO createBluetoothSocket
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return device.createRfcommSocketToServiceRecord(BLUETOOTH_MODULE_UUID);
    }
}