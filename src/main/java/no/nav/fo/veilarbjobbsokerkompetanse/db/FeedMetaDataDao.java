package no.nav.fo.veilarbjobbsokerkompetanse.db;

import no.nav.sbl.jdbc.Database;
import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.util.Date;

@Component
public class FeedMetaDataDao {

    private final Database database;

    @Inject
    public FeedMetaDataDao(Database database) {
        this.database = database;
    }

    public Date hentSisteLestTidspunkt() {
        return database.queryForObject(
                "SELECT tidspunkt_siste_endring " +
                        "FROM AVSLUTTETOPPFOLGING_FEED_METADATA",
                (rs) -> Database.hentDato(rs, "tidspunkt_siste_lesing")
        );
    }

    public void oppdaterSisteLestTidspunkt(Date date) {
        database.update(
                "UPDATE AVSLUTTETOPPFOLGING_FEED_METADATA SET tidspunkt_siste_lesing = ?",
                date
        );
    }
}