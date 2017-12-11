package no.nav.fo.veilarbjobbsokerkompetanse;

import lombok.Builder;
import lombok.Value;
import lombok.val;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Value
@Builder(toBuilder = true)
public class BesvarelseData {
    private long id;
    private String aktorId;
    private Timestamp lagretTidspunkt;
    private String besvarelse;
    private String raad;
}
