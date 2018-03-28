package no.nav.fo.veilarbjobbsokerkompetanse.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class Raad {

    private long raadId;
    private long besvarelseId;
    private String raadKey;
    private String raadTittel;
    private String raadIngress;
    private List<Aktivitet> raadAktiviteter;

}
