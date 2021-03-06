###############################################################################
#.Synopsis
# Parameters from the Command Line
#.Parameter -CONTROLSHEET
# Control Sheet name and extension only
#.Parameter -COUNTRY
# Country for this control sheet (Canada or USA)
#.Parameter -DATAPATH
# Full path to the Data folder where the control sheet is located
#.Parameter -DATE
# The Tax Date (Only for US Control Sheet)
###############################################################################
Param([alias("CONTROLSHEET")]$excelFileName,[alias("COUNTRY")]$aCountry,[alias("DATAPATH")]$pathToData, [alias("DATE")]$taxDate)

function Release-Reference
{
###############################################################################
#.Synopsis
# Release the reference by calling GC on it
#.Parameter reference
# The reference being release
###############################################################################
    [CmdletBinding()]
    Param(
    [parameter(Mandatory=$true)]
    [alias("REF")]
    [System.__ComObject]$reference)
    
    Process
    {
        [System.Runtime.InteropServices.Marshal]::ReleaseComObject([System.__ComObject]$reference) -gt 0        
        [System.GC]::Collect()
        [System.GC]::WaitForPendingFinalizers() 
    }
}

function Get-NewCSVName
{
###############################################################################
#.Synopsis
# Gets the name of the new CSV file for a given State 
#.Parameter -EXCELPATH
# Full path to the excel file
#.Parameter -WORKSHEET
# Worksheet currently being processed
###############################################################################
    [CmdletBinding()]
    Param(
    [parameter(Mandatory=$true)]
    [alias("EXCELPATH")]
    [String]$excelFilePath,

    [parameter(Mandatory=$false)]
    [alias("DATE")]
    [String]$taxDate,
    
    [parameter(Mandatory=$true)]
    [alias("WORKSHEET")]
    [String]$sheet)
    
    Process
    {
		if ($taxDate)
		{
			$onlyPath = Split-Path $excelFilePath -Parent
			$onlyName = Get-ChildItem $excelFilePath | ForEach-Object {$_.BaseName}
			$csvFile = $onlyPath + "\CSV\" + $onlyName + "_" + $sheet + "-" + $taxDate + ".csv"
		}
		else
		{
			$onlyPath = Split-Path $excelFilePath -Parent
			$onlyName = Get-ChildItem $excelFilePath | ForEach-Object {$_.BaseName}
			$csvFile = $onlyPath + "\CSV\" + $onlyName + "_" + $sheet + ".csv"
		}
		
        $csvFile
    }
}

