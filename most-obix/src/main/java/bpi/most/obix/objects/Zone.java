package bpi.most.obix.objects;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Alexej Strelzow
 * Date: 30.12.12
 * Time: 14:05
 */
public class Zone extends Obj {

    public static final String OBIX_ZONE_PREFIX = "/obix/zones/";

    public static final String ELEMENT_TAG = "zone";
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

    /**
     * Constructor, which will be called via reflection if
     * the object gets decoded.
     */
    public Zone() {
        uriList = new ArrayList<Uri>();
        dpList = new ArrayList<Dp>();
    }

    public Zone(int zoneID, String name) {
        this();
        this.zoneID = zoneID;
        this.zoneName = name;

        add(getZone());
        add(getZoneName());
    }

    public Int getZone() {
        if (zoneID == 0) {
            Obj obj = get(ZONE);
            if (obj != null) {
                return (Int)obj;
            }
        }
        return new Int(ZONE, zoneID);
    }

    public Str getZoneName() {
        if (zoneName == null) {
            Obj obj = get(NAME);
            if (obj != null) {
                return (Str)obj;
            }
        }
        return new Str(NAME, zoneName);
    }

    public Str getDescription() {
        if (description == null) {
            Obj obj = get(DESCRIPTION);
            if (obj != null) {
                return (Str)obj;
            }
        }
        return new Str(DESCRIPTION, description);
    }

    public void setDescription(String description) {
        if (this.description == null && description != null) {
            this.description = description;
            add(getDescription());
        }
    }

    public Str getCountry() {
        if (country == null) {
            Obj obj = get(COUNTRY);
            if (obj != null) {
                return (Str)obj;
            }
        }
        return new Str(COUNTRY, country);
    }

    public void setCountry(String country) {
        if (this.country == null && country != null) {
            this.country = country;
            add(getCountry());
        }
    }

    public Str getState() {
        if (state == null) {
            Obj obj = get(STATE);
            if (obj != null) {
                return (Str)obj;
            }
        }
        return new Str(STATE, state);
    }

    public void setState(String state) {
        if (this.state == null && state != null) {
            this.state = state;
            add(getState());
        }
    }

    public Str getCounty() {
        if (county == null) {
            Obj obj = get(COUNTY);
            if (obj != null) {
                return (Str)obj;
            }
        }
        return new Str(COUNTY, county);
    }

    public void setCounty(String county) {
        if (this.county == null && county != null) {
            this.county = county;
            add(getCounty());
        }
    }

    public Str getCity() {
        if (city == null) {
            Obj obj = get(CITY);
            if (obj != null) {
                return (Str)obj;
            }
        }
        return new Str(CITY, city);
    }

    public void setCity(String city) {
        if (this.city == null && city != null) {
            this.city = city;
            add(getCity());
        }
    }

    public Str getBuilding() {
        if (building == null) {
            Obj obj = get(BUILDING);
            if (obj != null) {
                return (Str)obj;
            }
        }
        return new Str(BUILDING, building);
    }

    public void setBuilding(String building) {
        if (this.building == null && building != null) {
            this.building = building;
            add(getBuilding());
        }
    }

    public Str getFloor() {
        if (floor == null) {
            Obj obj = get(FLOOR);
            if (obj != null) {
                return (Str)obj;
            }
        }
        return new Str(FLOOR, floor);
    }

    public void setFloor(String floor) {
        if (this.floor == null && floor != null) {
            this.floor = floor;
            add(getFloor());
        }
    }

    public Str getRoom() {
        if (room == null) {
            Obj obj = get(ROOM);
            if (obj != null) {
                return (Str)obj;
            }
        }
        return new Str(ROOM, room);
    }

    public void setRoom(String room) {
        if (this.room == room && room != null) {
            this.room = room;
            add(getRoom());
        }
    }

    public Real getArea() {
        if (area == 0.0) {
            Obj obj = get(AREA);
            if (obj != null) {
                return (Real)obj;
            }
        }
        return new Real(AREA, area);
    }

    public void setArea(double area) {
        if (this.area == area && area != 0.0d) {
            this.area = area;
            add(getArea());
        }
    }

    public Real getVolume() {
        if (volume == 0.0) {
            Obj obj = get(VOLUME);
            if (obj != null) {
                return (Real)obj;
            }
        }
        return new Real(VOLUME, volume);
    }

    public void setVolume(double volume) {
        if (this.volume == volume && volume != 0.0d) {
            this.volume = volume;
            add(getVolume());
        }
    }

    public void addURI(Uri uri) {
        if (uri != null) {
            uriList.add(uri);
        }
    }

    public void removeURI(Uri uri) {
        if (uriList.contains(uri)) {
            uriList.remove(uri);
        }
    }

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
        return ELEMENT_TAG;
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
            if (this.showData) {
                for (Dp dp : dpList) {
                    dp.setShowData(this.showData);
                    add(dp);
                }
            } else {
                for (Dp dp : dpList) {
                    dp.setShowData(this.showData);
                    remove(dp);
                }
            }
        }
    }

    public void addDp(Dp dp) {
        dpList.add(dp);
    }

    public Dp[] getDps() {
        if (dpList.isEmpty()) {
            java.util.List<Dp> kids = getKidsByClass(Dp.class);
            dpList.addAll(kids);
        }

        return dpList.toArray(new Dp[dpList.size()]);
    }

    public void removeDp(Dp dp) {
        dpList.remove(dp);
    }

}
