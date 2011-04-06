Brief notes on project usage

Do a "mvn package" on this project

Run using "mvn jetty:run"

If you want to have changes in the spreadsheet file picked up and the rule files updated automatically,
then open another terminal window, cd to this directory and run the "mvn exec:java" command.
This will use the filenames and directories in the POM to look for changes and refresh the data.

Enjoy

Derek