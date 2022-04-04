package br.com.gabrieltonhatti.estrategia5;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class ContaTesteWeb {

    private static ChromeDriver driver;

    @BeforeClass
    public static void reset() {
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

        driver
                .findElement(By.linkText("reset"))
                .click();
    }

    @Test
    public void inserir() {
        driver
                .findElement(By.linkText("Contas"))
                .click();
        driver
                .findElement(By.linkText("Adicionar"))
                .click();
        driver
                .findElement(By.id("nome"))
                .sendKeys("Conta estratégia #5");
        driver
                .findElement(By.tagName("button"))
                .click();

        String msg = driver
                .findElement(By.xpath("//div[@class='alert alert-success']"))
                .getText();

        Assert.assertEquals("Conta adicionada com sucesso!", msg);
    }

    @Test
    public void consultar() {
        driver
                .findElement(By.linkText("Contas"))
                .click();
        driver
                .findElement(By.linkText("Listar"))
                .click();
        driver
                .findElement(By.xpath("//td[contains(text(), 'Conta para saldo')]/..//a"))
                .click();

        String nomeConta = driver
                .findElement(By.id("nome"))
                .getAttribute("value");

        Assert.assertEquals("Conta para saldo", nomeConta);
    }

    @Test
    public void alterar() {
        driver
                .findElement(By.linkText("Contas"))
                .click();
        driver
                .findElement(By.linkText("Listar"))
                .click();
        driver
                .findElement(By.xpath("//td[contains(text(), 'Conta para alterar')]/..//a"))
                .click();
        driver
                .findElement(By.id("nome"))
                .sendKeys(" Alterada");
        driver
                .findElement(By.tagName("button"))
                .click();

        String msg = driver
                .findElement(By.xpath("//div[@class='alert alert-success']"))
                .getText();

        Assert.assertEquals("Conta alterada com sucesso!", msg);
    }

    @Test
    public void excluir() {
        driver
                .findElement(By.linkText("Contas"))
                .click();
        driver
                .findElement(By.linkText("Listar"))
                .click();
        driver
                .findElement(By.xpath("//td[contains(text(), 'Conta mesmo nome')]/..//a[2]"))
                .click();

        String msg = driver
                .findElement(By.xpath("//div[@class='alert alert-success']"))
                .getText();

        Assert.assertEquals("Conta removida com sucesso!", msg);
    }

    @AfterClass
    public static void fechar() {
        driver.quit();
    }

}
