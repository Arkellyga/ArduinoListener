package net.arkellyga.arduinolistener.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import net.arkellyga.arduinolistener.R;
import net.arkellyga.arduinolistener.utils.Tile;
import net.arkellyga.arduinolistener.utils.TilesFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TilesList extends Fragment {
    private static final String TAG = "TilesList";
    private ListView mLvTiles;
    private Button mBtnAdd;
    private ArrayList<Map<String, Object>> mTiles;
    private TilesFactory mFactory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFactory = new TilesFactory(getActivity());
        mTiles = parseTiles();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_tiles_fragment, null);
        setupUI(v);
        return v;
    }

    private ArrayList<Map<String, Object>> parseTiles() {
        ArrayList<Map<String, Object>> names = new ArrayList<>();
        Map<String, Object> m;
        ArrayList<Tile> tiles = mFactory.getTiles();
        for (Tile tile : tiles) {
            m = new HashMap<>();
            m.put("name", tile.getName());
            names.add(m);
        }
        return names;
    }

    private void setupUI(View v) {
        mLvTiles = v.findViewById(R.id.tiles_list_view_tiles);
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), mTiles, R.layout.layout_tile_list,
                new String[] {"name"}, new int[] {R.id.name_textview_tile_list});
        mLvTiles.setAdapter(adapter);
        mLvTiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EditTileFragment fragment = EditTileFragment.createInstance((String) mTiles.get(i).get("name"));
                getActivity().getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, fragment)
                        .commit();
            }
        });
        mBtnAdd = v.findViewById(R.id.add_button_tiles);
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, new EditTileFragment())
                        .commit();
            }
        });
    }

}