function Process-CanadianWorkSheet
{
###############################################################################
#.Synopsis
# Process a Canadian Excel Work Sheet
#.Parameter -TYPE
# The worksheet to be processed
#.Parameter -WORKSHEETS
# The Excel COM object representing all worksheets
#.Parameter -CSV
# The resulting CSV file path
###############################################################################
    [CmdletBinding()]
    Param(
    [parameter(Mandatory=$true)]
    [alias("TYPE")]
    [String]$worksheet,
    
    [parameter(Mandatory=$true)]
    [alias("WORKSHEETS")]
    [System.__ComObject]$worksheetList,
    
    [parameter(Mandatory=$true)]
    [alias("CSV")]
    [String]$csvFile)
    
    Process
    {
         # Create new CSV file for current state     
        New-Item -Path $csvFile -type file -Force

        # Acumulates the comma separated fields for current line
        $currentLine = ""
        
        # Canadian worksheets' headers start at row 1.
        $headerRow = 1
                
        # Get the worksheet for an specific state        
        $sheet = $worksheetList | where {$_.name -eq $worksheet}
        
        # row and column count    
        $rowCount = $sheet.UsedRange.Rows.Count            
        $columnCount = $sheet.Usedrange.Columns.Count
		
		# Real Columns (columnCounter) are less than columns reported by
		# Excel Objetc (columnCount). Use columnCoungter to break inner loop
		$columnCounter = 0
		
		# We stop when it is true
		$lastEmployeeProcessed = $false		

		# Nested For to iterate cell by cell                    
        for ($row=1; $row -le $rowCount; $row++)
        {
            # Reset current line
            $currentLine = ""
                    
            for ($column=1; $column -le $columnCount; $column++)
            {
                # Check header first                                
                if ($row -eq 1)
                {
			        # Header Cell is not empty
                    if ($sheet.Cells.Item($row,$column).Value2)
                    {
						if ($sheet.Cells.Item($row,$column).Value2 -eq "Prov")
						{
							if ($column -le 5)
							{
								$currentLine = $currentLine + "Province" + ","
								
							}
							else
							{
								$currentLine = $currentLine + "Prov tax" + ","
							}
						}
						elseIf ($sheet.Cells.Item($row,$column).Value2 -eq "QPP/CPP")
						{
							$currentLine = $currentLine + "CPP/QPP" + ","
						}
						elseIf ($sheet.Cells.Item($row,$column).Value2 -eq "QPP")
						{
							$currentLine = $currentLine + "CPP/QPP" + ","
						}						
						elseIf ($sheet.Cells.Item($row,$column).Value2 -eq "QHSF/NT/NU")
						{
							$currentLine = $currentLine + "QHSF/NT tax" + ","
						}	
						elseIf ($sheet.Cells.Item($row,$column).Value2 -eq "Frequency")
						{
							$currentLine = $currentLine + "P" + ","
						}							
						elseIf ($sheet.Cells.Item($row,$column).Value2 -eq "Federal")
						{
							$currentLine = $currentLine + "Fed tax" + ","
						}							
						elseIf ($sheet.Cells.Item($row,$column).Value2 -eq "E")
						{
							if ($column -gt 10)
							{
								$currentLine = $currentLine + "ETwo" + ","
							}
							else
							{
								$currentLine = $currentLine + "E" + ","
							}
						}						
						else						
						{
                        	$currentLine = $currentLine + $sheet.Cells.Item($row,$column).Value2 + ","
						}
                    }
                    else #Header cell is empty - assign a fake one
                    {
                        $currentLine = $currentLine + "Empty" + $column + ","                            
                    }
						
                    # There are more Columns than it should - break when "Net" is found
                    if ($sheet.Cells.Item($row,$column).Value2 -like "QHSF/NT*")
                    {						
						# It reached the last column - break inner For loop
						$columnCounter++
						break						
                    }
					
					$columnCounter++
                }
				else
				{
					# When column 1 is empty we are done with employees
					if ($column -eq 1)
					{
						if (!$sheet.Cells.Item($row,$column).Value2)
						{
							# It reached the last employee - break inner loop
							$lastEmployeeProcessed = $true
							break
						}					
					}
					
					# We are done with real columns
					if ($column -gt $columnCounter)
					{
						break
					}
					
		            # If the cell is a double we need to round using 2 digits
                    if ($sheet.Cells.Item($row,$column).Value2 -is [double])
                    {
	                    $currentLine = $currentLine + [Math]::Round($sheet.Cells.Item($row,$column).Value2, 2, [MidpointRounding]::AwayFromZero) + ","
                    }
					# If the cell is empty
                    elseif(!$sheet.Cells.Item($row,$column).Value2)
                    {
    	                $currentLine = $currentLine + "" + ","
                    }
                    # If the cell has ","
                    elseif($sheet.Cells.Item($row,$column).Value2.Contains(","))
                    {						
    	                $currentLine = $currentLine + $sheet.Cells.Item($row,$column).Value2.Replace(",","=") + ","
                    }					
                    else
                    {
        	            $currentLine = $currentLine + $sheet.Cells.Item($row,$column).Value2 + ","
            	    }																					
				}				
			}
			
			# Don't go beyond the last employee
			if (!$lastEmployeeProcessed)
			{
				# Delete the last comma before writing to file   
				Add-Content $csvFile ($currentLine -replace ".$")
			}
			else
			{
				# break outer loop
				break
			}			                    
		}
	}	
}	

