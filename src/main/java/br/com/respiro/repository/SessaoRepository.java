package br.com.respiro.repository;

import br.com.respiro.model.Sessao;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SessaoRepository {
    private final JdbcTemplate jdbc;

    public SessaoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Sessao> MAPPER = (rs, n) -> new Sessao(rs.getLong("id"),
            rs.getLong("usuario_id"), rs.getInt("minutos"), rs.getInt("presente") == 1,
            rs.getInt("pontos"), LocalDate.parse(rs.getString("dia")));

    public long inserir(long usuarioId, int minutos, boolean presente, int pontos, LocalDate dia) {
        return Db.inserir(jdbc,
                "INSERT INTO sessao (usuario_id, minutos, presente, pontos, dia) VALUES (?, ?, ?, ?, ?)",
                usuarioId, minutos, presente ? 1 : 0, pontos, dia.toString());
    }

    public List<Sessao> recentes(long usuarioId, int limite) {
        return jdbc.query("SELECT * FROM sessao WHERE usuario_id = ? ORDER BY id DESC LIMIT ?",
                MAPPER, usuarioId, limite);
    }

    public List<LocalDate> diasComSessao(long usuarioId, LocalDate inicio, LocalDate fim) {
        return jdbc.queryForList(
                "SELECT DISTINCT dia FROM sessao WHERE usuario_id = ? AND dia BETWEEN ? AND ?",
                String.class, usuarioId, inicio.toString(), fim.toString()).stream().map(LocalDate::parse).toList();
    }

    public int minutosDoDia(long usuarioId, LocalDate dia) {
        Integer total = jdbc.queryForObject(
                "SELECT COALESCE(SUM(minutos), 0) FROM sessao WHERE usuario_id = ? AND dia = ?",
                Integer.class, usuarioId, dia.toString());
        return total == null ? 0 : total;
    }
}
