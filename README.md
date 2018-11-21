# Seeburger-BIS-Settings
Seeburger BIS(Buisness Integration Software) Debug Mode Settings it is used for configuring the settings for debug mode.It is used Maven
project to easily managed build process. It have Jar file where the application it is started from there. 

Functionality:

  1. At the top is "Dir" - a directory folder which it can be chosen from the user or it is default directory used from a config.properties file which must
  be always in the same folder where it is the jar file(application). And there is a property in "config.properties" file called "folder" and there we can put whatever directory we want to.
  If the properties file doesn't found it will generate the default directory which is: "C:/bis6".

  2.Edit options: 3 checkboxes for 3 files. Every checked file it will be configured based on criteria: 

    - host.xml - it is searching with regular expression a specifig row which is commented and remove the comment.
    - vm.properties - Searching line which is containing "-Xms2G -Xmx4G" and replace it with "Xms1G - Xmx2G" with regex.
    - register.bat - Appending at the end of file - "CALL %BISAS_BIN%\\run-bisas.bat".

  3. Run button - It its running the above configuration and first of all it is searching a directory "bis6" from a parent directory given
  at dir field. If it's not found it is showing a red error message that inform us there is not directory found. After that it is checking
  if there is not single checkbox checked is printing a message in the console at the end of the applicaton.

  4. Restore Defaults button - restore all default settings in the files(not in the application).
  5. Clear button - Used for clear the default settings in the application.
  6. Console - At the end it is console for logging the activity of the user(printing messages).
