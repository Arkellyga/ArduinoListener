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

public class TextEditorFragment extends Fragment {
    private static final String KEY_NAME = "name";
    private TilesFactory mFactory;
    private Tile mTile;

    private EditText mEtData;

    private ImageButton mBtnSizeFastBack, mBtnSizeBack, mBtnSizeForward, mBtnSizeFastForward;
    private EditText mEtSize;

    private ImageButton mBtnXFastBack, mBtnXBack, mBtnXForward, mBtnXFastForward;
    private EditText mEtX;

    private ImageButton mBtnYFastBack, mBtnYBack, mBtnYForward, mBtnYFastForward;
    private EditText mEtY;

    private onTextUpdateListener mListener;
    public interface onTextUpdateListener {
        void updateText(Tile tile);
    }

    public static TextEditorFragment createInstance(String name) {
        TextEditorFragment fragment = new TextEditorFragment();
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
        mListener = (onTextUpdateListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.text_page_editor, null);
        setupUI(v);
        return v;
    }

    private void setupUI(View v) {
        mEtData = v.findViewById(R.id.data_edittext_text_page);
        mEtData.setText(mTile.getData());
        mEtData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mTile.setData(charSequence.toString());
                mListener.updateText(mTile);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        setupX(v);
        setupY(v);
        setupTextSize(v);
    }

    private void setupX(View v) {
        mEtX = v.findViewById(R.id.text_x_edittext_text_page);
        if (mTile != null)
            mEtX.setText(String.valueOf(mTile.getX()));
        mBtnXFastBack = v.findViewById(R.id.fast_back_button_text_x_text_page);
        mBtnXBack = v.findViewById(R.id.back_button_text_x_text_page);
        mBtnXFastForward = v.findViewById(R.id.fast_forward_button_text_x_text_page);
        mBtnXForward = v.findViewById(R.id.forward_button_text_x_text_page);

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
                mTile.setTextX(Integer.parseInt(charSequence.toString()));
                mListener.updateText(mTile);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setupY(View v) {
        mEtY = v.findViewById(R.id.text_y_edittext_text_page);
        if (mTile != null)
            mEtY.setText(String.valueOf(mTile.getY()));
        mBtnYFastBack = v.findViewById(R.id.fast_back_button_text_y_text_page);
        mBtnYBack = v.findViewById(R.id.back_button_text_y_text_page);
        mBtnYFastForward = v.findViewById(R.id.fast_forward_button_text_y_text_page);
        mBtnYForward = v.findViewById(R.id.forward_button_text_y_text_page);

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
                mTile.setTextY(Integer.parseInt(charSequence.toString()));
                mListener.updateText(mTile);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setupTextSize(View v) {
        mEtSize = v.findViewById(R.id.text_size_edittext_text_page);
        if (mTile != null)
            mEtSize.setText(String.valueOf(mTile.getTextSize()));
        mBtnSizeFastBack = v.findViewById(R.id.fast_back_button_text_size_text_page);
        mBtnSizeBack = v.findViewById(R.id.back_button_text_size_text_page);
        mBtnSizeFastForward = v.findViewById(R.id.fast_forward_button_text_size_text_page);
        mBtnSizeForward = v.findViewById(R.id.forward_button_text_size_text_page);

        mBtnSizeFastBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtSize.getText().toString());
                i -= 10;
                mEtSize.setText(String.valueOf(i));
            }
        });

        mBtnSizeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtSize.getText().toString());
                i --;
                mEtSize.setText(String.valueOf(i));
            }
        });

        mBtnSizeForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtSize.getText().toString());
                i++;
                mEtSize.setText(String.valueOf(i));
            }
        });

        mBtnSizeFastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(mEtSize.getText().toString());
                i += 10;
                mEtSize.setText(String.valueOf(i));
            }
        });

        mEtSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mTile.setTextSize(Integer.parseInt(charSequence.toString()));
                mListener.updateText(mTile);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}
