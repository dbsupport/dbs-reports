/**
 * 
 */
package pl.com.dbs.reports.report;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.util.FileCopyUtils;

import pl.com.dbs.reports.api.report.pattern.PatternFactory;
import pl.com.dbs.reports.api.report.pattern.PatternValidator;
import pl.com.dbs.reports.api.support.db.SqlExecutor;
import pl.com.dbs.reports.profile.dao.ProfileDao;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.domain.ReportFactoryDefault;
import pl.com.dbs.reports.report.domain.builders.inflaters.ReportTextBlockInflaterDefault;
import pl.com.dbs.reports.report.domain.builders.inflaters.ReportTextBlockInflaterQuery;
import pl.com.dbs.reports.report.pattern.domain.PatternFactoryDefault;
import pl.com.dbs.reports.security.domain.SessionContext;
import pl.com.dbs.reports.support.encoding.EncodingContext;
import pl.com.dbs.reports.support.encoding.EncodingService;

/**
 * Raport generation tests.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SessionContext.class)
public class ReportFactoryDefaultTest {
	private ProfileDao profileDao;
	private ReportFactoryDefault rfactory;
	private PatternFactory pfactory;
	private Profile profile; 
	private ReportTextBlockInflaterQuery queryInflater;
	private ReportTextBlockInflaterDefault defaultInflater;
	private SqlExecutor<Map<String, String>> executor;
	private EncodingService encodingService;
	private EncodingContext encodingContext;
	
	
	@Before
	public void doBeforeEachTestCase() {
		MockitoAnnotations.initMocks(this);
		profileDao = mock(ProfileDao.class);
		profile = mock(Profile.class);
		when(profile.isGlobal()).thenReturn(true);
		when(profile.getUuid()).thenReturn("1");
		
		executor = mock(SqlExecutor.class);
		encodingContext = mock(EncodingContext.class);
		when(encodingContext.getInEncoding()).thenReturn("UTF-8");
		when(encodingContext.getOutEncoding()).thenReturn("UTF-8");
		encodingService = mock(EncodingService.class);
		when(encodingService.getEncodingContext()).thenReturn(encodingContext);
		queryInflater = new ReportTextBlockInflaterQuery(executor, encodingService);
		
		defaultInflater = new ReportTextBlockInflaterDefault();
		
		PowerMockito.mockStatic(SessionContext.class);
	    PowerMockito.when(SessionContext.getProfile()).thenReturn(profile);		
		
		//rfactory = new ReportFactoryDefault(profileDao, queryInflater);
		
		pfactory = new PatternFactoryDefault(profileDao, rfactory, Collections.<PatternValidator>emptyList());
		
		when(profileDao.find(anyLong())).thenReturn(profile);
		
	}
	
	@After  
    public void tearDown() {  
		rfactory = null;  
		pfactory = null;
		executor = null;  
    } 	

//	@Test
//	public void niewazna_wielkosc_liter() throws PatternValidationException, ReportValidationException, ConnectionException, SQLException, ClassNotFoundException, IOException, BadLocationException {
//		File file = read("pl/com/dbs/reports/test1/test1.zip");
//		PatternProduceContextDefault pcontext = mock(PatternProduceContextDefault.class);
//		when(pcontext.getFile()).thenReturn(file);
//		ReportPattern pattern = (ReportPattern)pfactory.produce(pcontext);
//		
//		ReportProduceContextDefault rcontext = mock(ReportProduceContextDefault.class);
//		when(rcontext.getParameters()).thenReturn(ImmutableMap.<String, String>of("IN_DATA", "TEstowe różne Wartości"));
//		PatternFormat format = mock(PatternFormat.class);
//		when(format.getPatternExtension()).thenReturn("txt");
//		when(format.getReportExtension()).thenReturn("txt");
//		when(format.getReportType()).thenReturn(ReportType.TXT);
//		when(rcontext.getFormat()).thenReturn(format);
//		when(rcontext.getName()).thenReturn("test");
//		when(rcontext.getPattern()).thenReturn(pattern);
//		
//		ResultSet rs1 = mock(ResultSet.class);
//		ResultSetMetaData rs1md = mock(ResultSetMetaData.class);
//		when(rs1md.getColumnCount()).thenReturn(2);
//		when(rs1md.getColumnName(1)).thenReturn("PV_DATA");
//		when(rs1md.getColumnName(2)).thenReturn("UNUSED");
//		when(rs1.getMetaData()).thenReturn(rs1md);		
//		
//		when(rs1.next()).thenReturn(true).thenReturn(false);
//		when(rs1.getString("PV_DATA")).thenReturn("2013/11/09");
//		when(rs1.getString("UNUSED")).thenReturn("COMPLETELY UNUSEFULL");
//		when(executor.execute(contains("PV_DATA FROM DUAL"))).thenReturn(rs1);		
//	}	
	
//	@Test
//	public void testDefinicjaTest3() throws PatternValidationException, ReportValidationException, ConnectionException, SQLException, ClassNotFoundException, IOException, BadLocationException {
//		File file = read("pl/com/dbs/reports/pattern/test3/test3.zip");
//		PatternProduceContextDefault pcontext = mock(PatternProduceContextDefault.class);
//		when(pcontext.getFile()).thenReturn(file);
//		ReportPattern pattern = (ReportPattern)pfactory.produce(pcontext);
//		
//		ReportProduceContextDefault rcontext = mock(ReportProduceContextDefault.class);
//		when(rcontext.getParameters()).thenReturn(ImmutableMap.<String, String>of("IN_DATA", "TEST"));
//		when(rcontext.getFormat()).thenReturn(ReportFormat.RTF);
//		when(rcontext.getName()).thenReturn("test");
//		when(rcontext.getPattern()).thenReturn(pattern);
//		
//		ResultSet rs1 = mock(ResultSet.class);
//		ResultSetMetaData rs1md = mock(ResultSetMetaData.class);
//		when(rs1md.getColumnCount()).thenReturn(2);
//		when(rs1md.getColumnName(1)).thenReturn("PV_DATA");
//		when(rs1md.getColumnName(2)).thenReturn("UNUSED");
//		when(rs1.getMetaData()).thenReturn(rs1md);
//		
//		when(rs1.next()).thenReturn(true).thenReturn(false);
//		when(rs1.getString("PV_DATA")).thenReturn("2013/11/09");
//		when(rs1.getString("UNUSED")).thenReturn("COMPLETELY UNUSEFULL");
//		when(executor.execute(contains("PV_DATA FROM DUAL"))).thenReturn(rs1);
//		
//		ResultSet rs2 = mock(ResultSet.class);
//		ResultSetMetaData rs2md = mock(ResultSetMetaData.class);
//		when(rs2md.getColumnCount()).thenReturn(3);
//		when(rs2md.getColumnName(1)).thenReturn("ZA00_NUDOSS");
//		when(rs2md.getColumnName(2)).thenReturn("NAZWISKO");
//		when(rs2md.getColumnName(2)).thenReturn("IMIE");
//		when(rs2.getMetaData()).thenReturn(rs2md);
//		
//		when(rs2.getString("ZA00_NUDOSS")).thenReturn("1");
//		when(rs2.getString("NAZWISKO")).thenReturn("Adam");
//		when(rs2.getString("IMIE")).thenReturn("Składam");
//		when(rs2.next()).thenReturn(true).thenReturn(true).thenReturn(false);
//		when(executor.execute(contains("FROM ZA00"))).thenReturn(rs2);
//		
//		Report report = rfactory.produce(rcontext);
//		
////		System.out.println(new String(report.getContent()));
////		
////		RTFEditorKit parser = new RTFEditorKit();
////		Document document = parser.createDefaultDocument();
////       	parser.read(new ByteArrayInputStream(report.getContent()), document, 0);
////       	System.out.println(document.getText(0, document.getLength()));
////		
//		assertTrue(report.getName().equals("test.rtf"));
//		
//		bytesToFile(report.getName(),  
//				new File("C:\\workspace\\dbs-reports-assets\\tests"),
//				report.getContent());
//	}
//	
//	@Test
//	public void testDefinicjaTest4() throws PatternValidationException, ReportValidationException, ConnectionException, SQLException, ClassNotFoundException, IOException, BadLocationException {
//		File file = read("pl/com/dbs/reports/pattern/test4/test4.zip");
//		PatternProduceContextDefault pcontext = mock(PatternProduceContextDefault.class);
//		when(pcontext.getFile()).thenReturn(file);
//		ReportPattern pattern = (ReportPattern)pfactory.produce(pcontext);
//		
//		ReportProduceContextDefault rcontext = mock(ReportProduceContextDefault.class);
//		when(rcontext.getParameters()).thenReturn(ImmutableMap.<String, String>of("IN_DATA", "2013/11/11"));
//		when(rcontext.getFormat()).thenReturn(ReportFormat.RTF);
//		when(rcontext.getName()).thenReturn("test");
//		when(rcontext.getPattern()).thenReturn(pattern);
//		
//		ResultSet rs1 = mock(ResultSet.class);
//		ResultSetMetaData rs1md = mock(ResultSetMetaData.class);
//		when(rs1md.getColumnCount()).thenReturn(2);
//		when(rs1md.getColumnName(1)).thenReturn("PV_DATA");
//		when(rs1md.getColumnName(2)).thenReturn("UNUSED");
//		when(rs1.getMetaData()).thenReturn(rs1md);		
//		
//		when(rs1.next()).thenReturn(true).thenReturn(false);
//		when(rs1.getString("PV_DATA")).thenReturn("2013/11/11");
//		when(rs1.getString("UNUSED")).thenReturn("COMPLETELY UNUSEFULL");
//		when(executor.execute(contains("2013/11/09"))).thenReturn(rs1);
//
//		
//		ResultSet rs2 = mock(ResultSet.class);
//		ResultSetMetaData rs2md = mock(ResultSetMetaData.class);
//		when(rs2md.getColumnCount()).thenReturn(3);
//		when(rs2md.getColumnName(1)).thenReturn("ZA00_NUDOSS");
//		when(rs2md.getColumnName(2)).thenReturn("NAZWISKO");
//		when(rs2md.getColumnName(2)).thenReturn("IMIE");
//		when(rs2.getMetaData()).thenReturn(rs2md);
//		
//		when(rs2.getString("ZA00_NUDOSS")).thenReturn("1");
//		when(rs2.getString("NAZWISKO")).thenReturn("Adam");
//		when(rs2.getString("IMIE")).thenReturn("Składam");
//		when(rs2.next()).thenReturn(true).thenReturn(true).thenReturn(false);
//		when(executor.execute(contains("ZA00_NUDOSS"))).thenReturn(rs2);
//		Report report = rfactory.produce(rcontext);
//		
//		assertTrue(report!=null);
//	}
	
	private File read(String src) {
		File file = null;
		try {
			URL url = ReportFactoryDefaultTest.class.getClassLoader().getResource(src);
			file = new File(url.toURI());
		} catch (URISyntaxException e) {}
		if (!file.exists()) throw new IllegalStateException(); 
		return file;
	}
	
	private File bytesToFile(String name, File dir, byte[] content) throws IOException {
		File tmpFile = new File(dir, name);
		//tmpFile.deleteOnExit();
		FileCopyUtils.copy(content, new FileOutputStream(tmpFile));
		return tmpFile;
	}	
}
