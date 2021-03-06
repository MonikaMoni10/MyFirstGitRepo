###############################################################################
#.Synopsis
# Parameters from the Command Line
#.Parameter -CONTROLVERSION
# Control Sheet version (i.e. 95h)
#.Parameter -TAXDATE (format YYY-MM-DD)
# Tax date
#.Parameter -BOUNDARYCONTROLEDITION
# Boundary Control Sheet version (i.e. 94a)
#.Parameter -BOUNDARYTAXDATE
# Boundary Tax Date (format YYY-MM-DD)
#.Parameter -DATAPATH
# Full path to the Data folder where the control sheet is located
###############################################################################
Param([alias("CONTROLEDITION")]$edition,[alias("TAXDATE")]$date,[alias("BOUNDARYCONTROLEDITION")]$boundaryEdition,[alias("BOUNDARYTAXDATE")]$boundaryDate, [alias("DATAPATH")]$pathToData)

function Get-Employee
{
###############################################################################
#.Synopsis
# Gets an employee from a CSV file
#.Parameter -CSVFILE
# The CSV file
#.Parameter -FIRST
# Indicates to return the first employee
#.Parameter -LAST
# Indicates to return the last employee
###############################################################################
    [CmdletBinding()]
    Param(
    [parameter(Mandatory=$true)]
    [alias("CSVFILE")]
    [string]$sourceFile,
	
    [parameter(Mandatory=$false)]
    [alias("FIRST")]
    [switch]$isFirst,
    
    [parameter(Mandatory=$false)]
    [alias("LAST")]
    [switch]$isLast)
    
    Process
    {
		if ($isFirst)
		{
			$employee = Import-Csv $sourceFile | Select "Case Number" | Select -First 1		
		}
		
		if ($isLast)
		{
			$employee = Import-Csv $sourceFile | Select "Case Number" | Select -Last 1		
		}
		
		$employee."Case Number"
	}
}

function Verify-Digits
{
###############################################################################
#.Synopsis
# Verify that the double has 2 digits and returns it as string
# Adds ".00" or "0" if verification fails.
#.Parameter aDouble
# A value of type double
###############################################################################
    [CmdletBinding()]
    Param(
    [parameter(Mandatory=$true)]
    [double]$aDouble)
    
    Process
    {
        # Get the string representation
        [string]$aString = $aDouble.ToString()
        
        # Has a decimal point?
        if(!($aString.Contains(".")))
        {
            $aString = $aString + ".00"
        }
        else
        {
            # Has only one decimal place?
            [string[]]$parts = $aString.Split(".")
            if ($parts[1].Length -eq 1)
            {
                $aString = $aString + "0"
            }
        }
        
        $aString
    }           
}


function Create-Directory
{
###############################################################################
#.Synopsis
# Creates a directory or file for each item in a list
#.Parameter -List
# A list of strings. Each string represents a path to a new folder or file
#.Parameter -Dir
# If present treats the list as directory paths
#.Parameter -File
# If present treats the list as file paths
###############################################################################

    [CmdletBinding()]
    Param(
    [parameter(Mandatory=$true, ParameterSetName="List", ValueFromPipeline=$true)]
    [string[]]$list,
    
    [parameter(Mandatory=$false)]
    [alias("Dir")]
    [switch]$isDirectory,
    
    [parameter(Mandatory=$false)]
    [alias("File")]
    [switch]$isFile)
    
    Process
    {
        if ($isDirectory)
        {
        
            $list | ForEach-Object {New-Item -Path $_ -type directory -Force}
        }
        
        if ($isFile)
        {
        
            $list | ForEach-Object {New-Item -Path $_ -type file -Force}
        }

    }
}

