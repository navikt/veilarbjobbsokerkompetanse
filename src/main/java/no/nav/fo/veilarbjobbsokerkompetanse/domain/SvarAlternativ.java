package no.nav.fo.veilarbjobbsokerkompetanse.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class SvarAlternativ {

    private long svarAlternativId;
    private long svarId;
    private String svarAlternativKey;
    private String svarAlternativ;

}
