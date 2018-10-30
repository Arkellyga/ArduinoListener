package net.arkellyga.arduinolistener.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TilesFactory {
    private static final String KEY_NAME = "name";
    private static final String KEY_DATA = "data";
    private static final String KEY_X = "x";
    private static final String KEY_Y = "y";
    private static final String KEY_WIDTH = "width";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_COLOR = "color";
    private static final String KEY_BORDER_COLOR = "border_color";
    private static final String KEY_BORDER_SIZE = "border_size";
    private static final String KEY_TEXT_SIZE = "text_size";
    private static final String KEY_TEXT_COLOR = "text_color";
    private static final String KEY_TEXT_X = "text_x";
    private static final String KEY_TEXT_Y = "text_y";
    private static final String KEY_SWITCH  = "switch";
    private static final String KEY_FIRST_CLICK = "first_click";
    private static final String KEY_SECOND_CLICK  = "second_click";

    private static final String mFileName  = "tiles.xml";

    private Context mContext;
    private JSONArray mArray;

    public TilesFactory(Context context) {
        mContext = context;
        openFile();
    }

    private void openFile() {
        boolean fileCheck = true;
        if (!mContext.getFileStreamPath(mFileName).exists())
            fileCheck = false;
        if (fileCheck) {
            StringBuilder sb = new StringBuilder();
            int content;
            try {
                FileInputStream is = mContext.openFileInput(mFileName);
                while ((content = is.read()) != -1) {
                    sb.append((char) content);
                }
                mArray = new JSONArray(sb.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } else
            mArray = new JSONArray();
    }

    public Tile getTileByName(String name) {
        try {
            JSONObject object;
            for (int i = 0; i < mArray.length(); i++) {
                object = mArray.getJSONObject(i);
                if (object.getString(KEY_NAME).equals(name)) {
                    Tile tile = new Tile();
                    tile.setName(object.getString(KEY_NAME));
                    tile.setData(object.getString(KEY_DATA));
                    tile.setX(object.getInt(KEY_X));
                    tile.setY(object.getInt(KEY_Y));
                    tile.setWidth(object.getInt(KEY_WIDTH));
                    tile.setHeight(object.getInt(KEY_HEIGHT));
                    tile.setColor(object.getInt(KEY_COLOR));
                    tile.setBorderColor(object.getInt(KEY_BORDER_COLOR));
                    tile.setBorderSize(object.getInt(KEY_BORDER_SIZE));
                    tile.setTextSize(object.getInt(KEY_TEXT_SIZE));
                    tile.setTextColor(object.getInt(KEY_TEXT_COLOR));
                    tile.setTextX(object.getInt(KEY_TEXT_X));
                    Log.d("Factory", "getTileByName: " + tile.getTextX() + " " + object.getInt(KEY_TEXT_X));
                    tile.setTextY(object.getInt(KEY_TEXT_Y));
                    tile.setSwitch(object.getBoolean(KEY_SWITCH));
                    tile.setOnFirstClick(object.getString(KEY_FIRST_CLICK));
                    tile.setOnSecondClick(object.getString(KEY_SECOND_CLICK));
                    return tile;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Tile> getTiles() {
        ArrayList<Tile> list = new ArrayList<>();
        try {
            Tile tile;
            JSONObject object;
            for (int i = 0; i < mArray.length(); i++) {
                tile = new Tile();
                object = mArray.getJSONObject(i);
                tile.setName(object.getString(KEY_NAME));
                tile.setData(object.getString(KEY_DATA));
                tile.setX(object.getInt(KEY_X));
                tile.setY(object.getInt(KEY_Y));
                tile.setWidth(object.getInt(KEY_WIDTH));
                tile.setHeight(object.getInt(KEY_HEIGHT));
                tile.setColor(object.getInt(KEY_COLOR));
                tile.setBorderColor(object.getInt(KEY_BORDER_COLOR));
                tile.setBorderSize(object.getInt(KEY_BORDER_SIZE));
                tile.setTextSize(object.getInt(KEY_TEXT_SIZE));
                tile.setTextColor(object.getInt(KEY_TEXT_COLOR));
                tile.setTextX(object.getInt(KEY_TEXT_X));
                tile.setTextY(object.getInt(KEY_TEXT_Y));
                tile.setSwitch(object.getBoolean(KEY_SWITCH));
                tile.setOnFirstClick(object.getString(KEY_FIRST_CLICK));
                tile.setOnSecondClick(object.getString(KEY_SECOND_CLICK));
                list.add(tile);
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addTile(Tile tile) {
        try {
            removeTileByName(tile.getName());
            JSONObject object = new JSONObject();
            object.put(KEY_NAME, tile.getName());
            object.put(KEY_DATA, tile.getData());
            object.put(KEY_X, tile.getX());
            object.put(KEY_Y, tile.getY());
            object.put(KEY_WIDTH, tile.getWidth());
            object.put(KEY_HEIGHT, tile.getHeight());
            object.put(KEY_COLOR, tile.getColor());
            object.put(KEY_BORDER_COLOR, tile.getBorderColor());
            object.put(KEY_BORDER_SIZE, tile.getBorderSize());
            object.put(KEY_TEXT_SIZE, tile.getTextSize());
            object.put(KEY_TEXT_COLOR, tile.getTextColor());
            object.put(KEY_TEXT_X, tile.getTextX());
            Log.d("Factory", "addTile: " + tile.getTextX());
            object.put(KEY_TEXT_Y, tile.getTextY());
            object.put(KEY_FIRST_CLICK, tile.getOnFirstClick());
            object.put(KEY_SECOND_CLICK, tile.getOnSecondClick());
            object.put(KEY_SWITCH, tile.isSwitch());
            mArray.put(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void removeTileByName(String name) {
        JSONArray newArray = new JSONArray();
        try {
            JSONObject object;
            for (int i = 0; i < mArray.length(); i++) {
                object = mArray.getJSONObject(i);
                if (!object.getString(KEY_NAME).equals(name))
                    newArray.put(object);
            }
            mArray = newArray;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            File file = mContext.getFileStreamPath("tiles.xml");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(mArray.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