Function Process-Employees
{
###############################################################################
#.Synopsis
# Receives Employees information and uses it to create FitNesse tests 
#.Parameter -TESTFILE
# File where the FitNesse test are created
#.Parameter -CASENUMBER
# Control Sheet's Case Number column is mappped as Fixture column employee
#.Parameter -PROVINCE
# Control Sheet's Province column is passed to differentiate one province from
# another
#.Parameter -EI
# Control Sheet's EI column is mappped as Fixture column EDT
#.Parameter -CPPQPP
# Control Sheet's CPP/QPP column is mappped as Fixture column EDT
#.Parameter -PROVTAX
# Control Sheet's Prov Tax column is mappped as Fixture column EDT
#.Parameter -FEDTAX
# Control Sheet's Federal Tax column is mappped as Fixture column EDT
#.Parameter -NTTAX
# Control Sheet's QHSF/NT tax column is mappped as Fixture column EDT
###############################################################################

    [CmdletBinding()]
    Param(
    [parameter(Mandatory=$true)]
    [alias("TESTFILE")]
    [string]$fitFile, 
    
    [parameter(Mandatory=$true)]
    [alias("CASENUMBER")]
    [string]$employee,   

    [parameter(Mandatory=$true)]
    [alias("PROVINCE")]
    [string]$abbreviation,  
    
    [parameter(Mandatory=$true)]
    [alias("EI")]
    [double]$eir1,  
        
    [parameter(Mandatory=$true)]
    [alias("CPPQPP")]
    [double]$cpp,
    
    [parameter(Mandatory=$true)]
    [alias("PROVTAX")]    
    [double]$tax,
    
    [parameter(Mandatory=$true)]
    [alias("FEDTAX")]    
    [double]$inctax,
    
    [parameter(Mandatory=$false)]
    [alias("NTTAX")]    
    [double]$qhsfnttax,
    
    [parameter(Mandatory=$false)]
    [alias("EXTRA")]
    [switch]$needsExtra)
    
    Process
    {
        # Employees 1 to 9 need a left padding 0
        if ($employee.length -eq 1)
        {
            $employee = "0$employee"
        }
        
        # Employees for Specific needs an extra left padding 0
        if ($needsExtra)
        {
            if ($employee.length -eq 2)
            {
                $employee = "0$employee"
            }
        }
                
        # EI Column -> EIR1
        $validEir1 = Verify-Digits($eir1)
        Add-Content $fitFile "|$employee|EIR1|$validEir1||"
        
        # CPP/QPP Column -> CPP
        $validCpp = Verify-Digits($cpp)
        Add-Content $fitFile "|$employee|CPP|$validCpp||"

        # Prov Tax Column -> It depends on the province abbreviation
        switch ($abbreviation)
        {

            "BC" {$validTax = Verify-Digits($tax); Add-Content $fitFile "|$employee|BCITAX|$validTax||"}

            "AB" {$validTax = Verify-Digits($tax); Add-Content $fitFile "|$employee|ABITAX|$validTax||"}

            "SK" {$validTax = Verify-Digits($tax); Add-Content $fitFile "|$employee|SKITAX|$validTax||"}

            "MB" {$validTax = Verify-Digits($tax); Add-Content $fitFile "|$employee|MBITAX|$validTax||"}

            "ON" {$validTax = Verify-Digits($tax); Add-Content $fitFile "|$employee|ONITAX|$validTax||"}        
            
            "NL" {$validTax = Verify-Digits($tax); Add-Content $fitFile "|$employee|NFITAX|$validTax||"}        
            
            "NB" {$validTax = Verify-Digits($tax); Add-Content $fitFile "|$employee|NBITAX|$validTax||"}        
            
            "NS" {$validTax = Verify-Digits($tax); Add-Content $fitFile "|$employee|NSITAX|$validTax||"}        

            "PEI" {$validTax = Verify-Digits($tax); Add-Content $fitFile "|$employee|PEITAX|$validTax||"}        
            
            "YK" {$validTax = Verify-Digits($tax); Add-Content $fitFile "|$employee|YTITAX|$validTax||"}                    
            
            "NT" {$validTax = Verify-Digits($tax); Add-Content $fitFile "|$employee|NTITAX|$validTax||"}                    
            
            "NU" {$validTax = Verify-Digits($tax); Add-Content $fitFile "|$employee|NUITAX|$validTax||"}                                            
        }              
        
        # Fed Tax Column -> INCTAX
        $validInctax = Verify-Digits($inctax)
        Add-Content $fitFile "|$employee|INCTAX|$validInctax||"
        
        #Only for NT and NU
        if ($abbreviation -eq "NT")
        {
            $validQhsfnttax = Verify-Digits($qhsfnttax)
            Add-Content $fitFile "|$employee|NTPRT|$validQhsfnttax||"
        }
        elseif ($abbreviation -eq "NU")
        {
            $validQhsfnttax = Verify-Digits($qhsfnttax)
            Add-Content $fitFile "|$employee|NUPRT|$validQhsfnttax||"
        }
        
    }
}


