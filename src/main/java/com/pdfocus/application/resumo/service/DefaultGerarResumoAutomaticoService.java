package com.pdfocus.application.resumo.service;

import com.pdfocus.application.disciplina.port.saida.DisciplinaRepository;
import com.pdfocus.application.resumo.dto.CriarResumoDeMaterialCommand;
import com.pdfocus.application.resumo.port.entrada.GerarResumoAutomaticoUseCase;
import com.pdfocus.application.material.port.saida.MaterialRepository;
import com.pdfocus.application.resumo.port.saida.ResumoRepository;
import com.pdfocus.application.resumo.port.saida.TextExtractorPort;
import com.pdfocus.application.resumo.port.saida.ResumidorIAPort;
import com.pdfocus.application.usuario.port.saida.UsuarioRepository;
import com.pdfocus.core.exceptions.LimiteResumoExcedidoException;
import com.pdfocus.core.exceptions.disciplina.DisciplinaNaoEncontradaException;
import com.pdfocus.core.exceptions.material.MaterialNaoEncontradoException;
import com.pdfocus.core.exceptions.resumo.TextoNaoPodeSerExtraidoException;
import com.pdfocus.core.models.Resumo;
import com.pdfocus.core.models.Disciplina;
import com.pdfocus.core.models.Usuario;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Implementação padrão do caso de uso {@link GerarResumoAutomaticoUseCase}.
 * <p>
 * Este serviço é responsável por orquestrar o processo de geração automática de resumos
 * com base em materiais PDF existentes. Ele integra diferentes portas de saída —
 * repositórios, extratores de texto e mecanismos de IA — para construir um fluxo completo:
 * </p>
 * <ol>
 *   <li>Validação da existência do material e da disciplina;</li>
 *   <li>Extração do texto do PDF via {@link TextExtractorPort};</li>
 *   <li>Geração de resumo automático usando {@link ResumidorIAPort};</li>
 *   <li>Criação e persistência do objeto de domínio {@link Resumo}.</li>
 * </ol>
 *
 * <p>O processo é totalmente transacional e resiliente a falhas.</p>
 */
@Service
@Transactional
public class DefaultGerarResumoAutomaticoService implements GerarResumoAutomaticoUseCase {

    private final MaterialRepository materialRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final TextExtractorPort textExtractorPort;
    private final ResumoRepository resumoRepository;
    private final ResumidorIAPort resumidorIAPort;
    private final UsuarioRepository usuarioRepository;

    // Injeta o valor do application.properties. Se não existir, o padrão é 3.
    @Value("${app.limites.resumos-diarios:3}")
    private int limiteDiario;


    /**
     * Constrói o serviço com todas as dependências necessárias para o fluxo de geração de resumos.
     *
     * @param materialRepository   Porta de saída para acesso a materiais.
     * @param disciplinaRepository Porta de saída para acesso a disciplinas.
     * @param textExtractorPort    Porta para extração de texto de PDFs.
     * @param resumoRepository     Porta de saída para persistência de resumos.
     * @param resumidorIAPort      Porta de integração com o mecanismo de resumo por IA.
     */
    public DefaultGerarResumoAutomaticoService(
            MaterialRepository materialRepository,
            DisciplinaRepository disciplinaRepository,
            TextExtractorPort textExtractorPort,
            ResumoRepository resumoRepository,
            ResumidorIAPort resumidorIAPort,
            UsuarioRepository usuarioRepository) {

        this.materialRepository = materialRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.textExtractorPort = textExtractorPort;
        this.resumoRepository = resumoRepository;
        this.resumidorIAPort = resumidorIAPort;
        this.usuarioRepository = usuarioRepository;
    }




