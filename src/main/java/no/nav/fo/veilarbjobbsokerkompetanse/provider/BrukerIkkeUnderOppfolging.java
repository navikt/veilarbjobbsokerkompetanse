package no.nav.fo.veilarbjobbsokerkompetanse.provider;

import no.nav.apiapp.feil.Feil;

import javax.ws.rs.core.Response;

public class BrukerIkkeUnderOppfolging implements Feil.Type {

    @Override
    public String getName() {
        return "BRUKER_IKKE_UNDER_OPPFOLGING";
    }

    @Override
    public Response.Status getStatus() {
        return Response.Status.CONFLICT;
    }
}
