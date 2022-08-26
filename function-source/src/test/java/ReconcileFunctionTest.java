import static org.junit.Assert.assertEquals;
import org.junit.*;
import com.functions.*;

public class ReconcileFunctionTest {
    
    @Test
    public void testReadConfig() throws Exception {
        ReconcileFunction r = new ReconcileFunction();
        r.readConfig("config.properties");
        assertEquals("https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-east4/datasets/datastore/fhirStores/fhirstore/fhir/", ReconcileFunction.STAGING_URL);
        assertEquals("https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-east4/datasets/datastore/fhirStores/finalFhirStore/fhir/", ReconcileFunction.FINAL_URL);
    }
    
}
