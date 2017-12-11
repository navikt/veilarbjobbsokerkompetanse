package no.nav.fo.veilarbjobbsokerkompetanse;

import java.sql.ResultSet;
import java.sql.SQLException;

class BesvarelseDataRowMapper {
    static BesvarelseData mapBesvarelse(ResultSet rs) throws SQLException {
        return BesvarelseData
            .builder()
            .id(rs.getLong("id"))
            .aktorId(rs.getString("aktor_id"))
            .lagretTidspunkt(rs.getTimestamp("lagret_tidspunkt"))
            .besvarelse(rs.getString("besvarelse"))
            .raad(rs.getString("raad"))
            .build();
    }
}
