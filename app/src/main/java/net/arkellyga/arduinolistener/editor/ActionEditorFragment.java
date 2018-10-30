package net.arkellyga.arduinolistener.editor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import net.arkellyga.arduinolistener.R;
import net.arkellyga.arduinolistener.utils.Tile;
import net.arkellyga.arduinolistener.utils.TilesFactory;

public class ActionEditorFragment extends Fragment {
    private static final String KEY_NAME = "name";
    private TilesFactory mFactory;
    private Tile mTile;

    private CheckBox mCbSwitch;
    private View mLayoutSecond;
    private EditText mEtFirstClick, mEtSecondClick;

    private onActionUpdateListener mListener;

    public interface onActionUpdateListener {
        void updateAction(Tile tile);
    }

    public static ActionEditorFragment createInstance(String name) {
        ActionEditorFragment fragment = new ActionEditorFragment();
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
        mListener = (onActionUpdateListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.action_page_editor, null);
        setupUI(v);
        return v;
    }

    void setupUI(View v) {
        mLayoutSecond = v.findViewById(R.id.layout_second_click_action_page);
        mEtFirstClick = v.findViewById(R.id.first_click_edittext_action_page);
        mEtFirstClick.setText(mTile.getOnFirstClick());
        mEtFirstClick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mTile.setOnFirstClick(charSequence.toString());
                mListener.updateAction(mTile);
            }

            @Override
            public void afterTextChanged(Editable editable) {            }
        });

        mEtSecondClick = v.findViewById(R.id.second_click_edittext_action_page);
        mEtSecondClick.setText(mTile.getOnSecondClick());
        mEtSecondClick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mTile.setOnSecondClick(charSequence.toString());
                mListener.updateAction(mTile);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mCbSwitch = v.findViewById(R.id.switch_checkbox_action_page);
        mCbSwitch.setChecked(mTile.isSwitch());
        mCbSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    mLayoutSecond.setVisibility(View.VISIBLE);
                else
                    mLayoutSecond.setVisibility(View.GONE);
                mTile.setSwitch(b);
                mListener.updateAction(mTile);
            }
        });
    }
}