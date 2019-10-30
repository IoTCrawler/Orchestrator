import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import org.junit.Before;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

public class NgsiLDClient_RemoteTest extends NgsiLDClientTest {

    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();


    @Before
    public void init() throws Exception {
        environmentVariables.set(NGSILD_BROKER_URL, "http://155.54.95.248:9090/ngsi-ld/");
        super.init();
    }

    public NgsiLDClient_RemoteTest(EntityLD entityLD) {
        super(entityLD);
    }


}