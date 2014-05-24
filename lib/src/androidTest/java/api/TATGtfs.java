package api;

import android.test.AndroidTestCase;

import com.atapiwrapper.library.BuildConfig;
import com.atapiwrapper.library.api.AtApi;
import com.atapiwrapper.library.api.model.ServerResponse;
import com.atapiwrapper.library.api.model.gtfs.Agency;
import com.atapiwrapper.library.api.model.gtfs.Calendar;
import com.atapiwrapper.library.api.model.gtfs.CalendarException;
import com.atapiwrapper.library.api.model.gtfs.Route;
import com.atapiwrapper.library.api.model.gtfs.Stop;
import com.atapiwrapper.library.api.model.gtfs.StopTime;
import com.atapiwrapper.library.api.model.gtfs.Trip;
import com.atapiwrapper.library.api.service.GtfsService;

import java.util.List;

import retrofit.RestAdapter;

/**
 * Tests the realtime api requests and responses
 */
public class TATGtfs extends AndroidTestCase {

	private RestAdapter mRestAdapter;
	private GtfsService mGtfsService;

	@Override protected void setUp() throws Exception {
		super.setUp();

		AtApi api = new AtApi(BuildConfig.API_KEY);
		mGtfsService = api.getGtfsService();
	}

	public void testAgencies() {
		ServerResponse<List<Agency>> result = mGtfsService.agencies();

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() > 0);
	}

	public void testCalendars() {
		ServerResponse<List<Calendar>> result = mGtfsService.calendars();

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() > 0);
	}

	public void testCalendarsByServiceId() {
		ServerResponse<List<Calendar>> result = mGtfsService.calendarsByServiceId("0051ML41870600124213");

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() > 0);
	}

	public void testCalendarsExceptions() {
		ServerResponse<List<CalendarException>> result = mGtfsService.calendarExceptions();

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() > 0);
	}

	public void testCalendarsExceptionsByServiceId() {
		ServerResponse<List<CalendarException>> result = mGtfsService.calendarExceptionsByServiceId("0051ML41870600124213");

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() > 0);
	}

	public void testRoutes() {
		ServerResponse<List<Route>> result = mGtfsService.routes();

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() > 0);
	}

	public void testRoutesById() {
		ServerResponse<List<Route>> result = mGtfsService.routesById("0051ML4187");

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() == 1);
	}

	public void testRoutesByLongName() {
		ServerResponse<List<Route>> result = mGtfsService.routesByLongName("Pt Chevalier To Britomart Via Herne Bay And College Hill");

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() == 1);
	}

	public void testRoutesSearch() {
		ServerResponse<List<Route>> result = mGtfsService.routesSearch("Chevalier");

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() > 0);
	}

	public void testRoutesGeoSearch() {
		ServerResponse<List<Route>> result = mGtfsService.routesGeoSearch(new Double("-36.896853"), new Double("174.907185"), new Double(
				"500"));

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() > 0);
	}

	public void testRoutesSearchByStopId() {
		ServerResponse<List<Route>> result = mGtfsService.routesSearchByStopId("0001");

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() > 0);
	}

	public void testStops() {
		ServerResponse<List<Stop>> result = mGtfsService.stops();

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() > 0);
	}

	public void testStopsById() {
		ServerResponse<List<Stop>> result = mGtfsService.stopsById("0001");

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() > 0);
	}

	public void testStopsByCode() {
		ServerResponse<List<Stop>> result = mGtfsService.stopsByCode("7106");

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() > 0);
	}

	public void testStopSearchByName() {
		ServerResponse<List<Stop>> result = mGtfsService.stopSearchByName("Victoria");

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() > 0);
	}

	public void testStopTimesByStopId() {
		ServerResponse<List<StopTime>> result = mGtfsService.stopTimesByStopId("0001");

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() > 0);
	}

	public void testStopTimesByTripId() {
		ServerResponse<List<StopTime>> result = mGtfsService.stopTimesByTripId("0051ML41870600124213");

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() > 0);
	}

	public void testTrips() {
		ServerResponse<List<Trip>> result = mGtfsService.trips();

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() > 0);
	}

	public void testTripsByTripId() {
		ServerResponse<List<Trip>> result = mGtfsService.tripsByTripId("0051ML41870600124213");

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() > 0);
	}

	public void testTripsByRouteId() {
		ServerResponse<List<Trip>> result = mGtfsService.tripsByRouteid("0051ML4187");

		//make sure we have content
		assertNotNull(result);
		assertNotNull(result.getStatus());
		assertEquals(result.getStatus(), ServerResponse.STATUS_OK);
		assertNotNull(result.getResponse());
		assertTrue(result.getResponse().size() > 0);
	}

}
