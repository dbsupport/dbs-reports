#
# RAP12 /  PRZECIĘTNE ZATRUDNIENIE W   M-CU  (WEDŁUG  ŚREDNIEJ ARYTMETYCZNEJ)                   
#

VERSION 2

INIT: 
SELECT
 NVL('^$IN_FIRM^','AUCHAN') INFIRMA, 
 NVL('^$IN_MIES^',TO_CHAR(SYSDATE,'RRRRMM')) PV_MIES,
 NVL('^$IN_IMIE^','Jaroslaw') PV_IMIE,
 NVL('^$IN_NAZWISKO^','Kowalczyk') PV_NAZWISKO,
 TO_CHAR(SYSDATE,'YYYY-MM-DD') V_DATA_W,
 '<TD>0.000</TD><TD>0.000</TD>' C_POL258,
 '<td><b>0.000</b></td><td><b>0.000</b></td>'  SUM_POL258
FROM DUAL; 

FIRMA: 
SELECT
  NIP F_NIP,
  REGON F_REGON,
  NAZWA_FIRMY F_NAZWA_FIRMY,
  KRAJ F_KRAJ,
  WOJEWODZTWO F_WOJEWODZTWO,
  POWIAT F_POWIAT,
  GMINA F_GMINA,
  ULICA F_ULICA,
  NR_DOMU F_NR_DOMU,
  NR_LOKALU F_NR_LOKALU,
  MIEJSCOWOSC F_MIEJSCOWOSC,
  KOD_POCZTOWY F_KOD_POCZTOWY,
  POCZTA F_POCZTA,
 SUBSTR('^$PV_MIES^',1,4)||'-'||SUBSTR('^$PV_MIES^',5,2) V_MIES,
 TOMEK.GENRAP_DDL('DECLARE A NUMBER; BEGIN A:=TOMEK.FRAP121_ALL('||SUBSTR('^$PV_MIES^',1,4)||','||SUBSTR('^$PV_MIES^',5,2)||','||'''^$IN_FIRM^'''||'); END;') wyniki
FROM TOMEK.FIRMY
WHERE FIRMA = '^$IN_FIRM^';

ALL_LOKAL:
SELECT TRIM(B.LOKALIZACJA) V_LOKAL 
  FROM TOMEK.LOKALIZACJE B
 WHERE B.FIRMA = '^$IN_FIRM^'
   ORDER BY 1;

DZIEN: 
SELECT DISTINCT YEAR||LPAD(MNTH,2,0)||LPAD(DAY,2,0) V_DATA
FROM TOMEK.RAP12_1_ALL
WHERE YEAR||LPAD(MNTH,2,0) = '^$PV_MIES^' 
ORDER BY 1;

LOKAL_DAT: 
SELECT '<TR align="center"><TD>'||DAY||'</TD><TD>'||LTRIM(TO_CHAR(ETATY,'99990D999'))||'</TD><TD>'||LTRIM(TO_CHAR(OSOBY,'99990D999'))||'</TD></TR>'  C_LOKAL_DAT
FROM TOMEK.RAP12_1_ALL
WHERE YEAR||LPAD(MNTH,2,0)||LPAD(DAY,2,0) = ^$V_DATA^
  AND LOK = '^$V_LOKAL^';
  
LOKAL_DAT_AVG: 
SELECT '<td><b>SUMA</b></td><td><b>'||LTRIM(TO_CHAR(ETATY,'999990D999'))||'</b></td><td><b>'||LTRIM(TO_CHAR(OSOBY,'999990D999'))||'</b></td>'  SUM_LOKAL
FROM TOMEK.RAP12_2_ALL
WHERE YEAR||LPAD(MNTH,2,0) = '^$PV_MIES^'
  AND LOK = '^$V_LOKAL^';  
    
AUCHAN_AVG: 
SELECT '<td><b>'||LTRIM(TO_CHAR(C,'999990D999'))||'</b></td><td><b>'||LTRIM(TO_CHAR(D,'999990D999'))||'</b></td>'  SUM_AUCHAN
FROM TOMEK.RAP12_3_ALL A
WHERE A.YEAR||LPAD(A.MNTH,2,'0') = '^$PV_MIES^'
  AND A.FIRMA = '^$IN_FIRM^'
;
