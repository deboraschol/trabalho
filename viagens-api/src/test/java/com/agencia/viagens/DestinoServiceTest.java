package com.agencia.viagens;

import com.agencia.viagens.dto.AvaliacaoDTO;
import com.agencia.viagens.dto.DestinoRequestDTO;
import com.agencia.viagens.dto.DestinoResponseDTO;
import com.agencia.viagens.exception.DestinoNotFoundException;
import com.agencia.viagens.model.Destino;
import com.agencia.viagens.repository.DestinoRepository;
import com.agencia.viagens.service.DestinoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DestinoServiceTest {

    @Mock
    private DestinoRepository destinoRepository;

    @InjectMocks
    private DestinoService destinoService;

    private Destino destino;

    @BeforeEach
    void setUp() {
        destino = Destino.builder()
                .id(1L)
                .nome("Paris")
                .localizacao("França")
                .descricao("Cidade Luz")
                .idioma("Francês")
                .moeda("Euro")
                .notaMedia(0.0)
                .totalAvaliacoes(0)
                .build();
    }

    // ----------------------------------------------------------------
    // Cadastrar
    // ----------------------------------------------------------------

    @Test
    @DisplayName("Deve cadastrar destino com sucesso")
    void deveCadastrarDestino() {
        DestinoRequestDTO dto = new DestinoRequestDTO();
        dto.setNome("Paris");
        dto.setLocalizacao("França");

        when(destinoRepository.save(any(Destino.class))).thenReturn(destino);

        DestinoResponseDTO response = destinoService.cadastrarDestino(dto);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNome()).isEqualTo("Paris");
        assertThat(response.getNotaMedia()).isEqualTo(0.0);
        verify(destinoRepository, times(1)).save(any(Destino.class));
    }

    // ----------------------------------------------------------------
    // Listar
    // ----------------------------------------------------------------

    @Test
    @DisplayName("Deve listar todos os destinos")
    void deveListarDestinos() {
        when(destinoRepository.findAll()).thenReturn(List.of(destino));

        List<DestinoResponseDTO> lista = destinoService.listarDestinos();

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getNome()).isEqualTo("Paris");
    }

    // ----------------------------------------------------------------
    // Visualizar
    // ----------------------------------------------------------------

    @Test
    @DisplayName("Deve retornar destino pelo id")
    void deveVisualizarDestino() {
        when(destinoRepository.findById(1L)).thenReturn(Optional.of(destino));

        DestinoResponseDTO response = destinoService.visualizarDestino(1L);

        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao visualizar id inexistente")
    void deveLancarExcecaoAoVisualizarIdInexistente() {
        when(destinoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> destinoService.visualizarDestino(99L))
                .isInstanceOf(DestinoNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ----------------------------------------------------------------
    // Avaliar — cálculo da média
    // ----------------------------------------------------------------

    @Test
    @DisplayName("Deve calcular nova média ao avaliar pela primeira vez")
    void deveCalcularMediaNaPrimeiraAvaliacao() {
        when(destinoRepository.findById(1L)).thenReturn(Optional.of(destino));
        when(destinoRepository.save(any(Destino.class))).thenAnswer(inv -> inv.getArgument(0));

        AvaliacaoDTO dto = new AvaliacaoDTO();
        dto.setNota(8);

        DestinoResponseDTO response = destinoService.avaliarDestino(1L, dto);

        // (0.0 * 0 + 8) / 1 = 8.0
        assertThat(response.getNotaMedia()).isEqualTo(8.0);
        assertThat(response.getTotalAvaliacoes()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve recalcular média ao avaliar mais de uma vez")
    void deveRecalcularMediaComMultiplasAvaliacoes() {
        destino.setNotaMedia(8.0);
        destino.setTotalAvaliacoes(1);

        when(destinoRepository.findById(1L)).thenReturn(Optional.of(destino));
        when(destinoRepository.save(any(Destino.class))).thenAnswer(inv -> inv.getArgument(0));

        AvaliacaoDTO dto = new AvaliacaoDTO();
        dto.setNota(6);

        DestinoResponseDTO response = destinoService.avaliarDestino(1L, dto);

        // (8.0 * 1 + 6) / 2 = 7.0
        assertThat(response.getNotaMedia()).isEqualTo(7.0);
        assertThat(response.getTotalAvaliacoes()).isEqualTo(2);
    }

    // ----------------------------------------------------------------
    // Excluir
    // ----------------------------------------------------------------

    @Test
    @DisplayName("Deve excluir destino com sucesso")
    void deveExcluirDestino() {
        when(destinoRepository.findById(1L)).thenReturn(Optional.of(destino));

        destinoService.excluirDestino(1L);

        verify(destinoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir id inexistente")
    void deveLancarExcecaoAoExcluirIdInexistente() {
        when(destinoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> destinoService.excluirDestino(99L))
                .isInstanceOf(DestinoNotFoundException.class);
    }
}
