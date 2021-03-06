###############################################################################
#.Synopsis
# Parameters from the Command Line
#.Parameter -COMPANY
# Company name (i.e. SIT56D)
#.Parameter -UPDATEDATE
# The tax update date
#.Parameter -RUNDATE
# The tax run date
#.Parameter -DATAPATH
# Full path to the Data folder where the control sheet is located
###############################################################################
Param([alias("COMPANY")]$database,[alias("UPDATEDATE")]$update,[alias("RUNDATE")]$run,[alias("DATAPATH")]$pathToData)

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
			$employee = Import-Csv $sourceFile | Select "Case #" | Select -First 1		
		}
		
		if ($isLast)
		{
			$employee = Import-Csv $sourceFile | Select "Case #" | Select -Last 1		
		}
		
		$employee."Case #"
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

Function Create-Test
{
###############################################################################
#.Synopsis
# Creates the a FitNesse test using a csv file
#.Parameter -CSVFILE
# File where the CSV information for this test is located
#.Parameter -TESTFILE
# File where the FitNesse test are created
#.Parameter -STATE
# The Control Sheet tab name (FED, CA, NY, etc)
#.Parameter -DATABASE
# The database to be used as Company Name
#.Parameter -DATE
# The date to be used as run date
#.Parameter -FIT
# This test uses FIT
#.Parameter -SIT
# This test uses SIT
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
    [alias("STATE")]
    [string]$controlTabName,
        
    [parameter(Mandatory=$true)]
    [alias("DATABASE")]
    [string]$company,
    
    [parameter(Mandatory=$true)]
    [alias("DATE")]
    [string]$session,
        
    [parameter(Mandatory=$false)]
    [alias("FIT")]
    [switch]$isFit,
    
    [parameter(Mandatory=$false)]
    [alias("SIT")]
    [switch]$isSit)
    
    Process
    {
		$firstEmployee = Get-Employee -CSVFILE $sourceFile -FIRST
		$lastEmployee = Get-Employee -CSVFILE $sourceFile -LAST
		
		# According to Sage 300: PR16 is greater than PR155 so PR16 and up are not found
		# when the range is PR01 - PR155. Right now PR is the only worksheet with more than 
		# 100 employees. This is a quick fix. In the future we may want to always look 
		# for ranges XX01 to XX999
		if ($controlTabName.Contains("PR"))
		{
			$lastEmployee = "PR999"
		}
		
        # Populate federal test file
        Add-Content $fitFile "|Payroll Calc Fixture|$company|ADMIN|ADMIN|UP|$session|$firstEmployee|$lastEmployee|"
        Add-Content $fitFile "|employee|EDT|amount?|employer amount?|"

        # Get Employees - Exclude employees which FIT / SIT is NA (Currently only Oregon
		if ($isFit)
        {
        	$employees = Import-Csv $sourceFile | Where-Object {($_."FIT/PP" -ne "NA")}
		}

		if ($isSit)
        {
        	$employees = Import-Csv $sourceFile | Where-Object {($_."SIT/PP" -ne "NA")}
		}
		
		# Process Employees
        if ($isFit)
        {
            $employees | ForEach-Object -Process {Process-Employees `
                -TESTFILE $fitFile `
                -CASENUMBER $_."Case #" `
                -ITPP ($_."FIT/PP").Replace("$","") `
                -STATE $controlTabName}
        }
        
        if ($isSit)
        {
            $employees | ForEach-Object -Process {Process-Employees `
                -TESTFILE $fitFile `
                -CASENUMBER $_."Case #" `
                -ITPP ($_."SIT/PP").Replace("$","") `
                -STATE $controlTabName}
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
#.Parameter -ITPP
# Control Sheet's FIT/PP column is mappped as Fixture column EDT
#.Parameter -STATE
# The Control Sheet tab name (FED, CA, NY, etc)
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
    [alias("ITPP")]
    [double]$tax,
    
    [parameter(Mandatory=$true)]
    [alias("STATE")]
    [string]$controlTabName)
    
    Process
    {
        $validTax = Verify-Digits($tax)
        
        # SIT/PP Column -> It depends on the state name
        switch ($controlTabName)
        {

            "FED" {Add-Content $fitFile "|$employee|USFIT|$validTax||"}

            "CA" {Add-Content $fitFile "|$employee|CASIT|$validTax||"}
            
            "NY" {Add-Content $fitFile "|$employee|NYSIT|$validTax||"}
            
            "NYY" {Add-Content $fitFile "|$employee|NYYCIT|$validTax||"}
            
            "KY" {Add-Content $fitFile "|$employee|KYSIT|$validTax||"}
            
            "CT" {Add-Content $fitFile "|$employee|CTSIT|$validTax||"}
            
            "ME" {Add-Content $fitFile "|$employee|MESIT|$validTax||"}
            
            "NM" {Add-Content $fitFile "|$employee|NMSIT|$validTax||"}
            
            "DC" {Add-Content $fitFile "|$employee|DCSIT|$validTax||"}
            
            "MN" {Add-Content $fitFile "|$employee|MNSIT|$validTax||"}
            
            "ND" {Add-Content $fitFile "|$employee|NDSIT|$validTax||"}
            
            "OR" {Add-Content $fitFile "|$employee|ORSIT|$validTax||"}
            
            "OR1" {Add-Content $fitFile "|$employee|ORSIT|$validTax||"}
            
            "AL" {Add-Content $fitFile "|$employee|ALSIT|$validTax||"}
            
            "IA" {Add-Content $fitFile "|$employee|IASIT|$validTax||"}
            
            "MO" {Add-Content $fitFile "|$employee|MOSIT|$validTax||"}
			
			"MA" {Add-Content $fitFile "|$employee|MASIT|$validTax||"}
			
			"DE" {Add-Content $fitFile "|$employee|DESIT|$validTax||"}
			
			"RI" {Add-Content $fitFile "|$employee|RISIT|$validTax||"}
			
			"VT" {Add-Content $fitFile "|$employee|VTSIT|$validTax||"}
			
			"HI" {Add-Content $fitFile "|$employee|HISIT|$validTax||"}
			
			"OK" {Add-Content $fitFile "|$employee|OKSIT|$validTax||"}
			
			"ID" {Add-Content $fitFile "|$employee|IDSIT|$validTax||"}
			
			"Yonkers" {Add-Content $fitFile "|$employee|NYYCIT|$validTax||"}
			
			"PR" {Add-Content $fitFile "|$employee|PRSIT|$validTax||"}
			
			"MD" {Add-Content $fitFile "|$employee|MDSIT|$validTax||"}
            
            "KS" {Add-Content $fitFile "|$employee|KSSIT|$validTax||"}
                        
            "WI" {Add-Content $fitFile "|$employee|WISIT|$validTax||"}
                                    
            "NE" {Add-Content $fitFile "|$employee|NESIT|$validTax||"}
            
            "GA" {Add-Content $fitFile "|$employee|GASIT|$validTax||"}
            
            "IL" {Add-Content $fitFile "|$employee|ILSIT|$validTax||"}
            
            "CO" {Add-Content $fitFile "|$employee|COSIT|$validTax||"}
            
            "AZ" {Add-Content $fitFile "|$employee|AZSIT|$validTax||"}
            
            "AR" {Add-Content $fitFile "|$employee|ARSIT|$validTax||"}
            
            "IN" {Add-Content $fitFile "|$employee|INSIT|$validTax||"}
            
            "LA" {Add-Content $fitFile "|$employee|LASIT|$validTax||"}
            
            "MI" {Add-Content $fitFile "|$employee|MISIT|$validTax||"}
            
            "MS" {Add-Content $fitFile "|$employee|MSSIT|$validTax||"}
            
            "MT" {Add-Content $fitFile "|$employee|MTSIT|$validTax||"}
            
            "NJ" {Add-Content $fitFile "|$employee|NJSIT|$validTax||"}
            
            "NC" {Add-Content $fitFile "|$employee|NCSIT|$validTax||"}
            
            "OH" {Add-Content $fitFile "|$employee|OHSIT|$validTax||"}
            
            "PA" {Add-Content $fitFile "|$employee|PASIT|$validTax||"}
            
            "SC" {Add-Content $fitFile "|$employee|SCSIT|$validTax||"}
            
            "UT" {Add-Content $fitFile "|$employee|UTSIT|$validTax||"}
            
            "VA" {Add-Content $fitFile "|$employee|VASIT|$validTax||"}
            
            "WV" {Add-Content $fitFile "|$employee|WVSIT|$validTax||"}
            
            "NYC" {Add-Content $fitFile "|$employee|NYCCIT|$validTax||"}
        }                
    }
}
            
