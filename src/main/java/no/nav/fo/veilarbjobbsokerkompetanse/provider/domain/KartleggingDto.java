package no.nav.fo.veilarbjobbsokerkompetanse.provider.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class KartleggingDto {

    private boolean underOppfolging;
    private String oppsummering;
    private String oppsummeringKey;
    private List<RaadDto> raad;
    private List<BesvarelseDto> besvarelse;
    private List<KulepunktDto> kulepunkter;
}
