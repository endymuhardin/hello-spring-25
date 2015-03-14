Small web application with Spring 2.5, using its newest annotation and component scanning capability.

How to build from scratch
  1. [`Download \*.tar.bz2 file`](http://hello-spring-25.googlecode.com/files/tutorial-spring25.tar.bz2)
  1. Extract
  1. cd into extracted folder
  1. Execute ant build-winstone


How to deploy:
  1. [Download `\*.war` file](http://hello-spring-25.googlecode.com/files/tutorial-spring25.war)
  1. Deploy in your favorite application server


How to run without application server
  1. [Download `\*.jar` file](http://hello-spring-25.googlecode.com/files/tutorial-spring25-winstone.jar)
  1. Execute java -jar 

&lt;filename&gt;

.jar in command line


This application needs MySQL database to run properly, with the following configuration:
  * database host : localhost
  * database name : belajar
  * database user : belajar
  * database pass : java

And table T\_PERSON with the following field:

  * id INT PRIMARY KEY AUTO\_INCREMENT
  * name VARCHAR(255)
  * email VARCHAR(255)