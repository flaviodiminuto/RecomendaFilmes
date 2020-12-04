package com.sexto.ia.service;

import com.sexto.ia.exceptions.SessaoExpiradaException;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@ApplicationScoped
public class ControlaSessaoService {

    private final int duracao = 360;
    private final String sessionName = "predicado";
    private final String validValue = "1";

    public boolean cookieValido(Cookie cookie){
        return cookie != null && cookie.getValue().equals(validValue);
    }

    public NewCookie mantemSessao(Map<String, Cookie> cookieMap) throws SessaoExpiradaException {
        Cookie cookie = cookieMap.get(sessionName);
        if (cookieValido(cookie)) {
            return getCookiePredicadoValido();
        }
        throw new SessaoExpiradaException();
    }

    public NewCookie getCookiePredicadoValido(){
        Instant expira = Instant.now().plusSeconds(duracao);
        return new NewCookie(sessionName,
                validValue,
                "",
                null,
                1,
                null,
                duracao,
                Date.from(expira),
                false,
                false);
    }
}
