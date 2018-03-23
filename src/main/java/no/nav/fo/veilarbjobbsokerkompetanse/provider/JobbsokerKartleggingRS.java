package no.nav.fo.veilarbjobbsokerkompetanse.provider;

import no.nav.apiapp.feil.Feil;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarbjobbsokerkompetanse.provider.domain.BesvarelseDto;
import no.nav.fo.veilarbjobbsokerkompetanse.service.BesvarelseService;
import no.nav.modig.core.context.SubjectHandler;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.function.Supplier;

import static no.nav.apiapp.feil.Feil.Type.FINNES_IKKE;
import static no.nav.fo.veilarbjobbsokerkompetanse.Mapper.map;

@Component
@Path("/jobbsokerkartlegging")
@Produces("application/json")
public class JobbsokerKartleggingRS {

    private static final Supplier<Feil> FANT_IKKE_AKTOR =
            () -> new Feil(FINNES_IKKE, "Finer ikke aktørId for gitt Fnr");

    @Inject
    private AktorService aktorService;

    @Inject
    private BesvarelseService besvarelseService;

    @GET
    public BesvarelseDto hentNyesteBesvarelseForAktor() {
        return map(besvarelseService.fetchMostRecentByAktorId(getAktorId()));
    }

    @POST
    @Path("/opprett")
    public BesvarelseDto opprettBesvarelse(BesvarelseDto besvarelseDto) {
        String aktorId = getAktorId();
        besvarelseService.create(aktorId, map(besvarelseDto));
        return map(besvarelseService.fetchMostRecentByAktorId(aktorId));
    }

    private String getAktorId() {
        String fnr = SubjectHandler.getSubjectHandler().getUid();
        return aktorService.getAktorId(fnr).orElseThrow(FANT_IKKE_AKTOR);
    }

}
