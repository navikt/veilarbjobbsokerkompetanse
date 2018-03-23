package no.nav.fo.veilarbjobbsokerkompetanse.provider.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.SvarAlternativ;

import java.util.List;

@Data
@Accessors(chain = true)
public class SvarDto {

    private String sporsmalKey;
    private String sporsmal;
    private String tipsKey;
    private String tips;
    private List<SvarAlternativDto> svarAlternativ;

}
