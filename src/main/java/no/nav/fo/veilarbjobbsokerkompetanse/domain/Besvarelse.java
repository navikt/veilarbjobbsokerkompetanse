package no.nav.fo.veilarbjobbsokerkompetanse.domain;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder(toBuilder = true)
public class Besvarelse {

    private long besvarelseId;
    private long aktorId;
    private Date besvarelseDao;

}