Function Process-QcEmployees
{
###############################################################################
#.Synopsis
# Receives Quebec Employees information and uses it to create FitNesse tests 
#.Parameter -TESTFILE
# File where the FitNesse test are created
#.Parameter -CASENUMBER
# Control Sheet's Case Number column is mappped as Fixture column employee
#.Parameter -CPPQPP
# Control Sheet's CPP/QPP column is mappped as Fixture column EDT
#.Parameter -QCEI
# Control Sheet's QC EI column is mappped as Fixture column EDT
#.Parameter -QPIP
# Control Sheet's QPIP column is mappped as Fixture column EDT
#.Parameter -PROVTAX
# Control Sheet's Prov Tax column is mappped as Fixture column EDT
#.Parameter -FEDTAX
# Control Sheet's Federal Tax column is mappped as Fixture column EDT
#.Parameter -QHSF
# Control Sheet's QHSF/NT tax column is mappped as Fixture column EDT
###############################################################################

    [CmdletBinding()]
    Param(
    [parameter(Mandatory=$true)]
    [alias("TESTFILE")]
    [string]$fitFile, 
    
    [parameter(Mandatory=$true)]    
    [alias("CASENUMBER")]
    [string]$employee,   
        
    [parameter(Mandatory=$true)]
    [alias("CPPQPP")]
    [double]$cpp,
    
    [parameter(Mandatory=$true)]
    [alias("QCEI")]
    [double]$qeir1,  
    
    [parameter(Mandatory=$true)]
    [alias("QPIP")]
    [double]$pipq,  
        
    [parameter(Mandatory=$true)]
    [alias("PROVTAX")]    
    [double]$tax,
    
    [parameter(Mandatory=$true)]
    [alias("FEDTAX")]    
    [double]$inctax,
 
    [parameter(Mandatory=$true)]
    [alias("QHSF")]    
    [double]$qhsfnt)
    
    Process
    {
        # Employees 1 to 9 need a left padding 0
        if ($employee.length -eq 1)
        {
            $employee = "0$employee"
        }
                    
        # CPP/QPP Column -> QPP
        $validCpp = Verify-Digits($cpp)
        Add-Content $fitFile "|$employee|QPP|$validCpp||"

        # QC EI Column -> QEIR1
        $validQeir1 = Verify-Digits($qeir1) 
        Add-Content $fitFile "|$employee|QEIR1|$validQeir1||"        

        # QPIP Column -> QPIP
        $validPipq = Verify-Digits($pipq)
        Add-Content $fitFile "|$employee|QPIP|$validPipq||"     
        
        # Prov Tax Column -> QITAX
        $validTax = Verify-Digits($tax)
        Add-Content $fitFile "|$employee|QITAX|$validTax||"
                        
        # Fed Tax Column -> INCTAX
        $validInctax = Verify-Digits($inctax)
        Add-Content $fitFile "|$employee|INCTAX|$validInctax||"

        # QHSF/NT tax Column -> QHSF
        $validQhsfnt = Verify-Digits($qhsfnt)
        Add-Content $fitFile "|$employee|QHSF||$validQhsfnt|"        
    }
}

Function Process-OcEmployees
{
###############################################################################
#.Synopsis
# Receives Outside of Canada Employees information and uses it to create FitNesse tests 
#.Parameter -TESTFILE
# File where the FitNesse test are created
#.Parameter -CASENUMBER
# Control Sheet's Case Number column is mappped as Fixture column employee
#.Parameter -EI
# Control Sheet's EI column is mappped as Fixture column EDT
#.Parameter -CPPQPP
# Control Sheet's CPP/QPP column is mappped as Fixture column EDT
#.Parameter -FEDTAX
# Control Sheet's Federal Tax column is mappped as Fixture column EDT
###############################################################################

    [CmdletBinding()]
    Param(
    [parameter(Mandatory=$true)]
    [alias("TESTFILE")]
    [string]$fitFile, 
    
    [parameter(Mandatory=$true)]    
    [alias("CASENUMBER")]
    [string]$employee,   

    [parameter(Mandatory=$true)]
    [alias("EI")]
    [double]$eir1,  
            
    [parameter(Mandatory=$true)]
    [alias("CPPQPP")]
    [double]$cpp,
                  
    [parameter(Mandatory=$true)]
    [alias("FEDTAX")]    
    [double]$inctax)
     
    Process
    {
        # Employees 1 to 9 need a left padding 0
        if ($employee.length -eq 1)
        {
            $employee = "0$employee"
        }

        # EI Column -> EIR1
        $validEir1 = Verify-Digits($eir1) 
        Add-Content $fitFile "|$employee|EIR1|$validEir1||"        
                    
        # CPP/QPP Column -> CPP
        $validCpp = Verify-Digits($cpp)
        Add-Content $fitFile "|$employee|CPP|$validCpp||"
                        
        # Fed Tax Column -> INCTAX
        $validInctax = Verify-Digits($inctax)
        Add-Content $fitFile "|$employee|INCTAX|$validInctax||"
    }
}

