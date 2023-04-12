import time
import os

import pyperclip
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support.select import Select

#for opening browser

ser_obj= Service('chromedriver.exe')
chrome_options = Options()

#creating directory

dir_path = os.path.join(os.getcwd(), 'ग्राम पंचायत प्रधान\अमेठी')
if not os.path.exists(dir_path):
    os.mkdir(dir_path)
print(dir_path)

chrome_options.add_experimental_option('prefs', {
    'download.default_directory': dir_path,
    'download.prompt_for_download': False,
    'download.directory_upgrade': True,
    'safebrowsing.enabled': True
})

driver= webdriver.Chrome(service=ser_obj,options=chrome_options)

driver.implicitly_wait(10)  #if web element is not located on the web page within time frame, it will throw an exception.

driver.get('https://sec.up.nic.in/site/DownloadCandidateFaDebt.aspx')

driver.maximize_window()  # For Maximize the Window

#choose pradhan from drop down

pradhan=driver.find_element(By.XPATH,'//*[@id="ctl00_ContentPlaceHolder1_ddlPostTypes"]')
drp=Select(pradhan)
drp.select_by_index(5)

#choose janpad from drop down

janpad= driver.find_element(By.XPATH,'//*[@id="ctl00_ContentPlaceHolder1_ddlDistrictName"]')
drp=Select(janpad)
drp.select_by_index(1)

#choose block from drop down

block =driver.find_element(By.XPATH,'//*[@id="ctl00_ContentPlaceHolder1_ddlBlockName"]')
drp=Select(block)
drp.select_by_index(1)

#choose panchayat from drop down

panchayat =driver.find_element(By.XPATH,'//*[@id="ctl00_ContentPlaceHolder1_ddlGpName"]')
drp=Select(panchayat)
drp.select_by_index(2)

driver.find_element(By.XPATH,'//*[@id="ctl00_ContentPlaceHolder1_btnSubmit"]').click() #click on the View button

#download the data

driver.find_element(By.XPATH,'//*[@id="ctl00_ContentPlaceHolder1_GridView2_ctl02_Button3"]').click() #Click on the Download button

#Enter the phone number
driver.find_element(By.XPATH,'//*[@id="ctl00_ContentPlaceHolder1_txtRegMobileNo"]').send_keys(8299877420) #Enter the phone number into the inbox

time.sleep(8) # time sleep for entering CAPTCHA
#Enter the CAPTCHA
driver.find_element(By.XPATH,'//*[@id="ctl00_ContentPlaceHolder1_btnGenerate"]').click() #Click on the Generate Button after entering the captcha

#for popup  alert window

alertwin=driver.switch_to.alert
alertwin.accept()

time.sleep(20) #time sleep for OTP, and open the new tab for message
#for OTP
driver.switch_to.new_window('tab')
driver.get("https://messages.google.com/web/authentication")
time.sleep(10)     # time sleep for opening the messages
driver.find_element(By.XPATH,
                    '/html/body/mw-app/mw-bootstrap/div/main/mw-main-container/div/mw-main-nav/mws-conversations-list/nav/div[1]/mws-conversation-list-item[1]/a/div[2]/h3').click()
time.sleep(5) # Click on the option menu
driver.find_element(By.XPATH,"/html/body/mw-app/mw-bootstrap/div/main/mw-main-container/div/mw-main-nav/mws-conversations-list/nav/div[1]/mws-conversation-list-item[1]/a").click()
driver.find_element(By.XPATH, "//div[@class='text-msg ng-star-inserted']").click()
driver.find_element(By.XPATH,"//button[@aria-label='Message actions menu']//span[@class='mat-mdc-button-touch-target']").click()
driver.find_element(By.XPATH,"//span[normalize-space()='Copy text']").click()
time.sleep(2)  # copy the message
driver.find_element(By.XPATH,"//button[@aria-label='Message actions menu']//span[@class='mat-mdc-button-touch-target']").click()
time.sleep(1) #click on the option menu
driver.find_element(By.XPATH,"//span[normalize-space()='Delete']").click()
time.sleep(1) # delete the message
driver.find_element(By.XPATH,"//span[normalize-space()='Delete']").click()

otp = pyperclip.paste()
o=''
for c in otp:
    if c.isdigit():
        o = o + c
# print("OTP= : " + o)
#handling windows
allwin=driver.window_handles
driver.switch_to.window(allwin[0])

driver.find_element(By.XPATH,"//input[@id='ctl00_ContentPlaceHolder1_txtOTP']").send_keys(o)# sending the OTP
driver.find_element(By.XPATH,'//*[@id="ctl00_ContentPlaceHolder1_btnVerifyOTP"]').click() #Click on the button

#switching to alert window
alertwin=driver.switch_to.alert
alertwin.accept()

driver.find_element(By.XPATH,'//*[@id="ctl00_ContentPlaceHolder1_DnwldFile1"]').click() # Click to download PDF

driver.find_element(By.XPATH,'//*[@id="ctl00_ContentPlaceHolder1_candidateSearch"]').click() # Switch to Candidate Search Page
