package br.com.gabrieltonhatti.estrategia3;

import br.com.gabrieltonhatti.entidades.Conta;
import br.com.gabrieltonhatti.entidades.Usuario;
import br.com.gabrieltonhatti.service.ContaService;
import br.com.gabrieltonhatti.service.UsuarioService;
import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class GeradorMassas {

    public static final String CHAVE_CONTA_SB = "CONTA_SB";
    public static final String CHAVE_CONTA = "CONTA";

    public void gerarContaSeuBarriga() throws SQLException, ClassNotFoundException {
        System.setProperty("webdriver.chrome.driver", "/home/gabriel/develop/drivers/chromedriver");
        ChromeDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://seubarriga.wcaquino.me/");

        driver
                .findElement(By.id("email"))
                .sendKeys("gabriel.teste@gmail.com");
        driver
                .findElement(By.id("senha"))
                .sendKeys("123456");
        driver
                .findElement(By.tagName("button"))
                .click();

        Faker faker = new Faker();
        String registro = faker.gameOfThrones().character() + " " + faker.gameOfThrones().dragon();
        driver
                .findElement(By.linkText("Contas"))
                .click();
        driver
                .findElement(By.linkText("Adicionar"))
                .click();
        driver
                .findElement(By.id("nome"))
                .sendKeys(registro);
        driver
                .findElement(By.tagName("button"))
                .click();
        driver.quit();

        new MassaDAOImpl()
                .inserirMassa(CHAVE_CONTA_SB, registro);
    }

    public void gerarConta() throws Exception {
        ContaService service = new ContaService();
        UsuarioService userService = new UsuarioService();
        Faker faker = new Faker();
        Usuario usuarioGlobal;

        usuarioGlobal = new Usuario(
                faker.name().fullName(),
                faker.internet().emailAddress(),
                faker.internet().password()
        );

        Usuario usuarioSalvo = userService.salvar(usuarioGlobal);
        Conta conta = new Conta(faker.superhero().name(), usuarioGlobal);
         service.salvar(conta);
         new MassaDAOImpl()
                 .inserirMassa(CHAVE_CONTA, conta.getNome());
    }

    public static void main(String[] args) throws Exception {
//        GeradorMassas gerador = new GeradorMassas();
//
//        for (int i = 0; i < 10; i++) {
//            gerador.gerarConta();
//        }

        String massa = new MassaDAOImpl()
                .obterMassa(CHAVE_CONTA_SB);

        System.out.println(massa);
    }

}
