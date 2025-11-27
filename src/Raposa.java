import java.util.Iterator;
import java.util.List;

/**
 * Um modelo simples de uma raposa.
 * Raposas envelhecem, se movem, caçam coelhos e morrem.
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-11 (traduzido)
 */
public class Raposa extends Animal {
    private static final int IDADE_REPRODUTIVA = 10;
    private static final int IDADE_MAXIMA = 150;
    private static final double PROBABILIDADE_REPRODUCAO = 0.09;
    private static final int TAMANHO_MAXIMO_NINHADA = 3;
    private static final int VALOR_ALIMENTAR = 9;

    /**
     * Cria uma raposa. Pode ser criada como recém-nascida (idade zero
     * e não faminta) ou com idade aleatória.
     * 
     * @param idadeAleatoria Se verdadeiro, a raposa terá idade e nível de fome
     *                       aleatórios.
     */
    public Raposa(boolean idadeAleatoria) {
        super(idadeAleatoria);
        if (idadeAleatoria) {
            setNivelAlimento(getAleatorio().nextInt(VALOR_ALIMENTAR));
        } else {
            setNivelAlimento(VALOR_ALIMENTAR);
        }
    }

    /**
     * Isto é o que a raposa faz na maior parte do tempo: caçar.
     * Nesse processo, pode se reproduzir, morrer de fome
     * ou morrer de velhice.
     * 
     * @param campoAtual      O campo atual.
     * @param campoAtualizado O campo onde os animais atualizados devem ser
     *                        colocados.
     * @param novasRaposas    Uma lista para armazenar as novas raposas nascidas
     */
    @Override
    public void agir(Campo campoAtual, Campo campoAtualizado, List<Ator> novasRaposas) {
        incrementarIdade();
        incrementarFome();
        if (estaVivo()) {
            int nascimentos = reproduzir();
            for (int i = 0; i < nascimentos; i++) {
                Localizacao loc = campoAtualizado.localizacaoAdjacenteLivre(getLocalizacao());

                if (loc != null) {
                    Raposa novaRaposa = new Raposa(false);
                    novasRaposas.add(novaRaposa);
                    novaRaposa.definirLocalizacao(loc);
                    campoAtualizado.colocar(novaRaposa, loc);
                }
            }

            Localizacao novaLocalizacao = encontrarComida(campoAtual, getLocalizacao());
            if (novaLocalizacao == null) {
                novaLocalizacao = campoAtualizado.localizacaoAdjacenteLivre(getLocalizacao());
            }

            if (novaLocalizacao != null) {
                definirLocalizacao(novaLocalizacao);
                campoAtualizado.colocar(this, novaLocalizacao);
            } else {
                definirEstaVivo(false);
            }
        }
    }

    /**
     * Retorna a idade máxima da raposa.
     * 
     * @return A idade máxima.
     */
    @Override
    protected int IDADE_MAXIMA() {
        return IDADE_MAXIMA;
    }

    /**
     * Ordena que a raposa procure coelhos ou ratos adjacentes à sua localização
     * atual.
     * 
     * @param campo       O campo onde procurar.
     * @param localizacao A posição atual no campo.
     * @return Onde o alimento foi encontrado, ou null se não foi.
     */
    private Localizacao encontrarComida(Campo campo, Localizacao localizacao) {
        Iterator locaisAdjacentes = campo.localizacoesAdjacentes(localizacao);
        while (locaisAdjacentes.hasNext()) {
            Localizacao onde = (Localizacao) locaisAdjacentes.next();
            Object animal = campo.getObjetoEm(onde);

            if (animal instanceof Coelho) {
                Coelho coelho = (Coelho) animal;
                if (coelho.estaVivo()) {
                    coelho.definirEstaVivo(false);
                    setNivelAlimento(VALOR_ALIMENTAR);
                    return onde;
                }
            } else if (animal instanceof Rato) {
                Rato rato = (Rato) animal;
                if (rato.estaVivo()) {
                    rato.definirEstaVivo(false);
                    setNivelAlimento(VALOR_ALIMENTAR);
                    return onde;
                }
            }
        }
        return null;
    }

    /**
     * A probabilidade de reprodução da raposa.
     * 
     * @return A probabilidade de reprodução.
     */
    @Override
    protected double PROBABILIDADE_REPRODUCAO() {
        return PROBABILIDADE_REPRODUCAO;
    }

    /**
     * O tamanho máximo da ninhada da raposa.
     * 
     * @return O tamanho máximo da ninhada.
     */
    @Override
    protected int TAMANHO_MAXIMO_NINHADA() {
        return TAMANHO_MAXIMO_NINHADA;
    }

    /**
     * A idade em que uma raposa começa a procriar.
     * 
     * @return A idade reprodutiva.
     */
    @Override
    protected int getIdadeReprodutiva() {
        return IDADE_REPRODUTIVA;
    }
}
