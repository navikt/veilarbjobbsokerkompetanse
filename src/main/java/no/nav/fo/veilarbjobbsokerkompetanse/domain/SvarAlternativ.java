package no.nav.fo.veilarbjobbsokerkompetanse.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class SvarAlternativ {

    private long svarAlternativId;
    private long besvarelseId;
    private String svarAlternativKey;
    private String svarAlternativ;

}
