package no.nav.fo.veilarbjobbsokerkompetanse.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Svar {

    private long svarId;
    private long besvarelseId;
    private String sporsmalKey;
    private String sporsmal;
    private String tipsKey;
    private String tips;

}