Function Create-GenericTest
{
###############################################################################
#.Synopsis
# Creates the Generic Test using the Generic csv file
#.Parameter -CSVFILE
# File where the CSV information for this test is located
#.Parameter -TESTFILE
# File where the FitNesse test are created
#.Parameter -DATABASE
# The database to be used as Company Name
#.Parameter -DATE
# The date to be used as Session Date
###############################################################################
 [CmdletBinding()]
    Param(
    [parameter(Mandatory=$true)]
    [alias("CSVFILE")]
    [string]$sourceFile,
    
    [parameter(Mandatory=$true)]
    [alias("TESTFILE")]
    [string]$fitFile,
    
    [parameter(Mandatory=$true)]
    [alias("DATABASE")]
    [string]$company,
    
    [parameter(Mandatory=$true)]
    [alias("DATE")]
    [string]$session)
    
    Process
    {
		$firstEmployee = "0" + (Get-Employee -CSVFILE $sourceFile -FIRST)
		$lastEmployee = Get-Employee -CSVFILE $sourceFile -LAST

        # Populate generic test file
        Add-Content $fitFile "|Payroll Calc Fixture|$company|ADMIN|ADMIN|CP|$session|$firstEmployee|$lastEmployee|"
        Add-Content $fitFile "|employee|EDT|amount?|employer amount?|"

        # Process British Columbia Employees
        $bcEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "BC")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $bcEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}

        # Process Alberta Employees
        $abEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "AB")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $abEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}

        # Process Saskatchewan Employees
        $skEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "SK")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $skEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}
            
        # Process Manitoba Employees
        $mbEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "MB")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $mbEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}
            
        # Process Ontario Employees
        $onEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "ON")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $onEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}    
            
        # Process Quebec Employees
        $qcEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "QC")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $qcEmployees | ForEach-Object -Process {Process-QcEmployees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -CPPQPP $_."CPP/QPP" `
            -QCEI $_."QC EI" `
            -QPIP $_."QPIP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"  `
            -QHSF $_."QHSF/NT tax"}
            
        # Process Newfoundland and Labrador Employees
        $nlEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NL")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nlEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}  
            
        # Process New Brunswick Employees
        $nbEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NB")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nbEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}   
            
        # Process Nova Scotia Employees
        $nsEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NS")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nsEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}   
                
        # Process Prince Edward Island Employees
        $peiEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "PEI")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $peiEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}       
            
        # Process Yukon Employees
        $ykEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "YK")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $ykEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}       

        # Process Northwest Territories Employees
        $ntEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NT")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $ntEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax" `
            -NTTAX $_."QHSF/NT tax"}       

        # Process Nunavut Employees
        $nuEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NU")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nuEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax" `
            -NTTAX $_."QHSF/NT tax"}                           
    }
}

Function Create-MaximumTest
{
###############################################################################
#.Synopsis
# Creates the Maximum Test using the Maximum csv file
#.Parameter -CSVFILE
# File where the CSV information for this test is located
#.Parameter -TESTFILE
# File where the FitNesse test are created
#.Parameter -DATABASE
# The database to be used as Company Name
#.Parameter -DATE
# The date to be used as Session Date
###############################################################################
 [CmdletBinding()]
    Param(
    [parameter(Mandatory=$true)]
    [alias("CSVFILE")]
    [string]$sourceFile,
    
    [parameter(Mandatory=$true)]
    [alias("TESTFILE")]
    [string]$fitFile,
    
    [parameter(Mandatory=$true)]
    [alias("DATABASE")]
    [string]$company,
    
    [parameter(Mandatory=$true)]
    [alias("DATE")]
    [string]$session)
    
    Process
    {
		$firstEmployee = "0" + (Get-Employee -CSVFILE $sourceFile -FIRST)
		$lastEmployee = Get-Employee -CSVFILE $sourceFile -LAST
		
        # Populate maximum test file
        Add-Content $fitFile "|Payroll Calc Fixture|$company|ADMIN|ADMIN|CP|$session|$firstEmployee|$lastEmployee|"
        Add-Content $fitFile "|employee|EDT|amount?|employer amount?|"
        
        # Process Alberta Employees
        $abEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "AB")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $abEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}
            
        # Process British Columbia Employees
        $bcEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "BC")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $bcEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}

        # Process Manitoba Employees
        $mbEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "MB")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $mbEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}      
            
        # Process New Brunswick Employees
        $nbEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NB")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nbEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}      
            
        # Process Newfoundland and Labrador Employees
        $nlEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NL")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nlEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}   
            
        # Process Nova Scotia Employees
        $nsEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NS")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nsEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"} 
            
        # Process Ontario Employees
        $onEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "ON")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $onEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}   
            
        # Process Quebec Employees
        $qcEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "QC")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $qcEmployees | ForEach-Object -Process {Process-QcEmployees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -CPPQPP $_."CPP/QPP" `
            -QCEI $_."QC EI" `
            -QPIP $_."QPIP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"  `
            -QHSF $_."QHSF/NT tax"} 
            
        # Process Yukon Employees
        $ykEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "YK")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $ykEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}   
            
        # Process Northwest Territories Employees
        $ntEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NT")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $ntEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax" `
            -NTTAX $_."QHSF/NT tax"}  
            
        # Process Saskatchewan Employees
        $skEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "SK")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $skEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}  
            
        # Process Prince Edward Island Employees
        $peiEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "PEI")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $peiEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}  
            
        # Process Nunavut Employees
        $nuEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NU")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nuEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax" `
            -NTTAX $_."QHSF/NT tax"}                                                                                                                                                    
    }
}

