package yichunc2.cmu.edu.androidnbaplayerinfo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/*
 * This class provides capabilities to search for a NBA player information given a search term.  The method "search" is the entry to the class.
 */

public class GetPicture {

    CoolPlayerInfo cpi = null;

    /*
     * search is the public GetPicture method.  Its arguments are the search term, and the CoolPlayerInfo object that called it..
     */
    public void search(String searchTerm, CoolPlayerInfo cpi) {
        this.cpi = cpi;
        new AsyncMyWebServiceSearch().execute(searchTerm);
    }

    private class AsyncMyWebServiceSearch extends AsyncTask<String, Void, Result> {
        protected Result doInBackground(String... searchTerm) {

            // instantiate a result object to hold the return value.
            Result r = new Result();
            // call getInfo method to fetch information back to my app from the web service.
            getInfo(searchTerm[0], r);
            return r;
        }

        protected void onPostExecute(Result r) {
            // Passing Result object back to UI thread and display the information.
            cpi.pictureReady(r);
        }

        private int getInfo(String searchTerm, Result r) {

            // Make an HTTP GET passing the name on the URL line
            r.setValue(null, null);
            HttpURLConnection conn;
            int status = 0;

            try {
                // pass the name on the URL line
                URL url = new URL("https://docker-xkdgsitusd.now.sh/Project4Task2Servlet/"+searchTerm); // Task 2 deployment.
//                URL url = new URL("https://docker-dnwcfbxzjb.now.sh/Project4Task1Servlet/"+searchTerm); // Task 1 deployment.
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                // tell the server what format we want back
                conn.setRequestProperty("Accept", "text/plain");

                // wait for response
                status = conn.getResponseCode();

                // If things went poorly, don't try to read any response, just return.
                if (status != 200) {
                    return conn.getResponseCode();
                }

                String output;
                // things went well so let's read the response
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                // Received data from web service.
                output = br.readLine();
                conn.disconnect();

                JSONObject response = new JSONObject(output);

                // Search pic and set result bitmap.
                String pictureURL = response.getString("officialImageSrc");
                URL u = new URL(pictureURL);
                Bitmap myPic = getRemoteImage(u);
                // My easter egg.
                if (myPic == null) {
                    u = new URL("http://www.andrew.cmu.edu/course/95-702/Images/AndrewCarnegie.jpg");
                    myPic = getRemoteImage(u);
                }

                // set the response object
                r.setValue(response, myPic);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // return HTTP status to caller
            return status;
        }

        /*
         * Given a URL referring to an image, return a bitmap of that image
         */
        private Bitmap getRemoteImage(final URL url) {
            try {
                final URLConnection conn = url.openConnection();
                conn.connect();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                Bitmap bm = BitmapFactory.decodeStream(bis);
                bis.close();
                return bm;
            } catch (IOException e) {
                System.out.println("Picture not found. I'm going to put Andrew's pic as the player's profile picture.");
                return null;
            }
        }
    }
}