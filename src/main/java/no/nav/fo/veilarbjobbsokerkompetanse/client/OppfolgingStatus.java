package no.nav.fo.veilarbjobbsokerkompetanse.client;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OppfolgingStatus {

    private String fnr;
    private boolean underOppfolging;

}
