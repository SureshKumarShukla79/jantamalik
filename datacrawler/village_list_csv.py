import numpy as np
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support.select import Select
import os
import pandas as pd

options = webdriver.ChromeOptions()

ser_obj = Service('chromedriver.exe')
driver = webdriver.Chrome(service=ser_obj, options=options)

driver.get('https://sec.up.nic.in/site/DownloadCandidateFaDebt.aspx')
driver.maximize_window()

village_list = []
list_count = 1

block_count = 0
panchayat_count = 0

pradhan = driver.find_element(By.XPATH, '//*[@id="ctl00_ContentPlaceHolder1_ddlPostTypes"]')
drp = Select(pradhan)
drp.select_by_index(5)
janpad = driver.find_element(By.XPATH, '//*[@id="ctl00_ContentPlaceHolder1_ddlDistrictName"]')
drp1 = Select(janpad)
janpad_count = len(drp1.options)-1

for i in range(1, janpad_count+1):
    print("reached ", i)
    pradhan = driver.find_element(By.XPATH, '//*[@id="ctl00_ContentPlaceHolder1_ddlPostTypes"]')
    drp = Select(pradhan)
    drp.select_by_index(5)

    janpad = driver.find_element(By.XPATH, '//*[@id="ctl00_ContentPlaceHolder1_ddlDistrictName"]')
    drp1 = Select(janpad)
    listofjanpad = drp1.options[janpad_count].text
    drp1.select_by_index(janpad_count)

    block = driver.find_element(By.XPATH, '//*[@id="ctl00_ContentPlaceHolder1_ddlBlockName"]')
    drp2 = Select(block)
    block_count = len(drp2.options) - 1
    print("Number of Block- ", block_count)
    while block_count > 0:
        block = driver.find_element(By.XPATH, '//*[@id="ctl00_ContentPlaceHolder1_ddlBlockName"]')
        drp2 = Select(block)
        listofblock = drp2.options[block_count].text
        drp2.select_by_index(block_count)

        panchayat = driver.find_element(By.XPATH, '//*[@id="ctl00_ContentPlaceHolder1_ddlGpName"]')
        drp3 = Select(panchayat)
        panchayat_count = len(drp3.options) - 1
        for i in drp3.options:
            listofpanchayat = i.text
            village_list.insert(list_count,
                                {"District": listofjanpad, "Block": listofblock, "Panchayat": listofpanchayat})
            list_count = list_count + 1

        block_count = block_count - 1
        panchayat_count += panchayat_count
        print("-----Number of Panchayat-----> ", panchayat_count)

    janpad_count = janpad_count - 1

#print("length of village",len(village_list))
#print(village_list)

np.savetxt("VillageList.csv", village_list, delimiter=", ",  # Set the delimiter as a comma followed by a space
           fmt='% s', encoding='utf-8')  # Set the format of the data as string
