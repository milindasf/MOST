package bpi.most.obix.contracts;

import bpi.most.obix.objects.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alexej Strelzow
 * Date: 30.12.12
 * Time: 13:54
 * To change this template use File | Settings | File Templates.
 */
public interface IZone extends IObj {

    Int getZone();
    Str getZoneName();
    Str getDescription();
    Str getCountry();
    Str getState();
    Str getCounty();
    Str getCity();
    Str getBuilding();
    Str getFloor();
    Str getRoom();

    Real getArea();
    Real getVolume();

    void addURI(Uri uri);
    void removeURI(Uri uri);

    List getDatapointURIs();

}