Function Create-SpecificTest
{
###############################################################################
#.Synopsis
# Creates the Specific Test using the Specific csv file
#.Parameter -CSVFILE
# File where the CSV information for this test is located
#.Parameter -TESTFILE
# File where the FitNesse test are created
#.Parameter -DATABASE
# The database to be used as Company Name
#.Parameter -DATE
# The date to be used as Session Date
###############################################################################
 [CmdletBinding()]
    Param(
    [parameter(Mandatory=$true)]
    [alias("CSVFILE")]
    [string]$sourceFile,
    
    [parameter(Mandatory=$true)]
    [alias("TESTFILE")]
    [string]$fitFile,
    
    [parameter(Mandatory=$true)]
    [alias("DATABASE")]
    [string]$company,
    
    [parameter(Mandatory=$true)]
    [alias("DATE")]
    [string]$session)
    
    Process
    {
		$firstEmployee = "0" + (Get-Employee -CSVFILE $sourceFile -FIRST)
		$lastEmployee = Get-Employee -CSVFILE $sourceFile -LAST

        # Populate specific test file
        Add-Content $fitFile "|Payroll Calc Fixture|$company|ADMIN|ADMIN|CP|$session|$firstEmployee|$lastEmployee|"
        Add-Content $fitFile "|employee|EDT|amount?|employer amount?|"
        
        # Process British Columbia Employees
        $bcEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "BC")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $bcEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}
            
        # Process New Brunswick Employees
        $nbEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NB")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nbEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}   
            
        # Process Newfoundland and Labrador Employees
        $nlEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NL")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nlEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}              

        # Process Northwest Territories Employees
        $ntEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NT")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $ntEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax" `
            -NTTAX $_."QHSF/NT tax"}             

        # Process Nunavut Employees
        $nuEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NU")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nuEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax" `
            -NTTAX $_."QHSF/NT tax"} 
            
        # Process Ontario Employees
        $onEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "ON")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $onEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}  
            
        # Process Quebec Employees
        $qcEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "QC")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $qcEmployees | ForEach-Object -Process {Process-QcEmployees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -CPPQPP $_."CPP/QPP" `
            -QCEI $_."QC EI" `
            -QPIP $_."QPIP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"  `
            -QHSF $_."QHSF/NT tax"} 
            
        # Process Saskatchewan Employees
        $skEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "SK")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $skEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}             
                        
        # Process Yukon Employees
        $ykEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "YK")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $ykEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}                          
    }
}

Function Create-BoundarySpecificTest
{
###############################################################################
#.Synopsis
# Creates the Boundary Specific Test using the Specific csv file
#.Parameter -CSVFILE
# File where the CSV information for this test is located
#.Parameter -TESTFILE
# File where the FitNesse test are created
#.Parameter -DATABASE
# The database to be used as Company Name
#.Parameter -DATE
# The date to be used as Session Date
###############################################################################
 [CmdletBinding()]
    Param(
    [parameter(Mandatory=$true)]
    [alias("CSVFILE")]
    [string]$sourceFile,
    
    [parameter(Mandatory=$true)]
    [alias("TESTFILE")]
    [string]$fitFile,
    
    [parameter(Mandatory=$true)]
    [alias("DATABASE")]
    [string]$company,
    
    [parameter(Mandatory=$true)]
    [alias("DATE")]
    [string]$session)
    
    Process
    {
		# The original Boundary Specific database had 3 digits for employee id
		# i.e 001 or 015. This DB was the odd one, since in all ALL other DBs 
		# the employee id has two digits: 01 or 15.
		# In the next tax update Boundary Specific employees have two digits.
		# $firstEmployee = "00" + (Get-Employee -CSVFILE $sourceFile -FIRST)
		# $lastEmployee = "0" + (Get-Employee -CSVFILE $sourceFile -LAST)

		$firstEmployee = "0" + (Get-Employee -CSVFILE $sourceFile -FIRST)
		$lastEmployee = (Get-Employee -CSVFILE $sourceFile -LAST)
		
        # Populate specific test file
        Add-Content $fitFile "|Payroll Calc Fixture|$company|ADMIN|ADMIN|CP|$session|$firstEmployee|$lastEmployee|"
        Add-Content $fitFile "|employee|EDT|amount?|employer amount?|"
        
        # Process Manitoba Employees
        $mbEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "MB")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $mbEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax" `
            }
			#-EXTRA}      
                              
			
        # Process New Brunswick Employees
        $nbEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NB")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nbEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax" `
            }
			#-EXTRA}                             
			
        # Process Nova Scotia Employees
        $nsEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NS")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nsEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax" `
			}
            #-EXTRA}                  
			
        # Process Saskatchewan Employees
        $skEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "SK")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $skEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax" `
			}
            #-EXTRA}                  
    }                                               
}

