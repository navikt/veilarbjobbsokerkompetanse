package no.nav.fo.veilarbjobbsokerkompetanse.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbjobbsokerkompetanse.JobbsokerKartlegging;

import java.sql.ResultSet;

import static no.nav.fo.veilarbjobbsokerkompetanse.db.JobbsokerKartleggingDAO.*;

class JobbsokerKartleggingRowMapper {

    @SneakyThrows
    static JobbsokerKartlegging mapJobbsokerKartlegging(ResultSet rs) {
        return JobbsokerKartlegging
            .builder()
            .id(rs.getLong(ID))
            .aktorId(rs.getString(AKTOR_ID))
            .lagretTidspunkt(rs.getTimestamp(LAGRET_TIDSPUNKT))
            .besvarelse(rs.getString(BESVARELSE))
            .raad(rs.getString(RAAD))
            .build();
    }
}
