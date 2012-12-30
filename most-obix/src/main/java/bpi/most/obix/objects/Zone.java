package bpi.most.obix.objects;

import bpi.most.obix.contracts.IZone;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Alexej Strelzow
 * Date: 30.12.12
 * Time: 14:05
 * To change this template use File | Settings | File Templates.
 */
public class Zone extends Obj implements IZone  {

    public static final String ZONE = "zone";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String COUNTRY = "country";
    public static final String STATE = "state";
    public static final String COUNTY = "county";
    public static final String CITY = "city";
    public static final String BUILDING = "building";
    public static final String FLOOR = "floor";
    public static final String ROOM = "room";

    public static final String AREA = "area";
    public static final String VOLUME = "volume";

    public static final String URI = "uri";


    private int zoneID;
    private String zoneName;
    private String description;
    private String country;
    private String state;
    private String county;
    private String city;
    private String building;
    private String floor;
    private String room;

    private double area;
    private double volume;

    private java.util.List<Uri> uriList;  // should be maintained!

    public Zone(int zoneID, String name) {
        this.zoneID = zoneID;
        this.zoneName = name;

        uriList = new ArrayList<Uri>();
    }

    @Override
    public Int getZone() {
        return new Int(ZONE, zoneID);
    }

    @Override
    public Str getZoneName() {
        return new Str(NAME, zoneName);
    }

    @Override
    public Str getDescription() {
        return new Str(DESCRIPTION, description);
    }

    @Override
    public Str getCountry() {
        return new Str(COUNTRY, country);
    }

    @Override
    public Str getState() {
        return new Str(STATE, state);
    }

    @Override
    public Str getCounty() {
        return new Str(COUNTY, county);
    }

    @Override
    public Str getCity() {
        return new Str(CITY, city);
    }

    @Override
    public Str getBuilding() {
        return new Str(BUILDING, building);
    }

    @Override
    public Str getFloor() {
        return new Str(FLOOR, floor);
    }

    @Override
    public Str getRoom() {
        return new Str(ROOM, room);
    }

    @Override
    public Real getArea() {
        return new Real(AREA, area);
    }

    @Override
    public Real getVolume() {
        return new Real(VOLUME, volume);
    }

    @Override
    public void addURI(Uri uri) {
        if (uri != null) {
            uriList.add(uri);
        }
    }

    @Override
    public void removeURI(Uri uri) {
        if (uriList.contains(uri)) {
            uriList.remove(uri);
        }
    }

    @Override
    public List getDatapointURIs() {
        List list = new List(URI, new Contract("obix:Uri"));
        list.addAll(uriList.toArray(new Uri[uriList.size()]));
        return list;
    }
}
