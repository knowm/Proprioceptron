Proprioceptron
==========

A virtual environment for testing and simulating machine learning algorithms

Maven
==========
The Proprioceptron Java artifacts are currently hosted on the Xeiam Nexus repository here:

    <repositories>
        <repository>
            <id>xeiam_oss-snapshot</id>
            <snapshots />
            <url>http://nexus.xeiam.com/content/repositories/snapshots/</url>
        </repository>
        <repository>
            <id>xeiam_oss-release</id>
            <releases />
            <url>http://nexus.xeiam.com/content/repositories/releases/</url>
        </repository>
    </repositories>

Building
==========
mvn clean package  
mvn javadoc:javadoc  