package br.com.respiro.service;

import br.com.respiro.dto.AuthResponse;
import br.com.respiro.dto.LoginRequest;
import br.com.respiro.dto.RegistroRequest;
import br.com.respiro.exception.ApiException;
import br.com.respiro.model.Usuario;
import br.com.respiro.repository.UsuarioRepository;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UsuarioRepository usuarios;
    private final UsuarioService usuarioService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UsuarioRepository usuarios, UsuarioService usuarioService) {
        this.usuarios = usuarios;
        this.usuarioService = usuarioService;
    }

    public AuthResponse registrar(RegistroRequest r) {
        String email = r.email().trim().toLowerCase();
        if (usuarios.porEmail(email).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "Já existe uma conta com esse e-mail");
        }
        String token = novoToken();
        long id = usuarios.inserir(r.nome().trim(), email, encoder.encode(r.senha()), token);
        return new AuthResponse(token, usuarioService.perfil(id));
    }

    public AuthResponse login(LoginRequest r) {
        Usuario u = usuarios.porEmail(r.email().trim().toLowerCase())
                .filter(x -> encoder.matches(r.senha(), x.senhaHash()))
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "E-mail ou senha incorretos"));
        String token = novoToken();
        usuarios.salvarToken(u.id(), token);
        return new AuthResponse(token, usuarioService.perfil(u.id()));
    }

    private String novoToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
