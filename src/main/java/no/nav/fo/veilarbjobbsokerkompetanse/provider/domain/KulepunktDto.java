package no.nav.fo.veilarbjobbsokerkompetanse.provider.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class KulepunktDto {

    private String kulepunktKey;
    private int kulepunktPrioritet;
    private String kulepunkt;

}
