package andy.spiderlibrary.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by andyli on 2015/10/18.
 */
public class DataFormatUtils {

    /**
     * 將InputStream轉換成jsonObject
     * @param is
     * @param charsetName
     * @return
     * @throws IOException
     */
    public static JSONObject convertJsonObject(InputStream is,String charsetName) throws IOException {

        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, charsetName));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());

            return jsonObject;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
