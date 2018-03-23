package no.nav.fo.veilarbjobbsokerkompetanse;

import no.nav.fo.veilarbjobbsokerkompetanse.domain.Besvarelse;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Raad;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Svar;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.SvarAlternativ;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.domain.BesvarelseDto;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.domain.RaadDto;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.domain.SvarAlternativDto;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.domain.SvarDto;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.util.stream.Collectors.toList;

public class Mapper {

    public static BesvarelseDto map(Besvarelse besvarelse) {
        return new BesvarelseDto()
                .setUnderOppfolging(besvarelse.isUnderOppfolging())
                .setBesvarelseDato(LocalDateTime.ofInstant(besvarelse.getBesvarelseDato(), ZoneId.of("Europe/Oslo")))
                .setRaad(besvarelse.getRaad().stream().map(Mapper::map).collect(toList()))
                .setSvar(besvarelse.getSvar().stream().map(Mapper::map).collect(toList()));
    }

    private static RaadDto map(Raad raad) {
        return new RaadDto().setRaad(raad.getRaad());
    }

    private static SvarDto map(Svar svar) {
        return new SvarDto()
                .setSporsmalKey(svar.getSporsmalKey())
                .setSporsmal(svar.getSporsmal())
                .setTipsKey(svar.getTipsKey())
                .setTips(svar.getTips())
                .setSvarAlternativ(svar.getSvarAlternativ().stream().map(Mapper::map).collect(toList()));
    }

    private static SvarAlternativDto map(SvarAlternativ svarAlternativ) {
        return new SvarAlternativDto()
                .setSvarAlternativKey(svarAlternativ.getSvarAlternativKey())
                .setSvarAlternativ(svarAlternativ.getSvarAlternativ());
    }

    public static Besvarelse map(BesvarelseDto besvarelseDto) {
        return Besvarelse.builder()
                .underOppfolging(besvarelseDto.isUnderOppfolging())
                .besvarelseDato(besvarelseDto.getBesvarelseDato().atZone(ZoneId.of("Europe/Oslo")).toInstant())
                .raad(besvarelseDto.getRaad().stream().map(Mapper::map).collect(toList()))
                .svar(besvarelseDto.getSvar().stream().map(Mapper::map).collect(toList()))
                .build();
    }

    private static SvarAlternativ map(SvarAlternativDto svarAlternativDto) {
        return SvarAlternativ.builder()
                .svarAlternativKey(svarAlternativDto.getSvarAlternativKey())
                .svarAlternativ(svarAlternativDto.getSvarAlternativ())
                .build();
    }

    private static Svar map(SvarDto svarDto) {
        return Svar.builder()
                .sporsmalKey(svarDto.getSporsmalKey())
                .sporsmal(svarDto.getSporsmal())
                .tipsKey(svarDto.getTipsKey())
                .tips(svarDto.getTips())
                .svarAlternativ(svarDto.getSvarAlternativ().stream().map(Mapper::map).collect(toList()))
                .build();
    }

    private static Raad map(RaadDto raadDto) {
        return Raad.builder()
                .raad(raadDto.getRaad())
                .build();
    }
}
