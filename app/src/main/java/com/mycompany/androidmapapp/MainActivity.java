package com.mycompany.androidmapapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.Key;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;


public class MainActivity extends FragmentActivity {

    private final String USER_AGENT = "Safari/8.0.3";
    public final static String EXTRA_LAT = "com.mycompany.androidmapapp.LAT";
    public final static String EXTRA_LNG = "com.mycompany.androidmapapp.LNG";
    public static LatLng coords;
    public static GoogleMap gmap;
    //public static StringBuilder address;

    public void getLocation(View view){
        Log.i("mylocation", "hello world");
        Intent intent = new Intent(this, DisplayMap.class);
        EditText editText = (EditText) findViewById(R.id.edit_location);
        String location = editText.getText().toString();
        String[] tokens = location.split(" ");
        StringBuilder address = new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json?address=");
        for(String s : tokens){
            address.append(s + "+");
        }
        address.deleteCharAt(address.length() - 1);
        Coordinates coordinates = new Coordinates();
        coordinates.execute(address.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gmap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        Log.i("aftermap", "test");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

class Coordinates extends AsyncTask<String, Void, Void> {

    static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    protected Void doInBackground(String... address) {
        Void result = null;
        Log.i("BeforeTry", "test");

        try {
            Log.i("AfterTry", address[0]);
            GenericUrl obj = new GenericUrl(address[0]);

            HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer()
            {
                @Override
                public void initialize(HttpRequest request){
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                }
            });
            HttpRequest request = requestFactory.buildGetRequest(obj);
            HttpResponse httpResponse = request.execute();
            Log.i("afterRequest", "test");
            Result locationResult = httpResponse.parseAs(Result.class);
            MainActivity.coords = new LatLng(locationResult.geometries.get(0).locations.coordinate.lat,
                    locationResult.geometries.get(0).locations.coordinate.lng);
            Log.i("latlng", "test");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    protected void onPostExecute(Void aVoid) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(MainActivity.coords);
        Marker marker = MainActivity.gmap.addMarker(markerOptions);

        MainActivity.gmap.addMarker(markerOptions.title("Searched"));
        MainActivity.gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(MainActivity.coords, 17));
    }

    public static class Result{
        @Key("results")
        public List<Geometry> geometries;
    }

    public static class Geometry{
        @Key("geometry")
        public Location locations;
    }

    public static class Location{
        @Key("location")
        public Coordinate coordinate;
    }

    public static class Coordinate{
        @Key("lat")
        public double lat;
        @Key("lng")
        public double lng;
    }

}



