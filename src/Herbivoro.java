import java.util.List;

/**
 * Representa um herbívoro na simulação de predador e presa.
 * Esta é uma classe abstrata que define os comportamentos e 
 * características comuns a todos os herbívoros do modelo, como comer grama.
 */
public abstract class Herbivoro extends Animal {
    /**
     * Cria um herbívoro.
     * @param idadeAleatoria Se verdadeiro, o herbívoro terá uma idade aleatória.
     */
    public Herbivoro(boolean idadeAleatoria) {
        super(idadeAleatoria);
    }

    /**
     * Isto é o que o herbívoro faz na maior parte do tempo — ele corre
     * por aí. Às vezes ele se reproduz, come grama ou morre de velhice.
     * A fome agora também é considerada.
     * @param campoAtual O campo atual.
     * @param campoAtualizado O campo onde os animais atualizados devem ser colocados.
     * @param novosHerbivoros Uma lista para armazenar os novos herbívoros nascidos.
     */
    @Override
    public void agir(CampoInterativo campoAtual, CampoInterativo campoAtualizado, List<Ator> novosHerbivoros) {
        incrementarIdade();
        incrementarFome();
        
        if(estaVivo()) {
            // Ação específica de Herbívoros: Comer grama onde está
            tentarComerGrama(campoAtual);
            
            // Reprodução
            processarReproducao(campoAtualizado, novosHerbivoros);
            
            // Movimentação simples
            tentarMoverLivremente(campoAtualizado);
        }
    }

    /**
     * Cria um novo filhote do herbívoro.
     * @return Um novo herbívoro.
     */
    protected abstract Herbivoro criarFilho();

    /**
     * Tenta comer grama na localização atual.
     * @param campo O campo onde o herbívoro está localizado.
     */
    private void tentarComerGrama(CampoInterativo campo) {
        int comida = campo.comerGrama(getLocalizacao());
        if (comida > 0) {
            setNivelAlimento(getNivelAlimento() + comida);
            if (getNivelAlimento() > Configuracao.VALOR_ALIMENTAR_MAX_HERBIVORO)
                setNivelAlimento(Configuracao.VALOR_ALIMENTAR_MAX_HERBIVORO);
        }
    }
}