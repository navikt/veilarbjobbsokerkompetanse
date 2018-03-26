package no.nav.fo.veilarbjobbsokerkompetanse.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Raad {

    private long raadId;
    private long besvarelseId;
    private String raadKey;
    private String raad;

}
