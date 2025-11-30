import java.util.List;
import java.util.Random;

/**
 * Classe abstrata base para todos os animais na simulação.
 * <p>
 * Esta classe implementa comportamentos comuns a todos os animais,
 * independentemente de serem predadores ou herbívoros. Define o ciclo
 * de vida básico: nascer, envelhecer, se alimentar, reproduzir e morrer.
 * </p>
 * 
 * <p>
 * <strong>Hierarquia de Classes:</strong>
 * </p>
 * 
 * <pre>
 * Animal (abstrata)
 *  ├── Predador (abstrata)
 *  │    ├── Raposa
 *  │    ├── Cobra
 *  │    ├── Gavião
 *  │    └── Urso
 *  └── Herbivoro (abstrata)
 *       ├── Coelho
 *       └── Rato
 * </pre>
 * 
 * <p>
 * <strong>Características Comuns:</strong>
 * </p>
 * <ul>
 * <li><strong>Idade:</strong> Incrementa a cada turno, causa morte ao atingir
 * máximo</li>
 * <li><strong>Fome:</strong> Diminui a cada turno, causa morte ao chegar a
 * zero</li>
 * <li><strong>Localização:</strong> Posição atual no campo</li>
 * <li><strong>Reprodução:</strong> Baseada em idade, probabilidade e espaço
 * disponível</li>
 * <li><strong>Movimento:</strong> Busca de células adjacentes livres</li>
 * </ul>
 * 
 * <p>
 * <strong>Ciclo de Vida Padrão:</strong>
 * </p>
 * <ol>
 * <li>Nasce com idade 0 ou aleatória (população inicial)</li>
 * <li>A cada turno: {@link #incrementarIdade()} e
 * {@link #incrementarFome()}</li>
 * <li>Verifica se ainda está vivo após envelhecimento/fome</li>
 * <li>Se vivo: tenta se alimentar (implementação nas subclasses)</li>
 * <li>Se vivo: tenta reproduzir ({@link #processarReproducao})</li>
 * <li>Se vivo: move-se ({@link #tentarMoverLivremente})</li>
 * <li>Se morreu: é removido da simulação</li>
 * </ol>
 * 
 * <p>
 * <strong>Padrão Template Method:</strong>
 * </p>
 * <p>
 * Esta classe define o esqueleto do comportamento animal, delegando
 * detalhes específicos (idade máxima, dieta, etc.) para subclasses
 * através de métodos abstratos.
 * </p>
 * 
 * @author David J. Barnes e Michael Kolling
 * @author Grupo 10
 * @version 2025-11-30
 * @see Ator
 * @see Predador
 * @see Herbivoro
 */
public abstract class Animal implements Ator {

    // ========== ATRIBUTOS ESTÁTICOS ==========

    /**
     * Gerador de números aleatórios compartilhado por todos os animais.
     * <p>
     * Usado para decisões estocásticas (reprodução, movimento, etc.).
     * </p>
     */
    private static final Random aleatorio = new Random();

    // ========== ATRIBUTOS DE ESTADO ==========

    /**
     * Indica se o animal está vivo na simulação.
     */
    private boolean vivo;

    /**
     * Posição atual do animal no campo.
     */
    private Localizacao localizacao;

    /**
     * Idade atual do animal em passos de simulação.
     * <p>
     * Incrementada a cada turno. Ao atingir {@link #idadeMaxima()},
     * o animal morre de velhice.
     * </p>
     */
    private int idade;

    /**
     * Nível atual de energia/alimento do animal.
     * <p>
     * Decrementado a cada turno (fome). Incrementado ao se alimentar.
     * Ao chegar a zero, o animal morre de inanição.
     * </p>
     */
    private int nivelAlimento;

    // ========== CONSTRUTOR ==========

    /**
     * Cria um novo animal com estado inicial configurado.
     * <p>
     * <strong>Inicialização de idade:</strong>
     * <ul>
     * <li>idadeAleatoria = false: idade = 0 (filhote recém-nascido)</li>
     * <li>idadeAleatoria = true: idade aleatória (população inicial
     * estabelecida)</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Inicialização de alimento:</strong>
     * <ul>
     * <li>Filhotes: nível padrão ({@link Configuracao#VALOR_ALIMENTAR})</li>
     * <li>População inicial: valor aleatório (simula estados variados)</li>
     * </ul>
     * </p>
     * 
     * @param idadeAleatoria Se true, simula animal já existente; se false, simula
     *                       nascimento
     */
    public Animal(boolean idadeAleatoria) {
        vivo = true;
        idade = 0;
        if (idadeAleatoria) {
            idade = aleatorio.nextInt(idadeMaxima());
        }
        localizacao = null;

        setNivelAlimento(Configuracao.VALOR_ALIMENTAR);
        if (idadeAleatoria)
            setNivelAlimento(getAleatorio().nextInt(Configuracao.VALOR_ALIMENTAR));
    }

