
************** RegressionTestHarness Version 1.0 12/03/2013 by Mohammed Imran ******************

SYSTEM REQUIREMENTS
-------------------
Windows/Linux
* Ant
* Java

SOFTWARE LIBRARIES/VERSION USED/WHERE TO DOWNLOAD/UPGRADING TO LATEST VERSIONS
------------------------------------------------------------------------------

FREEMAKER - 2.3.19
------------------
DOWNLOAD LATEST VERSION FROM: http://mvnrepository.com/artifact/org.freemarker/freemarker

UPGRADING
---------
if you decide to upgrade the above freemarker version you will need to remove the "freemarker-2.3.19.jar"
from the lib directory and place the new jar file in the lib directory. You will also need to update the reference contained
in the ".classpath" file.

LOG4J - 1.2.17
--------------
DOWNLOAD LATEST VERSION FROM: http://logging.apache.org/log4j/1.2/download.html

UPGRADING
---------
if you decide to upgrade the above log4j version you will need to remove the "log4j-1.2.17.jar"
from the lib directory and place the new jar file in the lib directory. You will also need to update the reference contained
in the ".classpath" file.

TESTNG - 6.8
------------
DOWNLOAD LATEST VERSION FROM: http://search.maven.org/#search|ga|1|a%3A%22testng%22

UPGRADING
---------
if you decide to upgrade the above testNG version you will need to remove the "testng-6.8.jar" from the lib directory 
and place the new jar file in the lib directory. You will also need to update the reference contained
in the ".classpath" and "build.xml" files.

SELENIUM - 2.31.0
-----------------
DOWNLOAD LATEST VERSION FROM: http://code.google.com/p/selenium/downloads/list

UPGRADING
---------
if you decide to upgrade the above selenium server version you will need to remove the "selenium-server-standalone-2.31.0.jar"
from the lib directory and place the new jar file in the lib directory. You will also need to update the reference contained
in the ".classpath" file.

IEDriverServer - 2.31.0
-----------------------
DOWNLOAD LATEST VERSION FROM: http://code.google.com/p/selenium/downloads/list

UPGRADING
---------
if you decide to upgrade the above IEDriverServer version you will need to replace the binary executables at
* lib/IE_32_DriverServer
* lib/IE_64_DriverServer

ChromeDriver - 26.0.1383.0
-------------------------------
DOWNLOAD LATEST VERSION FROM: http://code.google.com/p/chromedriver/downloads/list

if you decide to upgrade the above ChromeDriver version you will need to to replace the binary executables at

* lib/CHROME_Win_DriverServer
* lib/CHROME_Linux_32_DriverServer
* lib/CHROME_Linux_64_DriverServer

ANT
---
if you upgrade your ANT version then ensure you place a copy of ${env.ANT_HOME}/lib/ant-junit.jar into the lib folder.
The lib/ant-junit.jar at present is 1.8.2

TECHNICAL SUPPORT
-----------------
If you need technical assistance, you may contact me in the following ways:
1. Email mohammed.imran010@gmail.com available 24/7
2. Blog  http://seleniumregressionproject.blogspot.co.uk

LICENSING
---------
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.