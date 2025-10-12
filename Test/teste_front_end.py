from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.common.by import By
import time
import os

# Caminho absoluto para o arquivo HTML
caminho_Front = 'http://localhost:5173/'

# Iniciar o Chrome
driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()))

# Abrir o HTML local
driver.get(caminho_Front)

# Espera a página carregar
time.sleep(20)

# Fechar o navegador
driver.quit()