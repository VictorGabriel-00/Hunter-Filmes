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
    driver.maximize_window()
    driver.get(url)
    
    # Aguardar a página carregar
    wait = WebDriverWait(driver, 10)
    
    botaoCadastro = wait.until(EC.element_to_be_clickable((By.CLASS_NAME, 'cadastro')))
    botaoCadastro.click()

    usuario = wait.until(EC.presence_of_element_located((By.ID, 'usuario')))
    usuario.send_keys('teste')
    
    email = wait.until(EC.presence_of_element_located((By.ID,'email')))
    email.send_keys('Victor@teste.com')


    senha = wait.until(EC.presence_of_element_located((By.ID, 'senha')))
    senha.send_keys('123')

    confirmaSenha = wait.until(EC.presence_of_element_located((By.ID, 'confirmarSenha')))
    confirmaSenha.send_keys('123')

    dataNascimento = wait.until(EC.presence_of_element_located((By.ID, 'nascimento')))
    dataNascimento.send_keys('12/05/2005')

    # Encontrar e clicar no botão
    botao = wait.until(EC.element_to_be_clickable((By.CLASS_NAME, 'cadastrar')))
    botao.click()

    botaoPerfil = wait.until(EC.element_to_be_clickable((By.CLASS_NAME, 'criar-perfil-button')))
    botaoPerfil.click()

    nome = wait.until(EC.presence_of_element_located((By.ID, 'nomePerfil')))
    nome.send_keys('Victor')

    botaoUsuario = wait.until(EC.element_to_be_clickable((By.CLASS_NAME, 'criar')))
    botaoUsuario.click()
    
    
    # Aguardar um pouco para ver o resultado
    time.sleep(10)
    
    print("Teste executado com sucesso!")
    
except Exception as e:
    print(f"Erro durante o teste: {e}")
    
finally:
    # Fechar o navegador
    driver.quit()