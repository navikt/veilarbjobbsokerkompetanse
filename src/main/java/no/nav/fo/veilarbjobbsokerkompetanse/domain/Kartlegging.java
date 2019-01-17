package no.nav.fo.veilarbjobbsokerkompetanse.domain;

import lombok.Builder;
import lombok.Value;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class Kartlegging {

    private long kartleggingId;
    private Timestamp kartleggingTidspunkt;

    private String aktorId;
    private boolean underOppfolging;
    private String oppsummering;
    private String oppsummeringKey;

    private List<Raad> raad;
    private List<Besvarelse> besvarelse;
    private List<Kulepunkt> kulepunkter;
}
