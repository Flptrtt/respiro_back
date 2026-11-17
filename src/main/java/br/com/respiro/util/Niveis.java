package br.com.respiro.util;

import br.com.respiro.dto.NivelResponse;

/** Semente -> Broto -> Muda -> Árvore, por pontos acumulados. */
public final class Niveis {
    private static final String[] NOMES = {"Semente", "Broto", "Muda", "Árvore"};
    private static final int[] MINIMOS = {0, 200, 800, 2000};

    private Niveis() {}

    public static NivelResponse de(int pontos) {
        int i = 0;
        while (i < MINIMOS.length - 1 && pontos >= MINIMOS[i + 1]) i++;
        boolean topo = i == MINIMOS.length - 1;
        return new NivelResponse(i + 1, NOMES[i], topo ? null : MINIMOS[i + 1], topo ? null : NOMES[i + 1]);
    }
}
