package no.nav.fo.veilarbjobbsokerkompetanse.provider.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class BesvarelseDto {

    private boolean underOppfolging;
    private LocalDateTime besvarelseDato;
    private List<RaadDto> raad;
    private List<SvarDto> svar;

}
