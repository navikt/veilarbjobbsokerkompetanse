package no.nav.fo.veilarbjobbsokerkompetanse.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Svar;
import no.nav.sbl.jdbc.Database;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
class SvarDao {

    @Inject
    private Database db;

    @Inject
    private SvarAlternativDao svarAlternativDao;

    void create(Svar svar, long besvarelseId) {
        long svarId = db.nesteFraSekvens("SVAR_SEQ");
        db.update("INSERT INTO SVAR (" +
                        "svar_id, " +
                        "besvarelse_id, " +
                        "sporsmal_key, " +
                        "sporsmal, " +
                        "tips_key, " +
                        "tips) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                svarId,
                besvarelseId,
                svar.getSporsmalKey(),
                svar.getSporsmal(),
                svar.getTipsKey(),
                svar.getTips()
        );
        svar.getSvarAlternativ().forEach(sa -> svarAlternativDao.create(sa, svarId));
    }

    List<Svar> fetchByBesvarelseId(long besvarelseId) {
        List<Svar> svar = db.query("SELECT * FROM SVAR WHERE svar_id = ?",
                this::map,
                besvarelseId
        );
        return svar.stream()
                .map(s -> s.toBuilder()
                        .svarAlternativ(svarAlternativDao.fetchBySvarId(s.getSvarId()))
                        .build())
                .collect(toList());
    }

    @SneakyThrows
    private Svar map(ResultSet rs) {
        return Svar.builder()
                .svarId(rs.getLong("svar_id"))
                .besvarelseId(rs.getLong("besvarelse_id"))
                .sporsmalKey(rs.getString("sporsmal_key"))
                .sporsmal(rs.getString("sporsmal"))
                .tipsKey(rs.getString("tips_key"))
                .tips(rs.getString("tips"))
                .build();
    }

}