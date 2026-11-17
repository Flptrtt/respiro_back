package br.com.respiro.repository;

import br.com.respiro.model.Usuario;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepository {
    private final JdbcTemplate jdbc;

    public UsuarioRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Usuario> MAPPER = (rs, n) -> {
        String dia = rs.getString("ultimo_dia_ativo");
        return new Usuario(rs.getLong("id"), rs.getString("nome"), rs.getString("email"),
                rs.getString("senha_hash"), rs.getInt("horas_iniciais"), rs.getInt("meta_diaria_horas"),
                rs.getInt("pontos"), rs.getInt("sequencia"), rs.getInt("melhor_sequencia"),
                dia == null ? null : LocalDate.parse(dia), rs.getInt("onboarding_ok") == 1);
    };

    public Optional<Usuario> porId(long id) {
        return jdbc.query("SELECT * FROM usuario WHERE id = ?", MAPPER, id).stream().findFirst();
    }

    public Optional<Usuario> porEmail(String email) {
        return jdbc.query("SELECT * FROM usuario WHERE email = ?", MAPPER, email).stream().findFirst();
    }

    public Optional<Usuario> porToken(String token) {
        return jdbc.query("SELECT * FROM usuario WHERE token = ?", MAPPER, token).stream().findFirst();
    }

    public long inserir(String nome, String email, String senhaHash, String token) {
        return Db.inserir(jdbc, "INSERT INTO usuario (nome, email, senha_hash, token) VALUES (?, ?, ?, ?)",
                nome, email, senhaHash, token);
    }

    public void salvarToken(long id, String token) {
        jdbc.update("UPDATE usuario SET token = ? WHERE id = ?", token, id);
    }

    public void atualizarOnboarding(long id, int horasIniciais, int metaHoras) {
        jdbc.update("UPDATE usuario SET horas_iniciais = ?, meta_diaria_horas = ?, onboarding_ok = 1 WHERE id = ?",
                horasIniciais, metaHoras, id);
    }

    public void atualizarMeta(long id, int metaHoras) {
        jdbc.update("UPDATE usuario SET meta_diaria_horas = ? WHERE id = ?", metaHoras, id);
    }

    public void atualizarProgresso(long id, int pontos, int sequencia, int melhorSequencia, LocalDate dia) {
        jdbc.update("UPDATE usuario SET pontos = ?, sequencia = ?, melhor_sequencia = ?, ultimo_dia_ativo = ? WHERE id = ?",
                pontos, sequencia, melhorSequencia, dia.toString(), id);
    }

    public List<String> interesses(long id) {
        return jdbc.queryForList("SELECT nome FROM interesse WHERE usuario_id = ? ORDER BY nome", String.class, id);
    }

    public void substituirInteresses(long id, List<String> nomes) {
        jdbc.update("DELETE FROM interesse WHERE usuario_id = ?", id);
        for (String nome : nomes) {
            jdbc.update("INSERT OR IGNORE INTO interesse (usuario_id, nome) VALUES (?, ?)", id, nome);
        }
    }
}
