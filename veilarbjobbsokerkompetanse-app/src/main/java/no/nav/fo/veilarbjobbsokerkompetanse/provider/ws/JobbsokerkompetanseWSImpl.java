package no.nav.fo.veilarbjobbsokerkompetanse.provider.ws;


import lombok.val;
import no.nav.apiapp.soap.SoapTjeneste;
import no.nav.dialogarena.aktor.AktorService;
import no.nav.fo.veilarbjobbsokerkompetanse.db.JobbsokerKartleggingDAO;
import no.nav.tjeneste.domene.brukerdialog.jobbsokerkompetanse.v1.binding.JobbsokerkompetanseV1;
import no.nav.tjeneste.domene.brukerdialog.jobbsokerkompetanse.v1.meldinger.HentJobbsokerKartleggingResponse;
import no.nav.tjeneste.domene.brukerdialog.jobbsokerkompetanse.v1.meldinger.HentNyesteJobbsokerKartleggingRequest;
import no.nav.tjeneste.domene.brukerdialog.jobbsokerkompetanse.v1.meldinger.OpprettJobbsokerKartleggingRequest;
import no.nav.tjeneste.domene.brukerdialog.jobbsokerkompetanse.v1.meldinger.OpprettJobbsokerKartleggingResponse;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@SoapTjeneste("/jobbsokerkompetanse/ws")
public class JobbsokerkompetanseWSImpl implements JobbsokerkompetanseV1 {

    private final JobbsokerKartleggingDAO jobbsokerKartleggingDAO;
    private final AktorService aktorService;
    private final Provider<HttpServletRequest> requestProvider;

    @Inject
    public JobbsokerkompetanseWSImpl(JobbsokerKartleggingDAO jobbsokerKartleggingDAO, AktorService aktorService, Provider<HttpServletRequest> requestProvider) {
        this.jobbsokerKartleggingDAO = jobbsokerKartleggingDAO;
        this.aktorService = aktorService;
        this.requestProvider = requestProvider;
    }

    @Override
    public OpprettJobbsokerKartleggingResponse opprettJobbsokerKartlegging(OpprettJobbsokerKartleggingRequest opprettJobbsokerKartleggingRequest) {
        return Optional.of(opprettJobbsokerKartleggingRequest.getJobbsokerkartlegging())
            .map(jobbsokerKartlegging -> no.nav.fo.veilarbjobbsokerkompetanse.JobbsokerKartlegging.opprettFraWsDto(jobbsokerKartlegging, aktorId(), now()))
            .map(jobbsokerKartleggingDAO::opprettJobbsokerKartlegging)
            .map(no.nav.fo.veilarbjobbsokerkompetanse.JobbsokerKartlegging::hentTilWsDto)
            .map(jobbsokerKartlegging -> {
                val response = new OpprettJobbsokerKartleggingResponse();
                response.setJobbsokerkartlegging(jobbsokerKartlegging);
                return response;
            })
            .orElseThrow(RuntimeException::new);
    }

    @Override
    public HentJobbsokerKartleggingResponse hentJobbsokerKartlegging(HentNyesteJobbsokerKartleggingRequest hentNyesteJobbsokerKartleggingRequest) {
        return Optional.of(jobbsokerKartleggingDAO.hentNyesteJobbsokerKartlegging(aktorId()))
            .map(no.nav.fo.veilarbjobbsokerkompetanse.JobbsokerKartlegging::hentTilWsDto)
            .map(jobbsokerKartlegging -> {
                val response = new HentJobbsokerKartleggingResponse();
                response.setJobbsokerkartlegging(jobbsokerKartlegging);
                return response;
            }).orElseThrow(RuntimeException::new);
    }

    @Override
    public void ping() {
    }

    String aktorId() {
        return fnr()
            .map(s -> aktorService.getAktorId(s).get())
            .orElse(null);
    }

    Optional<String> fnr() {
        return Optional.ofNullable(requestProvider.get().getParameter("fnr"));
    }

    private Timestamp now() {
        return Timestamp.valueOf(LocalDateTime.now());
    }
}
