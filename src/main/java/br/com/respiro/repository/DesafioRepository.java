package br.com.respiro.repository;

import br.com.respiro.model.Desafio;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DesafioRepository {
    private final JdbcTemplate jdbc;

    public DesafioRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Desafio> MAPPER = (rs, n) -> new Desafio(rs.getLong("id"),
            rs.getLong("usuario_id"), rs.getString("nome"), rs.getInt("duracao_dias"),
            rs.getInt("meta_diaria_min"), rs.getInt("progresso_min"), LocalDate.parse(rs.getString("criado_em")));

    public List<Desafio> listar(long usuarioId) {
        return jdbc.query("SELECT * FROM desafio WHERE usuario_id = ? ORDER BY id DESC", MAPPER, usuarioId);
    }

    public long inserir(long usuarioId, String nome, int duracaoDias, int metaDiariaMin, LocalDate hoje) {
        return Db.inserir(jdbc,
                "INSERT INTO desafio (usuario_id, nome, duracao_dias, meta_diaria_min, criado_em) VALUES (?, ?, ?, ?, ?)",
                usuarioId, nome, duracaoDias, metaDiariaMin, hoje.toString());
    }

    /** Soma minutos aos desafios ainda ativos (dentro do prazo e não completos). */
    public void somarProgresso(long usuarioId, int minutos, LocalDate hoje) {
        jdbc.update("""
                UPDATE desafio
                   SET progresso_min = MIN(progresso_min + ?, duracao_dias * meta_diaria_min)
                 WHERE usuario_id = ?
                   AND progresso_min < duracao_dias * meta_diaria_min
                   AND date(criado_em, '+' || duracao_dias || ' days') > ?
                """, minutos, usuarioId, hoje.toString());
    }
}