###############################################################################
#.Synopsis
# Program starts here
###############################################################################

# To test inside PowerShell IDE
#$database = "SIT56D"
#$update = "2012-06-30"
#$run = "2012-01-01"
#$pathToData = "C:\?\Data"

# Calculate needed Paths
$pathElements = $pathToData.Split("\")
$frontPagePath = $pathElements[0] + "\" + $pathElements[1] + "\FitNesseRoot\FrontPage"

# Separate Year, Month, Day
$dateElements = $update.Split("-")


# List of folder paths
$taxUpdateFolder = $frontPagePath + "\Sage300UsTaxUpdateTests" 
$setupFolder = $taxUpdateFolder + "\SetUp"

$suiteTaxUpdateFolder = $taxUpdateFolder + "\SuiteUsTaxUpdate" + $dateElements[0] + $dateElements[1] + $dateElements[2]

$federalFolder = $suiteTaxUpdateFolder + "\FederalTest"
$californiaFolder = $suiteTaxUpdateFolder + "\CaliforniaTest"
$newYorkFolder = $suiteTaxUpdateFolder + "\NewYorkTest"
$yonkersFolder = $suiteTaxUpdateFolder + "\YonkersTest"
$kentuckyFolder = $suiteTaxUpdateFolder + "\KentuckyTest"
$connecticutFolder = $suiteTaxUpdateFolder + "\ConneticutTest"
$maineFolder = $suiteTaxUpdateFolder + "\MaineTest"
$newMexicoFolder = $suiteTaxUpdateFolder + "\NewMexicoTest"
$dcFolder = $suiteTaxUpdateFolder + "\DcTest"
$minnesotaFolder = $suiteTaxUpdateFolder + "\MinnesotaTest"
$northDakotaFolder = $suiteTaxUpdateFolder + "\NorthDakotaTest"
$oregonFolder= $suiteTaxUpdateFolder + "\OregonTest"
$oregon1Folder = $suiteTaxUpdateFolder + "\OregonOneTest"
$alabamaFolder = $suiteTaxUpdateFolder + "\AlabamaTest"
$iowaFolder = $suiteTaxUpdateFolder + "\IowaTest"
$missouriFolder = $suiteTaxUpdateFolder + "\MissouriTest"

$massachusettsFolder = $suiteTaxUpdateFolder + "\MassachusettsTest"
$delawareFolder = $suiteTaxUpdateFolder + "\DelawareTest"
$rhodeIslandFolder = $suiteTaxUpdateFolder + "\RhodeIslandTest"
$vermontFolder = $suiteTaxUpdateFolder + "\VermontTest"
$hawaiiFolder = $suiteTaxUpdateFolder + "\HawaiiTest"
$oklahomaFolder = $suiteTaxUpdateFolder + "\OklahomaTest"
$idahoFolder = $suiteTaxUpdateFolder + "\IdahoTest"
$yonkers1Folder = $suiteTaxUpdateFolder + "\YonkersOneTest"
$puertoRicoFolder = $suiteTaxUpdateFolder + "\PuertoRicoTest"

$marylandFolder = $suiteTaxUpdateFolder + "\MarylandTest"

$kansasFolder = $suiteTaxUpdateFolder + "\KansasTest"
$wisconsinFolder = $suiteTaxUpdateFolder + "\WisconsinTest"
$nebraskaFolder = $suiteTaxUpdateFolder + "\NebraskaTest"

$georgiaFolder = $suiteTaxUpdateFolder + "\GeorgiaTest"
$illinoisFolder = $suiteTaxUpdateFolder + "\IllinoisTest"
$coloradoFolder = $suiteTaxUpdateFolder + "\ColoradoTest"

$arizonaFolder = $suiteTaxUpdateFolder + "\ArizonaTest"
$arkansasFolder = $suiteTaxUpdateFolder + "\ArkansasTest"
$indianaFolder = $suiteTaxUpdateFolder + "\IndianaTest"
$louisianaFolder = $suiteTaxUpdateFolder + "\LouisianaTest"
$michiganFolder = $suiteTaxUpdateFolder + "\MichiganTest"
$mississippiFolder = $suiteTaxUpdateFolder + "\MississippiTest"
$montanaFolder = $suiteTaxUpdateFolder + "\MontanaTest"
$newJerseyFolder = $suiteTaxUpdateFolder + "\NewJerseyTest"
$northCarolinaFolder = $suiteTaxUpdateFolder + "\NorthCarolinaTest"
$ohioFolder = $suiteTaxUpdateFolder + "\OhioTest"
$pennsylvaniaFolder = $suiteTaxUpdateFolder + "\PennsylvaniaTest"
$southCarolinaFolder = $suiteTaxUpdateFolder + "\SouthCarolinaTest"
$utahFolder = $suiteTaxUpdateFolder + "\UtahTest"
$virginiaFolder = $suiteTaxUpdateFolder + "\VirginiaTest"
$westVirginiaFolder = $suiteTaxUpdateFolder + "\WestVirginiaTest"
$newYorkCityFolder = $suiteTaxUpdateFolder + "\NewYorkCityTest"

[string[]]$folderList = $taxUpdateFolder, $setupFolder, $suiteTaxUpdateFolder

# List of file paths
$outSuite = $taxUpdateFolder + "\content.txt"
$inSuite = $suiteTaxUpdateFolder + "\content.txt"

$setup = $taxUpdateFolder + "\SetUp\content.txt"

$federalTest = $federalFolder + "\content.txt"
$californiaTest = $californiaFolder + "\content.txt"
$newYorkTest = $newYorkFolder + "\content.txt"
$yonkersTest = $yonkersFolder + "\content.txt"
$kentuckyTest = $kentuckyFolder + "\content.txt"
$connecticutTest = $connecticutFolder + "\content.txt"
$maineTest = $maineFolder + "\content.txt"
$newMexicoTest = $newMexicoFolder + "\content.txt"
$dcTest = $dcFolder + "\content.txt"
$minnesotaTest = $minnesotaFolder + "\content.txt"
$northDakotaTest = $northDakotaFolder + "\content.txt"
$oregonTest = $oregonFolder + "\content.txt"
$oregon1Test = $oregon1Folder + "\content.txt"
$alabamaTest = $alabamaFolder + "\content.txt"
$iowaTest = $iowaFolder + "\content.txt"
$missouriTest = $missouriFolder + "\content.txt"

$massachusettsTest = $massachusettsFolder + "\content.txt"
$delawareTest = $delawareFolder + "\content.txt"
$rhodeIslandTest = $rhodeIslandFolder + "\content.txt"
$vermontTest = $vermontFolder + "\content.txt"
$hawaiiTest = $hawaiiFolder + "\content.txt"
$oklahomaTest = $oklahomaFolder + "\content.txt"
$idahoTest = $idahoFolder + "\content.txt"
$yonkers1Test = $yonkers1Folder + "\content.txt"
$puertoRicoTest = $puertoRicoFolder + "\content.txt"

$marylandTest = $marylandFolder + "\content.txt"

$kansasTest = $kansasFolder + "\content.txt"
$wisconsinTest = $wisconsinFolder + "\content.txt"
$nebraskaTest = $nebraskaFolder + "\content.txt"

$georgiaTest = $georgiaFolder + "\content.txt"
$illinoisTest = $illinoisFolder + "\content.txt"
$coloradoTest = $coloradoFolder + "\content.txt"

$arizonaTest = $arizonaFolder + "\content.txt"
$arkansasTest = $arkansasFolder + "\content.txt"
$indianaTest = $indianaFolder + "\content.txt"
$louisianaTest = $louisianaFolder + "\content.txt"
$michiganTest = $michiganFolder + "\content.txt"
$mississippiTest = $mississippiaFolder + "\content.txt"
$montanaTest = $montanaFolder + "\content.txt"
$newJerseyTest = $newJerseyFolder + "\content.txt"
$northCarolinaTest = $northCarolinaFolder + "\content.txt"
$ohioTest = $ohioFolder + "\content.txt"
$pennsylvaniaTest = $pennsylvaniaFolder + "\content.txt"
$southCarolinaTest = $southCarolinaFolder + "\content.txt"
$utahTest = $utahFolder + "\content.txt"
$virginiaTest = $virginiaFolder + "\content.txt"
$westVirginiaTest = $westVirginiaFolder + "\content.txt"
$newYorkcityTest = $newYorkcityFolder + "\content.txt"

[string[]]$fileList = $outSuite, $inSuite, $setup

# List of CSV files
$federalCSV = $pathToData + "\CSV\control sheet_Fed-" + $update + ".csv"
$californiaCSV = $pathToData + "\CSV\control sheet_Ca-" + $update + ".csv"
$newYorkCSV = $pathToData + "\CSV\control sheet_Ny-" + $update + ".csv"
$yonkersCSV = $pathToData + "\CSV\control sheet_Nyy-" + $update + ".csv"
$kentuckyCSV = $pathToData + "\CSV\control sheet_Ky-" + $update + ".csv"
$connecticutCSV = $pathToData + "\CSV\control sheet_Ct-" + $update + ".csv"
$maineCSV = $pathToData + "\CSV\control sheet_Me-" + $update + ".csv"
$newMexicoCSV = $pathToData + "\CSV\control sheet_Nm-" + $update + ".csv"
$dcCSV = $pathToData + "\CSV\control sheet_Dc-" + $update + ".csv"
$minnesotaCSV = $pathToData + "\CSV\control sheet_Mn-" + $update + ".csv"
$northDakotaCSV = $pathToData + "\CSV\control sheet_Nd-" + $update + ".csv"
$oregonCSV = $pathToData + "\CSV\control sheet_Or-" + $update + ".csv"
$oregon1CSV = $pathToData + "\CSV\control sheet_Or1-" + $update + ".csv"
$alabamaCSV = $pathToData + "\CSV\control sheet_Al-" + $update + ".csv"
$iowaCSV = $pathToData + "\CSV\control sheet_Ia-" + $update + ".csv"
$missouriCSV = $pathToData + "\CSV\control sheet_Mo-" + $update + ".csv"

$massachusettsCSV = $pathToData + "\CSV\control sheet_Ma-" + $update + ".csv"
$delawareCSV = $pathToData + "\CSV\control sheet_De-" + $update + ".csv"
$rhodeIslandCSV = $pathToData + "\CSV\control sheet_Ri-" + $update + ".csv"
$vermontCSV = $pathToData + "\CSV\control sheet_Vt-" + $update + ".csv"
$hawaiiCSV = $pathToData + "\CSV\control sheet_Hi-" + $update + ".csv"
$oklahomaCSV = $pathToData + "\CSV\control sheet_Ok-" + $update + ".csv"
$idahoCSV = $pathToData + "\CSV\control sheet_Id-" + $update + ".csv"
$yonkers1CSV = $pathToData + "\CSV\control sheet_Yonkers-" + $update + ".csv"
$puertoRicoCSV = $pathToData + "\CSV\control sheet_Pr-" + $update + ".csv"

$marylandCSV = $pathToData + "\CSV\control sheet_Md-" + $update + ".csv"

$kansasCSV = $pathToData + "\CSV\control sheet_Ks-" + $update + ".csv"
$wisconsinCSV = $pathToData + "\CSV\control sheet_Wi-" + $update + ".csv"
$nebraskaCSV = $pathToData + "\CSV\control sheet_Ne-" + $update + ".csv"

$georgiaCSV = $pathToData + "\CSV\control sheet_Ga-" + $update + ".csv"
$illinoisCSV = $pathToData + "\CSV\control sheet_Il-" + $update + ".csv"
$coloradoCSV = $pathToData + "\CSV\control sheet_Co-" + $update + ".csv"

$arizonaCSV = $pathToData + "\CSV\control sheet_Az-" + $update + ".csv"
$arkansasCSV = $pathToData + "\CSV\control sheet_Ar-" + $update + ".csv"
$indianaCSV = $pathToData + "\CSV\control sheet_In-" + $update + ".csv"
$louisianaCSV = $pathToData + "\CSV\control sheet_La-" + $update + ".csv"
$michiganCSV = $pathToData + "\CSV\control sheet_Mi-" + $update + ".csv"
$mississippiCSV = $pathToData + "\CSV\control sheet_Ms-" + $update + ".csv"
$montanaCSV = $pathToData + "\CSV\control sheet_Mt-" + $update + ".csv"
$newJerseyCSV = $pathToData + "\CSV\control sheet_Nj-" + $update + ".csv"
$northCarolinaCSV = $pathToData + "\CSV\control sheet_Nc-" + $update + ".csv"
$ohioCSV = $pathToData + "\CSV\control sheet_Oh-" + $update + ".csv"
$pennsylvaniaCSV = $pathToData + "\CSV\control sheet_Pa-" + $update + ".csv"
$southCarolinaCSV = $pathToData + "\CSV\control sheet_Sc-" + $update + ".csv"
$utahCSV = $pathToData + "\CSV\control sheet_Ut-" + $update + ".csv"
$virginiaCSV = $pathToData + "\CSV\control sheet_Va-" + $update + ".csv"
$westVirginiaCSV = $pathToData + "\CSV\control sheet_Wv-" + $update + ".csv"
$newYorkCityCSV = $pathToData + "\CSV\control sheet_Nyc-" + $update + ".csv"

# Create required folders and files
Create-Directory -List $folderList -Dir
Create-Directory -List $fileList -File

# Populate Outside Suite content file
Add-Content $outSuite "!contents"

# Populate Inside Suite content file
Add-Content $inSuite "!contents"

# Populate setup file
Add-Content $setup "|import|"
Add-Content $setup "|fitnesse.fixtures|"
Add-Content $setup "|com.sage.accpac.automation.payroll.calc.fixture|"

# Create US Tax Update Tests
if (Test-Path $federalCSV)
{
	Create-Directory -List $federalFolder -Dir
	Create-Directory -List $federalTest -File
	Create-Test -CSVFILE $federalCSV -TESTFILE $federalTest -STATE "FED" -DATABASE $database -DATE $run -FIT
}
	
if (Test-Path $californiaCSV)	
{
	Create-Directory -List $californiaFolder -Dir
	Create-Directory -List $californiaTest -File	
	Create-Test -CSVFILE $californiaCSV -TESTFILE $californiaTest -STATE "CA" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $newYorkCSV) 
{
	Create-Directory -List $newYorkFolder -Dir
	Create-Directory -List $newYorkTest -File	
	Create-Test -CSVFILE $newYorkCSV -TESTFILE $newYorkTest -STATE "NY" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $yonkersCSV)
{
	Create-Directory -List $yonkersFolder -Dir
	Create-Directory -List $yonkersTest -File		
	Create-Test -CSVFILE $yonkersCSV -TESTFILE $yonkersTest -STATE "NYY" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $kentuckyCSV)
{
	Create-Directory -List $kentuckyFolder -Dir
	Create-Directory -List $kentuckyTest -File			
	Create-Test -CSVFILE $kentuckyCSV -TESTFILE $kentuckyTest -STATE "KY" -DATABASE $database -DATE $run -SIT
}
		
if (Test-Path $connecticutCSV)
{
	Create-Directory -List $connecticutFolder -Dir
	Create-Directory -List $connecticutTest -File				
	Create-Test -CSVFILE $connecticutCSV -TESTFILE $connecticutTest -STATE "CT" -DATABASE $database -DATE $run -SIT
}
		
if (Test-Path $maineCSV)
{
	Create-Directory -List $maineFolder -Dir
	Create-Directory -List $maineTest -File					
	Create-Test -CSVFILE $maineCSV -TESTFILE $maineTest -STATE "ME" -DATABASE $database -DATE $run -SIT
}
			
if (Test-Path $newMexicoCSV)
{
	Create-Directory -List $newMexicoFolder -Dir
	Create-Directory -List $newMexicoTest -File						
	Create-Test -CSVFILE $newMexicoCSV -TESTFILE $newMexicoTest -STATE "NM" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $dcCSV)
{
	Create-Directory -List $dcFolder -Dir
	Create-Directory -List $dcTest -File							
	Create-Test -CSVFILE $dcCSV -TESTFILE $dcTest -STATE "DC" -DATABASE $database -DATE $run -SIT
}
	
if (Test-Path $minnesotaCSV)
{
	Create-Directory -List $minnesotaFolder -Dir
	Create-Directory -List $minnesotaTest -File								
	Create-Test -CSVFILE $minnesotaCSV -TESTFILE $minnesotaTest -STATE "MN" -DATABASE $database -DATE $run -SIT
}
	
if (Test-Path $northDakotaCSV)
{
	Create-Directory -List $northDakotaFolder -Dir
	Create-Directory -List $northDakotaTest -File									
	Create-Test -CSVFILE $northDakotaCSV -TESTFILE $northDakotaTest -STATE "ND" -DATABASE $database -DATE $run -SIT
}
	
if (Test-Path $oregonCSV)
{
	Create-Directory -List $oregonFolder -Dir
	Create-Directory -List $oregonTest -File										
	Create-Test -CSVFILE $oregonCSV -TESTFILE $oregonTest -STATE "OR" -DATABASE $database -DATE $run -SIT
}
	
if (Test-Path $oregon1CSV)
{
	Create-Directory -List $oregon1Folder -Dir
	Create-Directory -List $oregon1Test -File											
	Create-Test -CSVFILE $oregon1CSV -TESTFILE $oregon1Test -STATE "OR1" -DATABASE $database -DATE $run -SIT
}
	
if (Test-Path $alabamaCSV)
{
	Create-Directory -List $alabamaFolder -Dir
	Create-Directory -List $alabamaTest -File												
	Create-Test -CSVFILE $alabamaCSV -TESTFILE $alabamaTest -STATE "AL" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $iowaCSV)
{
	Create-Directory -List $iowaFolder -Dir
	Create-Directory -List $iowaTest -File													
	Create-Test -CSVFILE $iowaCSV -TESTFILE $iowaTest -STATE "IA" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $missouriCSV) 
{
	Create-Directory -List $missouriFolder -Dir
	Create-Directory -List $missouriTest -File														
	Create-Test -CSVFILE $missouriCSV -TESTFILE $missouriTest -STATE "MO" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $massachusettsCSV) 
{
	Create-Directory -List $massachusettsFolder -Dir
	Create-Directory -List $massachusettsTest -File															
	Create-Test -CSVFILE $massachusettsCSV -TESTFILE $massachusettsTest -STATE "MA" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $delawareCSV) 
{
	Create-Directory -List $delawareFolder -Dir
	Create-Directory -List $delawareTest -File																
	Create-Test -CSVFILE $delawareCSV -TESTFILE $delawareTest -STATE "DE" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $rhodeIslandCSV) 
{
	Create-Directory -List $rhodeIslandFolder -Dir
	Create-Directory -List $rhodeIslandTest -File																	
	Create-Test -CSVFILE $rhodeIslandCSV -TESTFILE $rhodeIslandTest -STATE "RI" -DATABASE $database -DATE $run -SIT
}	
	
if (Test-Path $vermontCSV) 
{
	Create-Directory -List $vermontFolder -Dir
	Create-Directory -List $vermontTest -File																		
	Create-Test -CSVFILE $vermontCSV -TESTFILE $vermontTest -STATE "VT" -DATABASE $database -DATE $run -SIT
}	

if (Test-Path $hawaiiCSV) 
{
	Create-Directory -List $hawaiiFolder -Dir
	Create-Directory -List $hawaiiTest -File																			
	Create-Test -CSVFILE $hawaiiCSV -TESTFILE $hawaiiTest -STATE "HI" -DATABASE $database -DATE $run -SIT
}
	
if (Test-Path $oklahomaCSV) 
{
	Create-Directory -List $oklahomaFolder -Dir
	Create-Directory -List $oklahomaTest -File																				
	Create-Test -CSVFILE $oklahomaCSV -TESTFILE $oklahomaTest -STATE "OK" -DATABASE $database -DATE $run -SIT
}
	
if (Test-Path $idahoCSV) 
{
	Create-Directory -List $idahoFolder -Dir
	Create-Directory -List $idahoTest -File		
	Create-Test -CSVFILE $idahoCSV -TESTFILE $idahoTest -STATE "ID" -DATABASE $database -DATE $run -SIT
}
	
if (Test-Path $yonkers1CSV) 
{
	Create-Directory -List $yonkers1Folder -Dir
	Create-Directory -List $yonkers1Test -File		
	Create-Test -CSVFILE $yonkers1CSV -TESTFILE $yonkers1Test -STATE "Yonkers" -DATABASE $database -DATE $run -SIT
}
	
if (Test-Path $puertoRicoCSV) 
{
	Create-Directory -List $puertoRicoFolder -Dir
	Create-Directory -List $puertoRicoTest -File			
	Create-Test -CSVFILE $puertoRicoCSV -TESTFILE $puertoRicoTest -STATE "PR" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $marylandCSV) 
{
	Create-Directory -List $marylandFolder -Dir
	Create-Directory -List $marylandTest -File		
	Create-Test -CSVFILE $marylandCSV -TESTFILE $marylandTest -STATE "MD" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $kansasCSV) 
{
	Create-Directory -List $kansasFolder -Dir
	Create-Directory -List $kansasTest -File		
	Create-Test -CSVFILE $kansasCSV -TESTFILE $kansasTest -STATE "KS" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $wisconsinCSV) 
{
	Create-Directory -List $wisconsinFolder -Dir
	Create-Directory -List $wisconsinTest -File		
	Create-Test -CSVFILE $wisconsinCSV -TESTFILE $wisconsinTest -STATE "WI" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $nebraskaCSV) 
{
	Create-Directory -List $nebraskaFolder -Dir
	Create-Directory -List $nebraskaTest -File		
	Create-Test -CSVFILE $nebraskaCSV -TESTFILE $nebraskaTest -STATE "NE" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $georgiaCSV) 
{
	Create-Directory -List $georgiaFolder -Dir
	Create-Directory -List $georgiaTest -File		
	Create-Test -CSVFILE $georgiaCSV -TESTFILE $georgiaTest -STATE "GA" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $illinoisCSV) 
{
	Create-Directory -List $illinoisFolder -Dir
	Create-Directory -List $illinoisTest -File		
	Create-Test -CSVFILE $illinoisCSV -TESTFILE $illinoisTest -STATE "IL" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $coloradoCSV) 
{
	Create-Directory -List $coloradoFolder -Dir
	Create-Directory -List $coloradoTest -File		
	Create-Test -CSVFILE $coloradoCSV -TESTFILE $coloradoTest -STATE "CO" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $arizonaCSV) 
{
	Create-Directory -List $arizonaFolder -Dir
	Create-Directory -List $arizonaTest -File		
	Create-Test -CSVFILE $arizonaCSV -TESTFILE $arizonaTest -STATE "AZ" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $arkansasCSV) 
{
	Create-Directory -List $arkansasFolder -Dir
	Create-Directory -List $arkansasTest -File		
	Create-Test -CSVFILE $arkansasCSV -TESTFILE $arkansasTest -STATE "AR" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $indianaCSV) 
{
	Create-Directory -List $indianaFolder -Dir
	Create-Directory -List $indianaTest -File		
	Create-Test -CSVFILE $indianaCSV -TESTFILE $indianaTest -STATE "IN" -DATABASE $database -DATE $run -SIT
}


if (Test-Path $louisianaCSV) 
{
	Create-Directory -List $louisianaFolder -Dir
	Create-Directory -List $louisianaTest -File		
	Create-Test -CSVFILE $louisianaCSV -TESTFILE $louisianaTest -STATE "LA" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $michiganCSV) 
{
	Create-Directory -List $michiganFolder -Dir
	Create-Directory -List $michiganTest -File		
	Create-Test -CSVFILE $michiganCSV -TESTFILE $michiganTest -STATE "MI" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $mississippiCSV) 
{
	Create-Directory -List $mississippiFolder -Dir
	Create-Directory -List $mississippiTest -File		
	Create-Test -CSVFILE $mississippiCSV -TESTFILE $mississippiTest -STATE "MS" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $montanaCSV) 
{
	Create-Directory -List $montanaFolder -Dir
	Create-Directory -List $montanaTest -File		
	Create-Test -CSVFILE $montanaCSV -TESTFILE $montanaTest -STATE "MT" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $newJerseyCSV) 
{
	Create-Directory -List $newjerseyFolder -Dir
	Create-Directory -List $newJerseyTest -File		
	Create-Test -CSVFILE $newJerseyCSV -TESTFILE $newJerseyTest -STATE "NJ" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $northCarolinaCSV) 
{
	Create-Directory -List $northCarolinaFolder -Dir
	Create-Directory -List $northCarolinaTest -File		
	Create-Test -CSVFILE $northCarolinaCSV -TESTFILE $northCarolinaTest -STATE "NC" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $ohioCSV) 
{
	Create-Directory -List $ohioFolder -Dir
	Create-Directory -List $ohioTest -File		
	Create-Test -CSVFILE $ohioCSV -TESTFILE $ohioTest -STATE "OH" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $pennsylvaniaCSV) 
{
	Create-Directory -List $pensylvaniaFolder -Dir
	Create-Directory -List $pennsylvaniaTest -File		
	Create-Test -CSVFILE $pennsylvaniaCSV -TESTFILE $pennsylvaniaTest -STATE "PA" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $southCarolinaCSV) 
{
	Create-Directory -List $southCarolinaFolder -Dir
	Create-Directory -List $southCarolinaTest -File		
	Create-Test -CSVFILE $southCarolinaCSV -TESTFILE $southCarolinaTest -STATE "SC" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $utahCSV) 
{
	Create-Directory -List $utahFolder -Dir
	Create-Directory -List $utahTest -File		
	Create-Test -CSVFILE $utahCSV -TESTFILE $utahTest -STATE "UT" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $virginiaCSV) 
{
	Create-Directory -List $virginiaFolder -Dir
	Create-Directory -List $virginiaTest -File		
	Create-Test -CSVFILE $virginiaCSV -TESTFILE $virginiaTest -STATE "VA" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $westVirginiaCSV) 
{
	Create-Directory -List $westVirginiaFolder -Dir
	Create-Directory -List $westvirginiaTest -File		
	Create-Test -CSVFILE $westVirginiaCSV -TESTFILE $westVirginiaTest -STATE "WV" -DATABASE $database -DATE $run -SIT
}

if (Test-Path $newYorkCityCSV) 
{
	Create-Directory -List $newYorkCityFolder -Dir
	Create-Directory -List $newYorkCityTest -File		
	Create-Test -CSVFILE $newYorkCityCSV -TESTFILE $newYorkCityTest -STATE "NYC" -DATABASE $database -DATE $run -SIT
}
