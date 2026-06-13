package com.agencia.viagens.controller;

import com.agencia.viagens.dto.AvaliacaoDTO;
import com.agencia.viagens.dto.DestinoRequestDTO;
import com.agencia.viagens.dto.DestinoResponseDTO;
import com.agencia.viagens.service.DestinoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST que expõe os endpoints da API de Destinos de Viagem.
 *
 * <pre>
 * POST   /api/destinos                    → Cadastrar destino
 * GET    /api/destinos                    → Listar todos os destinos
 * GET    /api/destinos/pesquisa           → Pesquisar por nome ou localização
 * GET    /api/destinos/{id}              → Visualizar destino específico
 * PATCH  /api/destinos/{id}/avaliar      → Avaliar destino (nota 1-10)
 * DELETE /api/destinos/{id}             → Excluir destino
 * </pre>
 */
@RestController
@RequestMapping("/api/destinos")
@RequiredArgsConstructor
public class DestinoController {

    private final DestinoService destinoService;

    // ----------------------------------------------------------------
    // POST /api/destinos → Cadastrar destino
    // ----------------------------------------------------------------

    /**
     * Cadastra um novo destino de viagem.
     *
     * @param dto corpo da requisição com os dados do destino
     * @return 201 Created com o destino criado
     */
    @PostMapping
    public ResponseEntity<DestinoResponseDTO> cadastrarDestino(
            @Valid @RequestBody DestinoRequestDTO dto) {

        DestinoResponseDTO response = destinoService.cadastrarDestino(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ----------------------------------------------------------------
    // GET /api/destinos → Listar todos os destinos
    // ----------------------------------------------------------------

    /**
     * Lista todos os destinos de viagem disponíveis.
     *
     * @return 200 OK com a lista completa de destinos
     */
    @GetMapping
    public ResponseEntity<List<DestinoResponseDTO>> listarDestinos() {
        return ResponseEntity.ok(destinoService.listarDestinos());
    }

    // ----------------------------------------------------------------
    // GET /api/destinos/pesquisa?nome=&localizacao= → Pesquisar
    // ----------------------------------------------------------------

    /**
     * Pesquisa destinos por nome e/ou localização.
     * Ambos os parâmetros são opcionais; a busca é parcial e case-insensitive.
     *
     * <p>Exemplos:
     * <ul>
     *   <li>{@code GET /api/destinos/pesquisa?nome=paris}</li>
     *   <li>{@code GET /api/destinos/pesquisa?localizacao=franca}</li>
     *   <li>{@code GET /api/destinos/pesquisa?nome=rio&localizacao=brasil}</li>
     * </ul>
     *
     * @param nome        filtro parcial por nome (opcional)
     * @param localizacao filtro parcial por localização (opcional)
     * @return 200 OK com os destinos encontrados
     */
    @GetMapping("/pesquisa")
    public ResponseEntity<List<DestinoResponseDTO>> pesquisarDestinos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String localizacao) {

        return ResponseEntity.ok(destinoService.pesquisarDestinos(nome, localizacao));
    }

    // ----------------------------------------------------------------
    // GET /api/destinos/{id} → Visualizar destino específico
    // ----------------------------------------------------------------

    /**
     * Retorna as informações detalhadas de um destino de viagem.
     *
     * @param id identificador único do destino
     * @return 200 OK com os dados do destino, ou 404 se não encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<DestinoResponseDTO> visualizarDestino(
            @PathVariable Long id) {

        return ResponseEntity.ok(destinoService.visualizarDestino(id));
    }

    // ----------------------------------------------------------------
    // PATCH /api/destinos/{id}/avaliar → Avaliar destino
    // ----------------------------------------------------------------

    /**
     * Recebe uma nota (1-10) e atualiza a média de avaliação do destino.
     *
     * @param id  identificador do destino
     * @param dto corpo com o campo {@code nota} (inteiro de 1 a 10)
     * @return 200 OK com o destino e sua nova nota média
     */
    @PatchMapping("/{id}/avaliar")
    public ResponseEntity<DestinoResponseDTO> avaliarDestino(
            @PathVariable Long id,
            @Valid @RequestBody AvaliacaoDTO dto) {

        return ResponseEntity.ok(destinoService.avaliarDestino(id, dto));
    }

    // ----------------------------------------------------------------
    // DELETE /api/destinos/{id} → Excluir destino
    // ----------------------------------------------------------------

    /**
     * Exclui permanentemente um destino de viagem.
     *
     * @param id identificador do destino a ser removido
     * @return 204 No Content em caso de sucesso, ou 404 se não encontrado
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirDestino(@PathVariable Long id) {
        destinoService.excluirDestino(id);
        return ResponseEntity.noContent().build();
    }
}
