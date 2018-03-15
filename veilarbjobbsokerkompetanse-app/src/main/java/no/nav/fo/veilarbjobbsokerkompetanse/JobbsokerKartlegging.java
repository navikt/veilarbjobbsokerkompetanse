package no.nav.fo.veilarbjobbsokerkompetanse;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.val;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.ws.DateParser;

import java.sql.Timestamp;

@Value
@Builder(toBuilder = true)
public class JobbsokerKartlegging {

    private long id;
    @NonNull
    private String aktorId;
    @NonNull
    private Timestamp lagretTidspunkt;
    @NonNull
    private String besvarelse;
    @NonNull
    private String raad;

}
