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

import net.arkellyga.arduinolistener.ui.ColorSquare;
import net.arkellyga.arduinolistener.R;
import net.arkellyga.arduinolistener.utils.Tile;
import net.arkellyga.arduinolistener.utils.TilesFactory;

import yuku.ambilwarna.AmbilWarnaDialog;

public class ColorEditorFragment extends Fragment {
    private static final String KEY_NAME = "name";
    private TilesFactory mFactory;
    private Tile mTile;

    private ImageButton mBtnSizeFastBack, mBtnSizeBack, mBtnSizeForward, mBtnSizeFastForward;
    private EditText mEtSize;

    private View mColorLayout;
    private ColorSquare mColor;

    private View mBorderColorLayout;
    private ColorSquare mBorderColor;

    private View mTextColorLayout;
    private ColorSquare mTextColor;

    private onColorUpdateListener mListener;
    public interface onColorUpdateListener {
        void updateColor(Tile tile);
    }

    public static ColorEditorFragment createInstance(String name) {
        ColorEditorFragment fragment = new ColorEditorFragment();
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
        mListener = (onColorUpdateListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.color_page_editor, null);
        setupUI(v);
        return v;
    }

    void setupUI(View v) {
        setupBorderSize(v);
        mColorLayout = v.findViewById(R.id.layout_background_color_page);
        mBorderColorLayout = v.findViewById(R.id.layout_border_color_page);
        mTextColorLayout = v.findViewById(R.id.layout_text_color_color_page);
        mColor = v.findViewById(R.id.color_background_color_page);
        mBorderColor = v.findViewById(R.id.color_border_color_page);
        mTextColor = v.findViewById(R.id.color_text_color_page);

        mColor.setColor(mTile.getColor());
        mColorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(), mColor.getColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        mColor.setColor(color);
                        mTile.setColor(color);
                        mListener.updateColor(mTile);
                    }
                });
                dialog.show();
            }
        });

        mBorderColor.setColor(mTile.getBorderColor());
        mBorderColorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(), mBorderColor.getColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        mBorderColor.setColor(color);
                        mTile.setBorderColor(color);
                        mListener.updateColor(mTile);
                    }
                });
                dialog.show();
            }
        });

        mTextColor.setColor(mTile.getColor());
        mTextColorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(), mTextColor.getColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        mTextColor.setColor(color);
                        mTile.setTextColor(color);
                        mListener.updateColor(mTile);
                    }
                });
                dialog.show();
            }
        });
    }

    void setupBorderSize(View v) {
        mEtSize = v.findViewById(R.id.border_size_edittext_color_page);
        mEtSize.setText(String.valueOf(mTile.getBorderSize()));

        mBtnSizeFastBack = v.findViewById(R.id.fast_back_button_border_size_color_page);
        mBtnSizeBack = v.findViewById(R.id.back_button_border_size_color_page);
        mBtnSizeFastForward = v.findViewById(R.id.fast_forward_button_border_size_color_page);
        mBtnSizeForward = v.findViewById(R.id.forward_button_border_size_color_page);

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
                mTile.setBorderSize(Integer.parseInt(charSequence.toString()));
                mListener.updateColor(mTile);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
