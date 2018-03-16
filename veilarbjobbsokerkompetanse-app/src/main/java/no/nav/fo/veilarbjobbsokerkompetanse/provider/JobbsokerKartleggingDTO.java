package no.nav.fo.veilarbjobbsokerkompetanse.provider;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.sql.Timestamp;

@Value
@Builder(toBuilder = true)
public class JobbsokerKartleggingDTO {

    private long id;
    private Timestamp lagretTidspunkt;
    @NonNull
    private String besvarelse;
    @NonNull
    private String raad;

}
