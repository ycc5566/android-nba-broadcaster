package yichunc2.cmu.edu.androidnbaplayerinfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class CoolPlayerInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CoolPlayerInfo cpi = this;

        /*
         * Find the "submit" button, and add a listener to it
         */
        Button submitButton = (Button)findViewById(R.id.submit);
        // Add a listener to the send button
        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View viewParam) {
                String searchTerm = ((EditText)findViewById(R.id.searchTerm)).getText().toString();
                GetPicture gp = new GetPicture();
                gp.search(searchTerm, cpi);
            }
        });
    }

    /*
     * This is called by the GetPicture object when the picture is ready.  This allows for passing back the Bitmap picture for updating the ImageView
     */
    public void pictureReady(Result result) {
        ImageView pictureView = (ImageView)findViewById(R.id.coolPlayerInfo);
        TextView searchView = (EditText)findViewById(R.id.searchTerm);
        TextView name = (TextView)findViewById(R.id.name);
        TextView age = (TextView)findViewById(R.id.age);
        age.setVisibility(View.INVISIBLE); // reset every time.
        TextView team = (TextView)findViewById(R.id.team);
        team.setVisibility(View.INVISIBLE); // reset every time.
        TextView jerseyNumber = (TextView)findViewById(R.id.jerseyNumber);
        jerseyNumber.setVisibility(View.INVISIBLE); // reset every time.
        TextView primaryPosition = (TextView)findViewById(R.id.primaryPosition);
        primaryPosition.setVisibility(View.INVISIBLE); // reset every time.
        TextView height = (TextView)findViewById(R.id.height);
        height.setVisibility(View.INVISIBLE); // reset every time.
        TextView weight = (TextView)findViewById(R.id.weight);
        weight.setVisibility(View.INVISIBLE); // reset every time.
        TextView annualAvgSalary = (TextView)findViewById(R.id.annualAvgSalary);
        annualAvgSalary.setVisibility(View.INVISIBLE); // reset every time.
        if (result.playerInfo != null) {
            try {
                // handle name view.
                name.setText(getString(R.string.name) + " "
                        + (result.playerInfo.get("firstName") != JSONObject.NULL ? (result.playerInfo.getString("firstName")+ " ")  : "Unknown")
                        + (result.playerInfo.get("lastName") != JSONObject.NULL ? result.playerInfo.getString("lastName"): "Unknown"));
                // there must be a age. No need to handle.
                age.setText(getString(R.string.age) + " " + result.playerInfo.getString("age"));
                age.setVisibility(View.VISIBLE);
                // handle team view.
                team.setText(getString(R.string.team) + " "
                        + (result.playerInfo.get("abbreviation") != JSONObject.NULL ? result.playerInfo.getString("abbreviation"): "No team"));
                team.setVisibility(View.VISIBLE);
                // handle jersey number view.
                jerseyNumber.setText(getString(R.string.jerseyNumber) + " "
                        + (result.playerInfo.get("jerseyNumber") != JSONObject.NULL ? result.playerInfo.getString("jerseyNumber"): "Unknown"));
                jerseyNumber.setVisibility(View.VISIBLE);
                // handle primary position view.
                primaryPosition.setText(getString(R.string.primaryPosition) + " "
                        + (result.playerInfo.get("primaryPosition") != JSONObject.NULL ? result.playerInfo.getString("primaryPosition"): "Unknown"));
                primaryPosition.setVisibility(View.VISIBLE);
                // handle height view.
                height.setText(getString(R.string.height) + " "
                        + (result.playerInfo.get("height") != JSONObject.NULL ? result.playerInfo.getString("height"): "Unknown"));
                height.setVisibility(View.VISIBLE);
                // handle weight view.
                weight.setText(getString(R.string.weight) + " "
                        + (result.playerInfo.get("weight") != JSONObject.NULL ?result.playerInfo.getString("weight"):"Unknown"));
                weight.setVisibility(View.VISIBLE);
                if (result.playerInfo.get("annualAverageSalary") != JSONObject.NULL) {
                    annualAvgSalary.setText(getString(R.string.annualAvgSalary) + " " + result.playerInfo.getInt("annualAverageSalary"));
                    annualAvgSalary.setVisibility(View.VISIBLE);
                }
                pictureView.setImageBitmap(result.getPlayerPic());
                pictureView.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                System.out.println("Something wrong with the received data. Please email to <yichunc2@andrew.cmu.edu> for further assistance.");
            }
        } else {
            pictureView.setImageResource(R.mipmap.ic_launcher);
            pictureView.setVisibility(View.VISIBLE);
            name.setText("Sorry, I could not find any player at age " + searchView.getText());
        }
        searchView.setText("");
        name.setVisibility(View.VISIBLE);
        pictureView.invalidate();
    }
}
