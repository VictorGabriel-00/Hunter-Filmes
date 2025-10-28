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
    # Maximizar a janela do navegador
    driver.maximize_window()
    # Navegar até a página
    driver.get(url)
    
    # Aguardar a página carregar
    wait = WebDriverWait(driver, 10)
    
    # Encontrar e clicar no botão de cadastro
    botaoCadastro = wait.until(EC.element_to_be_clickable((By.CLASS_NAME, 'cadastro')))
    botaoCadastro.click()
    time.sleep(2)
    # Preencher o formulário de cadastro
    usuario = wait.until(EC.presence_of_element_located((By.ID, 'usuario')))
    usuario.send_keys('teste')
    time.sleep(2)
    
    # Preencher o campo de email
    email = wait.until(EC.presence_of_element_located((By.ID,'email')))
    email.send_keys('Victor@teste.com')
    time.sleep(2)

    # Preencher o campo de senha
    senha = wait.until(EC.presence_of_element_located((By.ID, 'senha')))
    senha.send_keys('123')
    time.sleep(2)

    # Preencher o campo de confirmação de senha
    confirmaSenha = wait.until(EC.presence_of_element_located((By.ID, 'confirmarSenha')))
    confirmaSenha.send_keys('123')
    time.sleep(2)

    # Preencher o campo de data de nascimento
    dataNascimento = wait.until(EC.presence_of_element_located((By.ID, 'nascimento')))
    dataNascimento.send_keys('12/05/2005')
    time.sleep(2)

    # Encontrar e clicar no botão
    botao = wait.until(EC.element_to_be_clickable((By.CLASS_NAME, 'cadastrar')))
    botao.click()

    # Adicionar perfil após cadastro
    botaoPerfil = wait.until(EC.element_to_be_clickable((By.CLASS_NAME, 'criar-perfil-button')))
    botaoPerfil.click()

    # Preencher o campo de nome
    nome = wait.until(EC.presence_of_element_located((By.ID, 'nomePerfil')))
    nome.send_keys('Victor')
    time.sleep(2)

    # Preencher o campo de idade
    botaoUsuario = wait.until(EC.element_to_be_clickable((By.CLASS_NAME, 'criar')))
    botaoUsuario.click()
    
    
    # Aguardar um pouco para ver o resultado
    time.sleep(5)

     # Verificar se o botão de cadastro está presente
    if(botaoCadastro and usuario and email and senha and confirmaSenha and dataNascimento): {
        print("Todos os campos de cadastro estão presentes.")
    }

    print("Teste executado com sucesso!")
    
except Exception as e:
    print(f"Erro durante o teste: {e}")
    
finally:
    # Fechar o navegador
    driver.quit()