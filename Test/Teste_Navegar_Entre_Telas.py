from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
import time

# Configurar opções do Chrome
chrome_options = Options()
chrome_options.add_argument('--no-sandbox')
chrome_options.add_argument('--disable-dev-shm-usage')
chrome_options.add_argument('--disable-gpu')
# chrome_options.add_argument('--headless')  # Descomente se quiser rodar sem interface

# Inicializar o driver
driver = webdriver.Chrome(options=chrome_options)

try:
    # Acessar a URL
    url = "http://localhost:5173/" 
    driver.maximize_window() # Ajuste a porta conforme necessário
    driver.get(url)
    
    # Aguardar a página carregar
    wait = WebDriverWait(driver, 10)
    
    # Esperar o elemento 'usuario' estar presente
    usuario = wait.until(EC.presence_of_element_located((By.ID, 'usuario')))
    usuario.send_keys('teste@teste.com')
    time.sleep(3)
    
    # Esperar o elemento 'senha' estar presente
    senha = wait.until(EC.presence_of_element_located((By.ID, 'senha')))
    senha.send_keys('123')
    time.sleep(3)

    # Encontrar e clicar no botão
    botao = wait.until(EC.element_to_be_clickable((By.CLASS_NAME, 'login')))
    botao.click()
    time.sleep(3)

    # Aguardar um pouco para ver o resultado
    time.sleep(3)
    
    #Selecionar o Perfil do Usuario
    perfil = wait.until(EC.element_to_be_clickable((By.CLASS_NAME, 'perfil-opcao')))
    perfil.click()
    time.sleep(3)

    #Navegar para o Menu Hamburger
    MenuHamburger = wait.until(EC.element_to_be_clickable((By.CLASS_NAME, 'menu-hamburger')))
    MenuHamburger.click()
    time.sleep(3)

    selecionarMenu = wait.until(EC.element_to_be_clickable((By.ID, '2')))
    selecionarMenu.click()
    time.sleep(3)

    MenuHamburger.click()
    time.sleep(3)

    selecionarMenu = wait.until(EC.element_to_be_clickable((By.ID, '3')))
    selecionarMenu.click();
    time.sleep(3);

    MenuHamburger.click();
    time.sleep(3);

    selecionarMenu = wait.until(EC.element_to_be_clickable((By.ID, '1')))
    selecionarMenu.click();
    time.sleep(3);

    ImagemBanner = wait.until(EC.element_to_be_clickable((By.ID, 'Banner')))
    ImagemBanner.click();
    time.sleep(3);

    AddLista = wait.until(EC.element_to_be_clickable((By.CLASS_NAME, 'btn-minha-lista')))
    AddLista.click();
    time.sleep(3);
    
    MenuHamburger.click()
    time.sleep(3)

    selecionarMenu = wait.until(EC.element_to_be_clickable((By.ID, '4')))
    selecionarMenu.click();
    time.sleep(3);

    print("Teste executado com sucesso!")
    
except Exception as e:
    print(f"Erro durante o teste: {e}")
    
finally:
    # Fechar o navegador
    driver.quit()