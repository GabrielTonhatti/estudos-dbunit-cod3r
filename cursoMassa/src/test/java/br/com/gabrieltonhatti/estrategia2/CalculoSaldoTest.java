package br.com.gabrieltonhatti.estrategia2;

import br.com.gabrieltonhatti.dao.SaldoDAO;
import br.com.gabrieltonhatti.dao.impl.SaldoDAOImpl;
import br.com.gabrieltonhatti.entidades.Conta;
import br.com.gabrieltonhatti.entidades.TipoTransacao;
import br.com.gabrieltonhatti.entidades.Transacao;
import br.com.gabrieltonhatti.entidades.Usuario;
import br.com.gabrieltonhatti.service.ContaService;
import br.com.gabrieltonhatti.service.TransacaoService;
import br.com.gabrieltonhatti.service.UsuarioService;
import br.com.gabrieltonhatti.utils.DataUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class CalculoSaldoTest {

    // 1 Usuário
    // 1 conta
    // 1 transação

    // deve considerar transações do mesmo usuário
    // deve considerar transações da mesma conta
    // deve considerar transações pagas
    // deve considerar transações até a data corrente
    // deve somar receitas
    // deve subtrair despesas

    @Test
    public void deveCalcularSaldoCorreto() throws Exception {
        UsuarioService usuarioService = new UsuarioService();
        ContaService contaService = new ContaService();
        TransacaoService transacaoService = new TransacaoService();

        // Usuários
        Usuario usuario = usuarioService.salvar(new Usuario("Usuário", "email@email.com", "123"));
        Usuario usuarioAlternativo = usuarioService
                .salvar(new Usuario("Usuário Alternativo", "email2@qualquer.com", "123"));

        // Contas
        Conta conta = contaService.salvar(new Conta("Conta principal", usuario.getId()));
        Conta contaSecundaria = contaService.salvar(new Conta("Conta Secundária", usuario.getId()));
        Conta contaUsuarioAlternativo = contaService
                .salvar(new Conta("Conta Usuário Alternativo", usuarioAlternativo.getId()));

        // Transações
        // Transação Correta / Saldo = 2
        transacaoService.salvar(
                new Transacao(
                        "Transação Inicial",
                        "Envolvido",
                        TipoTransacao.RECEITA,
                        new Date(),
                        2.0,
                        true,
                        conta,
                        usuario
                )
        );

        // Transação usuário alternativo / Saldo = 2
        transacaoService.salvar(
                new Transacao(
                        "Transação outro Usuário",
                        "Envolvido",
                        TipoTransacao.RECEITA,
                        new Date(),
                        4.0,
                        true,
                        contaUsuarioAlternativo,
                        usuarioAlternativo
                )
        );

        // Transação de outra conta / Saldo = 2
        transacaoService.salvar(
                new Transacao(
                        "Transação outra conta",
                        "Envolvido",
                        TipoTransacao.RECEITA,
                        new Date(),
                        8.0,
                        true,
                        contaSecundaria,
                        usuario
                )
        );

        // Transação pendente / Saldo = 2
        transacaoService.salvar(
                new Transacao(
                        "Transação pendente",
                        "Envolvido",
                        TipoTransacao.RECEITA,
                        new Date(),
                        16.0,
                        false,
                        conta,
                        usuario
                )
        );

        // Transação passada / Saldo = 34
        transacaoService.salvar(
                new Transacao(
                        "Transação passada",
                        "Envolvido",
                        TipoTransacao.RECEITA,
                        DataUtils.obterDatacomDiferencaDias(-1),
                        32.0,
                        true,
                        conta,
                        usuario
                )
        );

        // Transação futura / Saldo = 34
        transacaoService.salvar(
                new Transacao(
                        "Transação futura",
                        "Envolvido",
                        TipoTransacao.RECEITA,
                        DataUtils.obterDatacomDiferencaDias(1),
                        64.0,
                        true,
                        conta,
                        usuario
                )
        );

        // Transação despesa / Saldo = -94
        transacaoService.salvar(
                new Transacao(
                        "Transação despesa",
                        "Envolvido",
                        TipoTransacao.DESPESA,
                        new Date(),
                        128.0,
                        true,
                        conta,
                        usuario
                )
        );

        // Testes de saldo com valor negativo dá azar / Saldo = 162
        transacaoService.salvar(
                new Transacao(
                        "Transação da sorte",
                        "Envolvido",
                        TipoTransacao.RECEITA,
                        new Date(),
                        256.0,
                        true,
                        conta,
                        usuario
                )
        );

        SaldoDAO saldoDAO = new SaldoDAOImpl();
        Assert.assertEquals(Double.valueOf(162.0), saldoDAO.getSaldoConta(conta.getId()));
        Assert.assertEquals(Double.valueOf(8.0), saldoDAO.getSaldoConta(contaSecundaria.getId()));
        Assert.assertEquals(Double.valueOf(4.0), saldoDAO.getSaldoConta(contaUsuarioAlternativo.getId()));

    }



}
