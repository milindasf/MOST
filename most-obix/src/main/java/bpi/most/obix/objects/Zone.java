package bpi.most.obix.objects;

import bpi.most.obix.contracts.IZone;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Alexej Strelzow
 * Date: 30.12.12
 * Time: 14:05
 */
public class Zone extends Obj implements IZone  {

    public static final String OBIX_ZONE_PREFIX = "/obix/zones/";

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
    private java.util.List<Dp> dpList;

    private boolean showData = false;

    public Zone(int zoneID, String name) {
        this.zoneID = zoneID;
        this.zoneName = name;

        uriList = new ArrayList<Uri>();
        dpList = new ArrayList<Dp>();

        add(getZone());
        add(getZoneName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Int getZone() {
        return new Int(ZONE, zoneID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Str getZoneName() {
        return new Str(NAME, zoneName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Str getDescription() {
        return new Str(DESCRIPTION, description);
    }

    public void setDescription(String description) {
        if (this.description == null && description != null) {
            this.description = description;
            add(getDescription());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Str getCountry() {
        return new Str(COUNTRY, country);
    }

    public void setCountry(String country) {
        if (this.country == null && country != null) {
            this.country = country;
            add(getCountry());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Str getState() {
        return new Str(STATE, state);
    }

    public void setState(String state) {
        if (this.state == null && state != null) {
            this.state = state;
            add(getState());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Str getCounty() {
        return new Str(COUNTY, county);
    }

    public void setCounty(String county) {
        if (this.county == null && county != null) {
            this.county = county;
            add(getCounty());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Str getCity() {
        return new Str(CITY, city);
    }

    public void setCity(String city) {
        if (this.city == null && city != null) {
            this.city = city;
            add(getCity());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Str getBuilding() {
        return new Str(BUILDING, building);
    }

    public void setBuilding(String building) {
        if (this.building == null && building != null) {
            this.building = building;
            add(getBuilding());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Str getFloor() {
        return new Str(FLOOR, floor);
    }

    public void setFloor(String floor) {
        if (this.floor == null && floor != null) {
            this.floor = floor;
            add(getFloor());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Str getRoom() {
        return new Str(ROOM, room);
    }

    public void setRoom(String room) {
        if (this.room == room && room != null) {
            this.room = room;
            add(getRoom());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getArea() {
        return new Real(AREA, area);
    }

    public void setArea(double area) {
        if (this.area == area && area != 0.0d) {
            this.area = area;
            add(getArea());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getVolume() {
        return new Real(VOLUME, volume);
    }

    public void setVolume(double volume) {
        if (this.volume == volume && volume != 0.0d) {
            this.volume = volume;
            add(getVolume());
        }
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getElement() {
        return "zone";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Uri getHref() {
        return new Uri(String.valueOf(zoneID), OBIX_ZONE_PREFIX + String.valueOf(zoneID));
    }

    public void setShowData(boolean showData) {
        this.showData = showData;

        if (!dpList.isEmpty()) {
            for (Dp dp : dpList) {
                dp.setShowData(showData);
            }
        }
    }

    public void addDp(Dp dp) {
        if (uriList.contains(dp.getHref())) { // can lead to problems. TODO: 1st: override equals/hashcode in Uri
            dpList.add(dp);

            if (showData) {
                dp.setShowData(showData);
                add(dp);
            }
        }
    }

    public void removeDp(Dp dp) {
        if (uriList.contains(dp.getHref())) { // can lead to problems. TODO: 1st: override equals/hashcode in Uri
            dpList.remove(dp);
            remove(dp);
        }
    }

}
