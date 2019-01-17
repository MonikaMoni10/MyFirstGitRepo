REM How to use?
REM
REM In the Command Line prompt type:
REM
REM createCanadianTests.bat 95h 2012-01-01 94a 2011-07-01
REM
REM The first argument is the Control Sheet Edition (i.e. 95h)
REM The second argument is the tax date (i.e. 2012-01-01)
REM The third argument is the Boundary Control Sheet Edition (i.e. 94a)
REM The fourth argument is the boundary tax date (i.e. 2011-07-01)

set SCRIPT_PATH=%~dp0
powershell.exe -command "& %SCRIPT_PATH%CanadianTaxUpdate.ps1 -CONTROLEDITION '%1' -TAXDATE '%2' -BOUNDARYCONTROLEDITION %3 -BOUNDARYTAXDATE %4 -DATAPATH '%SCRIPT_PATH%Data'"