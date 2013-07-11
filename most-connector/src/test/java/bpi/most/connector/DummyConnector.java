package bpi.most.connector;

import bpi.most.domain.connector.ConnectorVO;
import bpi.most.dto.DpDataDTO;
import bpi.most.dto.DpDatasetDTO;
import bpi.most.dto.UserDTO;
import bpi.most.service.api.DatapointService;
import junit.framework.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: harald
 * Date: 25.06.13
 * Time: 09:53
 * To change this template use File | Settings | File Templates.
 */
public class DummyConnector extends Connector{

    private boolean initialized = false;
    private boolean anotherInit = false;

    private boolean preDestroyCalled = false;
    private boolean anotherPreDestroyCalled = false;

    /**
     * @PostConstruct lets this method be called after all dependency injection has happened.
     * as for all annotated methods: the name of the method is not important.
     */
    @PostConstruct
    public void initMe(){
        initialized = true;
    }

    /**
     * you can have several postconstruct methods
     */
    @PostConstruct
    public void anotherInit(){
        anotherInit = true;
    }

    /**
     * preDestroy methods should theoretically be called before they are destroyed; this happens when springs
     * application context is closed (app closed)
     */
    @PreDestroy
    public void beforeClose(){
        preDestroyCalled = true;
    }

    @PreDestroy
    public void AnotherBeforeCloseMethod(){
        anotherPreDestroyCalled = true;
    }

    /**
     * connect the connector to a datapoint
     */
    public DummyConnector(ConnectorVO connectorDTO, UserDTO user) {
        super(connectorDTO, user);
    }

    @Override
    protected boolean writeData(DpDataDTO newValue) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DpDataDTO getSourceData() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DpDatasetDTO getSourceData(Date starttime) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public DatapointService getDpService(){
        return dpService;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public boolean isAnotherInit() {
        return anotherInit;
    }

    public boolean isPreDestroyCalled() {
        return preDestroyCalled;
    }

    public boolean isAnotherPreDestroyCalled() {
        return anotherPreDestroyCalled;
    }
}
