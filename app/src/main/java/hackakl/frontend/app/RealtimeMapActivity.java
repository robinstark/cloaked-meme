package hackakl.frontend.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.atapiwrapper.library.api.AtApi;
import com.atapiwrapper.library.api.model.ServerResponse;
import com.atapiwrapper.library.api.model.realtime.vehiclelocations.VehicleLocation;
import com.atapiwrapper.library.api.model.realtime.vehiclelocations.VehicleLocationResponse;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RealtimeMapActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_map);
        setUpMapIfNeeded();
        button = (Button) findViewById(R.id.button);
        button.setText("Foo!");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public void loadData() {
        AtApi api = new AtApi("f6d06e87-e4be-41c6-9b8c-3507660447d8");

        api.getRealtimeService().vehiclelocations(new Callback<ServerResponse<VehicleLocationResponse>>() {
            @Override
            public void success(ServerResponse<VehicleLocationResponse> vehicleLocationResponseServerResponse, Response response) {
                final List<VehicleLocation> locationList = vehicleLocationResponseServerResponse.getResponse().getVehicleLocations();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMap.clear();
                        for (VehicleLocation loc: locationList) {
                            final LatLng l = new LatLng(loc.getVehicle().getPosition().getLatitude(), loc.getVehicle().getPosition().getLongitude());
                            final String snippet = loc.getVehicle().getTrip().getRouteId();
                            final String title = loc.getVehicle().getTrip().getTripId();
                            mMap.addMarker(new MarkerOptions().position(l).snippet(snippet).title(title));

                        }
                    }
                });

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);

        loadData();
    }
}