Function Create-CommissionTest
{
###############################################################################
#.Synopsis
# Creates the Commission Test using the Commission csv file
#.Parameter -CSVFILE
# File where the CSV information for this test is located
#.Parameter -TESTFILE
# File where the FitNesse test are created
#.Parameter -DATABASE
# The database to be used as Company Name
#.Parameter -DATE
# The date to be used as Session Date
###############################################################################
 [CmdletBinding()]
    Param(
    [parameter(Mandatory=$true)]
    [alias("CSVFILE")]
    [string]$sourceFile,
    
    [parameter(Mandatory=$true)]
    [alias("TESTFILE")]
    [string]$fitFile,
    
    [parameter(Mandatory=$true)]
    [alias("DATABASE")]
    [string]$company,
    
    [parameter(Mandatory=$true)]
    [alias("DATE")]
    [string]$session)
    
    Process
    {
		$firstEmployee = "0" + (Get-Employee -CSVFILE $sourceFile -FIRST)
		$lastEmployee = Get-Employee -CSVFILE $sourceFile -LAST

        # Populate specific test file
        Add-Content $fitFile "|Payroll Calc Fixture|$company|ADMIN|ADMIN|CP|$session|$firstEmployee|$lastEmployee|"
        Add-Content $fitFile "|employee|EDT|amount?|employer amount?|"
        
        # Process Alberta Employees
        $abEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "AB")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $abEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}   
            
        # Process British Columbia Employees
        $bcEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "BC")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $bcEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"} 
            
        # Process Manitoba Employees
        $mbEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "MB")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $mbEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}                
            
        # Process New Brunswick Employees
        $nbEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NB")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nbEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}   
            
        # Process Newfoundland and Labrador Employees
        $nlEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NL")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nlEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}              
            
        # Process Nova Scotia Employees
        $nsEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NS")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nsEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}
            
        # Process Ontario Employees
        $onEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "ON")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $onEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}              
                                   
        # Process Prince Edward Island Employees
        $peiEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "PEI")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $peiEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}  
            
        # Process Saskatchewan Employees
        $skEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "SK")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $skEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}   
            
        # Process Yukon Employees
        $ykEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "YK")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $ykEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}                          
                        
        # Process Northwest Territories Employees
        $ntEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NT")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $ntEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax" `
            -NTTAX $_."QHSF/NT tax"}    
            
        # Process Quebec Employees
        $qcEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "QC")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $qcEmployees | ForEach-Object -Process {Process-QcEmployees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -CPPQPP $_."CPP/QPP" `
            -QCEI $_."QC EI" `
            -QPIP $_."QPIP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"  `
            -QHSF $_."QHSF/NT tax"}   
            
        # Process Nunavut Employees
        $nuEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NU")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nuEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax" `
            -NTTAX $_."QHSF/NT tax"}                                                                                                               
            
        # Process Outside Canada Employees
        $ocEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "os Cda")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}
        $ocEmployees | ForEach-Object -Process {Process-OcEmployees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -FEDTAX $_."Fed tax"}                    
    }
}

Function Create-QuebecTest
{
###############################################################################
#.Synopsis
# Creates the Quebec Test using the Quebec csv file
#.Parameter -CSVFILE
# File where the CSV information for this test is located
#.Parameter -TESTFILE
# File where the FitNesse test are created
#.Parameter -DATABASE
# The database to be used as Company Name
#.Parameter -DATE
# The date to be used as Session Date
###############################################################################
 [CmdletBinding()]
    Param(
    [parameter(Mandatory=$true)]
    [alias("CSVFILE")]
    [string]$sourceFile,
    
    [parameter(Mandatory=$true)]
    [alias("TESTFILE")]
    [string]$fitFile,
    
    [parameter(Mandatory=$true)]
    [alias("DATABASE")]
    [string]$company,
    
    [parameter(Mandatory=$true)]
    [alias("DATE")]
    [string]$session)
    
    Process
    {
		$firstEmployee = "0" + (Get-Employee -CSVFILE $sourceFile -FIRST)
		$lastEmployee = Get-Employee -CSVFILE $sourceFile -LAST

        # Populate specific test file
        Add-Content $fitFile "|Payroll Calc Fixture|$company|ADMIN|ADMIN|CP|$session|$firstEmployee|$lastEmployee|"
        Add-Content $fitFile "|employee|EDT|amount?|employer amount?|"
        
        # Process Quebec Employees
        $qcEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "QC")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $qcEmployees | ForEach-Object -Process {Process-QcEmployees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -CPPQPP $_."CPP/QPP" `
            -QCEI $_."QC EI" `
            -QPIP $_."QPIP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"  `
            -QHSF $_."QHSF/NT tax"}         
    }
}
Function Create-BenefitTest
{
###############################################################################
#.Synopsis
# Creates the Benefit Test using the Benefit csv file
#.Parameter -CSVFILE
# File where the CSV information for this test is located
#.Parameter -TESTFILE
# File where the FitNesse test are created
#.Parameter -DATABASE
# The database to be used as Company Name
#.Parameter -DATE
# The date to be used as Session Date
###############################################################################
 [CmdletBinding()]
    Param(
    [parameter(Mandatory=$true)]
    [alias("CSVFILE")]
    [string]$sourceFile,
    
    [parameter(Mandatory=$true)]
    [alias("TESTFILE")]
    [string]$fitFile,
    
    [parameter(Mandatory=$true)]
    [alias("DATABASE")]
    [string]$company,
    
    [parameter(Mandatory=$true)]
    [alias("DATE")]
    [string]$session)
    
    Process
    {
		$firstEmployee = "0" + (Get-Employee -CSVFILE $sourceFile -FIRST)
		$lastEmployee = Get-Employee -CSVFILE $sourceFile -LAST

        # Populate specific test file
        Add-Content $fitFile "|Payroll Calc Fixture|$company|ADMIN|ADMIN|CP|$session|$firstEmployee|$lastEmployee|"
        Add-Content $fitFile "|employee|EDT|amount?|employer amount?|"

        # Process British Columbia Employees
        $bcEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "BC")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $bcEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"} 
                    
        # Process Alberta Employees
        $abEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "AB")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $abEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}   
            
        # Process Saskatchewan Employees
        $skEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "SK")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $skEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}               
            
        # Process Manitoba Employees
        $mbEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "MB")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $mbEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}   
            
        # Process Ontario Employees
        $onEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "ON")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $onEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}     
            
        # Process Quebec Employees
        $qcEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "QC")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $qcEmployees | ForEach-Object -Process {Process-QcEmployees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -CPPQPP $_."CPP/QPP" `
            -QCEI $_."QC EI" `
            -QPIP $_."QPIP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"  `
            -QHSF $_."QHSF/NT tax"}         
            
        # Process Prince Edward Island Employees
        $peiEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "PEI")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $peiEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}  
            
        # Process Newfoundland and Labrador Employees
        $nlEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NL")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nlEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}              
            
        # Process New Brunswick Employees
        $nbEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NB")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nbEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}   
                        
        # Process Nova Scotia Employees
        $nsEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NS")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nsEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}
                                                                                  
        # Process Yukon Employees
        $ykEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "YK")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $ykEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax"}                          
                        
        # Process Northwest Territories Employees
        $ntEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NT")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $ntEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax" `
            -NTTAX $_."QHSF/NT tax"}    
                        
        # Process Nunavut Employees
        $nuEmployees = Import-Csv $sourceFile | Where-Object {($_.Province -eq "NU")} | Where-Object {(!($_.P -eq 2) -and !($_.P -eq 1))}

        $nuEmployees | ForEach-Object -Process {Process-Employees `
            -TESTFILE $fitFile `
            -CASENUMBER $_."Case Number" `
            -PROVINCE $_."Province" `
            -EI $_.EI `
            -CPPQPP $_."CPP/QPP" `
            -PROVTAX $_."Prov tax" `
            -FEDTAX $_."Fed tax" `
            -NTTAX $_."QHSF/NT tax"}                                                                                                                                                                                                 
    }
}
###############################################################################
#.Synopsis
# Program starts here
###############################################################################

