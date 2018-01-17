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
    private String aktorId;
    @NonNull
    private Timestamp lagretTidspunkt;
    @NonNull
    private String besvarelse;
    @NonNull
    private String raad;

    public static JobbsokerKartlegging opprettFraWsDto(no.nav.tjeneste.domene.brukerdialog.jobbsokerkompetanse.v1.informasjon.JobbsokerKartlegging jobbsokerKartlegging,
                                                       String aktorId,
                                                       Timestamp now) {
        return JobbsokerKartlegging
            .builder()
            .aktorId(aktorId)
            .lagretTidspunkt(now)
            .besvarelse(jobbsokerKartlegging.getBesvarelse())
            .raad(jobbsokerKartlegging.getRaad())
            .build();
    }

    public static no.nav.tjeneste.domene.brukerdialog.jobbsokerkompetanse.v1.informasjon.JobbsokerKartlegging hentTilWsDto(JobbsokerKartlegging jobbsokerKartlegging) {
        val jsk = new no.nav.tjeneste.domene.brukerdialog.jobbsokerkompetanse.v1.informasjon.JobbsokerKartlegging();
        jsk.setId(jobbsokerKartlegging.getId());
        jsk.setLagretTidspunkt(DateParser.getXmlCalendar(jobbsokerKartlegging.getLagretTidspunkt()));
        jsk.setBesvarelse(jobbsokerKartlegging.getBesvarelse());
        jsk.setRaad(jobbsokerKartlegging.getRaad());
        return jsk;
    }
}
