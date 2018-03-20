package no.nav.fo.veilarbjobbsokerkompetanse.domain;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class Besvarelse {

    private long besvarelseId;
    private long aktorId;
    private boolean underOppfolging;
    private Instant besvarelseDato;

    private List<Raad> raad;
    private List<Svar> svar;

}
