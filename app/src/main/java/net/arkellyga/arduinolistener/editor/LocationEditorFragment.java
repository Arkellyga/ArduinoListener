package net.arkellyga.arduinolistener.editor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import net.arkellyga.arduinolistener.R;
import net.arkellyga.arduinolistener.utils.Tile;
import net.arkellyga.arduinolistener.utils.TilesFactory;

public class LocationEditorFragment extends Fragment {
    private static final String KEY_NAME = "name";
    private TilesFactory mFactory;
    private Tile mTile;

    private EditText mEtName;

    private ImageButton mBtnXFastBack, mBtnXBack, mBtnXForward, mBtnXFastForward;
    private EditText mEtX;

    private ImageButton mBtnYFastBack, mBtnYBack, mBtnYForward, mBtnYFastForward;
    private EditText mEtY;

    private ImageButton mBtnWidthFastBack, mBtnWidthBack, mBtnWidthForward, mBtnWidthFastForward;
    private EditText mEtWidth;

    private ImageButton mBtnHeightFastBack, mBtnHeightBack, mBtnHeightForward, mBtnHeightFastForward;
    private EditText mEtHeight;

    private onLocationUpdateListener mListener;
    public interface onLocationUpdateListener {
        void updateLocation(Tile tile);
    }

    public static LocationEditorFragment createInstance(String name) {
        LocationEditorFragment fragment = new LocationEditorFragment();
        if (name != null) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_NAME, name);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFactory = new TilesFactory(getActivity());
        if (getArguments() != null)
            mTile = mFactory.getTileByName(getArguments().getString(KEY_NAME));
        else
            mTile = new Tile();
        mListener = (onLocationUpdateListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.location_page_editor, null);
        setupUI(v);
        return v;
    }

    private void setupUI(View v) {
        mEtName = v.findViewById(R.id.name_edittext_location_page);
        mEtName.setText(mTile.getName());
        mEtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    mTile.setName(charSequence.toString());
                    mListener.updateLocation(mTile);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setupX(v);
        setupY(v);
        setupWidth(v);
        setupHeight(v);
    }

    private void setupX(View v) {
        mEtX = v.findViewById(R.id.tile_x_edittext_location_page);
        if (mTile != null)
            mEtX.setText(String.valueOf(mTile.getX()));
        mBtnXFastBack = v.findViewById(R.id.fast_back_button_tile_x_location_page);
        mBtnXBack = v.findViewById(R.id.back_button_tile_x_location_page);
        mBtnXFastForward = v.findViewById(R.id.fast_forward_button_tile_x_location_page);
        mBtnXForward = v.findViewById(R.id.forward_button_tile_x_location_page);

        mBtnXFastBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtX.getText().toString());
                i -= 10;
                mEtX.setText(String.valueOf(i));
            }
        });

        mBtnXBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtX.getText().toString());
                i --;
                mEtX.setText(String.valueOf(i));
            }
        });

        mBtnXForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtX.getText().toString());
                i++;
                mEtX.setText(String.valueOf(i));
            }
        });

        mBtnXFastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtX.getText().toString());
                i += 10;
                mEtX.setText(String.valueOf(i));
            }
        });

        mEtX.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    mTile.setX(Integer.parseInt(charSequence.toString()));
                    mListener.updateLocation(mTile);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setupY(View v) {
        mEtY = v.findViewById(R.id.tile_y_edittext_location_page);
        if (mTile != null)
            mEtY.setText(String.valueOf(mTile.getY()));
        mBtnYFastBack = v.findViewById(R.id.fast_back_button_tile_y_location_page);
        mBtnYBack = v.findViewById(R.id.back_button_tile_y_location_page);
        mBtnYFastForward = v.findViewById(R.id.fast_forward_button_tile_y_location_page);
        mBtnYForward = v.findViewById(R.id.forward_button_tile_y_location_page);

        mBtnYFastBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtY.getText().toString());
                i -= 10;
                mEtY.setText(String.valueOf(i));
            }
        });

        mBtnYBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtY.getText().toString());
                i --;
                mEtY.setText(String.valueOf(i));
            }
        });

        mBtnYForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtY.getText().toString());
                i++;
                mEtY.setText(String.valueOf(i));
            }
        });

        mBtnYFastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtY.getText().toString());
                i += 10;
                mEtY.setText(String.valueOf(i));
            }
        });

        mEtY.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    mTile.setY(Integer.parseInt(charSequence.toString()));
                    mListener.updateLocation(mTile);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setupWidth(View v) {
        mEtWidth = v.findViewById(R.id.tile_width_edittext_location_page);
        if (mTile != null)
            mEtWidth.setText(String.valueOf(mTile.getWidth()));
        mBtnWidthFastBack = v.findViewById(R.id.fast_back_button_tile_width_location_page);
        mBtnWidthBack = v.findViewById(R.id.back_button_tile_width_location_page);
        mBtnWidthFastForward = v.findViewById(R.id.fast_forward_button_tile_width_location_page);
        mBtnWidthForward = v.findViewById(R.id.forward_button_tile_width_location_page);

        mBtnWidthFastBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtWidth.getText().toString());
                i -= 10;
                mEtWidth.setText(String.valueOf(i));
            }
        });

        mBtnWidthBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtWidth.getText().toString());
                i --;
                mEtWidth.setText(String.valueOf(i));
            }
        });

        mBtnWidthForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtWidth.getText().toString());
                i++;
                mEtWidth.setText(String.valueOf(i));
            }
        });

        mBtnWidthFastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtWidth.getText().toString());
                i += 10;
                mEtWidth.setText(String.valueOf(i));
            }
        });

        mEtWidth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    mTile.setWidth(Integer.parseInt(charSequence.toString()));
                    mListener.updateLocation(mTile);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setupHeight(View v) {
        mEtHeight = v.findViewById(R.id.tile_height_edittext_location_page);
        if (mTile != null)
            mEtHeight.setText(String.valueOf(mTile.getHeight()));
        mBtnHeightFastBack = v.findViewById(R.id.fast_back_button_tile_height_location_page);
        mBtnHeightBack = v.findViewById(R.id.back_button_tile_height_location_page);
        mBtnHeightFastForward = v.findViewById(R.id.fast_forward_button_tile_height_location_page);
        mBtnHeightForward = v.findViewById(R.id.forward_button_tile_height_location_page);

        mBtnHeightFastBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtHeight.getText().toString());
                i -= 10;
                mEtHeight.setText(String.valueOf(i));
            }
        });

        mBtnHeightBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtHeight.getText().toString());
                i --;
                mEtHeight.setText(String.valueOf(i));
            }
        });

        mBtnHeightForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtHeight.getText().toString());
                i++;
                mEtHeight.setText(String.valueOf(i));
            }
        });

        mBtnHeightFastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtHeight.getText().toString());
                i += 10;
                mEtHeight.setText(String.valueOf(i));
            }
        });

        mEtHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    mTile.setHeight(Integer.parseInt(charSequence.toString()));
                    mListener.updateLocation(mTile);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