function Process-UsWorkSheet
{
###############################################################################
#.Synopsis
# Process a US Excel Work Sheet
#.Parameter -STATE
# The worksheet to be processed
#.Parameter -WORKSHEETS
# The Excel COM object representing all worksheets
#.Parameter -CSV
# The resulting CSV file path
###############################################################################
    [CmdletBinding()]
    Param(
    [parameter(Mandatory=$true)]
    [alias("STATE")]
    [String]$anState,
    
    [parameter(Mandatory=$true)]
    [alias("WORKSHEETS")]
    [System.__ComObject]$worksheetList,
    
    [parameter(Mandatory=$true)]
    [alias("CSV")]
    [String]$csvFile)
    
    Process
    {
        # NY, NYY, DC, and OR have these strings in the first column
        # after employee names 
        $blackList = "Test supplemental rate", "Test resident supplemental", " ", "Note:", "Supplemental withholding test", "Legend",`
                   "This is for testing the supplemental withholding rate of 7.4% previously it was 7.8%.",`
				   "Note this case is for testing the supplemental withholding (lump sum distibution)"
        # Acumulates the comma separated fields for current line
        $currentLine = ""
        
        # US worksheets' headers do not start at row 1.
        $headerRow = 0
                
        # Get the worksheet for an specific state        
        $sheet = $worksheetList | where {$_.name -eq $anState}
        
		if ($sheet)
		{
			# row and column count    
			$rowCount = $sheet.UsedRange.Rows.Count            
			$columnCount = $sheet.Usedrange.Columns.Count

			# Do not start until header is identified
			$start = $FALSE
			
			# Create new CSV file for current state     
			New-Item -Path $csvFile -type file -Force
			
			# Nested For to iterate cell by cell                    
			for ($row=1; $row -le $rowCount; $row++)
			{
				# Reset current line
				$currentLine = ""
						
				for ($column=1; $column -le $columnCount; $column++)
				{
					# Check if first header is found                                
					if ($column -eq 1)
					{
						# This is hard coded - "Case #" should always be the first header
						if (($sheet.Cells.Item($row,$column).Value2 -eq "Case #") -or ($sheet.Cells.Item($row,$column).Value2 -eq "Number"))
						{
							# Header row is found, we can start
							$start = $TRUE
							$headerRow = $row
						}
					}
						
					# Enter only if header was found    
					if ($start)
					{
						# This branch processes only the header
						if  ($row -eq $headerRow)
						{
							# Header Cell is not empty
							if ($sheet.Cells.Item($row,$column).Value2)
							{
								# Yonkers has a column header that breaks the CSV format
								if ($sheet.Cells.Item($row,$column).Value2 -eq "(S,M)")
								{
									$currentLine = $currentLine + "SM" + ","
								}
								#Massachusetts has two colums with unexpected names
								elseif($sheet.Cells.Item($row,$column).Value2 -eq "Number")
								{
									$currentLine = $currentLine + "Case #" + ","
								}
								elseif($sheet.Cells.Item($row,$column).Value2 -eq "revised")
								{
									$currentLine = $currentLine + "SIT/PP" + ","
								}
								else
								{
									$currentLine = $currentLine + $sheet.Cells.Item($row,$column).Value2 + ","
								}
							}
							else #Header cell is emty - assign a fake one
							{
								$currentLine = $currentLine + "Empty" + $column + ","                            
							}
						}
						else
						{
							# There are some empty lines, only enter if cell one is not empty
							if ($sheet.Cells.Item($row,1).Value2)
							{
								# Make sure the cell value in column one is not in the black list
								if ($blackList -notcontains $sheet.Cells.Item($row,1).Value2)
								{
									# If the cell is a double we need to round using 2 digits
									if ($sheet.Cells.Item($row,$column).Value2 -is [double])
									{
										$currentLine = $currentLine + [Math]::Round($sheet.Cells.Item($row,$column).Value2, 2, [MidpointRounding]::AwayFromZero) + ","
									}
									elseif(!$sheet.Cells.Item($row,$column).Value2)
									{
										$currentLine = $currentLine + "" + ","
									}
									else
									{
										$currentLine = $currentLine + $sheet.Cells.Item($row,$column).Value2 + ","
									}
								}
							}
						}
						  
					}
				}
				
				# Before going to the next row, write current line to file       
				if ($start)
				{       
					if ($currentLine)
					{         
						# Delete the last comma before writing to file   
						Add-Content $csvFile ($currentLine -replace ".$")
					}
				}
						
			}
		}	
            
        # Release reference before leaving
		if ($sheet)
		{
			Release-Reference -REF $sheet    
		}
    }    
}
    