    // ========== MÉTODOS DE ACESSO A RECURSOS ==========

    /**
     * Retorna o gerador de números aleatórios compartilhado.
     * <p>
     * Acessível a subclasses para decisões estocásticas.
     * </p>
     * 
     * @return Gerador Random compartilhado
     */
    protected Random getAleatorio() {
        return aleatorio;
    }

    // ========== MÉTODOS DE ESTADO DE VIDA ==========

    /**
     * Verifica se o animal está vivo.
     * 
     * @return true se vivo, false se morto
     */
    @Override
    public boolean estaVivo() {
        return vivo;
    }

    /**
     * Marca o animal como morto e zera seu nível de alimento.
     * <p>
     * Chamado quando:
     * <ul>
     * <li>Idade máxima é atingida</li>
     * <li>Nível de alimento chega a zero</li>
     * <li>Animal é predado por outro</li>
     * <li>Não há espaço para se mover (superpopulação)</li>
     * </ul>
     * </p>
     */
    public void morrer() {
        vivo = false;
        nivelAlimento = 0;
    }

    // ========== MÉTODOS DE LOCALIZAÇÃO ==========

    /**
     * Retorna a localização atual do animal no campo.
     * 
     * @return Posição atual do animal
     */
    public Localizacao getLocalizacao() {
        return localizacao;
    }

    /**
     * Define a localização do animal usando coordenadas diretas.
     * 
     * @param linha  Coordenada Y no campo
     * @param coluna Coordenada X no campo
     */
    protected void definirLocalizacao(int linha, int coluna) {
        this.localizacao = new Localizacao(linha, coluna);
    }

    /**
     * Define a localização do animal usando objeto Localizacao.
     * 
     * @param localizacao Nova posição do animal
     */
    protected void definirLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    // ========== MÉTODOS DE IDADE E ENVELHECIMENTO ==========

    /**
     * Retorna a idade atual do animal em passos de simulação.
     * 
     * @return Idade do animal
     */
    protected int getIdade() {
        return idade;
    }

    /**
     * Incrementa a idade do animal em um passo.
     * <p>
     * Se a idade ultrapassar {@link #idadeMaxima()}, o animal
     * morre de velhice automaticamente.
     * </p>
     */
    protected void incrementarIdade() {
        idade++;
        if (idade > idadeMaxima()) {
            morrer();
        }
    }

    // ========== MÉTODOS DE ALIMENTAÇÃO ==========

    /**
     * Retorna o nível atual de energia/alimento do animal.
     * 
     * @return Nível de alimento (energia restante)
     */
    protected int getNivelAlimento() {
        return nivelAlimento;
    }

    /**
     * Define o nível de energia/alimento do animal.
     * <p>
     * Usado ao se alimentar para restaurar energia.
     * </p>
     * 
     * @param nivelAlimento Novo nível de energia
     */
    protected void setNivelAlimento(int nivelAlimento) {
        this.nivelAlimento = nivelAlimento;
    }

    /**
     * Decrementa o nível de alimento (simula fome).
     * <p>
     * Chamado a cada turno. Se o nível chegar a zero ou menos,
     * o animal morre de inanição.
     * </p>
     */
    protected void incrementarFome() {
        nivelAlimento--;
        if (nivelAlimento <= 0)
            morrer();
    }

    // ========== MÉTODOS DE REPRODUÇÃO ==========

    /**
     * Verifica se o animal atingiu a idade reprodutiva.
     * 
     * @return true se pode reproduzir, false caso contrário
     */
    public boolean podeReproduzir() {
        return idade >= getIdadeReprodutiva();
    }

