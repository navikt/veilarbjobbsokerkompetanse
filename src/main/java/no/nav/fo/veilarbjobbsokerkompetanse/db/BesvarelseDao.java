package no.nav.fo.veilarbjobbsokerkompetanse.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Besvarelse;
import no.nav.sbl.jdbc.Database;

import java.sql.ResultSet;
import java.util.List;

import static java.util.stream.Collectors.toList;

class BesvarelseDao {

    private final Database db;
    private final SvarAlternativDao svarAlternativDao;

    BesvarelseDao(Database db) {
        this.db = db;
        this.svarAlternativDao = new SvarAlternativDao(db);
    }

    void create(Besvarelse besvarelse, long kartleggingId) {
        long besvarelseId = db.nesteFraSekvens("BESVARELSE_SEQ");
        db.update("INSERT INTO BESVARELSE (" +
                        "besvarelse_id, " +
                        "kartlegging_id, " +
                        "sporsmal_key, " +
                        "sporsmal, " +
                        "tips_key, " +
                        "tips) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                besvarelseId,
                kartleggingId,
                besvarelse.getSporsmalKey(),
                besvarelse.getSporsmal(),
                besvarelse.getTipsKey(),
                besvarelse.getTips()
        );
        besvarelse.getSvarAlternativ().forEach(sa -> svarAlternativDao.create(sa, besvarelseId));
    }

    List<Besvarelse> fetchByKartleggingId(long kartleggingId) {
        List<Besvarelse> besvarelse = db.query("SELECT * FROM BESVARELSE WHERE kartlegging_id = ?",
                this::map,
                kartleggingId
        );
        return besvarelse.stream()
                .map(s -> s.toBuilder()
                        .svarAlternativ(svarAlternativDao.fetchByBesvarelseId(s.getBesvarelseId()))
                        .build())
                .collect(toList());
    }

    @SneakyThrows
    private Besvarelse map(ResultSet rs) {
        return Besvarelse.builder()
                .besvarelseId(rs.getLong("besvarelse_id"))
                .kartleggingId(rs.getLong("kartlegging_id"))
                .sporsmalKey(rs.getString("sporsmal_key"))
                .sporsmal(rs.getString("sporsmal"))
                .tipsKey(rs.getString("tips_key"))
                .tips(rs.getString("tips"))
                .build();
    }

}
