package my.kylogger.johnmelodyme.IOT.bluetooth_hc_05.Helper;
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
 * @Class: ConnectedThread.class
 *
 */
import android.bluetooth.BluetoothSocket;
import android.os.SystemClock;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static my.kylogger.johnmelodyme.IOT.bluetooth_hc_05.BluetoothActivity.MESSAGE_READ;
import static my.kylogger.johnmelodyme.IOT.bluetooth_hc_05.BluetoothActivity.staticHandler;

public class ConnectedThread extends Thread{
    private static final String TAG = "Bluetooth";
    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    // TODO ConnectedThread();
    public ConnectedThread(BluetoothSocket socket){
        bluetoothSocket = socket;
        InputStream in = null;
        OutputStream out = null;

        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            Log.d(TAG, "ConnectedThread: " + e);
        }
        inputStream = in;
        outputStream = out;
    }

    // TODO run();
    public void run(){
        int bytes, Sleep;
        byte [] buffer = new byte[1024];
        while (true) {
            try {
                bytes = inputStream.available();
                Sleep = 100;
                if (bytes != 0){
                    SystemClock.sleep(Sleep);
                    bytes = inputStream.available();
                    bytes = inputStream.read(buffer, 0, bytes);
                    staticHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                }
            } catch (IOException e) {
                Log.e(TAG, "buffer:", e);
                break;
            }
        }
    }

    // TODO write();
    public void Write(String input){
        byte [] bytes = input.getBytes();
        try {
            outputStream.write(bytes);
        } catch (Exception e) {
            Log.e(TAG, "Write: ", e);
        }
    }

    // TODO Cancel();
    public void Cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            Log.e(TAG, ConnectedThread.TAG, e );
        }
    }
}
