package no.nav.fo.veilarbjobbsokerkompetanse.provider.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SvarAlternativDto {

    private String svarAlternativKey;
    private String svarAlternativ;

}
