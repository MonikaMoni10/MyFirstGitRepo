REM How to use?
REM
REM In the Command Line prompt type:
REM
REM createUSTests.bat SIT56D 2012-01-31
REM
REM First argument is the company / database name
REM The second argument is the tax run date

set SCRIPT_PATH=%~dp0
powershell.exe -command "& %SCRIPT_PATH%USTaxUpdate.ps1 -COMPANY '%1' -RUNDATE '%2' -DATAPATH '%SCRIPT_PATH%Data'"