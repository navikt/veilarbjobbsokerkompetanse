package no.nav.fo.veilarbjobbsokerkompetanse;

import no.nav.fo.veilarbjobbsokerkompetanse.domain.*;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.domain.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoZonedDateTime;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class Mapper {

    public static KartleggingDto map(Besvarelse besvarelse) {
        return new KartleggingDto()
                .setUnderOppfolging(besvarelse.isUnderOppfolging())
                .setBesvarelseDato(LocalDateTime.ofInstant(besvarelse.getBesvarelseDato(), ZoneId.of("Europe/Oslo")))
                .setRaad(besvarelse.getRaad().stream().map(Mapper::map).collect(toList()))
                .setBesvarelse(besvarelse.getSvar().stream().map(Mapper::map).collect(toList()));
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

    private static BesvarelseDto map(Svar svar) {
        return new BesvarelseDto()
                .setSporsmalKey(svar.getSporsmalKey())
                .setSporsmal(svar.getSporsmal())
                .setTipsKey(svar.getTipsKey())
                .setTips(svar.getTips())
                .setSvarAlternativer(svar.getSvarAlternativ().stream().map(Mapper::map).collect(toList()));
    }

    private static SvarAlternativDto map(SvarAlternativ svarAlternativ) {
        return new SvarAlternativDto()
                .setSvarAlternativKey(svarAlternativ.getSvarAlternativKey())
                .setSvarAlternativ(svarAlternativ.getSvarAlternativ());
    }

    public static Besvarelse map(KartleggingDto kartleggingDto) {
        Instant besvarelseDato = ofNullable(kartleggingDto.getBesvarelseDato())
                .map(d -> d.atZone(ZoneId.of("Europe/Oslo")))
                .map(ChronoZonedDateTime::toInstant)
                .orElse(null);
        return Besvarelse.builder()
                .underOppfolging(kartleggingDto.isUnderOppfolging())
                .besvarelseDato(besvarelseDato)
                .raad(kartleggingDto.getRaad().stream().map(Mapper::map).collect(toList()))
                .svar(kartleggingDto.getBesvarelse().stream().map(Mapper::map).collect(toList()))
                .build();
    }

    private static SvarAlternativ map(SvarAlternativDto svarAlternativDto) {
        return SvarAlternativ.builder()
                .svarAlternativKey(svarAlternativDto.getSvarAlternativKey())
                .svarAlternativ(svarAlternativDto.getSvarAlternativ())
                .build();
    }

    private static Svar map(BesvarelseDto besvarelseDto) {
        return Svar.builder()
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
