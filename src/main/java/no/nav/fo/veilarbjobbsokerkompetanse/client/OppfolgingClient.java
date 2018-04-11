package no.nav.fo.veilarbjobbsokerkompetanse.client;

import no.nav.sbl.rest.RestUtils;
import org.springframework.stereotype.Component;

import static no.nav.sbl.util.EnvironmentUtils.getRequiredProperty;

@Component
public class OppfolgingClient {

    public static final String VEILARBOPPFOLGINGAPI_URL_PROPERTY_NAME = "VEILARBOPPFOLGINGAPI_URL";
    private static final String VEILARBOPPFOLGING_TARGET = getRequiredProperty(VEILARBOPPFOLGINGAPI_URL_PROPERTY_NAME);
    private static final String FNR_QUERY_PARAM = "fnr";

    public boolean underOppfolging(String fnr) {
        OppfolgingStatus oppfolgingStatus = RestUtils.withClient(c ->
                c.target(VEILARBOPPFOLGING_TARGET + "/oppfolging")
                        .queryParam(FNR_QUERY_PARAM, fnr)
                        .request()
                        .get(OppfolgingStatus.class));
        return oppfolgingStatus.isUnderOppfolging();
    }

}
