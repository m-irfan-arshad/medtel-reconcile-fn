import static org.junit.Assert.assertEquals;
import java.nio.file.*;
import org.json.JSONObject;
import org.junit.*;
import com.functions.*;

public class EncounterTest {
    
    private Encounter encounter;

    @Before
    public void init() throws Exception {
        String data = new String(
            Files.readAllBytes(Paths.get("src/test/json/sample-bundle/test-encounter.json").toAbsolutePath()));
        JSONObject json = new JSONObject(data);
        encounter = new Encounter(json);
        ReconcileFunction rec = new ReconcileFunction();
        rec.readConfig("config.properties");
    }

    @Test
    public void testGetSubjecte() {
         assertEquals("Patient/27cbc4c3-95e6-194a-ab62-2d07dbfbd458", encounter.getSubject());
    }

    @Test
    public void testGetVN() {
        assertEquals("496905630", encounter.getVN());
    } 

    @Test
    public void testEncounterSearch() {
        //WIP
    }

}
