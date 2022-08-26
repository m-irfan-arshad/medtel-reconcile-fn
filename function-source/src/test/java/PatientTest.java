import static org.junit.Assert.assertEquals;
import java.nio.file.*;
import org.json.JSONObject;
import org.junit.*;
import com.functions.*;

public class PatientTest {
    
    private Patient patient;

    @Before
    public void init() throws Exception {
        String data = new String(
            Files.readAllBytes(Paths.get("src/test/json/sample-bundle/test-patient.json").toAbsolutePath()));
        JSONObject json = new JSONObject(data);
        patient = new Patient(json);
        ReconcileFunction rec = new ReconcileFunction();
        rec.readConfig("config.properties");
    }

    @Test
    public void testGetFirstName() {
        assertEquals("CATHTHREE", patient.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("CUPID", patient.getLastName());
    }

    @Test
    public void testGetDOB() {
        assertEquals("1983-11-30", patient.getDOB());
    }

    @Test
    public void testGetSSN() {
        assertEquals("345-85-6885", patient.getSSN());
    }

    @Test
    public void testGetMRN() {
        assertEquals("10064618", patient.getMRN());
    }

    @Test
    public void testPatientSearch() {
        //WIP
    }


}
