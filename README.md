# SESSL/ML3 Quick Start #

In this repository you find a number of files to quickly get started with simulation experiments with SESSL and ML3.
You do not need to install any additional software, as all required artifacts are downloaded 
automatically.
The supplied example experiment can be executed with
* `./mvnw scala:run` or the `run.sh` script on Unix
* `mvnw.cmd scala:run` or the `run.bat` script on Windows


To customize the example, you can edit three files:
* Change the SESSL version to use in the pom.xml
* Change the log level (i.e., the verbosity of the console output) in the pom.xml
* Edit the simulation model in the *.ml3 file or create a new one
* Edit the simulation experiment Specification in the .scala file or create a new one

You can rename and replace the *.ml3 and *.scala files. However, make sure that
* the experiment object in the *.scala file is correctly referenced (including the package path) in the pom.xml
* the *.ml3 model file is correctly referenced in the *scala file
 
Just see the example for a working configuration.
