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
    url = "http://localhost:5173/"  # Ajuste a porta conforme necessário
    driver.get(url)
    
    # Aguardar a página carregar
    wait = WebDriverWait(driver, 10)
    
    # Esperar o elemento 'usuario' estar presente
    usuario = wait.until(EC.presence_of_element_located((By.ID, 'usuario')))
    usuario.send_keys('teste@teste.com')
    
    # Esperar o elemento 'senha' estar presente
    senha = wait.until(EC.presence_of_element_located((By.ID, 'senha')))
    senha.send_keys('123')
    
    # Encontrar e clicar no botão
    botao = wait.until(EC.element_to_be_clickable((By.ID, 'loading')))
    botao.click()
    
    # Aguardar um pouco para ver o resultado
    time.sleep(3)
    
    print("Teste executado com sucesso!")
    
except Exception as e:
    print(f"Erro durante o teste: {e}")
    
finally:
    # Fechar o navegador
    driver.quit()