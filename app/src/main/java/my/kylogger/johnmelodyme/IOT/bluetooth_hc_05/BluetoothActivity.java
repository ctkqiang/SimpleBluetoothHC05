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
 * @Copyright: John Melody Melissa  © Copyright 2020
 * @INPIREDBYGF : Sin Dee <3
 * @Class: BluetoothActivity.class
 *
 */

import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BluetoothActivity extends AppCompatActivity {
    public static final String TAG = "Bluetooth";
    public final static int REQUEST_ENABLE_BLUETOOTH = 0x1;
    public final static int MESSAGE_READ = 0x2;
    public final static int CONNECTING_STATUS = 0x3;
    public static BluetoothAdapter bluetoothAdapter;
    public static Handler staticHandler;
    private TextView RX, Status;
    private Button ShowPairedDevice;
    private ListView listViewPairedDevices;

    // TODO DeclarationInit()
    public void DeclarationInit(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        RX = findViewById(R.id.RX);
        Status = findViewById(R.id.Status);
        ShowPairedDevice = findViewById(R.id.ShowPairedDevice);
        listViewPairedDevices = findViewById(R.id.lv_devices);
    }

    @Override
    // TODO onCreate()
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: " + BluetoothActivity.class.getSimpleName());
        DeclarationInit();
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
        if (menuItem.getItemId() == R.id.bluetoothonoff) {
            if (!(bluetoothAdapter == null)){
                //findViewById(R.id.bluetoothonoff).setBackgroundResource(R.drawable.ic_bluetooth_connected_black_24dp);
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
