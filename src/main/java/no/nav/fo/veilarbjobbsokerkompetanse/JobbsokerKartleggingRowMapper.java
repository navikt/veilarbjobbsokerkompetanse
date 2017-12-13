package no.nav.fo.veilarbjobbsokerkompetanse;

import java.sql.ResultSet;
import java.sql.SQLException;

class JobbsokerKartleggingRowMapper {
    static JobbsokerKartlegging mapJobbsokerKartlegging(ResultSet rs) throws SQLException {
        return JobbsokerKartlegging
            .builder()
            .id(rs.getLong("id"))
            .aktorId(rs.getString("aktor_id"))
            .lagretTidspunkt(rs.getTimestamp("lagret_tidspunkt"))
            .besvarelse(rs.getString("besvarelse"))
            .raad(rs.getString("raad"))
            .build();
    }
}
