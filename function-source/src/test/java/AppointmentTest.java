import static org.junit.Assert.assertEquals;
import java.nio.file.*;
import org.json.JSONObject;
import org.junit.*;
import static com.functions.HTTPHandler.*;
import com.functions.*;
import static org.mockito.Mockito.*;
import static com.functions.ReconcileFunction.FINAL_URL;

public class AppointmentTest {

    private Appointment appointment;

    @Before
    public void init() throws Exception {
        String data = new String(
            Files.readAllBytes(Paths.get("src/test/json/sample-bundle/test-appointment.json").toAbsolutePath()));
        JSONObject json = new JSONObject(data);
        appointment = new Appointment(json);
        ReconcileFunction rec = new ReconcileFunction();
        rec.readConfig("config.properties");
    }

    @Test
    public void testGetPatient() {
        assertEquals("Patient/27cbc4c3-95e6-194a-ab62-2d07dbfbd458", appointment.getPatient());
    }

    @Test
    public void testGetPractitioner() {
        assertEquals("Practitioner/bf0b9fec-b7f0-e253-7671-5fe1ae23dc02", appointment.getPractitioner());
    }

    @Test
    public void testGetLocation() {
        assertEquals("Location/ce692c7c-84ea-4b8f-d3ff-3ec9f0e51bd8", appointment.getLocation());
    }

    @Test
    public void testGetDate() {
        assertEquals("2022-04-29T11:10:00Z", appointment.getDate());
    }

    @Test
    public void testGetServiceRequestReference() {
        assertEquals("ServiceRequest/1e03ea4b-0cb0-dc43-a384-5bf274eb86ba", appointment.getServiceRequestReference());
    }

    @Test
    public void testGetVN() throws Exception {
        // mock HTTP responses
        String serviceRequestData = new String(Files
                .readAllBytes(Paths.get("src/test/json/sample-bundle/test-service-request.json").toAbsolutePath()));
        JSONObject serviceRequestJSON = new JSONObject(serviceRequestData);
        String encounterData = new String(
                Files.readAllBytes(Paths.get("src/test/json/sample-bundle/test-encounter.json").toAbsolutePath()));
        JSONObject encounterJSON = new JSONObject(encounterData);
        mockStatic(HTTPHandler.class);
        when(GET(FINAL_URL + "ServiceRequest/1e03ea4b-0cb0-dc43-a384-5bf274eb86ba")).thenReturn(serviceRequestJSON);
        when(GET(FINAL_URL + "Encounter/d9435d70-7aff-5314-e46f-66e9e69ef8c4")).thenReturn(encounterJSON);

        assertEquals("496905630", appointment.getVN());
    }

    @Test
    public void testAppointmentSearch() throws Exception {
        // WIP
    }
}
