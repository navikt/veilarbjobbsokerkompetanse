package no.nav.fo.veilarbjobbsokerkompetanse.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Kulepunkt {

    private long kulepunktId;
    private long kartleggingId;

    private String kulepunktKey;
    private int kulepunktPrioritet;
    private String kulepunkt;
}
