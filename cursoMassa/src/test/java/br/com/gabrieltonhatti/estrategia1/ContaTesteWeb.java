package br.com.gabrieltonhatti.estrategia1;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContaTesteWeb {

    private ChromeDriver driver;

    @Before
    public void login() {
        System.setProperty("webdriver.chrome.driver", "/home/gabriel/develop/drivers/chromedriver");
        driver = new ChromeDriver();
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
    }

    @Test
    public void teste_1_inserir() {
        driver
                .findElement(By.linkText("Contas"))
                .click();
        driver
                .findElement(By.linkText("Adicionar"))
                .click();
        driver
                .findElement(By.id("nome"))
                .sendKeys("Conta estragégia #1");
        driver
                .findElement(By.tagName("button"))
                .click();

        String msg = driver
                .findElement(By.xpath("//div[@class='alert alert-success']"))
                .getText();

        Assert.assertEquals("Conta adicionada com sucesso!", msg);
    }

    @Test
    public void teste_2_consultar() {
        driver
                .findElement(By.linkText("Contas"))
                .click();
        driver
                .findElement(By.linkText("Listar"))
                .click();
        driver
                .findElement(By.xpath("//td[contains(text(), 'Conta estragégia #1')]/..//a"))
                .click();

        String nomeConta = driver
                .findElement(By.id("nome"))
                .getAttribute("value");

        Assert.assertEquals("Conta estragégia #1", nomeConta);
    }

    @Test
    public void teste_3_alterar() {
        driver
                .findElement(By.linkText("Contas"))
                .click();
        driver
                .findElement(By.linkText("Listar"))
                .click();
        driver
                .findElement(By.xpath("//td[contains(text(), 'Conta estragégia #1')]/..//a"))
                .click();
        driver
                .findElement(By.id("nome"))
                .clear();
        driver
                .findElement(By.id("nome"))
                .sendKeys("Conta estragégia #1 Alterada");
        driver
                .findElement(By.tagName("button"))
                .click();

        String msg = driver
                .findElement(By.xpath("//div[@class='alert alert-success']"))
                .getText();

        Assert.assertEquals("Conta alterada com sucesso!", msg);
    }

    @Test
    public void teste_4_excluir() {
        driver
                .findElement(By.linkText("Contas"))
                .click();
        driver
                .findElement(By.linkText("Listar"))
                .click();
        driver
                .findElement(By.xpath("//td[contains(text(), 'Conta estragégia #1')]/..//a[2]"))
                .click();

        String msg = driver
                .findElement(By.xpath("//div[@class='alert alert-success']"))
                .getText();

        Assert.assertEquals("Conta removida com sucesso!", msg);
    }

    @After
    public void fechar() {
        driver.quit();
    }

}
