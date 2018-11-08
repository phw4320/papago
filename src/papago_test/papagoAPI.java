package papago_test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
public class papagoAPI {
	public static int getType(String word) {
        for(int i = 0; i < word.length(); i++) {
            int index = word.charAt(i);
            if(index<65 || index>122) {
            	return 1;
            }
        }        
        return 0;
    }

    public static void main(String[] args) {
        String clientId = "aVMEeje3zSn8VVRQ9Zue";
        String clientSecret = "D0cwboSiKl";
        String sentence = "hello world hahaha";
        String[] words = sentence.split(" ");
        for(String word : words) {
            if (getType(word)==1) {
            	System.out.println("영어->한글만 번역이 가능합니다.");
            	return;
            }
        }
    
        Map<Map<Map<StringBuffer,StringBuffer>,StringBuffer>,StringBuffer> result = new HashMap<Map<Map<StringBuffer,StringBuffer>,StringBuffer>,StringBuffer>();
        try {
            String text = URLEncoder.encode("test", "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            // post request
            String postParams = "source=en&target=ko&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            String resStr = response.toString();
            Gson gson = new Gson();
            result = gson.fromJson(resStr,Map.class);
            Object obj = result.get("message");
            resStr = gson.toJson(obj);
            result = gson.fromJson(resStr,Map.class);
            obj = result.get("result");
            resStr = gson.toJson(obj);
            result = gson.fromJson(resStr,Map.class);
            obj = result.get("translatedText");
            System.out.println(obj);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}