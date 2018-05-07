package no.nav.fo.veilarbjobbsokerkompetanse;

import no.nav.fo.veilarbjobbsokerkompetanse.domain.Kartlegging;

import static no.nav.metrics.MetricsFactory.createEvent;

public class Metrikker {

    public static void opprettKartleggingMetrikk(Kartlegging kartlegging) {
        createEvent("jobbsokerkompetanse.opprett.kartlegging")
                .addFieldToReport("underOppfolging", kartlegging.isUnderOppfolging())
                .report();

        kartlegging.getRaad().forEach(r ->
                createEvent("jobbsokerkompetanse.opprett.raad")
                        .addFieldToReport("raadKey", r.getRaadKey())
                        .addFieldToReport("raadTittel", r.getRaadTittel())
                        .report());

    }

}
