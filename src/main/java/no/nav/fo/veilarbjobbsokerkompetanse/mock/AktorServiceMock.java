package no.nav.fo.veilarbjobbsokerkompetanse.mock;

import no.nav.dialogarena.aktor.AktorService;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class AktorServiceMock implements AktorService {

    @Override
    public Optional<String> getFnr(String s) {
        return ofNullable(s);
    }

    @Override
    public Optional<String> getAktorId(String s) {
        return ofNullable(s);
    }

}
