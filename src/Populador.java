import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Classe responsável pela população inicial do campo de simulação.
 * <p>
 * Esta classe implementa a lógica de distribuição aleatória de animais
 * no campo no início da simulação, respeitando probabilidades configuradas
 * e evitando sobrescrever obstáculos fixos.
 * </p>
 * 
 * <p>
 * <strong>Processo de População:</strong>
 * </p>
 * <ol>
 * <li>Percorre todas as células do campo</li>
 * <li>Pula células que já contêm obstáculos</li>
 * <li>Para cada célula vazia, testa probabilidades de cada espécie</li>
 * <li>Cria animais com idades aleatórias para simular população
 * estabelecida</li>
 * <li>Embaralha a lista final para aleatorizar ordem de ação</li>
 * </ol>
 * 
 * <p>
 * <strong>Probabilidades de Criação:</strong>
 * </p>
 * <p>
 * Cada espécie tem uma probabilidade independente definida em
 * {@link Configuracao}.
 * Como os testes são sequenciais, espécies testadas primeiro têm prioridade
 * em caso de múltiplos sucessos (situação rara devido a probabilidades baixas).
 * </p>
 * 
 * <p>
 * <strong>Extensibilidade:</strong>
 * </p>
 * <p>
 * Para adicionar novas espécies à simulação:
 * <ol>
 * <li>Adicione a probabilidade em {@link Configuracao}</li>
 * <li>Adicione um teste {@code else if} no método {@link #popular}</li>
 * <li>Não é necessário modificar outras classes</li>
 * </ol>
 * </p>
 * 
 * @author Grupo 10
 * @version 1.0
 * @see Configuracao
 * @see Campo
 * @see Animal
 */
public class Populador {

    // ========== MÉTODOS DE REGISTRO ==========

    /**
     * Registra um animal no campo e na lista de atores.
     * <p>
     * Este método centraliza o processo de adicionar um animal à simulação,
     * garantindo que todas as referências sejam configuradas corretamente:
     * <ul>
     * <li>Adiciona à lista de animais (para processamento)</li>
     * <li>Define a localização do animal</li>
     * <li>Coloca o animal no campo (para visualização)</li>
     * </ul>
     * </p>
     * 
     * @param animal  Instância do animal a ser registrado
     * @param linha   Coordenada Y (linha) no campo
     * @param coluna  Coordenada X (coluna) no campo
     * @param campo   Campo onde o animal será posicionado
     * @param animais Lista de atores onde o animal será adicionado
     */
    private void registrarAnimal(Animal animal, int linha, int coluna, Campo campo, List<Ator> animais) {
        animais.add(animal);
        animal.definirLocalizacao(linha, coluna);
        campo.colocar(animal, linha, coluna);
    }

    // ========== MÉTODOS DE POPULAÇÃO ==========

    /**
     * Popula o campo com uma distribuição aleatória de animais.
     * <p>
     * <strong>Algoritmo de população:</strong>
     * <ol>
     * <li>Percorre cada célula do campo (linha por linha)</li>
     * <li>Verifica se a célula está vazia (sem obstáculos)</li>
     * <li>Testa sequencialmente a criação de cada espécie</li>
     * <li>Cria animais com idade aleatória (população estabelecida)</li>
     * <li>Embaralha a lista final para ordem de ação aleatória</li>
     * </ol>
     * </p>
     * 
     * <p>
     * <strong>Ordem de Prioridade das Espécies:</strong>
     * </p>
     * <ol>
     * <li>Raposa (predador médio)</li>
     * <li>Coelho (herbívoro)</li>
     * <li>Rato (herbívoro)</li>
     * <li>Cobra (predador)</li>
     * <li>Gavião (predador aéreo)</li>
     * <li>Urso (predador de topo)</li>
     * </ol>
     * 
     * <p>
     * <strong>Por que embaralhar?</strong>
     * </p>
     * <p>
     * O embaralhamento final garante que a ordem de processamento dos animais
     * seja aleatória, evitando viés onde animais criados primeiro sempre
     * agiriam antes dos criados depois.
     * </p>
     * 
     * @param campo   Campo a ser populado com animais
     * @param animais Lista vazia que será preenchida com os animais criados
     */
    public void popular(Campo campo, List<Ator> animais) {
        Random aleatorio = new Random();

        // Percorre todas as células do campo
        for (int linha = 0; linha < campo.getProfundidade(); linha++) {
            for (int coluna = 0; coluna < campo.getLargura(); coluna++) {

                // Pula células que já contêm obstáculos (rios, pedras)
                if (campo.getObjetoEm(linha, coluna) != null) {
                    continue;
                }

                // Testa criação de cada espécie baseado em probabilidades configuradas
                if (aleatorio.nextDouble() <= Configuracao.PROBABILIDADE_CRIACAO_RAPOSA) {
                    registrarAnimal(new Raposa(true), linha, coluna, campo, animais);
                } else if (aleatorio.nextDouble() <= Configuracao.PROBABILIDADE_CRIACAO_COELHO) {
                    registrarAnimal(new Coelho(true), linha, coluna, campo, animais);
                } else if (aleatorio.nextDouble() <= Configuracao.PROBABILIDADE_CRIACAO_RATO) {
                    registrarAnimal(new Rato(true), linha, coluna, campo, animais);
                } else if (aleatorio.nextDouble() <= Configuracao.PROBABILIDADE_CRIACAO_COBRA) {
                    registrarAnimal(new Cobra(true), linha, coluna, campo, animais);
                } else if (aleatorio.nextDouble() <= Configuracao.PROBABILIDADE_CRIACAO_GAVIAO) {
                    registrarAnimal(new Gaviao(true), linha, coluna, campo, animais);
                } else if (aleatorio.nextDouble() <= Configuracao.PROBABILIDADE_CRIACAO_URSO) {
                    registrarAnimal(new Urso(true), linha, coluna, campo, animais);
                }
                // Novas espécies podem ser adicionadas aqui seguindo o mesmo padrão
            }
        }

        // Embaralha para garantir ordem de ação aleatória
        Collections.shuffle(animais);
    }
}