    /**
     * {@inheritDoc}
     * <p>
     * Este método tenta gerar um resumo automaticamente a partir de um material PDF.
     * Caso o comando já traga um conteúdo pronto, ele é usado diretamente; caso contrário,
     * o serviço realiza a extração do texto e gera o resumo via IA.
     * </p>
     *
     * @param comando   O comando com os dados do material e da disciplina.
     * @param usuarioId O ID do usuário autenticado que solicita o resumo.
     * @return O {@link Resumo} gerado e salvo no sistema.
     * @throws MaterialNaoEncontradoException se o material informado não for encontrado.
     * @throws DisciplinaNaoEncontradaException se a disciplina associada não existir.
     * @throws TextoNaoPodeSerExtraidoException se ocorrer erro na extração de texto do PDF.
     */
    @Override
    public Resumo executar(CriarResumoDeMaterialCommand comando, UUID usuarioId) {
        System.out.println("🚀 Iniciando geração automática de resumo...");

        // 1. BUSCAR O USUÁRIO E VALIDAR A COTA ANTES DE TUDO
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + usuarioId));

        validarCotaDiaria(usuario);

        var material = materialRepository.buscarPorIdEUsuario(comando.materialId(), usuarioId)
                .orElseThrow(() -> new MaterialNaoEncontradoException(comando.materialId()));

        Disciplina disciplina = disciplinaRepository.findById(comando.disciplinaId())
                .orElseThrow(() -> new DisciplinaNaoEncontradaException(comando.disciplinaId()));

        String conteudo;

        if (comando.conteudo() != null && !comando.conteudo().isBlank()) {
            conteudo = comando.conteudo();
            System.out.println("📝 Usando conteúdo manual fornecido pelo usuário.");
        } else {
            try {
                System.out.println("🔤 Extraindo texto do PDF...");
                String textoCompleto = textExtractorPort.extrairTexto(material.getNomeStorage());
                System.out.println("✅ Texto extraído com sucesso (" + textoCompleto.length() + " caracteres).");

                System.out.println("🤖 Gerando resumo via IA...");
                conteudo = resumidorIAPort.resumir(textoCompleto, 300);
                System.out.println("✅ Resumo gerado com " + conteudo.length() + " caracteres.");

            } catch (Exception e) {
                throw new TextoNaoPodeSerExtraidoException(material.getId().toString(), e);
            }
        }

        String titulo = (comando.titulo() != null && !comando.titulo().isBlank())
                ? comando.titulo()
                : "Resumo - " + material.getNomeOriginal();

        Resumo resumo = Resumo.criarDeMaterial(
                UUID.randomUUID(),
                usuarioId,
                titulo,
                conteudo,
                disciplina,
                comando.materialId()
        );

        Resumo resumoSalvo = resumoRepository.salvar(resumo);
        System.out.println("💾 Resumo salvo com ID: " + resumoSalvo.getId());

        // 2. INCREMENTA A COTA E SALVA O USUÁRIO APÓS O SUCESSO
        incrementarCota(usuario);

        return resumoSalvo;
    }

    /**
     * Lógica de Lazy Reset:
     * Verifica se o dia mudou para resetar o contador.
     * Se for o mesmo dia, verifica se atingiu o limite.
     */
    private void validarCotaDiaria(Usuario usuario) {
        LocalDate hoje = LocalDate.now();

        // Se a data do último uso não for hoje, reseta o contador (Lazy Reset)
        if (!hoje.equals(usuario.getDataUltimoUso())) {
            System.out.println("🔄 Novo dia detectado. Resetando cota do usuário.");
            usuario.setResumosHoje(0);
            usuario.setDataUltimoUso(hoje);
            // ✅ CORREÇÃO: Salvar o reset IMEDIATAMENTE no banco
            usuarioRepository.salvar(usuario);
        }

        // Verifica se atingiu o limite
        if (usuario.getResumosHoje() >= limiteDiario) {
            System.out.println("🚫 Limite diário atingido para usuário: " + usuario.getId());
            throw new LimiteResumoExcedidoException("Você atingiu seu limite de " + limiteDiario + " resumos diários. Volte amanhã!");
        }
    }

    /**
     * Incrementa o contador de uso e persiste a alteração no banco.
     */
    private void incrementarCota(Usuario usuario) {
        usuario.setResumosHoje(usuario.getResumosHoje() + 1);
        usuario.setDataUltimoUso(LocalDate.now()); // Garante que a data está atualizada
        usuarioRepository.salvar(usuario);
        System.out.println("📈 Cota atualizada: " + usuario.getResumosHoje() + "/" + limiteDiario);
    }

}
