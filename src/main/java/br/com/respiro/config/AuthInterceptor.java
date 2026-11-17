package br.com.respiro.config;

import br.com.respiro.exception.ApiException;
import br.com.respiro.model.Usuario;
import br.com.respiro.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/** Lê "Authorization: Bearer <token>" e deixa o id do usuário no request. */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final UsuarioRepository usuarios;

    public AuthInterceptor(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) return true;
        String header = req.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) throw naoAutorizado();
        Usuario u = usuarios.porToken(header.substring(7)).orElseThrow(AuthInterceptor::naoAutorizado);
        req.setAttribute("usuarioId", u.id());
        return true;
    }

    private static ApiException naoAutorizado() {
        return new ApiException(HttpStatus.UNAUTHORIZED, "Sessão expirada. Entre novamente.");
    }
}
