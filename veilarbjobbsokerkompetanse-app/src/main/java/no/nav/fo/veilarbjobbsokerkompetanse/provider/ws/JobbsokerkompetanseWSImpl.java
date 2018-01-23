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

@SoapTjeneste("/jobbsokerkompetanse")
public class JobbsokerkompetanseWSImpl implements JobbsokerkompetanseV1 {

    @Inject
    private JobbsokerKartleggingDAO jobbsokerKartleggingDAO;

    @Inject
    private AktorService aktorService;

    @Inject
    private Provider<HttpServletRequest> requestProvider;

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

    private String aktorId() {
        return aktorService.getAktorId(fnr()).get();
    }

    private String fnr() {
        return Optional.ofNullable(requestProvider.get().getParameter("fnr"))
                .orElseThrow(RuntimeException::new);
    }

    private Timestamp now() {
        return Timestamp.valueOf(LocalDateTime.now());
    }
}
