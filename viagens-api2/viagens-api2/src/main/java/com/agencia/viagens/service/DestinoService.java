package com.agencia.viagens.service;

import com.agencia.viagens.dto.AvaliacaoDTO;
import com.agencia.viagens.dto.DestinoRequestDTO;
import com.agencia.viagens.dto.DestinoResponseDTO;
import com.agencia.viagens.exception.DestinoNotFoundException;
import com.agencia.viagens.model.Destino;
import com.agencia.viagens.repository.DestinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Camada de serviço responsável por toda a lógica de negócio
 * relacionada aos Destinos de Viagem.
 */
@Service
@RequiredArgsConstructor
public class DestinoService {

    private final DestinoRepository destinoRepository;

    // ----------------------------------------------------------------
    // 1. Cadastrar destino
    // ----------------------------------------------------------------

    /**
     * Cadastra um novo destino de viagem.
     *
     * @param dto dados do destino a ser criado
     * @return destino criado com id gerado
     */
    @Transactional
    public DestinoResponseDTO cadastrarDestino(DestinoRequestDTO dto) {
        Destino destino = Destino.builder()
                .nome(dto.getNome())
                .localizacao(dto.getLocalizacao())
                .descricao(dto.getDescricao())
                .idioma(dto.getIdioma())
                .moeda(dto.getMoeda())
                .notaMedia(0.0)
                .totalAvaliacoes(0)
                .build();

        return toResponseDTO(destinoRepository.save(destino));
    }

    // ----------------------------------------------------------------
    // 2. Listar todos os destinos
    // ----------------------------------------------------------------

    /**
     * Retorna todos os destinos cadastrados.
     *
     * @return lista de destinos disponíveis
     */
    @Transactional(readOnly = true)
    public List<DestinoResponseDTO> listarDestinos() {
        return destinoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ----------------------------------------------------------------
    // 3. Pesquisar por nome ou localização
    // ----------------------------------------------------------------

    /**
     * Pesquisa destinos por nome e/ou localização.
     * Ambos os filtros são opcionais e funcionam com busca parcial.
     *
     * @param nome        parte do nome a pesquisar (pode ser {@code null})
     * @param localizacao parte da localização (pode ser {@code null})
     * @return lista de destinos que atendem aos critérios
     */
    @Transactional(readOnly = true)
    public List<DestinoResponseDTO> pesquisarDestinos(String nome, String localizacao) {
        // Converte strings vazias em null para que o JPQL ignore o filtro
        String nomeFiltro = (nome != null && nome.isBlank()) ? null : nome;
        String localizacaoFiltro = (localizacao != null && localizacao.isBlank()) ? null : localizacao;

        return destinoRepository
                .pesquisarPorNomeOuLocalizacao(nomeFiltro, localizacaoFiltro)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ----------------------------------------------------------------
    // 4. Visualizar destino por id
    // ----------------------------------------------------------------

    /**
     * Retorna as informações detalhadas de um destino específico.
     *
     * @param id identificador do destino
     * @return dados detalhados do destino
     * @throws DestinoNotFoundException se o destino não for encontrado
     */
    @Transactional(readOnly = true)
    public DestinoResponseDTO visualizarDestino(Long id) {
        Destino destino = buscarPorId(id);
        return toResponseDTO(destino);
    }

    // ----------------------------------------------------------------
    // 5. Avaliar destino (PATCH — recalcula nota média)
    // ----------------------------------------------------------------

    /**
     * Recebe uma nova nota (1-10) e atualiza a média acumulada do destino.
     * Fórmula: novaMedia = (mediaAtual × totalAnterior + novaNota) / (totalAnterior + 1)
     *
     * @param id  identificador do destino
     * @param dto avaliação com a nota enviada
     * @return destino com a nova média calculada
     * @throws DestinoNotFoundException se o destino não for encontrado
     */
    @Transactional
    public DestinoResponseDTO avaliarDestino(Long id, AvaliacaoDTO dto) {
        Destino destino = buscarPorId(id);

        int totalAnterior   = destino.getTotalAvaliacoes();
        double mediaAtual   = destino.getNotaMedia();
        double novaNota     = dto.getNota();

        // Média acumulada incremental
        double novaMedia = ((mediaAtual * totalAnterior) + novaNota) / (totalAnterior + 1);

        // Arredonda para uma casa decimal
        destino.setNotaMedia(Math.round(novaMedia * 10.0) / 10.0);
        destino.setTotalAvaliacoes(totalAnterior + 1);

        return toResponseDTO(destinoRepository.save(destino));
    }

    // ----------------------------------------------------------------
    // 6. Excluir destino
    // ----------------------------------------------------------------

    /**
     * Remove permanentemente um destino de viagem.
     *
     * @param id identificador do destino a excluir
     * @throws DestinoNotFoundException se o destino não for encontrado
     */
    @Transactional
    public void excluirDestino(Long id) {
        buscarPorId(id); // lança exceção se não existir
        destinoRepository.deleteById(id);
    }

    // ----------------------------------------------------------------
    // Métodos utilitários privados
    // ----------------------------------------------------------------

    private Destino buscarPorId(Long id) {
        return destinoRepository.findById(id)
                .orElseThrow(() -> new DestinoNotFoundException(
                        "Destino com id " + id + " não encontrado."));
    }

    private DestinoResponseDTO toResponseDTO(Destino destino) {
        return DestinoResponseDTO.builder()
                .id(destino.getId())
                .nome(destino.getNome())
                .localizacao(destino.getLocalizacao())
                .descricao(destino.getDescricao())
                .idioma(destino.getIdioma())
                .moeda(destino.getMoeda())
                .notaMedia(destino.getNotaMedia())
                .totalAvaliacoes(destino.getTotalAvaliacoes())
                .build();
    }
}
