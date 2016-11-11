/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hamcrest.text.IsEqualIgnoringWhiteSpace;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import pl.com.dbs.reports.api.report.ReportType;
import pl.com.dbs.reports.api.report.pattern.PatternFactory;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternValidator;
import pl.com.dbs.reports.profile.dao.ProfileDao;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.ReportFactoryDefaultTest;
import pl.com.dbs.reports.report.domain.ReportFactoryDefault;
import pl.com.dbs.reports.report.pattern.domain.PatternFactoryDefault;
import pl.com.dbs.reports.report.pattern.domain.PatternProduceContextDefault;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternFormat;
import pl.com.dbs.reports.security.domain.SessionContext;

/**
 * Raport generation tests.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SessionContext.class)
public class ReportTextBlockBuilderTest {
	private ProfileDao profileDao;
	private ReportFactoryDefault rfactory;
	private PatternFactory pfactory;
	private Profile profile; 
	
	@Before
	public void doBeforeEachTestCase() {
		MockitoAnnotations.initMocks(this);
		profileDao = mock(ProfileDao.class);
		profile = mock(Profile.class);
		when(profile.isGlobal()).thenReturn(true);
		when(profile.getUuid()).thenReturn("1");
		
		PowerMockito.mockStatic(SessionContext.class);
	    PowerMockito.when(SessionContext.getProfile()).thenReturn(profile);		
		
		pfactory = new PatternFactoryDefault(profileDao, rfactory, Collections.<PatternValidator>emptyList());
		
		when(profileDao.find(anyLong())).thenReturn(profile);
		
	}
	
	@After  
    public void tearDown() {  
		rfactory = null;  
		pfactory = null;
    } 	

	
	@Test
	public void zbiorowka_prdb_struktura_blokow() throws PatternValidationException, ReportBlockException {
		File file = read("pl/com/dbs/reports/domain/builders/test/Zbiorowka PRDB/Zbiorowka PRDB.zip");
		PatternProduceContextDefault pcontext = mock(PatternProduceContextDefault.class);
		when(pcontext.getFile()).thenReturn(file);
		ReportPattern pattern = (ReportPattern)pfactory.produce(pcontext);
		
		PatternFormat format = new ReportPatternFormat("test.txt", ReportType.TXT, "txt");
//		when(format.getPatternExtension()).thenReturn("txt");
//		when(format.getReportExtension()).thenReturn("txt");
//		when(format.getReportType()).thenReturn(ReportType.TXT);
		
		
		ReportTextBlocksBuilder builder = new ReportTextBlocksBuilder(pattern.getTransformate(format));
		ReportTextBlock root = builder.build().getRootBlock();
		
		//..first level..
		LinkedList<ReportTextBlock> level1 = root.getBlocks();
		assertNotNull(level1);
		assertTrue(level1.size() == 3);
		
		ReportTextBlock block1_1 = level1.get(0);
		assertNotNull(block1_1);
		assertTrue(block1_1.getLabel().equalsIgnoreCase("INIT"));
		assertNotNull(block1_1.getInflation());
		assertTrue(block1_1.getInflation().getLabel().equalsIgnoreCase("INIT"));
		assertTrue(block1_1.getInflation().input.size()==5);
		assertTrue(block1_1.getInflation().input.contains("IN_DATA"));
		assertTrue(block1_1.getInflation().input.contains("IN_FIRMA"));
		assertTrue(block1_1.getInflation().input.contains("IN_CENPLA"));
		assertTrue(block1_1.getInflation().input.contains("IN_LOK"));
		assertTrue(block1_1.getInflation().input.contains("IN_TOP"));
		assertTrue(IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
				"SELECT  TO_CHAR(SYSDATE,'yyyy-mm-dd') INGENDAT,  TO_CHAR(TO_DATE('^$IN_DATA^','YYYYMM'),'YYYYMM') INDATA,   " +
				"NVL('^$IN_FIRMA^', 'AUCHAN') INFIRMA,  NVL('^$IN_CENPLA^', '%') INCENPLA,  NVL('^$IN_LOK^', '%') INLOK,  " +
				"CASE '^$IN_TOP^' 	when 'TAK' then 'true' 	when 'NIE' then 'false' 	else 'false'  END AS INTOP	 FROM DUAL")
				.matches(block1_1.getInflation().getSql()));

		ReportTextBlock block2_1 = level1.get(1);
		assertNotNull(block2_1);
		assertTrue(StringUtils.isBlank(block2_1.getLabel()));
		assertNull(block2_1.getInflation());
		assertTrue(block2_1.hasContent());
		assertTrue(block2_1.getContent().startsWith("ZBIOR"));
		
		ReportTextBlock block3_1 = level1.get(2);
		assertNotNull(block3_1);
		assertTrue(block3_1.getLabel().equalsIgnoreCase("CNT"));
		assertNotNull(block3_1.getInflation());
		assertTrue(block3_1.getInflation().getLabel().equalsIgnoreCase("CNT"));
		assertFalse(block3_1.hasContent());
		assertTrue(IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
				"SELECT 	B.CDPAVE VCNTPOS, 	B.CDCOMP VCNTNAME, 	(SELECT X.LIBABR FROM ZD00 Y, ZD01 X WHERE Y.NUDOSS = X.NUDOSS AND " +
				"Y.SOCDOS = 'PPL' AND Y.CDSTCO = 'DL2' AND Y.CDCODE = B.CDCOMP) VCNTLAB, 	0 VCNTSUM FROM ZD00 A, ZDCG B WHERE 	" +
				"A.SOCDOS = 'PPL' AND A.CDSTCO = 'DRL' AND A.CDCODE = '90' 	AND A.NUDOSS = B.NUDOSS ORDER BY CDPAVE")
				.matches(block3_1.getInflation().getSql()));

		
		//..second level..
		assertTrue(block1_1.getBlocks().size()==0);
		assertTrue(block2_1.getBlocks().size()==0);
		
		LinkedList<ReportTextBlock> level2 = block3_1.getBlocks();
		assertNotNull(level2);
		assertTrue(level2.size() == 4);
		
		ReportTextBlock block1_2 = level2.get(0);
		assertNotNull(block1_2);
		assertTrue(block1_2.getLabel().equalsIgnoreCase("PITEM"));
		assertFalse(block1_2.hasContent());
		assertNotNull(block1_2.getInflation());
		assertTrue(block1_2.getInflation().getLabel().equalsIgnoreCase("PITEM"));
		assertTrue(block1_2.getInflation().input.size()==1);
		assertTrue(block1_2.getInflation().input.contains("VCNTNAME"));
		assertTrue(IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
				"SELECT 	CDELEM VPITEM, 	TYALIM VTYP, 	CDSIGN VSIGN FROM ZDBV A WHERE 	" +
				"A.NUDOSS = (SELECT NUDOSS FROM ZD00 WHERE SOCDOS = 'PPL' AND CDSTCO = 'DL2' " +
				"AND CDCODE = '^$VCNTNAME^')")
				.matches(block1_2.getInflation().getSql()));
		
		ReportTextBlock block2_2 = level2.get(1);
		assertNotNull(block2_2);
		assertTrue(StringUtils.isBlank(block2_2.getLabel()));
		assertNull(block2_2.getInflation());
		
		ReportTextBlock block3_2 = level2.get(2);
		assertNotNull(block3_2);
		assertTrue(block3_2.getLabel().equalsIgnoreCase("WIERSZ_CSV"));
		assertFalse(block3_2.hasContent());
		assertTrue(block3_2.parameters.size()==1);
		assertTrue(block3_2.parameters.contains("V_WIERSZ"));
		assertNotNull(block3_2.getInflation());
		assertTrue(block3_2.getInflation().getLabel().equalsIgnoreCase("WIERSZ_CSV"));
		assertTrue(block3_2.getInflation().input.size()==3);
		assertTrue(block3_2.getInflation().input.contains("VCNTNAME"));
		assertTrue(block3_2.getInflation().input.contains("VCNTLAB"));
		assertTrue(block3_2.getInflation().input.contains("VCNTSUM"));
		assertTrue(IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
				"SELECT   '^$VCNTNAME^'||';'||'^$VCNTLAB^'||';'||'^$VCNTSUM^'||';' " +
				"V_WIERSZ FROM DUAL WHERE TO_NUMBER('^$VCNTSUM^','FM9999999990.00') <> 0"
				).matches(block3_2.getInflation().getSql()));
		ReportTextBlock block4_2 = level2.get(3);
		assertNotNull(block4_2);
		assertTrue(StringUtils.isBlank(block4_2.getLabel()));
		assertNull(block4_2.getInflation());
		
		
		
		//..third level..
		assertTrue(block1_2.getBlocks().size()==1);
		assertTrue(block2_2.getBlocks().size()==0);
		assertTrue(block3_2.getBlocks().size()==1);
		assertTrue(block4_2.getBlocks().size()==0);
		
		LinkedList<ReportTextBlock> level3 = block1_2.getBlocks();
		assertNotNull(level3);
		assertTrue(level3.size() == 1);	
		
		ReportTextBlock block1_3 = level3.get(0);
		assertNotNull(block1_3);
		assertTrue(block1_3.getLabel().equalsIgnoreCase("PSUM"));
		assertFalse(block1_3.hasContent());
		assertTrue(block1_3.parameters.size()==0);
		assertNotNull(block1_3.getInflation());
		assertTrue(block1_3.getInflation().getLabel().equalsIgnoreCase("PSUM"));
		assertTrue(block1_3.getInflation().input.size()==9);
		assertTrue(block1_3.getInflation().input.contains("VCNTSUM"));
		assertTrue(block1_3.getInflation().input.contains("INDATA"));
		assertTrue(block1_3.getInflation().input.contains("VTYP"));
		assertTrue(block1_3.getInflation().input.contains("INTOP"));
		assertTrue(block1_3.getInflation().input.contains("VPITEM"));
		assertTrue(block1_3.getInflation().input.contains("VSIGN"));
		assertTrue(block1_3.getInflation().input.contains("INLOK"));
		assertTrue(block1_3.getInflation().input.contains("INCENPLA"));
		assertTrue(block1_3.getInflation().input.contains("INFIRMA"));
		assertTrue(IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
				"SELECT    TO_CHAR(     TO_NUMBER('^$VCNTSUM^','FM9999999990.90') + TOMEK.PAYITEM_ZX8K('^$INFIRMA^','^$INCENPLA^'," +
				"'^$INLOK^','^$INTOP^','^$VTYP^','^$VSIGN^','^$VPITEM^','^$INDATA^')     " +
				",'FM9999999990.90')	VCNTSUM FROM DUAL")
				.matches(block1_3.getInflation().getSql()));

		level3 = block3_2.getBlocks();
		assertNotNull(level3);
		assertTrue(level3.size() == 1);	
		
		ReportTextBlock block2_3 = level3.get(0);
		assertNotNull(block2_3);
		assertTrue(StringUtils.isBlank(block2_3.getLabel()));
		assertNull(block2_3.getInflation());		
	}

	@Test
	public void stan_zatr_struktura_blokow() throws PatternValidationException, ReportBlockException {
		File file = read("pl/com/dbs/reports/domain/builders/test/StanZatr001/STAN_ZATR_001.ZIP");
		PatternProduceContextDefault pcontext = mock(PatternProduceContextDefault.class);
		when(pcontext.getFile()).thenReturn(file);
		ReportPattern pattern = (ReportPattern)pfactory.produce(pcontext);

		PatternFormat format = new ReportPatternFormat(ReportType.TXT, "htm", "txt");

		ReportTextBlocksBuilder builder = new ReportTextBlocksBuilder(pattern.getTransformate(format));
		ReportTextBlock root = builder.build().getRootBlock();

		//..first level..
		LinkedList<ReportTextBlock> level1 = root.getBlocks();
		assertNotNull(level1);
		assertTrue(level1.size() == 9);

		ReportTextBlock block1_1 = level1.get(0);
		assertNotNull(block1_1);
		assertNull(block1_1.getLabel());
		assertTrue(block1_1.hasContent());
		assertTrue(block1_1.getContent().startsWith("<!DOCTYPE HTML"));
		assertNull(block1_1.getInflation());


		ReportTextBlock block2_1 = level1.get(1);
		assertTrue(block2_1.getLabel().equalsIgnoreCase("INIT"));
		assertNotNull(block2_1.getInflation());
		assertTrue(block2_1.getInflation().getLabel().equalsIgnoreCase("INIT"));
		assertTrue(block2_1.getInflation().input.size()==4);
		assertTrue(block2_1.getInflation().input.contains("IN_NAZWISKO"));
		assertTrue(block2_1.getInflation().input.contains("IN_MIES"));
		assertTrue(block2_1.getInflation().input.contains("IN_FIRM"));
		assertTrue(block2_1.getInflation().input.contains("IN_IMIE"));
		assertTrue(IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
				"SELECT  NVL('^$IN_FIRM^','AUCHAN') INFIRMA, " +
				"NVL('^$IN_MIES^',TO_CHAR(SYSDATE,'RRRRMM')) PV_MIES,  NVL('^$IN_IMIE^','Jarosław') PV_IMIE,  " +
				"NVL('^$IN_NAZWISKO^','Kowalczyk') PV_NAZWISKO,  TO_CHAR(SYSDATE,'YYYY-MM-DD') V_DATA_W,  " +
				"'<TD>0.000</TD><TD>0.000</TD>' C_POL258,  '<td><b>0.000</b></td><td><b>0.000</b></td>'  SUM_POL258 FROM DUAL")
			.matches(block2_1.getInflation().getSql()));
		assertTrue(block2_1.getBlocks().size() == 1);

		ReportTextBlock block4_1 = level1.get(3);
		assertNotNull(block4_1);
		assertTrue(block4_1.getLabel().equalsIgnoreCase("FIRMA"));
		assertNotNull(block4_1.getInflation());
		assertTrue(block4_1.getInflation().getLabel().equalsIgnoreCase("FIRMA"));
		assertTrue(block4_1.getInflation().input.size()==2);
		assertTrue(block4_1.getInflation().input.contains("PV_MIES"));
		assertTrue(block4_1.getInflation().input.contains("IN_FIRM"));
		assertFalse(block4_1.hasContent());
		assertTrue(IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
				"SELECT NIP F_NIP, REGON F_REGON, NAZWA_FIRMY F_NAZWA_FIRMY, KRAJ F_KRAJ, WOJEWODZTWO F_WOJEWODZTWO, " +
				"POWIAT F_POWIAT, GMINA F_GMINA, ULICA F_ULICA, NR_DOMU F_NR_DOMU, NR_LOKALU F_NR_LOKALU, MIEJSCOWOSC F_MIEJSCOWOSC, " +
				"KOD_POCZTOWY F_KOD_POCZTOWY, POCZTA F_POCZTA, SUBSTR('^$PV_MIES^',1,4)||'-'||SUBSTR('^$PV_MIES^',5,2) V_MIES, " +
				"TOMEK.GENRAP_DDL('DECLARE A NUMBER; BEGIN A:=TOMEK.FRAP121_ALL('||SUBSTR('^$PV_MIES^',1,4)||','||SUBSTR('^$PV_MIES^',5,2)||'," +
				"'||'''^$IN_FIRM^'''||'); END;') wyniki FROM TOMEK.FIRMY WHERE FIRMA = '^$IN_FIRM^'")
				.matches(block4_1.getInflation().getSql()));

		ReportTextBlock block6_1 = level1.get(5);
		assertNotNull(block6_1);
		assertTrue(block6_1.getLabel().equalsIgnoreCase("ALL_LOKAL"));
		assertNotNull(block6_1.getInflation());
		assertTrue(block6_1.getInflation().getLabel().equalsIgnoreCase("ALL_LOKAL"));
		assertTrue(block6_1.getInflation().input.size()==1);
		assertTrue(block6_1.getInflation().input.contains("IN_FIRM"));
		assertFalse(block6_1.hasContent());
		assertTrue(IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
						"SELECT TRIM(B.LOKALIZACJA) V_LOKAL " +
						"  FROM TOMEK.LOKALIZACJE B" +
						" WHERE B.FIRMA = '^$IN_FIRM^'" +
						"   ORDER BY 1")
				.matches(block6_1.getInflation().getSql()));

		ReportTextBlock block8_1 = level1.get(7);
		assertNotNull(block8_1);
		assertTrue(block8_1.getLabel().equalsIgnoreCase("AUCHAN_AVG"));
		assertNotNull(block8_1.getInflation());
		assertTrue(block8_1.getInflation().getLabel().equalsIgnoreCase("AUCHAN_AVG"));
		assertTrue(block8_1.getInflation().input.size()==2);
		assertTrue(block8_1.getInflation().input.contains("PV_MIES"));
		assertTrue(block8_1.getInflation().input.contains("IN_FIRM"));
		assertFalse(block8_1.hasContent());
		assertTrue(IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
				"SELECT '<td><b>'||LTRIM(TO_CHAR(C,'999990D999'))||'</b></td><td><b>'||LTRIM(TO_CHAR(D,'999990D999'))||'</b></td>'  SUM_AUCHAN " +
						"FROM TOMEK.RAP12_3_ALL A " +
						"WHERE A.YEAR||LPAD(A.MNTH,2,'0') = '^$PV_MIES^' " +
						"  AND A.FIRMA = '^$IN_FIRM^'")
				.matches(block8_1.getInflation().getSql()));


		//..second level..
		List<ReportTextBlock> level2 = block6_1.getBlocks();
		ReportTextBlock block2_2 = level2.get(1);
		assertNotNull(block2_2);
		assertTrue(block2_2.getLabel().equalsIgnoreCase("DZIEN"));
		assertNotNull(block2_2.getInflation());
		assertTrue(block2_2.getInflation().getLabel().equalsIgnoreCase("DZIEN"));
		assertTrue(block2_2.getInflation().input.size()==1);
		assertTrue(block2_2.getInflation().input.contains("PV_MIES"));
		assertFalse(block2_2.hasContent());
		assertTrue(IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
				"SELECT DISTINCT YEAR||LPAD(MNTH,2,0)||LPAD(DAY,2,0) V_DATA\n" +
						"FROM TOMEK.RAP12_1_ALL\n" +
						"WHERE YEAR||LPAD(MNTH,2,0) = '^$PV_MIES^' \n" +
						"ORDER BY 1")
				.matches(block2_2.getInflation().getSql()));

		ReportTextBlock block4_2 = level2.get(3);
		assertNotNull(block4_2);
		assertTrue(block4_2.getLabel().equalsIgnoreCase("LOKAL_DAT_AVG"));
		assertNotNull(block4_2.getInflation());
		assertTrue(block4_2.getInflation().getLabel().equalsIgnoreCase("LOKAL_DAT_AVG"));
		assertTrue(block4_2.getInflation().input.size()==2);
		assertTrue(block4_2.getInflation().input.contains("PV_MIES"));
		assertTrue(block4_2.getInflation().input.contains("V_LOKAL"));
		assertFalse(block4_2.hasContent());
		assertTrue(IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
				"SELECT '<td><b>SUMA</b></td><td><b>'||LTRIM(TO_CHAR(ETATY,'999990D999'))||'</b></td><td><b>'||LTRIM(TO_CHAR(OSOBY,'999990D999'))||'</b></td>'  SUM_LOKAL\n" +
						"FROM TOMEK.RAP12_2_ALL\n" +
						"WHERE YEAR||LPAD(MNTH,2,0) = '^$PV_MIES^'\n" +
						"  AND LOK = '^$V_LOKAL^'")
				.matches(block4_2.getInflation().getSql()));

		//..third level..
		List<ReportTextBlock> level3 = block2_2.getBlocks();
		ReportTextBlock block2_3 = level3.get(1);
		assertNotNull(block2_3);
		assertTrue(block2_3.getLabel().equalsIgnoreCase("LOKAL_DAT"));
		assertNotNull(block2_3.getInflation());
		assertTrue(block2_3.getInflation().getLabel().equalsIgnoreCase("LOKAL_DAT"));
		assertTrue(block2_3.getInflation().input.size()==2);
		assertTrue(block2_3.getInflation().input.contains("V_DATA"));
		assertTrue(block2_3.getInflation().input.contains("V_LOKAL"));
		assertFalse(block2_3.hasContent());
		assertTrue(IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
				"SELECT '<TR align=\"center\"><TD>'||DAY||'</TD><TD>'||LTRIM(TO_CHAR(ETATY,'99990D999'))||'</TD><TD>'||LTRIM(TO_CHAR(OSOBY,'99990D999'))||'</TD></TR>'  C_LOKAL_DAT\n" +
						"FROM TOMEK.RAP12_1_ALL\n" +
						"WHERE YEAR||LPAD(MNTH,2,0)||LPAD(DAY,2,0) = ^$V_DATA^\n" +
						"  AND LOK = '^$V_LOKAL^'")
				.matches(block2_3.getInflation().getSql()));


	}


	@Test
	public void postgress_struktura_blokow() throws PatternValidationException, ReportBlockException {
		File file = read("pl/com/dbs/reports/domain/builders/test/Postgress/Postgress.zip");
		PatternProduceContextDefault pcontext = mock(PatternProduceContextDefault.class);
		when(pcontext.getFile()).thenReturn(file);
		ReportPattern pattern = (ReportPattern) pfactory.produce(pcontext);

		PatternFormat format = new ReportPatternFormat(ReportType.TXT, "htm", "txt");

		ReportTextBlocksBuilder builder = new ReportTextBlocksBuilder(pattern.getTransformate(format));
		ReportTextBlock root = builder.build().getRootBlock();

		//..first level..
		LinkedList<ReportTextBlock> level1 = root.getBlocks();
		assertNotNull(level1);
		assertTrue(level1.size() == 5);

		ReportTextBlock block1_1 = level1.get(0);
		assertNotNull(block1_1);
		assertNull(block1_1.getLabel());
		assertTrue(block1_1.hasContent());
		assertTrue(block1_1.getContent().startsWith("<!DOCTYPE HTML"));
		assertNull(block1_1.getInflation());


		ReportTextBlock block2_1 = level1.get(1);
		assertTrue(block2_1.getLabel().equalsIgnoreCase("INIT"));
		assertNotNull(block2_1.getInflation());
		assertTrue(block2_1.getInflation().getLabel().equalsIgnoreCase("INIT"));
		assertTrue(block2_1.getInflation().input.size() == 4);
		assertTrue(block2_1.getInflation().input.contains("IN_NAZWISKO"));
		assertTrue(block2_1.getInflation().input.contains("IN_MIES"));
		assertTrue(block2_1.getInflation().input.contains("IN_FIRM"));
		assertTrue(block2_1.getInflation().input.contains("IN_IMIE"));
		assertTrue(IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
				"SELECT\n" +
						" NVL('^$IN_FIRM^','AUCHAN') INFIRMA, \n" +
						" NVL('^$IN_MIES^',TO_CHAR(SYSDATE,'RRRRMM')) PV_MIES,\n" +
						" NVL('^$IN_IMIE^','Jarosław') PV_IMIE,\n" +
						" NVL('^$IN_NAZWISKO^','Kowalczyk') PV_NAZWISKO,\n" +
						" TO_CHAR(SYSDATE,'YYYY-MM-DD') V_DATA_W,\n" +
						" '<TD>0.000</TD><TD>0.000</TD>' C_POL258,\n" +
						" '<td><b>0.000</b></td><td><b>0.000</b></td>'  SUM_POL258")
				.matches(block2_1.getInflation().getSql()));
		assertTrue(block2_1.getBlocks().size() == 1);


		ReportTextBlock block4_1 = level1.get(3);
		assertNotNull(block4_1);
		assertTrue(block4_1.getLabel().equalsIgnoreCase("FIRMA"));
		assertNotNull(block4_1.getInflation());
		assertTrue(block4_1.getInflation().getLabel().equalsIgnoreCase("FIRMA"));
		assertTrue(block4_1.getInflation().input.size()==2);
		assertTrue(block4_1.getInflation().input.contains("PV_MIES"));
		assertTrue(block4_1.getInflation().input.contains("IN_FIRM"));
		assertFalse(block4_1.hasContent());
		assertTrue(IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(
				"SELECT\n" +
						"  NIP F_NIP,\n" +
						"  REGON F_REGON,\n" +
						"  NAZWA_FIRMY F_NAZWA_FIRMY,\n" +
						"  KRAJ F_KRAJ,\n" +
						"  WOJEWODZTWO F_WOJEWODZTWO,\n" +
						"  POWIAT F_POWIAT,\n" +
						"  GMINA F_GMINA,\n" +
						"  ULICA F_ULICA,\n" +
						"  NR_DOMU F_NR_DOMU,\n" +
						"  NR_LOKALU F_NR_LOKALU,\n" +
						"  MIEJSCOWOSC F_MIEJSCOWOSC,\n" +
						"  KOD_POCZTOWY F_KOD_POCZTOWY,\n" +
						"  POCZTA F_POCZTA,\n" +
						" SUBSTR('^$PV_MIES^',1,4)||'-'||SUBSTR('^$PV_MIES^',5,2) V_MIES,\n" +
						" TOMEK.GENRAP_DDL('DECLARE A NUMBER; BEGIN A:=TOMEK.FRAP121_ALL('||SUBSTR('^$PV_MIES^',1,4)||','||SUBSTR('^$PV_MIES^',5,2)||','||'''^$IN_FIRM^'''||'); END;') wyniki")
				.matches(block4_1.getInflation().getSql()));
	}


	private File read(String src) {
		File file = null;
		try {
			URL url = ReportFactoryDefaultTest.class.getClassLoader().getResource(src);
			file = new File(url.toURI());
		} catch (URISyntaxException e) {}
		if (!file.exists()) throw new IllegalStateException(); 
		return file;
	}

}
