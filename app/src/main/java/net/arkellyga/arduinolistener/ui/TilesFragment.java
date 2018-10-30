package net.arkellyga.arduinolistener.ui;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import net.arkellyga.arduinolistener.R;
import net.arkellyga.arduinolistener.utils.Tile;
import net.arkellyga.arduinolistener.utils.TileParser;
import net.arkellyga.arduinolistener.utils.TilesFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TilesFragment extends Fragment {
    private static final String TAG  = "TilesFragment";
    private boolean mIsDebug = false;
    private TileLayout mTileLayout;
    private TileParser mParser;
    private boolean mIsFirstStartup = true;
    private boolean mIsLastTime = false;
    private ArrayList<TileView> mViews;
    private TilesFactory mFactory;

    private UsbSerialPort mPort;
    private SerialInputOutputManager mSerialManager;

    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    private final SerialInputOutputManager.Listener mSerialListener =
            new SerialInputOutputManager.Listener() {
                @Override
                public void onNewData(final byte[] data) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String dataStr = new String(data);
                            Toast.makeText(getActivity(), dataStr, Toast.LENGTH_SHORT).show();
                            /*if (mIsFirstStartup) {
                                long startTime = Calendar.getInstance().getTimeInMillis();
                                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                                String formatedTime = format.format(startTime);
                                mParser.parseCustomString(formatedTime, "%ON_STARTUP%");
                                mIsFirstStartup = false;
                            }
                            if (mIsLastTime) {
                                long startTime = Calendar.getInstance().getTimeInMillis();
                                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                                String formatedTime = format.format(startTime);
                                mParser.parseCustomString(formatedTime, "%LAST_TIME%");
                            } */
                            mParser.parseString(dataStr);
                        }
                    });
                }

                @Override
                public void onRunError(final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showMessage("Exception: " + e.getMessage());
                        }
                    });
                }
            };

    OnOpenEditorTile mEditorListener;
    interface OnOpenEditorTile {
        void editTile(Tile tile);
    }

    private void showMessage(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        startIoManager();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFactory = new TilesFactory(getActivity());
        mEditorListener = (OnOpenEditorTile) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_main_fragment, null);
        setupUI(view);
        return view;
    }

    private void setupUI(View v) {
        mTileLayout = v.findViewById(R.id.tile_layout_main);
        ArrayList<Tile> tiles = mFactory.getTiles();
        TileView tileView;
        TileLayout.LayoutParams params;
        mViews = new ArrayList<>();
        for (final Tile tile : tiles) {
            tileView = new TileView(getActivity());
            tileView.setData(tile.getData());
            tileView.setText(tile.getData());
            tileView.setColor(tile.getColor());
            tileView.setName(tile.getName());
            tileView.setTextSize(tile.getTextSize());
            tileView.setTextColor(tile.getTextColor());
            tileView.setTextX(tile.getTextX());
            tileView.setTextY(tile.getTextY());
            tileView.setBorderColor(tile.getBorderColor());
            tileView.setBorderSize(tile.getBorderSize());
            tileView.setOnFirstClick(tile.getOnFirstClick());
            tileView.setOnSecondClick(tile.getOnSecondClick());
            if (tile.isSwitch()) {
                tileView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final TileView view1 = (TileView) view;
                        try {
                            if (view1.isSwitch())
                                mPort.write(view1.getOnFirstClick().getBytes(), 2000);
                            else
                                mPort.write(view1.getOnSecondClick().getBytes(), 2000);
                            view1.setSwitch();
                        } catch (IOException | NullPointerException ex) {
                            ex.printStackTrace();
                        }
                        final Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                        anim.setDuration(500);
                        view1.startAnimation(anim);
                    }
                });
            } else {
                tileView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final TileView view1 = (TileView) view;
                        try {
                            mPort.write(view1.getOnFirstClick().getBytes(), 2000);
                        } catch (IOException | NullPointerException ex) {
                            ex.printStackTrace();
                        }
                        final Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                        anim.setDuration(500);
                        view1.startAnimation(anim);
                    }
                });
            }
            tileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mParser.parseString("-127");
                    mParser.parseString("relay1 on");
                    Log.d(TAG, "onClick: click");
                }
            });
            tileView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    TileView view1 = (TileView) view;
                    //startActivity(TilesActivity.startEditFragment(view1.getName(), getActivity()));
                    /*getActivity().getFragmentManager().beginTransaction()
                            .replace(R.id.editor_layout_main, EditTileFragment.createInstance(view1.getName()))
                            .addToBackStack(null)
                            .commit(); */
                    Log.d(TAG, "onLongClick: name " + view1.getName());
                    mEditorListener.editTile(tile);
                    return true;
                }
            });
            params = new TileLayout.LayoutParams();
            params.setTop(tile.getY());
            params.setLeft(tile.getX());
            params.setWidth(tile.getWidth());
            params.setHeight(tile.getHeight());
            tileView.setLayoutParams(params);
            mTileLayout.addView(tileView);
            mViews.add(tileView);
        }
        mParser = new TileParser(mViews, getActivity());
    }

    public void addNewTile(final Tile tile) {
        TileView tileView = new TileView(getActivity());
        tileView.setData(tile.getData());
        tileView.setText(tile.getData());
        tileView.setColor(tile.getColor());
        tileView.setName(tile.getName());
        tileView.setTextSize(tile.getTextSize());
        tileView.setTextColor(tile.getTextColor());
        tileView.setTextX(tile.getTextX());
        tileView.setTextY(tile.getTextY());
        tileView.setBorderColor(tile.getBorderColor());
        tileView.setBorderSize(tile.getBorderSize());
        tileView.setOnFirstClick(tile.getOnFirstClick());
        tileView.setOnSecondClick(tile.getOnSecondClick());
        if (tile.isSwitch()) {
            tileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final TileView view1 = (TileView) view;
                    try {
                        if (view1.isSwitch())
                            mPort.write(view1.getOnFirstClick().getBytes(), 2000);
                        else
                            mPort.write(view1.getOnSecondClick().getBytes(), 2000);
                        view1.setSwitch();
                    } catch (IOException | NullPointerException ex) {
                        ex.printStackTrace();
                    }
                    final Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                    anim.setDuration(500);
                    view1.startAnimation(anim);
                }
            });
        } else {
            tileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final TileView view1 = (TileView) view;
                    try {
                        mPort.write(view1.getOnFirstClick().getBytes(), 2000);
                    } catch (IOException | NullPointerException ex) {
                        ex.printStackTrace();
                    }
                    final Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                    anim.setDuration(500);
                    view1.startAnimation(anim);
                }
            });
        }
        tileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mParser.parseString("-127");
                mParser.parseString("relay1 on");
                Log.d(TAG, "onClick: click");
            }
        });
        tileView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                TileView view1 = (TileView) view;
                //startActivity(TilesActivity.startEditFragment(view1.getName(), getActivity()));
                getActivity().findViewById(R.id.editor_layout_main).setVisibility(View.VISIBLE);
                Log.d(TAG, "onLongClick: " + view1.getName());
                mEditorListener.editTile(tile);
                return true;
            }
        });
        TileLayout.LayoutParams params = new TileLayout.LayoutParams();
        params.setTop(tile.getY());
        params.setLeft(tile.getX());
        params.setWidth(tile.getWidth());
        params.setHeight(tile.getHeight());
        tileView.setLayoutParams(params);
        mTileLayout.addView(tileView);
        mViews.add(tileView);
        mParser.addView(tileView);
        Log.d(TAG, "addNewTile: done");
    }

    public void removeView(String name) {
        TileView view = getTileViewByName(name);
        mViews.remove(view);
        mTileLayout.removeView(getTileViewByName(name));
    }

    private TileView getTileViewByName(String name) {
        for (TileView tileView : mViews) {
            Log.d(TAG, "getTileViewByName: " + tileView.getName());
            if (tileView.getName().equals(name))
                return tileView;
        }
        return null;
    }

    public void updateView(final Tile tile, String oldName) {
        Log.d(TAG, "updateView:  oldname " + oldName + " new " + tile.getName());
        TileView tileView = getTileViewByName(oldName);
        tileView.setData(tile.getData());
        tileView.setColor(tile.getColor());
        //tileView.setName(tile.getName());
        tileView.setTextSize(tile.getTextSize());
        tileView.setTextColor(tile.getTextColor());
        tileView.setTextX(tile.getTextX());
        tileView.setTextY(tile.getTextY());
        tileView.setBorderColor(tile.getBorderColor());
        tileView.setBorderSize(tile.getBorderSize());
        tileView.setOnFirstClick(tile.getOnFirstClick());
        tileView.setOnSecondClick(tile.getOnSecondClick());
        if (tile.isSwitch()) {
            tileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final TileView view1 = (TileView) view;
                    try {
                        if (view1.isSwitch())
                            mPort.write(view1.getOnFirstClick().getBytes(), 2000);
                        else
                            mPort.write(view1.getOnSecondClick().getBytes(), 2000);
                        view1.setSwitch();
                    } catch (IOException | NullPointerException ex) {
                        ex.printStackTrace();
                    }
                    final Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                    anim.setDuration(500);
                    view1.startAnimation(anim);
                }
            });
        } else {
            tileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final TileView view1 = (TileView) view;
                    try {
                        mPort.write(view1.getOnFirstClick().getBytes(), 2000);
                    } catch (IOException | NullPointerException ex) {
                        ex.printStackTrace();
                    }
                    final Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                    anim.setDuration(500);
                    view1.startAnimation(anim);
                }
            });
        }
        tileView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                TileView view1 = (TileView) view;
                Log.d(TAG, "onLongClick: name " + view1.getName());
                //startActivity(TilesActivity.startEditFragment(view1.getName(), getActivity()));
                mEditorListener.editTile(tile);
                return true;
            }
        });
        TileLayout.LayoutParams params;
        params = new TileLayout.LayoutParams();
        params.setTop(tile.getY());
        params.setLeft(tile.getX());
        params.setWidth(tile.getWidth());
        params.setHeight(tile.getHeight());
        tileView.setLayoutParams(params);
        tileView.invalidate();
    }

    public void updateView(final Tile tile) {
        TileView tileView = getTileViewByName(tile.getName());
        tileView.setData(tile.getData());
        tileView.setColor(tile.getColor());
        tileView.setName(tile.getName());
        tileView.setTextSize(tile.getTextSize());
        tileView.setTextColor(tile.getTextColor());
        tileView.setTextX(tile.getTextX());
        tileView.setTextY(tile.getTextY());
        tileView.setBorderColor(tile.getBorderColor());
        tileView.setBorderSize(tile.getBorderSize());
        tileView.setOnFirstClick(tile.getOnFirstClick());
        tileView.setOnSecondClick(tile.getOnSecondClick());
        if (tile.isSwitch()) {
            tileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final TileView view1 = (TileView) view;
                    try {
                        if (view1.isSwitch())
                            mPort.write(view1.getOnFirstClick().getBytes(), 2000);
                        else
                            mPort.write(view1.getOnSecondClick().getBytes(), 2000);
                        view1.setSwitch();
                    } catch (IOException | NullPointerException ex) {
                        ex.printStackTrace();
                    }
                    final Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                    anim.setDuration(500);
                    view1.startAnimation(anim);
                }
            });
        } else {
            tileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final TileView view1 = (TileView) view;
                    try {
                        mPort.write(view1.getOnFirstClick().getBytes(), 2000);
                    } catch (IOException | NullPointerException ex) {
                        ex.printStackTrace();
                    }
                    final Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
                    anim.setDuration(500);
                    view1.startAnimation(anim);
                }
            });
        }
        tileView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                TileView view1 = (TileView) view;
                //startActivity(TilesActivity.startEditFragment(view1.getName(), getActivity()));
                Log.d(TAG, "onLongClick: name " + view1.getName());
                mEditorListener.editTile(tile);
                return true;
            }
        });
        TileLayout.LayoutParams params;
        params = new TileLayout.LayoutParams();
        params.setTop(tile.getY());
        params.setLeft(tile.getX());
        params.setWidth(tile.getWidth());
        params.setHeight(tile.getHeight());
        tileView.setLayoutParams(params);
        tileView.invalidate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mParser.unregisterReceivers();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        stopIoManager();
    }

    private void stopIoManager() {
        if (mSerialManager != null) {
            Log.i(TAG, "Stopping io manager ..");
            mSerialManager.stop();
            mSerialManager = null;
        }
        if (mPort != null) {
            try {
                mPort.close();
            } catch (IOException e) {
                // Ignore.
            }
            mPort = null;
        }
    }

    private void startIoManager() {
        // Find all available drivers from attached devices.
        UsbManager manager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (availableDrivers.isEmpty()) {
            Toast.makeText(getActivity(), "drivers empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Open a connection to the first available driver.
        UsbSerialDriver driver = availableDrivers.get(0);
        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
            Toast.makeText(getActivity(), "connection is null", Toast.LENGTH_SHORT).show();
            // You probably need to call UsbManager.requestPermission(driver.getDevice(), ..)
            PendingIntent mPendingIntent = PendingIntent.getActivity(getActivity(), 0, new Intent(getActivity(), MainActivity.class), 0);
            manager.requestPermission(driver.getDevice(), mPendingIntent);
            return;
        }
        mPort = driver.getPorts().get(0);
        try {
            mPort.open(connection);
            mPort.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            byte buffer[] = new byte[16];
            int numBytesRead = mPort.read(buffer, 1000);
            Log.d(TAG, "Read " + numBytesRead + " bytes. " + new String(buffer));
            Toast.makeText(getActivity(), "Read " + numBytesRead + " bytes. " + new String(buffer), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // Deal with error.
        }

        if (mPort != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialManager = new SerialInputOutputManager(mPort, mSerialListener);
            mExecutor.submit(mSerialManager);
        }
    }
}
