package com.agtinternational.iotcrawler.core.interfaces;

//import com.agtinternational.iotcrawler.core.models.NGSI_LD.EntityLD;
import com.agtinternational.iotcrawler.core.models.*;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import eu.neclab.iotplatform.ngsi.api.datamodel.NotifyCondition;
import eu.neclab.iotplatform.ngsi.api.datamodel.Restriction;

import java.util.List;
import java.util.function.Function;

public interface AbstractIotCrawlerClient extends Component {

    public List<IoTStream> getStreams(int limit) throws Exception;
    public List<IoTStream> getStreams(String query) throws Exception;

    public List<Sensor> getSensors(int limit) throws Exception;
    public List<Sensor> getSensors(String query) throws Exception;

    public List<IoTPlatform> getPlatforms(int limit) throws Exception;
    public List<IoTPlatform> getPlatforms(String query) throws Exception;

    public List<ObservableProperty> getObservableProperties(int limit) throws Exception;
    public List<ObservableProperty> getObservableProperties(String query) throws Exception;

//    public List<StreamObservation> getObservations() throws Exception;
    public List<StreamObservation> getObservations(String streamId, int limit) throws Exception;

//    public List<RDFModel> getEntities(FilteringSelector selector, int limit);

    public List<String> getEntityURIs(String query)  throws Exception;
    public List<EntityLD> getEntities(String entityType, int limit) throws Exception;

    public Boolean registerEntity(RDFModel model) throws Exception;
    public Boolean pushObservationsToBroker(List<StreamObservation> observations) throws Exception;
    public String subscribeTo(String streamId, String[] attributes, List<NotifyCondition> notifyConditions, Restriction restriction, Function<StreamObservation, Void> onChange) throws Exception;
}
