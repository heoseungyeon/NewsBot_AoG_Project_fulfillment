package com.o2o.action.server.util;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.net.ssl.HttpsURLConnection;

/*
 * Gson: https://github.com/google/gson
 * Maven info:
 *     groupId: com.google.code.gson
 *     artifactId: gson
 *     version: 2.8.1
 *
 * Once you have compiled or downloaded gson-2.8.1.jar, assuming you have placed it in the
 * same folder as this file (GetSentiment.java), you can compile and run this program at
 * the command line as follows.
 *
 * Execute the following two commands to build and run (change gson version if needed):
 * javac GetSentiment.java -classpath .;gson-2.8.1.jar -encoding UTF-8
 * java -cp .;gson-2.8.1.jar GetSentiment
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CognitiveService {
    private GetSentiment getSentiment = new GetSentiment();

    public String getTheSentimentPrettify(String text) throws Exception {
        Documents documents = new Documents();
        String response = "";
        try {
            getSentiment.Initialize();

            documents.add("1", "ko", text);
            response = getSentiment.getTheSentiment(documents);

            System.out.println(getSentiment.prettify(response));
        } catch (Exception e) {
            System.out.println(e);
        }
        return response;
    }

    class Document {
        public String id, language, text;

        public Document(String id, String language, String text) {
            this.id = id;
            this.language = language;
            this.text = text;
        }
    }

    class Documents {
        public List<Document> documents;

        public Documents() {
            this.documents = new ArrayList<Document>();
        }

        public void add(String id, String language, String text) {
            this.documents.add(new Document(id, language, text));
        }
    }

    public class GetSentiment {
        String subscription_key_var;
        String subscription_key;
        String endpoint_var;
        String endpoint;

        public void Initialize() throws Exception {
            subscription_key = "7022ba5e367c41f181b6f58a2a99ae3f";
            endpoint = "https://heo.cognitiveservices.azure.com/";
        }

        String path = "text/analytics/v3.0/sentiment";

        public String getTheSentiment(Documents documents) throws Exception {
            String text = new Gson().toJson(documents);
            byte[] encoded_text = text.getBytes("UTF-8");
            System.out.println("Request: "+text);
            System.out.println("Request: "+encoded_text);

            URL url = new URL("https://heo.cognitiveservices.azure.com/text/analytics/v3.0/sentiment");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/json");
            connection.setRequestProperty("Ocp-Apim-Subscription-Key", "7022ba5e367c41f181b6f58a2a99ae3f");
            connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.write(encoded_text, 0, encoded_text.length);
            wr.flush();
            wr.close();

            StringBuilder response = new StringBuilder();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            return response.toString();
        }

        public String prettify(String json_text) {
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(json_text).getAsJsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(json);
        }



    }
}