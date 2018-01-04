package no.nav.fo.veilarbjobbsokerkompetanse.api;

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

    public static JobbsokerKartleggingDTO opprettFraWsDto(no.nav.tjeneste.domene.brukerdialog.jobbsokerkompetanse.v1.informasjon.JobbsokerKartlegging jobbsokerKartlegging) {
        return JobbsokerKartleggingDTO.builder()
            .id(jobbsokerKartlegging.getId())
            .lagretTidspunkt(DateParser.getTimestamp(jobbsokerKartlegging.getLagretTidspunkt()))
            .besvarelse(jobbsokerKartlegging.getBesvarelse())
            .raad(jobbsokerKartlegging.getRaad())
            .build();
    }
}
