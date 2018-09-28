package no.nav.fo.veilarbjobbsokerkompetanse;

import no.nav.fo.veilarbjobbsokerkompetanse.domain.*;

import java.time.Instant;

import static java.util.Arrays.asList;

public class TestData {
    public static Kartlegging kartlegging() {
        return Kartlegging.builder()
            .besvarelse(asList(besvarelse(), besvarelse()))
            .raad(asList(raad(), raad()))
            .kulepunkter(asList(kulepunkt(), kulepunkt()))
            .oppsummering("Dette er en oppsummering")
            .oppsummeringKey("oppsummering-key1")
            .kartleggingDato(Instant.now())
            .build();
    }

    public static Kulepunkt kulepunkt() {
        return Kulepunkt.builder()
            .kulepunktKey("KULEPUNKT-KEY")
            .kulepunktPrioritet(10)
            .kulepunkt("KULEPUNKT-TEKST-TEKST-TEKST")
            .build();
    }

    public static Besvarelse besvarelse() {
        return Besvarelse.builder()
            .sporsmalKey("SPORSMAL-1-KEY")
            .sporsmal("SPORSMAL-1")
            .tipsKey("TIPS-1-KEY")
            .tips("TIPS")
            .svarAlternativ(asList(svarAlternativ(), svarAlternativ()))
            .build();
    }

    public static SvarAlternativ svarAlternativ() {
        return SvarAlternativ.builder()
            .svarAlternativKey("SVARALTERNATIV-1-KEY")
            .svarAlternativ("SVARALTERNATIV-1")
            .build();
    }

    public static Raad raad() {
        return Raad.builder()
            .raadKey("R1")
            .raadTittel("RaadTittel")
            .raadIngress("RaadIngress")
            .raadAktiviteter(asList(aktivitet(), aktivitet()))
            .build();
    }

    public static Aktivitet aktivitet() {
        return Aktivitet.builder()
            .tittel("AktivitetTittel")
            .innhold("AktivitetInnhold")
            .build();
    }
}
