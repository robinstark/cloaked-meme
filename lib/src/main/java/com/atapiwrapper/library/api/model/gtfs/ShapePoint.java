package com.atapiwrapper.library.api.model.gtfs;


import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ShapePoint implements Serializable, Parcelable {
	@JsonProperty("shape_pt_lat") private double lat;
    @JsonProperty("shape_pt_lon") private double lon;
    @JsonProperty("shape_pt_sequence") private int sequence;

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lon);
        dest.writeInt(this.sequence);
    }

    public ShapePoint() {
    }

    private ShapePoint(Parcel in) {
        this.lat = in.readDouble();
        this.lon = in.readDouble();
        this.sequence = in.readInt();
    }

    public static Parcelable.Creator<ShapePoint> CREATOR = new Parcelable.Creator<ShapePoint>() {
        public ShapePoint createFromParcel(Parcel source) {
            return new ShapePoint(source);
        }

        public ShapePoint[] newArray(int size) {
            return new ShapePoint[size];
        }
    };
}
