package hackakl.frontend.app;

import com.atapiwrapper.library.api.model.gtfs.Route;
import com.atapiwrapper.library.api.model.gtfs.ShapePoint;

import java.util.List;

/**
 * Created by robinstark on 25/05/14.
 */
public class RouteShape {
    public Route route;
    public List<ShapePoint> shape;
    public RouteShape(Route r, List<ShapePoint> s) {
        this.route = r;
        this.shape = s;
    }
}
