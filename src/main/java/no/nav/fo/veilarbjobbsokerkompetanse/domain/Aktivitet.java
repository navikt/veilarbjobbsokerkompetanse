package no.nav.fo.veilarbjobbsokerkompetanse.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Aktivitet {

    private long aktivitetId;
    private long raadId;
    private String tittel;
    private String innhold;

}
