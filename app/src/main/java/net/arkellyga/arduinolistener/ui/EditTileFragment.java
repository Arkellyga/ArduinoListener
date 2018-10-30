package net.arkellyga.arduinolistener.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import net.arkellyga.arduinolistener.R;
import net.arkellyga.arduinolistener.utils.Tile;
import net.arkellyga.arduinolistener.utils.TilesFactory;

import yuku.ambilwarna.AmbilWarnaDialog;

import static net.arkellyga.arduinolistener.utils.Consts.*;

public class EditTileFragment extends Fragment {
    private EditText mEtName, mEtData, mEtFirstClick, mEtSecondClick;
    private CheckBox mCbSwitch;
    private Button mBtnOk, mBtnCancel, mBtnDelete;
    private TextView mTvX, mTvY, mTvWidth, mTvHeight, mTvTextSize, mTvTextX, mTvTextY;
    private AppCompatSeekBar mSbX, mSbY, mSbWidth, mSbHeight, mSbTextSize, mSbTextX, mSbTextY;
    private ColorSquare mColorPicker, mTextColorPicker, mBorderColorPicker;
    private SharedPreferences mSharedPreferences;
    private TilesFactory mFactory;

    private TileView mExample;
    private LinearLayout.LayoutParams mParams;

    private static final String KEY_NAME = "name";
    private static final int CELL_SIZE = 48;
    private Tile mTile;

