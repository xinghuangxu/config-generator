SELECT 


td_Cycle.CY_CYCLE, 

td_TEST.TS_NAME, 

IIf([TS_USER_09] Is Null,"**Missing**",[TS_USER_09]) AS [TC Type], 

IIf([TS_USER_02] Is Null,0,IIf(InStr([TS_USER_02],":")<2,(Right([TS_USER_02],2)/60),Left([TS_USER_02],InStr([TS_USER_02],":")-1)+(Right([TS_USER_02],2)/60))) AS [Plan Dur],


Trim([TC_STATUS]) AS Status, 

td_TESTCYCL.TC_EXEC_DATE AS [Ex Date], 

Trim([TC_USER_04]) AS [Cur Status], 

Trim(IIf([TC_USER_02] Is Null,"",[TC_USER_02])) AS [Ex Status] INTO TotalTestCases


FROM td_Cycle INNER JOIN (td_TESTCYCL INNER JOIN td_TEST ON td_TESTCYCL.TC_TEST_ID = td_TEST.TS_TEST_ID) ON td_Cycle.CY_CYCLE_ID = td_TESTCYCL.TC_CYCLE_ID
WHERE (((IIf([TS_USER_09] Is Null,"**Missing**",[TS_USER_09]))<>"Setup" And (IIf([TS_USER_09] Is Null,"**Missing**",[TS_USER_09]))<>"Overhead" And (IIf([TS_USER_09] Is Null,"**Missing**",[TS_USER_09]))<>"Generic") AND ((Trim([TC_STATUS]))<>"N/A") AND ((Trim(IIf([TC_USER_02] Is Null,"",[TC_USER_02])))<>"Obsolete" And (Trim(IIf([TC_USER_02] Is Null,"",[TC_USER_02])))<>"Generic") AND ((td_TEST.TS_STATUS)<>"Obsolete" And (td_TEST.TS_STATUS)<>"Delete"));



/*

Boxcar 

Test Set: td.CYCLE CY_CYCLE

Test Case: td.Test TS_NAME

Type: td.Test TS_USER_05

Level:

Config:

TC Type: td.Test TS_USER_09

Plan Dur: td.Test TS_USER_02

Status: td.TestCycl TC_STATUS

Ex Date: TC_EXEC_DATE

Cur Status: td.TestCycl TC_USER_04

Ex Status: TC_USER_02

Run: 

Passed:

Failed:


*/