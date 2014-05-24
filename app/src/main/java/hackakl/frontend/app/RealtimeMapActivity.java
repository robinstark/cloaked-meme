package hackakl.frontend.app;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.widget.Button;

import com.atapiwrapper.library.api.AtApi;
import com.atapiwrapper.library.api.model.ServerResponse;
import com.atapiwrapper.library.api.model.gtfs.Route;
import com.atapiwrapper.library.api.model.gtfs.ShapePoint;
import com.atapiwrapper.library.api.model.gtfs.Stop;
import com.atapiwrapper.library.api.model.gtfs.Trip;
import com.atapiwrapper.library.api.model.realtime.vehiclelocations.VehicleLocation;
import com.atapiwrapper.library.api.model.realtime.vehiclelocations.VehicleLocationResponse;
import com.atapiwrapper.library.api.service.RealtimeService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.PolylineOptionsCreator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RealtimeMapActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Button button;
    private AtApi api;
    Map<String, Marker> vehicleMarkers;
    private List<VehicleLocation> vehicleLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = new AtApi(getString(R.string.at_api_key));
        vehicleMarkers = new HashMap<>();
        setContentView(R.layout.activity_realtime_map);
        setUpMapIfNeeded();
        button = (Button) findViewById(R.id.button);
        button.setText("Refresh");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        getActionBar().setLogo(R.drawable.logo);
        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setTitle("LinkAKL");
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setBackgroundDrawable(new ColorDrawable(android.R.color.white));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public void loadData() {
        api.getRealtimeService().vehiclelocations(new Callback<ServerResponse<VehicleLocationResponse>>() {
            @Override
            public void success(ServerResponse<VehicleLocationResponse> vehicleLocationResponseServerResponse, Response response) {
                vehicleLocations = vehicleLocationResponseServerResponse.getResponse().getVehicleLocations();

                for (VehicleLocation vl: vehicleLocations) {
                    final LatLng l = new LatLng(vl.getVehicle().getPosition().getLatitude(), vl.getVehicle().getPosition().getLongitude());
                    final String snippet = vl.getVehicle().getTrip().getRouteId();
                    final String title = vl.getVehicle().getTrip().getTripId();
                    final String vehicleId = vl.getVehicle().getVehicle().getId();
                    Log.d("MarkerId", vehicleId);
                    Marker existingMarker = vehicleMarkers.get(vehicleId);
                    if (existingMarker != null) {
                        animateMarkerToICS(existingMarker, l, new LatLngInterpolator.Spherical());
                    } else {
                        Log.d("ADDING marker:", vehicleId);
                        vehicleMarkers.put(vehicleId, mMap.addMarker(new MarkerOptions().position(l).snippet(snippet).title(title)));
                    }
                }
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

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setOnMyLocationChangeListener(new CenterOnMyLocationOnceListener(mMap));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                new LoadRouteAsyncTask().execute(marker.getSnippet(), marker.getTitle());
            }
        });

        loadData();
    }

    static void animateMarkerToICS(Marker marker, LatLng finalPosition, final LatLngInterpolator latLngInterpolator) {
        TypeEvaluator<LatLng> typeEvaluator = new TypeEvaluator<LatLng>() {
            @Override
            public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
                return latLngInterpolator.interpolate(fraction, startValue, endValue);
            }
        };
        Property<Marker, LatLng> property = Property.of(Marker.class, LatLng.class, "position");
        ObjectAnimator animator = ObjectAnimator.ofObject(marker, property, typeEvaluator, finalPosition);
        animator.setDuration(3000);
        animator.start();
    }

    private class CenterOnMyLocationOnceListener implements GoogleMap.OnMyLocationChangeListener {
        private final GoogleMap map;

        public CenterOnMyLocationOnceListener(GoogleMap map) {
            this.map = map;
        }
        @Override
        public void onMyLocationChange(Location location) {
            map.setOnMyLocationChangeListener(null);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
        }
    }

    private class LoadRouteAsyncTask extends AsyncTask<String, Void, RouteShape> {

        @Override
        protected RouteShape doInBackground(String... params) {
            final String routeId = params[0];
            final String tripId = params[1];

            List<ShapePoint> shape = api.getGtfsService().shapeByTripId(tripId).getResponse();
            Route route = api.getGtfsService().routesById(routeId).getResponse().get(0);
//            List<Stop> stops = api.getGtfsService().stopsByRoute

            return new RouteShape(route, shape);
        }

        @Override
        protected void onPostExecute(RouteShape routeShape) {
            PolylineOptions opts = new PolylineOptions();
            for (ShapePoint p : routeShape.shape) {
                final LatLng l = new LatLng(p.getLat(), p.getLon());
                opts.add(l);
            }

            opts.color(Color.parseColor("#FF8800"));
            opts.geodesic(true);
            opts.visible(true);
            opts.zIndex(1000.0f);

            mMap.addPolyline(opts);
            mMap.setTrafficEnabled(false);
        }
    }
}
