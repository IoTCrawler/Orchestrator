import org.junit.Before;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import static com.agtinternational.iotcrawler.fiware.clients.Constants.NGSILD_BROKER_URL;

public class EnvVariablesSetter {

    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Before
    public void init(){
        //public static final String IoTCTripleStoreURI = "http://10.67.42.53:10035/repositories/IoTCrawler2/sparql";
        //public static final String defaultTripleStoreURI = "http://10.67.42.53:10035/repositories/KB/sparql";


        environmentVariables.set(NGSILD_BROKER_URL, "http://djane:3000/ngsi-ld/");
        //environmentVariables.set(NGSILD_BROKER_URL, "http://155.54.95.248:9090/ngsi-ld/");
        //environmentVariables.set(NGSILD_BROKER_URL, "http://localhost:9090/ngsi-ld/");
    }
}