# To test inside PowerShell IDE
#$edition = 95h
#$year = 2012
#$boudaryEdition = 94a
#$pathToData = "C:\?\Data"

# Calculate needed Paths
$pathElements = $pathToData.Split("\")
$frontPagePath = $pathElements[0] + "\" + $pathElements[1] + "\FitNesseRoot\FrontPage"

# Database name postfix
$dbPostfix = ($edition -replace ".$") + "D"
$boundaryDBPostfix = ($boundaryEdition -replace ".$") + "D"

# Date is January 1st of the tax year (YYY-MM-DD)
#$date = [System.Convert]::ToString($year) + "-01-01"

#Boundary date is set to July 1st of the previos Tax Year
#$boundaryDate = [System.Convert]::ToString($year - 1) + "-07-01"

# List of folder paths
$taxUpdateFolder = $frontPagePath + "\Sage300CanadaTaxUpdateTests"
$setupFolder = $taxUpdateFolder + "\SetUp"

$suiteTaxUpdateFolder = $taxUpdateFolder + "\SuiteCanadianTaxUpdate"
$genericFolder = $suiteTaxUpdateFolder + "\GenericTest"
$maximumFolder = $suiteTaxUpdateFolder + "\MaximumTest"
$specificFolder = $suiteTaxUpdateFolder + "\SpecificTest"
$commissionFolder = $suiteTaxUpdateFolder + "\CommissionTest"
$quebecFolder = $suiteTaxUpdateFolder + "\QuebecTest"
$benefitFolder = $suiteTaxUpdateFolder + "\BenefitTest"

$suiteBoundaryTaxUpdateFolder = $taxUpdateFolder + "\SuiteBoundaryCanadianTaxUpdate"
$boundaryGenericFolder = $suiteBoundaryTaxUpdateFolder + "\GenericTest"
$boundaryMaximumFolder = $suiteBoundaryTaxUpdateFolder + "\MaximumTest"
$boundarySpecificFolder = $suiteBoundaryTaxUpdateFolder + "\SpecificTest"
$boundaryCommissionFolder = $suiteBoundaryTaxUpdateFolder + "\CommissionTest"
$boundaryQuebecFolder = $suiteBoundaryTaxUpdateFolder + "\QuebecTest"

[string[]]$folderList = $taxUpdateFolder, $setupFolder, $suiteTaxUpdateFolder, $genericFolder, $maximumFolder, `
$specificFolder, $commissionFolder, $quebecFolder, $benefitFolder, $suiteBoundaryTaxUpdateFolder, $boundaryGenericFolder, `
$boundaryMaximumFolder, $boundarySpecificFolder, $boundaryCommissionFolder, $boundaryQuebecFolder

# List of file paths
$outSuite = $taxUpdateFolder + "\content.txt"
$inSuite = $suiteTaxUpdateFolder + "\content.txt"
$inBoundarySuite = $suiteBoundaryTaxUpdateFolder + "\content.txt"

$setup = $setupFolder + "\content.txt"

$genericTest = $genericFolder + "\content.txt"
$maximumTest = $maximumFolder + "\content.txt"
$specificTest = $specificFolder + "\content.txt"
$commissionTest = $commissionFolder + "\content.txt"
$quebecTest = $quebecFolder + "\content.txt"
$benefitTest = $benefitFolder + "\content.txt"

$boundaryGenericTest = $boundaryGenericFolder + "\content.txt"
$boundaryMaximumTest = $boundaryMaximumFolder + "\content.txt"
$boundarySpecificTest = $boundarySpecificFolder + "\content.txt"
$boundaryCommissionTest = $boundaryCommissionFolder + "\content.txt"
$boundaryQuebecTest = $boundaryQuebecFolder + "\content.txt"


[string[]]$fileList = $outSuite, $inSuite, $setup, $genericTest, $maximumTest, $specificTest, $commissionTest, $quebecTest, $benefitTest,`
$inBoundarySuite, $boundaryGenericTest, $boundaryMaximumTest, $boundarySpecificTest, $boundaryCommissionTest, $boundaryQuebecTest

# List of CSV files
$genericCSV = $pathToData + "\CSV\consolidated_" + $edition + "_Generic.csv"
$maximumCSV = $pathToData + "\CSV\consolidated_" + $edition + "_Maximum.csv"
$specificCSV = $pathToData + "\CSV\consolidated_" + $edition + "_Specific.csv"
$commissionCSV = $pathToData + "\CSV\consolidated_" + $edition + "_Commission.csv"
$quebecCSV = $pathToData + "\CSV\consolidated_" + $edition + "_Quebec.csv"
$benefitCSV = $pathToData + "\CSV\consolidated_" + $edition + "_Benefits.csv"

$boundaryGenericCSV = $pathToData + "\CSV\consolidated_" + $boundaryEdition + "_Generic.csv"
$boundaryMaximumCSV = $pathToData + "\CSV\consolidated_" + $boundaryEdition + "_Maximum.csv"
$boundarySpecificCSV = $pathToData + "\CSV\consolidated_" + $boundaryEdition + "_Specific.csv"
$boundaryCommissionCSV = $pathToData + "\CSV\consolidated_" + $boundaryEdition + "_Commission.csv"
$boundaryQuebecCSV = $pathToData + "\CSV\consolidated_" + $boundaryEdition + "_Quebec.csv"

# Create required folders and files
Create-Directory -List $folderList -Dir
Create-Directory -List $fileList -File

# Populate Outside Suite content file
Add-Content $outSuite "!contents"

# Populate Inside Suite content file
Add-Content $inSuite "!contents"

# Populate Inside Boundary Suite content file
Add-Content $inBoundarySuite "!contents"

# Populate setup file
Add-Content $setup "|import|"
Add-Content $setup "|fitnesse.fixtures|"
Add-Content $setup "|com.sage.accpac.automation.payroll.calc.fixture|"

# Create Canadian Tax Update Tests
Create-GenericTest -CSVFILE $genericCSV -TESTFILE $genericTest -DATABASE ("GEN" + $dbPostfix) -DATE $date
Create-MaximumTest -CSVFILE $maximumCSV -TESTFILE $maximumTest -DATABASE ("MAX" + $dbPostfix) -DATE $date
Create-SpecificTest -CSVFILE $specificCSV -TESTFILE $specificTest -DATABASE ("SPE" + $dbPostfix) -DATE $date
Create-CommissionTest -CSVFILE $commissionCSV -TESTFILE $commissionTest -DATABASE ("COM" + $dbPostfix) -DATE $date
Create-QuebecTest -CSVFILE $quebecCSV -TESTFILE $quebecTest -DATABASE ("QC" + $dbPostfix) -DATE $date
Create-BenefitTest -CSVFILE $benefitCSV -TESTFILE $benefitTest -DATABASE ("BEN" + $dbPostfix) -DATE $date

# Create Boundary Canadian Tax Update Tests
Create-GenericTest -CSVFILE $boundaryGenericCSV -TESTFILE $boundaryGenericTest -DATABASE ("BGE" + $boundaryDBPostfix) -DATE $boundaryDate
Create-MaximumTest -CSVFILE $boundaryMaximumCSV -TESTFILE $boundaryMaximumTest -DATABASE ("BMX" + $boundaryDBPostfix) -DATE $boundaryDate
Create-BoundarySpecificTest -CSVFILE $boundarySpecificCSV -TESTFILE $boundarySpecificTest -DATABASE ("BSP" + $boundaryDBPostfix) -DATE $boundaryDate
Create-CommissionTest -CSVFILE $boundaryCommissionCSV -TESTFILE $boundaryCommissionTest -DATABASE ("BCO" + $boundaryDBPostfix) -DATE $boundaryDate
Create-QuebecTest -CSVFILE $boundaryQuebecCSV -TESTFILE $boundaryQuebecTest -DATABASE ("BQC" + $boundaryDBPostfix) -DATE $boundaryDate
