package no.nav.fo.veilarbjobbsokerkompetanse.domain;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class Kartlegging {

    private long kartleggingId;
    private String aktorId;
    private boolean underOppfolging;
    private String oppsummering;
    private String oppsummeringKey;
    private Instant kartleggingDato;

    private List<Raad> raad;
    private List<Besvarelse> besvarelse;
    private List<Kulepunkt> kulepunkter;
}
