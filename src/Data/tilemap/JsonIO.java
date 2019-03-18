package Data.tilemap;

import javax.json.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class JsonIO {

    private JsonObject jsonObject;

    public JsonIO(String filename) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JsonReader jsonReader = Json.createReader(inputStream);

        jsonObject = jsonReader.readObject();

    }

    public JsonValue getJsonValueFromTag(String tag){
        return this.jsonObject.get(tag);
    }

    public int getIntFromTag(String tag){
        return this.jsonObject.getInt(tag);
    }

    public boolean getBooleanFromTag(String tag){
        return this.jsonObject.getBoolean(tag);
    }

    public JsonArray getJsonArrayFromTag(String tag){
        return this.jsonObject.getJsonArray(tag);
    }

    public String getStringFromTag(String tag){
        return this.jsonObject.getString(tag);
    }

    public double getDoubleFromTag(String tag){
        return this.jsonObject.getJsonNumber(tag).doubleValue();
    }

}
