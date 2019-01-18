package no.nav.fo.veilarbjobbsokerkompetanse;

import no.nav.fo.veilarbjobbsokerkompetanse.domain.*;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.domain.*;

import static java.util.stream.Collectors.toList;

public class Mapper {

    public static KartleggingDto map(Kartlegging kartlegging) {
        return new KartleggingDto()
            .setUnderOppfolging(kartlegging.isUnderOppfolging())
            .setOppsummering(kartlegging.getOppsummering())
            .setOppsummeringKey(kartlegging.getOppsummeringKey())
            .setRaad(kartlegging.getRaad().stream().map(Mapper::map).collect(toList()))
            .setBesvarelse(kartlegging.getBesvarelse().stream().map(Mapper::map).collect(toList()))
            .setKulepunkter(kartlegging.getKulepunkter().stream().map(Mapper::map).collect(toList()));
    }

    private static KulepunktDto map(Kulepunkt kulepunkt) {
        return new KulepunktDto()
            .setKulepunktKey(kulepunkt.getKulepunktKey())
            .setKulepunktPrioritet(kulepunkt.getKulepunktPrioritet())
            .setKulepunkt(kulepunkt.getKulepunkt());
    }

    private static AktivitetDto map(Aktivitet aktivitet) {
        return new AktivitetDto()
            .setTittel(aktivitet.getTittel())
            .setInnhold(aktivitet.getInnhold());
    }

    private static RaadDto map(Raad raad) {
        return new RaadDto()
            .setRaadKey(raad.getRaadKey())
            .setRaadTittel(raad.getRaadTittel())
            .setRaadIngress(raad.getRaadIngress())
            .setRaadAktiviteter(raad.getRaadAktiviteter().stream().map(Mapper::map).collect(toList()));
    }

    private static BesvarelseDto map(Besvarelse besvarelse) {
        return new BesvarelseDto()
            .setSporsmalKey(besvarelse.getSporsmalKey())
            .setSporsmal(besvarelse.getSporsmal())
            .setTipsKey(besvarelse.getTipsKey())
            .setTips(besvarelse.getTips())
            .setSvarAlternativer(besvarelse.getSvarAlternativ().stream().map(Mapper::map).collect(toList()));
    }

    private static SvarAlternativDto map(SvarAlternativ svarAlternativ) {
        return new SvarAlternativDto()
            .setSvarAlternativKey(svarAlternativ.getSvarAlternativKey())
            .setSvarAlternativ(svarAlternativ.getSvarAlternativ());
    }

    public static Kartlegging map(KartleggingDto kartleggingDto) {
        return Kartlegging.builder()
            .underOppfolging(kartleggingDto.isUnderOppfolging())
            .oppsummering(kartleggingDto.getOppsummering())
            .oppsummeringKey(kartleggingDto.getOppsummeringKey())
            .raad(kartleggingDto.getRaad().stream().map(Mapper::map).collect(toList()))
            .besvarelse(kartleggingDto.getBesvarelse().stream().map(Mapper::map).collect(toList()))
            .kulepunkter(kartleggingDto.getKulepunkter().stream().map(Mapper::map).collect(toList()))
            .build();
    }

    private static Kulepunkt map(KulepunktDto kulepunktDto) {
        return Kulepunkt.builder()
            .kulepunktKey(kulepunktDto.getKulepunktKey())
            .kulepunktPrioritet(kulepunktDto.getKulepunktPrioritet())
            .kulepunkt(kulepunktDto.getKulepunkt())
            .build();
    }

    private static SvarAlternativ map(SvarAlternativDto svarAlternativDto) {
        return SvarAlternativ.builder()
            .svarAlternativKey(svarAlternativDto.getSvarAlternativKey())
            .svarAlternativ(svarAlternativDto.getSvarAlternativ())
            .build();
    }

    private static Besvarelse map(BesvarelseDto besvarelseDto) {
        return Besvarelse.builder()
            .sporsmalKey(besvarelseDto.getSporsmalKey())
            .sporsmal(besvarelseDto.getSporsmal())
            .tipsKey(besvarelseDto.getTipsKey())
            .tips(besvarelseDto.getTips())
            .svarAlternativ(besvarelseDto.getSvarAlternativer().stream().map(Mapper::map).collect(toList()))
            .build();
    }

    private static Raad map(RaadDto raadDto) {
        return Raad.builder()
            .raadKey(raadDto.getRaadKey())
            .raadTittel(raadDto.getRaadTittel())
            .raadIngress(raadDto.getRaadIngress())
            .raadAktiviteter(raadDto.getRaadAktiviteter().stream().map(Mapper::map).collect(toList()))
            .build();
    }

    private static Aktivitet map(AktivitetDto raadAktivitetDto) {
        return Aktivitet.builder()
            .tittel(raadAktivitetDto.getTittel())
            .innhold(raadAktivitetDto.getInnhold())
            .build();
    }
}
