package no.nav.fo.veilarbjobbsokerkompetanse;

import lombok.Builder;
import lombok.Value;

import java.sql.Timestamp;

@Value
@Builder(toBuilder = true)
public class JobbsokerKartlegging {
    private long id;
    private String aktorId;
    private Timestamp lagretTidspunkt;
    private String besvarelse;
    private String raad;
}
