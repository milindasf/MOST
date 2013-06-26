package bpi.most.service.impl.datapoint.virtual;

import java.util.Date;
import java.util.List;
import java.util.ServiceLoader;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bpi.most.domain.datapoint.Datapoint;
import bpi.most.domain.datapoint.DatapointDataVO;
import org.springframework.context.ApplicationContext;

/**
 * Finds {@link DatapointDataVO}s from virtual datapoints.
 *
 * @author Christoph Lauscher
 */
public class VirtualDatapointDataFinder {

    private static final Logger LOG = LoggerFactory.getLogger(VirtualDatapointDataFinder.class);

    private final EntityManager em;

    private ApplicationContext ctx;

    public VirtualDatapointDataFinder(EntityManager em, ApplicationContext ctx) {
        this.em = em;
        this.ctx = ctx;
    }
    
    private VirtualDatapoint getDatapoint(Datapoint dpEntity){
    	ServiceLoader<VirtualDatapointFactory> virtualDpLoader = ServiceLoader
				.load(VirtualDatapointFactory.class);
		// loop through all DpVirtualFactory implementations
		for (VirtualDatapointFactory virtualDp : virtualDpLoader) {
			VirtualDatapoint requestedDp = virtualDp.getVirtualDp(dpEntity, em);
			if (requestedDp != null) {

                if (ctx != null){
                    //inject fields if we have a spring application context
                    ctx.getAutowireCapableBeanFactory().autowireBean(requestedDp);
                }

				return requestedDp;
			}
		}
		return null;
    }

    public DatapointDataVO getData(Datapoint dpEntity) {
    	VirtualDatapoint vdp = getDatapoint(dpEntity);
    	if(vdp != null){
    		return vdp.getData();
    	}
    	return null;
    }

    public List<DatapointDataVO> getData(Datapoint dpEntity, Date starttime, Date endtime) {
    	VirtualDatapoint vdp = getDatapoint(dpEntity);
    	if(vdp != null){
    		return vdp.getData(starttime, endtime);
    	}
    	return null;
    }

    public List<DatapointDataVO> getDataPeriodic(Datapoint dpEntity, Date starttime, Date endtime, Float period, int mode) {
    	VirtualDatapoint vdp = getDatapoint(dpEntity);
    	if(vdp != null){
    		return vdp.getDataPeriodic(starttime, endtime, period, mode);
    	}
    	return null;
    }

    public Integer getNumberOfValues(Datapoint dpEntity, Date starttime, Date endtime) {
    	VirtualDatapoint vdp = getDatapoint(dpEntity);
    	if(vdp != null){
    		return vdp.getNumberOfValues(starttime, endtime);
    	}
    	return null;
    }
    
	public int addData(Datapoint dpEntity, DatapointDataVO data) {
		int result = 0;
    	VirtualDatapoint vdp = getDatapoint(dpEntity);
    	if(vdp != null){
    		result = vdp.addData(data);
    	}
		return result;
	}

	/**
	 * Deletes all data of datapoint. Use with caution!!
	 */
	public int delData(Datapoint dpEntity) {
		int result = 0;
    	VirtualDatapoint vdp = getDatapoint(dpEntity);
    	if(vdp != null){
    		result = vdp.delData();
    	}
		return result;
	}

	/**
	 * Deletes data of given timeslot
	 */
	public int delData(Datapoint dpEntity, Date starttime, Date endtime) {
		int result = 0;
    	VirtualDatapoint vdp = getDatapoint(dpEntity);
    	if(vdp != null){
    		result = vdp.delData(starttime, endtime);
    	}
		return result;
	}
}
