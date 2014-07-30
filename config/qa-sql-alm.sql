/****** Script for SelectTopNRows command from SSMS  ******/
SELECT ts.TS_USER_05 as 'Type','' as 'Level', cyc.CY_CYCLE as 'Test Set', ts.TS_NAME as 'Test Case', ts.TS_USER_02 as 'Plan Dur', ts.TS_USER_09 as 'TC Type',
 tscyc.TC_STATUS as 'Status', tscyc.TC_EXEC_DATE as 'Ex Date', tscyc.TC_USER_04 as 'Cur Status',
tscyc.TC_USER_02 as 'Ex Status' 
  FROM [pct_producttest_db].[td].[CYCLE] as cyc
  left join [pct_producttest_db].[td].[TESTCYCL] as tscyc on cyc.[CY_CYCLE_ID]=tscyc.[TC_CYCLE_ID]
  left join [pct_producttest_db].[td].[TEST] as ts on ts.[TS_TEST_ID]=tscyc.[TC_TEST_ID]
  WHERE cyc.[CY_CYCLE] like '%Denali2%'