package com.wenjian.commonskill.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.wenjian.commonskill.R;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ubt
 */
public class DeviceListActivity extends AppCompatActivity {
    private static final String TAG = "DeviceListActivity";

    private ScheduledExecutorService mService = Executors.newSingleThreadScheduledExecutor();

    private static final int REQUEST_ENABLE_BT = 100;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FragmentActivity activity = DeviceListActivity.this;
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Log.i(TAG, "connected: ");
                            mService.scheduleAtFixedRate(new Runnable() {
                                @Override
                                public void run() {
                                    String haha = "发送了一条消息";
                                    if (!isServer) {
                                        mBluetoothChatService.write(haha.getBytes());
                                    }
                                }
                            }, 0, 1, TimeUnit.SECONDS);

                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.i(TAG, "connecting: ");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Log.i(TAG, "连接失败: ");
                            break;
                        default:
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.i(TAG, "readMessage: " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:

                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
            }
        }
    };
    private BluetoothAdapter mBtAdapter;
    private ListView mLvPaired;
    private ListView mLvUnpaired;
    private SimpleAdapter mPairedAdapter;
    private SimpleAdapter mUnpairedAdapter;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "onReceive: " + action);
            if (Objects.equals(action, BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mUnpairedAdapter.addDevice(device);
                }
            } else if (Objects.equals(action, BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                if (mUnpairedAdapter.getCount() == 0) {
                    Log.i(TAG, "没有找到设备");
                }

                getSupportActionBar().setSubtitle("finished");
            } else if (Objects.equals(action, BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int bondState = device.getBondState();
                switch (bondState) {
                    case BluetoothDevice.BOND_BONDED:
                        Log.i(TAG, "BOND_BONDED: ");
                        loadBondedDevice();
                        break;
                    case BluetoothDevice.BOND_NONE:
                        Log.i(TAG, "BOND_NONE: ");

                        break;
                    case BluetoothDevice.BOND_BONDING:
                        Log.i(TAG, "BOND_BONDING: ");
                        break;
                    default:
                }
            }

        }
    };
    private boolean isServer = false;
    private BluetoothService mBluetoothChatService;
    private SimpleAdapter.OnItemClickListener mClickListener = new SimpleAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BluetoothDevice device) {
            Log.i(TAG, "onItemClick: " + device);
            BluetoothDevice remoteDevice = mBtAdapter.getRemoteDevice(device.getAddress());
            int bondState = remoteDevice.getBondState();
            if (bondState == BluetoothDevice.BOND_BONDED) {
                mBluetoothChatService.connect(device, false);
                Log.i(TAG, "客户端开始连接: ");
            } else {
                remoteDevice.createBond();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            Toast.makeText(this, "不支持蓝牙", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupUi();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        this.registerReceiver(mReceiver, filter);


        //加载绑定过的设备
        loadBondedDevice();


    }

    private void setupUi() {
        mLvPaired = findViewById(R.id.lv_paired);
        mLvUnpaired = findViewById(R.id.lv_unpaired);

        mPairedAdapter = new SimpleAdapter();
        mLvPaired.setAdapter(mPairedAdapter);

        mUnpairedAdapter = new SimpleAdapter();
        mLvUnpaired.setAdapter(mUnpairedAdapter);


        mPairedAdapter.setOnItemClickListener(mClickListener);
        mUnpairedAdapter.setOnItemClickListener(mClickListener);

    }

    private void loadBondedDevice() {
        Set<BluetoothDevice> bondedDevices = mBtAdapter.getBondedDevices();
        if (bondedDevices != null) {
            mPairedAdapter.setData(new ArrayList<>(bondedDevices));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            Toast.makeText(this, "蓝牙已开启", Toast.LENGTH_SHORT).show();
            ensureDiscoverable();
            setupService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBluetoothChatService != null) {
            mBluetoothChatService.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        this.unregisterReceiver(mReceiver);
    }

    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    private void ensureDiscoverable() {
        if (mBtAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    private void setupService() {

        mBluetoothChatService = new BluetoothService(this, mHandler);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.device_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_scan:
                doDiscovery();
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    private void doDiscovery() {
        mUnpairedAdapter.clear();

        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        mBtAdapter.startDiscovery();
        Log.i(TAG, "doDiscovery: " + "开始搜索设备");
        getSupportActionBar().setSubtitle("scanning...");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "蓝牙已开启", Toast.LENGTH_SHORT).show();
                    setupService();

                }
                break;

            default:
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBluetoothChatService != null) {
            if (isServer) {
                mBluetoothChatService.start();
                Log.i(TAG, "onResume: " + "服务端开始监听");
            }
        }
    }
}
