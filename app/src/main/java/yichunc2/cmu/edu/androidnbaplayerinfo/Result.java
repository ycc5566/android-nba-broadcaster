package yichunc2.cmu.edu.androidnbaplayerinfo;

import android.graphics.Bitmap;
import org.json.JSONObject;


// Seperation of concern.
public class Result {
    JSONObject playerInfo;
    Bitmap playerPic;

    public JSONObject getPlayerInfo() {
        return playerInfo;
    }

    public Bitmap getPlayerPic() {
        return playerPic;
    }

    public void setValue(JSONObject playerInfo, Bitmap playerPic) {
        this.playerInfo = playerInfo;
        this.playerPic = playerPic;
    }
}
