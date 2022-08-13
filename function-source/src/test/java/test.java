import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.Test;
import com.functions.*;

public class test {
    // Config
    //-----------------------------------------------------------------
    String fileName = "patient1_v1.json";

    //This should be a Fhir Store without a pub/sub configuration
    String stagingProject = "medtel-349114";
    String stagingLocation = "us-east4";
    String stagingDataset = "datastore";
    String stagingFhirStore = "testStagingFhirStore";

    String finalProject = "medtel-349114";
    String finalLocation = "us-east4";
    String finalDataset = "datastore";
    String finalFhirStore = "testFinalFhirStore";
    //-----------------------------------------------------------------
    HTTPHandler http = new HTTPHandler(true);

    @Test
    public void triggerReconcileViaFile() throws Exception {
        
        ReconcileFunction reconcile = new ReconcileFunction();
        reconcile.setStagingConfig(stagingProject, stagingLocation, stagingDataset, stagingFhirStore);
        reconcile.setFinalConfig(finalProject, finalLocation, finalDataset, finalFhirStore);

        // Read the file
        String data = new String(Files.readAllBytes(Paths.get("src/test/json/" + fileName).toAbsolutePath()));
        JSONObject json = new JSONObject(data);

        String url = String.format(
            "https://healthcare.googleapis.com/v1/projects/%1$s/locations/%2$s/datasets/%3$s/fhirStores/%4$s/fhir/%5$s",
            stagingProject, stagingLocation, stagingDataset, stagingFhirStore, json.getString("resourceType")
            //UNCOMMENT BELOW TO POST WITH SPECIFIC ID, OTHERWISE ID WILL BE RANDOMLY GENERATED
            //+ "/9999-9999-9999"
        );

        JSONObject response = http.POST(url, data);

        String mockPubSubMessage = String.format(
            "projects/%1$s/locations/%2$s/datasets/%3$s/fhirStores/%4$s/fhir/%5$s/%6$s",
            stagingProject, stagingLocation, stagingDataset, stagingFhirStore, response.getString("resourceType"), response.getString("id"));

        reconcile.run(mockPubSubMessage);
    }

    @Test
    public void deleteResource() throws Exception {
        String resourceId = "Patient/0d37206e-33ee-4326-88d1-0c7011d60e84";

        String url = String.format(
            "https://healthcare.googleapis.com/v1/projects/%1$s/locations/%2$s/datasets/%3$s/fhirStores/%4$s/fhir/",
            finalProject, finalLocation, finalDataset, finalFhirStore
        ) + resourceId;
        http.DELETE(url);
    }
}