function Process-ControlSheet
{
###############################################################################
#.Synopsis
# Process an Excel Control Sheet
#.Parameter -PATH
# The Excel Control Sheet full path
#.Parameter -STATELIST
# US worksheet names (as they appear in the Control Sheet) 
#.Parameter -DATE
# The Tax Date (Only for US Control Sheet)
#.Parameter -TYPELIST
# Canadian worksheet names (as they appear in the Control Sheet)
###############################################################################
    [CmdletBinding()]
    Param(
    [parameter(Mandatory=$true)]
    [alias("PATH")]
    [String]$excelFilePath,
    
    [parameter(Mandatory=$false)]
    [alias("STATELIST")]
    [String[]]$states,

    [parameter(Mandatory=$false)]
    [alias("DATE")]
    [String]$taxDate,
	
	[parameter(Mandatory=$false)]
    [alias("TYPELIST")]
    [String[]]$testTypes)
    
    Process
    {
        # Create Excel COM Objects
        $excel = New-Object -ComObject Excel.Application            
        $workBooks = $excel.Workbooks            
        $workBook = $workBooks.Open($excelFilePath)            
        $workSheets = $workBook.Worksheets
        
		# US Worksheets
        if ($states)
		{
	        Foreach($state in $states)
	        {
	            $newCSVFile = Get-NewCSVName -EXCELPATH $excelFilePath -WORKSHEET $state -DATE $taxDate
	            Process-UsWorkSheet -STATE $state -WORKSHEETS $workSheets -CSV $newCSVFile                
	        }
		}
		
		# Canadian Worksheets
        if ($testTypes)
		{
	        Foreach($type in $testTypes)
	        {
	            $newCSVFile = Get-NewCSVName -EXCELPATH $excelFilePath -WORKSHEET $type
	            Process-CanadianWorkSheet -TYPE $type -WORKSHEETS $workSheets -CSV $newCSVFile                
	        }
		}
		
        
        # Release all references to COM objects    
        Release-Reference -REF $workSheets                      
        $workBook.Close($false) # $false = Don't ask anything on closing
        Release-Reference -REF $workBook
        Release-Reference -REF $workBooks            
        $excel.Quit()            
        Release-Reference -REF $excel 
            
        # Kill excel process    
        spps -n EXCEL
    }
}

###############################################################################
#.Synopsis
# Program starts here
###############################################################################

# To test inside PowerShell IDE
#$excelFilePath = "C:\?\Data\Control sheet.xls"
#$excelFilePath = "C:\?\Data\consolidated_95h.xls"
#$excelFilePath = "C:\?\Data\consolidated_94a.xls"
#$country = "Canada"
#$country = "USA"
#$taxDate = "2012-01-31"
#$states = ("FED", "CA", "NY")
#$testTypes = ("Generic", "Maximum")

$excelFilePath = $pathToData + "\" + $excelFileName

# List of worksheets in the US and Canada Control Sheets
$states = ("FED", "CA", "NY", "NYY", "NYC", "KY", "CT", "ME", "NM", "DC", "MN", "ND", "OR", "OR1", "AL", "IA", "MO", `
"MA", "DE", "RI", "VT", "HI", "OK", "ID", "Yonkers", "PR", "MD", "KS", "WI", "NE", "GA", "IL", "CO", "AZ", "AR", "IN", "LA", "MI", `
"MS", "MT", "NJ", "NC", "OH", "PA", "SC", "UT", "VA", "WV")

$testTypes = ("Generic", "Maximum", "Specific", "Commission", "Quebec", "Benefits")


if ((Test-Path $excelFilePath) -and ($excelFilePath -match ".xls"))
{
	if ($aCountry.CompareTo("USA") -eq 0)
	{
    	Process-ControlSheet -PATH $excelFilePath -STATELIST $states -DATE $taxDate
	}
	elseIf ($aCountry.CompareTo("Canada") -eq 0)
	{
		Process-ControlSheet -PATH $excelFilePath -TYPELIST $testTypes
	}
	else
	{
		Write-Host "$aCountry : is not a valid Country"
	}
}
else
{
    Write-Host "File $excelFilePath not found"
}    