    /**
     * Calcula o número de filhotes nascidos neste turno.
     * <p>
     * <strong>Algoritmo:</strong>
     * <ol>
     * <li>Verifica se atingiu idade reprodutiva</li>
     * <li>Testa probabilidade de reprodução</li>
     * <li>Se sucesso: gera número aleatório de filhotes (1 a máximo)</li>
     * <li>Se falha: retorna 0</li>
     * </ol>
     * </p>
     * 
     * @return Número de filhotes (0 se não reproduziu)
     */
    protected int reproduzir() {
        int nascimentos = 0;
        if (podeReproduzir() && aleatorio.nextDouble() <= probabilidadeReproducao()) {
            nascimentos = aleatorio.nextInt(tamanhoMaximoNinhada()) + 1;
        }
        return nascimentos;
    }

    /**
     * Processa a reprodução criando e posicionando filhotes.
     * <p>
     * <strong>Processo:</strong>
     * <ol>
     * <li>Calcula número de nascimentos</li>
     * <li>Para cada filhote:
     * <ul>
     * <li>Busca localização adjacente livre</li>
     * <li>Se encontrou: cria filhote via {@link #criarFilho()}</li>
     * <li>Posiciona no campo e adiciona à lista</li>
     * <li>Se não encontrou: filhote não nasce (falta de espaço)</li>
     * </ul>
     * </li>
     * </ol>
     * </p>
     * 
     * @param campoAtualizado Campo de destino onde filhotes serão posicionados
     * @param novosAnimais    Lista onde filhotes serão registrados
     */
    protected void processarReproducao(CampoInterativo campoAtualizado, List<Ator> novosAnimais) {
        int nascimentos = reproduzir();

        for (int i = 0; i < nascimentos; i++) {
            Localizacao loc = campoAtualizado.localizacaoAdjacenteLivre(getLocalizacao());

            if (loc != null) {
                Animal filhote = criarFilho();
                novosAnimais.add(filhote);
                filhote.definirLocalizacao(loc);
                campoAtualizado.colocar(filhote, loc);
            }
        }
    }

    // ========== MÉTODOS DE MOVIMENTO ==========

    /**
     * Tenta mover o animal para uma célula adjacente livre.
     * <p>
     * <strong>Comportamento:</strong>
     * <ul>
     * <li>Se encontrou célula livre: move-se para lá</li>
     * <li>Se não encontrou: morre por superpopulação (sem espaço)</li>
     * </ul>
     * </p>
     * <p>
     * Usado principalmente quando não há objetivo específico de movimento
     * (ex: predador não encontrou presa, herbívoro apenas explorando).
     * </p>
     * 
     * @param campoAtualizado Campo de destino para o movimento
     */
    protected void tentarMoverLivremente(CampoInterativo campoAtualizado) {
        Localizacao novaLocalizacao = campoAtualizado.localizacaoAdjacenteLivre(getLocalizacao());

        if (novaLocalizacao != null) {
            moverPara(novaLocalizacao, campoAtualizado);
        } else {
            morrer();
        }
    }

    /**
     * Efetiva o movimento do animal para uma localização específica.
     * <p>
     * Método auxiliar usado tanto para movimento livre quanto
     * para movimento direcionado (ex: predador indo até presa).
     * </p>
     * 
     * @param localizacao     Nova posição do animal
     * @param campoAtualizado Campo onde o animal será posicionado
     */
    protected void moverPara(Localizacao localizacao, CampoInterativo campoAtualizado) {
        definirLocalizacao(localizacao);
        campoAtualizado.colocar(this, localizacao);
    }

    // ========== MÉTODOS ABSTRATOS (Template Method) ==========

    /**
     * Cria um novo filhote da mesma espécie.
     * <p>
     * Cada subclasse deve implementar retornando uma nova
     * instância de si mesma com idade zero.
     * </p>
     * 
     * @return Novo animal filhote
     */
    protected abstract Animal criarFilho();

    /**
     * Retorna a idade mínima para reprodução.
     * 
     * @return Idade reprodutiva em passos de simulação
     */
    protected abstract int getIdadeReprodutiva();

    /**
     * Retorna a probabilidade de reprodução por turno.
     * 
     * @return Probabilidade entre 0.0 e 1.0
     */
    protected abstract double probabilidadeReproducao();

    /**
     * Retorna o número máximo de filhotes por ninhada.
     * 
     * @return Tamanho máximo da ninhada
     */
    protected abstract int tamanhoMaximoNinhada();

    /**
     * Retorna a idade máxima que o animal pode atingir.
     * 
     * @return Idade máxima em passos de simulação
     */
    protected abstract int idadeMaxima();
}