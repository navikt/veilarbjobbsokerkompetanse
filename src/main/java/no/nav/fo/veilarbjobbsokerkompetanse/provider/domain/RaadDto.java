package no.nav.fo.veilarbjobbsokerkompetanse.provider.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class RaadDto {

    private String raadKey;
    private String raadTittel;
    private String raadIngress;
    private List<AktivitetDto> raadAktiviteter;

}
