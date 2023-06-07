import time

import numpy as np
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
ser_obj= Service('chromedriver.exe')
driver= webdriver.Chrome(service=ser_obj)

driver.implicitly_wait(5)  #if web element is not located on the web page within time frame, it will throw an exception.

driver.get('https://nfsa.up.gov.in/Food/citizen/Default.aspx')

driver.maximize_window()

village_list = []
list_count = 1

janpadCount=len(driver.find_elements(By.XPATH, "//table/tbody/tr/td/a"))
print(janpadCount)
for i in range(1, janpadCount+1):
    listOfJanpad =  driver.find_element(By.XPATH, "(//table/tbody/tr/td/a)["+str(janpadCount)+"]").text
    print(listOfJanpad)
    driver.find_element(By.XPATH, "(//table/tbody/tr/td/a)["+str(janpadCount)+"]").click()
    blockCount = len(driver.find_elements(By.XPATH, "//td[contains(text(),'ग्रामीण क्षेत्र')]/following::tr/td/a"))
    print(blockCount)
    for i in range(1, blockCount+1):

        listOfBlock=driver.find_element(By.XPATH,"(//td[contains(text(),'ग्रामीण क्षेत्र')]/following::tr/td/a)["+str(blockCount)+"]").text
        driver.find_element(By.XPATH,"(//td[contains(text(),'ग्रामीण क्षेत्र')]/following::tr/td/a)["+str(blockCount)+"]").click()
        time.sleep(1)
        gramPanchayatCount = len(driver.find_elements(By.XPATH,"//tr/td/a"))
        print(gramPanchayatCount)
        for i in range(1, gramPanchayatCount+1):
            listOfPanchayat = driver.find_element(By.XPATH,"(//tr/td/a)["+str(gramPanchayatCount)+"]").text

            # time.sleep(1)

            numOfRation= driver.find_element(By.XPATH,"(//tr/td/a)["+str(gramPanchayatCount)+"]/following::td[5]").text
            beneficiars = driver.find_element(By.XPATH,"(//tr/td/a)["+str(gramPanchayatCount)+"]/following::td[6]").text

            print("num of Ration Card: ",numOfRation)
            village_list.insert(list_count,

                                {"District": listOfJanpad, "Block": listOfBlock, "Panchayat": listOfPanchayat,"Number Of Ration": numOfRation,"Beneficiars":beneficiars})
            list_count = list_count + 1

            gramPanchayatCount=gramPanchayatCount-1
            # time.sleep(1) #for back to the previous page
        driver.back()
        blockCount=blockCount-1
    driver.back()
    janpadCount=janpadCount-1

np.savetxt("BPL.csv", village_list, delimiter=", ",
           fmt='% s', encoding='utf-8')

