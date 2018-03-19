package no.nav.fo.veilarbjobbsokerkompetanse.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Besvarelse;
import no.nav.sbl.jdbc.Database;

import javax.inject.Inject;
import java.sql.ResultSet;

public class BesvarelseDao implements Dao<Besvarelse> {

    @Inject
    private Database db;

    @Override
    public void create(Besvarelse besvarelse) {
        besvarelse.toBuilder().besvarelseId(db.nesteFraSekvens("BESVARELSE_SEQ"));
        db.update("INSERT INTO BESVARELSE (" +
                        "besvarelse_id, " +
                        "aktor_id, " +
                        "under_oppfolging, " +
                        "besvarelse_dato) " +
                        "VALUES (?, ?, ?, ?)",
                besvarelse.getBesvarelseId(),
                besvarelse.getAktorId(),
                besvarelse.isUnderOppfolging(),
                besvarelse.getBesvarelseDato()
        );
    }

    @Override
    public Besvarelse fetch(long id) {
        return db.queryForObject("SELECT * FROM BESVARELSE WHERE besvarelse_id = ?",
                this::map,
                id
        );
    }

    public Besvarelse fetchByAktorId(long aktorId) {
        return db.query("SELECT * FROM BESVARELSE WHERE aktor_id = ?",
                this::map,
                aktorId
        ).stream()
                .sorted((a, b) -> b.getBesvarelseDato().compareTo(a.getBesvarelseDato()))
                .findFirst().orElseThrow(RuntimeException::new); // TODO: Skal vi bare kaste RuntimeException?
    }

    @SneakyThrows
    @Override
    public Besvarelse map(ResultSet rs) {
        return Besvarelse.builder()
                .besvarelseId(rs.getLong("besvarelse_id"))
                .aktorId(rs.getLong("aktor_id"))
                .underOppfolging(rs.getBoolean("under_oppfolging"))
                .besvarelseDato(rs.getTimestamp("besvarelse_dato").toInstant())
                .build();
    }

}
