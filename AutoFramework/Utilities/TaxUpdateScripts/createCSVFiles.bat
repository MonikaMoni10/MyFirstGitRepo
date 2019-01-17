REM How to use?
REM
REM In the Command Line prompt type:
REM
REM createCSVFiles.bat consolidated_94a.xls Canada
REM
REM First argument is ONLY the control Sheet name with extension
REM The second argument is the country (Canada or USA)
REM
REM 
REM createCSVFiles.bat "Control sheet.xls" USA 2012-01-31
REM
REM First argument is ONLY Control Sheet name with extension and it has an empty space so quotes are needed
REM The second argument is the country (Canada or USA)
REM The third argument is the Tax Date (Only for US Control Sheet)
echo off
set SCRIPT_PATH=%~dp0
IF "%3"=="" (
	powershell.exe -command "& %SCRIPT_PATH%ConvertExcelToCSV.ps1 -CONTROLSHEET '%1' -COUNTRY '%2' -DATAPATH '%SCRIPT_PATH%Data' -DATE '00'"
) ELSE (
	powershell.exe -command "& %SCRIPT_PATH%ConvertExcelToCSV.ps1 -CONTROLSHEET '%1' -COUNTRY '%2' -DATAPATH '%SCRIPT_PATH%Data' -DATE '%3'" 
)