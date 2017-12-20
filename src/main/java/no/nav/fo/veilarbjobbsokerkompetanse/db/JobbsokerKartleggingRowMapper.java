package no.nav.fo.veilarbjobbsokerkompetanse.db;

import no.nav.fo.veilarbjobbsokerkompetanse.JobbsokerKartlegging;

import java.sql.ResultSet;
import java.sql.SQLException;

class JobbsokerKartleggingRowMapper {
    static JobbsokerKartlegging mapJobbsokerKartlegging(ResultSet rs) throws SQLException {
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
