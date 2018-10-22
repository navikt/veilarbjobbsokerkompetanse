package no.nav.fo.veilarbjobbsokerkompetanse.provider.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

@Data
@Accessors(chain = true)
public class AvsluttetOppfolgingFeedDto implements Comparable<AvsluttetOppfolgingFeedDto> {
    public static final String FEED_NAME = "avsluttetoppfolging";

    public String aktoerid;
    public Date sluttdato;
    public Date oppdatert;

    @Override
    public int compareTo(AvsluttetOppfolgingFeedDto avsluttetOppfolgingFeedDto) {
        return oppdatert.compareTo(avsluttetOppfolgingFeedDto.oppdatert);
    }
}
