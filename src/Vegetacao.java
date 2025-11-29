/**
 * Representa uma planta no campo.
 * Gerencia seu próprio crescimento e valor nutricional.
 */
public class Vegetacao {
    
    private int nivelCrescimento;

    public Vegetacao() {
        // Assume-se que a simulação começa com grama crescida completamente
        this.nivelCrescimento = Configuracao.MAX_CRESCIMENTO;
    }

    /**
     * Faz a planta crescer um estágio, se ainda não estiver madura.
     */
    public void crescer() {
        if (nivelCrescimento < Configuracao.MAX_CRESCIMENTO) {
            nivelCrescimento++;
        }
    }

    /**
     * Verifica se a planta está madura (comestível/visível).
     */
    public boolean estaMadura() {
        return nivelCrescimento >= Configuracao.MAX_CRESCIMENTO;
    }

    /**
     * O animal come a planta. A planta reseta seu crescimento.
     * @return O valor nutricional obtido.
     */
    public int vegetacaoEhComida() {
        if (estaMadura()) {
            nivelCrescimento = 0; // Planta foi comida
            return Configuracao.VALOR_NUTRICIONAL;
        }
        return 0; // Não havia nada para comer
    }

    // Getters e Setters
    public int getNivelCrescimento() {
        return nivelCrescimento;
    }

    public void setNivelCrescimento(int nivel) {
        this.nivelCrescimento = nivel;
    }
}