    public static EditTileFragment createInstance(String name) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_NAME, name);
        EditTileFragment fragment = new EditTileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParams = new LinearLayout.LayoutParams(CELL_SIZE, CELL_SIZE);
        mParams.gravity = Gravity.CENTER_HORIZONTAL;
        mFactory = new TilesFactory(getActivity());
        try {
            mListener = (OnFragmentInteractionListener) getActivity();
            Log.d("EditTile", "onAttach: done");
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        if (getArguments() != null) {
            String name = getArguments().getString(KEY_NAME);
            if (name != null) {
                mTile = mFactory.getTileByName(name);
                Log.d("EDIT", "onCreateView: " + mTile.getTextX());
            }
        } else {
            mTile = new Tile();
            // Set default values
            mTile.setName("New");
            mTile.setData("$$");
            mTile.setX(0);
            mTile.setY(0);
            mTile.setHeight(1);
            mTile.setWidth(1);
            mTile.setColor(Color.BLACK);
            mTile.setTextSize(16);
            mTile.setTextColor(Color.WHITE);
            mTile.setTextX(10);
            mTile.setTextY(10);
            mTile.setBorderColor(Color.GRAY);
            mFactory.addTile(mTile);
            mFactory.save();
            addNewTile();
        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_edit_tile_fragment, null);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        setupUI(v);
        return v;
    }

    private void setupUI(final View v) {
        mEtName = v.findViewById(R.id.name_edittext_edit_tile);
        mEtData = v.findViewById(R.id.data_edittext_edit_tile);
        mEtFirstClick = v.findViewById(R.id.first_click_edittext_edit_tile);
        mEtSecondClick = v.findViewById(R.id.second_click_edittext_tile);

        mCbSwitch = v.findViewById(R.id.switch_checkbox_edit_tile);
        mCbSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                v.findViewById(R.id.layout_second_click_edit_tile).setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        mSbX = v.findViewById(R.id.x_seekbar_edit_tile);
        mSbX.setMax(Integer.parseInt(mSharedPreferences.getString(PREF_TILES_ROW, "10")));
        mTvX = v.findViewById(R.id.x_textview_edit_tile);
        mTvX.setText(getResources().getString(R.string.tile_x, mSbX.getProgress()));

        mSbY = v.findViewById(R.id.y_seekbar_edit_tile);
        mSbY.setMax(Integer.parseInt(mSharedPreferences.getString(PREF_TILES_COLUMN, "10")));
        mTvY = v.findViewById(R.id.y_textview_edit_tile);
        mTvY.setText(getResources().getString(R.string.tile_y, mSbY.getProgress()));

        mSbTextSize = v.findViewById(R.id.textsize_seekbar_edit_tile);
        mSbTextSize.setMax(100);
        mTvTextSize = v.findViewById(R.id.textsize_textview_edit_tile);
        mTvTextSize.setText(getResources().getString(R.string.tile_text_size, mSbTextSize.getProgress()));

        mSbWidth = v.findViewById(R.id.width_seekbar_edit_tile);
        mSbWidth.setMax(Integer.parseInt(mSharedPreferences.getString(PREF_TILES_ROW, "10")));
        mTvWidth = v.findViewById(R.id.width_textview_edit_tile);
        mTvWidth.setText(getResources().getString(R.string.tile_width, mSbWidth.getProgress()));

        mSbHeight = v.findViewById(R.id.height_seekbar_edit_tile);
        mSbHeight.setMax(Integer.parseInt(mSharedPreferences.getString(PREF_TILES_COLUMN, "10")));
        mTvHeight = v.findViewById(R.id.height_textview_edit_tile);
        mTvHeight.setText(getResources().getString(R.string.tile_height, mSbHeight.getProgress()));

        mSbTextX = v.findViewById(R.id.x_text_seekbar_edit_tile);
        mSbTextX.setMax(CELL_SIZE);
        mTvTextX = v.findViewById(R.id.x_text_textview_edit_tile);
        mTvTextX.setText(getResources().getString(R.string.tile_text_x, mSbTextX.getProgress()));

        mSbTextY = v.findViewById(R.id.y_text_seekbar_edit_tile);
        mSbTextY.setMax(CELL_SIZE);
        mTvTextY = v.findViewById(R.id.y_text_textview_edit_tile);
        mTvTextY.setText(getResources().getString(R.string.tile_text_y, mSbTextY.getProgress()));

        mExample = v.findViewById(R.id.example_tileview_edit_tile);
        mBorderColorPicker = v.findViewById(R.id.border_color_square_edit_tile);
        mColorPicker = v.findViewById(R.id.color_square_edit_tile);
        mTextColorPicker = v.findViewById(R.id.text_color_square_edit_tile);
        mBtnOk = v.findViewById(R.id.ok_button_edit_tile);
        mBtnCancel = v.findViewById(R.id.cancel_button_edit_tile);
        mBtnDelete = v.findViewById(R.id.delete_button_edit_tile);
        setFields();
        setupListeners(v);
    }

    private void setupListeners(View v) {
        mSbTextX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d("Edit", "onProgressChanged: " + i);
                mTvTextX.setText(getResources().getString(R.string.tile_text_x, i));
                mTile.setTextX(i);
                updateTile();
                mExample.setTextX(i);
                mExample.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSbTextY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mTvTextY.setText(getResources().getString(R.string.tile_text_y, i));
                mTile.setTextY(i);
                updateTile();
                mExample.setTextY(i);
                mExample.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSbX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mTvX.setText(getResources().getString(R.string.tile_x, i));
                mTile.setX(i);
                updateTile();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSbY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mTvY.setText(getResources().getString(R.string.tile_y, i));
                mTile.setY(i);
                updateTile();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSbWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mTvWidth.setText(getResources().getString(R.string.tile_width, i));
                mTile.setWidth(i);
                updateTile();
                mSbTextX.setMax(i * CELL_SIZE);
                mParams.width = i * CELL_SIZE;
                mExample.setLayoutParams(mParams);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSbHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mTvHeight.setText(getResources().getString(R.string.tile_height, i));
                mTile.setHeight(i);
                updateTile();
                mSbTextY.setMax(i * CELL_SIZE);
                mParams.height = i * CELL_SIZE;
                mExample.setLayoutParams(mParams);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSbTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mTvTextSize.setText(getResources().getString(R.string.tile_text_size, i));
                mTile.setTextSize(i);
                updateTile();
                mExample.setTextSize(i);
                mExample.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Ok", Toast.LENGTH_SHORT).show();
                save();
                getActivity().getFragmentManager().beginTransaction().remove(EditTileFragment.this).commit();
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
                getActivity().getFragmentManager().beginTransaction().remove(EditTileFragment.this).commit();
            }
        });
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTile != null) {
                    mFactory.removeTileByName(mTile.getName());
                    mFactory.save();
                }
                getActivity().getFragmentManager().beginTransaction().remove(EditTileFragment.this).commit();
            }
        });
        mColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(), Color.RED, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        mColorPicker.setColor(color);
                        mTile.setColor(color);
                        updateTile();
                        Log.d("edittile", "onOk: " + color);
                    }
                });
                dialog.show();
            }
        });
        mTextColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(), Color.RED, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        mTextColorPicker.setColor(color);
                        mTile.setTextColor(color);
                        updateTile();
                        mExample.setTextColor(color);
                        mExample.invalidate();
                        Log.d("edittile", "onOk: " + color);
                    }
                });
                dialog.show();
            }
        });

        mBorderColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(), Color.RED, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        mBorderColorPicker.setColor(color);
                        mTile.setBorderColor(color);
                        updateTile();
                    }
                });
                dialog.show();
            }
        });
    }

    private OnFragmentInteractionListener mListener;
    interface OnFragmentInteractionListener {
        void updateTile(Tile tile);
        void addNewTile(Tile tile);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void addNewTile() {
        if (mTile != null && mListener != null)
            mListener.addNewTile(mTile);
    }

    private void updateTile() {
        if (mTile != null && mListener != null)
            mListener.updateTile(mTile);
    }

    private void openTilesFragment() {
        getActivity().getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new TilesList())
                .commit();
    }

    private void setFields() {
        if (mTile != null) {
            mEtName.setText(mTile.getName());
            mEtData.setText(mTile.getData());
            mEtFirstClick.setText(mTile.getOnFirstClick());
            mEtSecondClick.setText(mTile.getOnSecondClick());
            mCbSwitch.setChecked(mTile.isSwitch());
            mSbX.setProgress(mTile.getX());
            mSbY.setProgress(mTile.getY());
            mSbWidth.setProgress(mTile.getWidth());
            mSbHeight.setProgress(mTile.getHeight());
            mSbTextSize.setProgress(mTile.getTextSize());

            mSbTextX.setMax(1000);
            mSbTextY.setMax(1000);
            Log.d("Edit", "setFields: " + mTile.getTextX());
            mSbTextX.setProgress(mTile.getTextX());
            mSbTextY.setProgress(mTile.getTextY());

            mColorPicker.setColor(mTile.getColor());
            mTextColorPicker.setColor(mTile.getTextColor());
            mBorderColorPicker.setColor(mTile.getBorderColor());

            mParams = new LinearLayout.LayoutParams(mTile.getWidth() * CELL_SIZE, mTile.getHeight() * CELL_SIZE);
            mParams.gravity = Gravity.CENTER_HORIZONTAL;

            mExample.setColor(mTile.getColor());
            mExample.setName(mTile.getName());
            mExample.setTextSize(mTile.getTextSize());
            mExample.setTextX(mTile.getTextX());
            mExample.setTextY(mTile.getTextY());
            mExample.setBorderColor(mTile.getBorderColor());
        }
        mExample.setLayoutParams(mParams);
    }

    private void save() {
        if (mTile != null)
            mFactory.removeTileByName(mTile.getName());
        else
            mTile = new Tile();
        mTile.setName(mEtName.getText().toString());
        mTile.setData(mEtData.getText().toString());
        mTile.setX(mSbX.getProgress());
        mTile.setY(mSbY.getProgress());
        mTile.setHeight(mSbHeight.getProgress());
        mTile.setWidth(mSbWidth.getProgress());
        mTile.setColor(mColorPicker.getColor());
        mTile.setTextSize(mSbTextSize.getProgress());
        mTile.setTextColor(mTextColorPicker.getColor());
        mTile.setTextX(mSbTextX.getProgress());
        Log.d("Edit", "save: " + mSbTextX.getProgress());
        mTile.setTextY(mSbTextY.getProgress());
        mTile.setOnFirstClick(mEtFirstClick.getText().toString());
        mTile.setOnSecondClick(mEtSecondClick.getText().toString());
        mTile.setSwitch(mCbSwitch.isChecked());
        mTile.setBorderColor(mBorderColorPicker.getColor());

        mFactory.addTile(mTile);
        mFactory.save();
    }
}
