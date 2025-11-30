import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Responsável por preencher o campo com animais no início da simulação.
 */
public class Populador {
    
    /**
     * Registra um animal no campo e na lista de animais.
     * @param animal O animal a ser registrado.
     * @param linha A linha onde o animal será colocado.
     * @param coluna A coluna onde o animal será colocado.
     * @param campo O campo onde o animal será colocado.
     * @param animais A lista onde o animal será adicionado.
     */
    private void registrarAnimal(Animal animal, int linha, int coluna, Campo campo, List<Ator> animais) {
        animais.add(animal);
        animal.definirLocalizacao(linha, coluna);
        campo.colocar(animal, linha, coluna);
    }

    /**
     * Popula o campo com animais aleatoriamente.
     * @param campo O campo a ser preenchido.
     * @param animais A lista onde os novos animais serão registrados.
     */
    public void popular(Campo campo, List<Ator> animais) {
        Random aleatorio = new Random();
        
        for(int linha = 0; linha < campo.getProfundidade(); linha++) {
            for(int coluna = 0; coluna < campo.getLargura(); coluna++) {
                
                // Se já existe algo (obstáculo ou outro animal), pula
                if(campo.getObjetoEm(linha, coluna) != null) {
                    continue;
                }

                // Lógica de criação baseada na Configuração
                if(aleatorio.nextDouble() <= Configuracao.PROBABILIDADE_CRIACAO_RAPOSA) {
                    registrarAnimal(new Raposa(true), linha, coluna, campo, animais);
                }
                else if(aleatorio.nextDouble() <= Configuracao.PROBABILIDADE_CRIACAO_COELHO) {
                    registrarAnimal(new Coelho(true), linha, coluna, campo, animais);
                }
                else if(aleatorio.nextDouble() <= Configuracao.PROBABILIDADE_CRIACAO_RATO) {
                    registrarAnimal(new Rato(true), linha, coluna, campo, animais);
                }
                else if(aleatorio.nextDouble() <= Configuracao.PROBABILIDADE_CRIACAO_COBRA) {
                    registrarAnimal(new Cobra(true), linha, coluna, campo, animais);
                }
                else if(aleatorio.nextDouble() <= Configuracao.PROBABILIDADE_CRIACAO_GAVIAO) {
                    registrarAnimal(new Gaviao(true), linha, coluna, campo, animais);
                }
                else if(aleatorio.nextDouble() <= Configuracao.PROBABILIDADE_CRIACAO_URSO) {
                    registrarAnimal(new Urso(true), linha, coluna, campo, animais);
                }
                // Adicione novas espécies aqui no futuro sem mexer no Simulador
            }
        }
        Collections.shuffle(animais);
    }
}