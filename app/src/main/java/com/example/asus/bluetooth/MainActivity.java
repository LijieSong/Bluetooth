package com.example.asus.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.bluetooth.popupwindow.AccessPopWindow;
import com.example.asus.bluetooth.popupwindow.EndTimePopWindow;
import com.example.asus.bluetooth.popupwindow.StartTimePopWindow;
import com.example.asus.bluetooth.popupwindow.WeekPopWindow;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private GridView grid_button;

    private int[] button_image = {R.mipmap.week, R.mipmap.hydrovalve, R.mipmap.start, R.mipmap.stop};

    private String[] button_name = {"定时周期", "设备通道", "开启时间", "关闭时间"};
    //水阀按键
    private ImageView hy1, hy2, hy3, hy4, but_timing, but_send_message;
    private Button but_bluetooth, but_connect, but_hybr_all, but_app_text;
    //蓝牙适配器
    private BluetoothAdapter bluetoothAdapter;
    PopupWindow popWindow = null;

    private BluetoothSocket socket;
    private OutputStream outputStream;

    private Context mContext;
    private TextView get_text, txt_mactime, get_time;
    private boolean click = false;
    private boolean mConnect = false;
    public static byte[] message = new byte[24];
    private byte[] b = null;
    //timertask
    private final Timer mTimer = new Timer();
    private TimerTask task = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    try {
                        if (socket != null) {
                            if (socket.isConnected() == true) {
                                getMesssage();
                                txt_mactime.setVisibility(View.VISIBLE);
                            }
                            if (b != null) {
                                String s = ByteUtils.bytesToHexString(b);
//                               FormatTransfer.printBytes(b);
                              String s1 =  StrUtil.bytesToHexString(b);
                                get_text.setText(String.valueOf(b[6]) + "年" + String.valueOf(b[4]) + "月" + String.valueOf(b[3]) + "日");
                                get_time.setText(String.valueOf(b[2]) + "时" + String.valueOf(b[1]) + "分" + String.valueOf(b[0]) + "秒");
//                                Log.e("slj", "----s:" +s+ "-------s1"+s1
//                                        +"-------" + String.valueOf(b[6]) + "年" + String.valueOf(b[4]) + "月" + String.valueOf(b[3]) + "日" + String.valueOf(b[2]) + "时" + String.valueOf(b[1]) + "分" + String.valueOf(b[0]) + "秒");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    Toast.makeText(mContext, R.string.success_connected, Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        mContext = this;
        message[0] = 85;
        message[1] = 85;
        //获取蓝牙适配器
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        info();
        task = new TimerTask() {
            public void run() {
                //execute the task
                handler.sendEmptyMessage(0);
            }
        };
        mTimer.schedule(task, 0, 990);
    }

    /**
     * 初始化控件
     */
    private void info() {
        hy1 = (ImageView) findViewById(R.id.hy1);
        hy2 = (ImageView) findViewById(R.id.hy2);
        hy3 = (ImageView) findViewById(R.id.hy3);
        hy4 = (ImageView) findViewById(R.id.hy4);
        hy1.setOnClickListener(this);
        hy2.setOnClickListener(this);
        hy3.setOnClickListener(this);
        hy4.setOnClickListener(this);
        but_bluetooth = (Button) findViewById(R.id.bluetooth);
        but_connect = (Button) findViewById(R.id.connect);
        but_hybr_all = (Button) findViewById(R.id.hybr_all);
        but_app_text = (Button) findViewById(R.id.app_text);
        but_timing = (ImageView) findViewById(R.id.timing);
        get_text = (TextView) findViewById(R.id.get_text);
        txt_mactime = (TextView) findViewById(R.id.txt_mactime);
        get_time = (TextView) findViewById(R.id.get_time);
        but_send_message = (ImageView) findViewById(R.id.send_message);
        but_bluetooth.setOnClickListener(this);
        but_connect.setOnClickListener(this);
        but_hybr_all.setOnClickListener(this);
        but_app_text.setOnClickListener(this);
        but_timing.setOnClickListener(this);
        but_send_message.setOnClickListener(this);
        //get button name and image
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < button_image.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", button_image[i]);
            map.put("text", button_name[i]);
            list.add(map);
        }
        grid_button = (GridView) findViewById(R.id.grid_button);
        SimpleAdapter adapter = new SimpleAdapter(mContext, list, R.layout.grid_item,
                new String[]{"image", "text"}, new int[]{R.id.grid_image, R.id.grid_text});
        grid_button.setAdapter(adapter);
        grid_button.setOnItemClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (socket != null) {
            if (socket.isConnected() == true) {
                txt_mactime.setVisibility(View.VISIBLE);
            } else {
                txt_mactime.setVisibility(View.GONE);
            }
        }
    }

    //item点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                popWindow = new WeekPopWindow(mContext);
                getPopWindow();
                break;
            case 1:
                popWindow = new AccessPopWindow(mContext);
                getPopWindow();
                break;
            case 2:
                popWindow = new StartTimePopWindow(mContext);
                getPopWindow();
                break;
            case 3:
                popWindow = new EndTimePopWindow(mContext);
                getPopWindow();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                // 当DeviceListActivity返回设备连接
                if (data != null) {
                    if (resultCode == Activity.RESULT_OK) {
                        connectDevice(data, true);
                    }
                }
                break;
        }
    }

    //获取蓝牙连接
    public void getBluetooth() {

        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();//强制开启蓝牙
            }
            Intent enable = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
            startActivity(enable);
        } else {
            Toast.makeText(mContext, R.string.blutooth_is_null, Toast.LENGTH_LONG).show();
        }
    }


    private void connectDevice(Intent data, boolean secure) {
        // 设备的MAC地址
        String address = data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        //得到BluetoothDevice对象
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        // 试图连接到设备
        try {
            getConnect(device);
        } catch (Exception e) {
            Toast.makeText(mContext, R.string.failed_connected, Toast.LENGTH_SHORT).show();
        }

    }

    //连接方法
    private void getConnect(final BluetoothDevice device) {
        final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
        final UUID uuid = UUID.fromString(SPP_UUID);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    socket = device.createRfcommSocketToServiceRecord(uuid);
                    socket.connect();
                    mConnect = true;
                    handler.sendEmptyMessage(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
//        Toast.makeText(mContext, R.string.success_connected, Toast.LENGTH_SHORT).show();
    }

    //弹出窗
    private void getPopWindow() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popWindow.showAtLocation(findViewById(R.id.root),
                Gravity.CENTER, 0, 0);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }


    //给终端发送数据
    public void sendMessage() {

        if (socket != null) {
            if (socket.isConnected() == true) {
                try {
                    outputStream = socket.getOutputStream();
                    outputStream.write(message);
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(mContext, R.string.bluetooth_connected_first, Toast.LENGTH_SHORT).show();
        }
    }

    //给终端发送字节数据
    public void sendMessage(final byte[] msg) {
        if (socket != null) {
            if (socket.isConnected() == true) {
                try {
                    outputStream = socket.getOutputStream();
                    outputStream.write(msg);
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(mContext, R.string.bluetooth_connected_first, Toast.LENGTH_SHORT).show();
        }
    }

    //接收信息
    private void getMesssage() throws IOException {
        if (socket.isConnected() == true) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        InputStream is = socket.getInputStream();
                        b = new byte[11];
                        if (b != null) {
                            is.read(b);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bluetooth:
                getBluetooth();
                break;
            case R.id.connect:
                Intent serverIntent = new Intent(mContext, DeviceListActivity.class);
                startActivityForResult(serverIntent, 1);
                break;
            case R.id.hybr_all:
                if (click == false) {
                    message[2] = (byte) 204;
                    message[3] = 5;
                    sendMessage();
                    message[3] = 0;
                    click = true;
                    if (socket != null) {
                        if (socket.isConnected() == true) {
                            hy1.setBackgroundResource(R.mipmap.hydron1);
                            hy2.setBackgroundResource(R.mipmap.hydron1);
                            hy3.setBackgroundResource(R.mipmap.hydron1);
                            hy4.setBackgroundResource(R.mipmap.hydron1);
                        }
                    }
                } else {
                    message[2] = (byte) 204;
                    message[3] = 80;
                    sendMessage();
                    message[3] = 0;
                    click = false;
                    if (socket != null) {
                        if (socket.isConnected() == true) {
                            hy1.setBackgroundResource(R.mipmap.hybroff1);
                            hy2.setBackgroundResource(R.mipmap.hybroff1);
                            hy3.setBackgroundResource(R.mipmap.hybroff1);
                            hy4.setBackgroundResource(R.mipmap.hybroff1);
                        }
                    }
                }
                break;
            case R.id.app_text://使用说明
                Intent intent = new Intent(mContext, TextActivity.class);
                startActivity(intent);
                break;

            case R.id.timing://定时
                if (socket != null) {
                    if (message[3] != 0) {
                        message[2] = (byte) 170;
                        sendMessage();
                        try {
                            getMesssage();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(mContext, R.string.success_timetask, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, R.string.time_is_first, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, R.string.bluetooth_connected_first, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.send_message://校准时间
                try {
                    if (socket != null) {
                        if (socket.isConnected() == true) {
//                            SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd-HH-mm");//设置日期格式
//                            String data = df.format(new Date());
//                            String[] date = data.split("-");
//                            String year = date[0];
//                            String month = date[1];
//                            String data1 = date[2];
//                            String hour = date[3];
//                            String min = date[4];
//                            String sec = date[5];
//                            String weekOfDate = FormatTransfer.getWeekOfDate(new Date());
                            SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmm");//设置日期格式
                            String data = df.format(new Date());
                            String weekOfDate = FormatTransfer.getWeekOfDate(new Date());
                            String s = FormatTransfer.insertStringInParticularPosition(data, weekOfDate, 2);
                            char[] array = s.toCharArray();
                            final byte[] bytes = ByteUtils.decodeHex(array);
//                            final byte[] bytes = FormatTransfer.HexString2Byte(s);
                            message[2] =(byte) 187;
                            for (int i = 0;i<bytes.length;i++){
                                Log.e("slj","bytes里面的数据:" + bytes[i]);
                                message[3+i] =bytes[i];
                                Log.e("slj","bytes里面的数据:"+ message[3+i]+"-------" + bytes[i]);
                            }
                            String hexString = StrUtil.byteTOString(message);//StrUtil.bytesToHexString(message);
                            Log.e("slj", "hexString: "+hexString);
                            sendMessage();
                            handler.sendEmptyMessage(0);
                            Thread.sleep(1000);
                            Toast.makeText(MainActivity.this, R.string.success_time, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, R.string.bluetooth_connected_first, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.hy1:
                if (click == false) {
                    hy1.setBackgroundResource(R.mipmap.hydron1);
                    message[2] = (byte) 204;
                    message[3] = 1;
                    sendMessage();
                    message[3] = 0;
                    click = true;
                } else {
                    message[2] = (byte) 204;
                    message[3] = 16;
                    sendMessage();
                    message[3] = 0;
                    click = false;
                    hy1.setBackgroundResource(R.mipmap.hybroff1);
                }
                break;
            case R.id.hy2:
                if (click == false) {
                    hy2.setBackgroundResource(R.mipmap.hydron1);
                    message[2] = (byte) 204;
                    message[3] = 2;
                    sendMessage();
                    message[3] = 0;
                    click = true;
                } else {
                    message[2] = (byte) 204;
                    message[3] = 32;
                    sendMessage();
                    message[3] = 0;
                    click = false;
                    hy2.setBackgroundResource(R.mipmap.hybroff1);
                }
                break;
            case R.id.hy3:
                if (click == false) {
                    hy3.setBackgroundResource(R.mipmap.hydron1);
                    message[2] = (byte) 204;
                    message[3] = 3;
                    sendMessage();
                    message[3] = 0;
                    click = true;
                } else {
                    message[2] = (byte) 204;
                    message[3] = 48;
                    sendMessage();
                    message[3] = 0;
                    click = false;
                    hy3.setBackgroundResource(R.mipmap.hybroff1);
                }
                break;
            case R.id.hy4:
                if (click == false) {
                    hy4.setBackgroundResource(R.mipmap.hydron1);
                    message[2] = (byte) 204;
                    message[3] = 4;
                    sendMessage();
                    message[3] = 0;
                    click = true;
                } else {
                    message[2] = (byte) 204;
                    message[3] = 64;
                    sendMessage();
                    message[3] = 0;
                    click = false;
                    hy4.setBackgroundResource(R.mipmap.hybroff1);
                }
                break;
        }
//        if (mConnect == true) {
//            switch (v.getId()) {
//                case R.id.hy1:
//                    if (click == false) {
//                        hy1.setImageResource(R.mipmap.hydron1);
//                        Toast.makeText(this,"点击了开关1",Toast.LENGTH_SHORT).show();
//                        message[2] = (byte) 204;
//                        message[3] = 1;
//                        sendMessage();
//                        message[3] = 0;
//                        click = true;
//                    } else {
//                        message[2] = (byte) 204;
//                        message[3] = 16;
//                        sendMessage();
//                        message[3] = 0;
//                        click = false;
//                        hy1.setImageResource(R.mipmap.hybroff1);
//                    }
//                    break;
//                case R.id.hy2:
//                    if (click == false) {
//                        hy2.setImageResource(R.mipmap.hydron1);
//                        message[2] = (byte) 204;
//                        message[3] = 2;
//                        sendMessage();
//                        message[3] = 0;
//                        click = true;
//                    } else {
//                        message[2] = (byte) 204;
//                        message[3] = 32;
//                        sendMessage();
//                        message[3] = 0;
//                        click = false;
//                        hy2.setImageResource(R.mipmap.hybroff1);
//                    }
//                    break;
//                case R.id.hy3:
//                    if (click == false) {
//                        hy3.setImageResource(R.mipmap.hydron1);
//                        message[2] = (byte) 204;
//                        message[3] = 3;
//                        sendMessage();
//                        message[3] = 0;
//                        click = true;
//
//                    } else {
//                        message[2] = (byte) 204;
//                        message[3] = 48;
//                        sendMessage();
//                        message[3] = 0;
//                        click = false;
//                        hy3.setImageResource(R.mipmap.hybroff1);
//                    }
//                    break;
//                case R.id.hy4:
//                    if (click == false) {
//                        hy4.setImageResource(R.mipmap.hydron1);
//                        message[2] = (byte) 204;
//                        message[3] = 4;
//                        sendMessage();
//                        message[3] = 0;
//                        click = true;
//                    } else {
//                        message[2] = (byte) 204;
//                        message[3] = 64;
//                        sendMessage();
//                        message[3] = 0;
//                        click = false;
//                        hy4.setImageResource(R.mipmap.hybroff1);
//                    }
//                    break;
//            }
//
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                try {
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
