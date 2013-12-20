/**
 * 
 */
package pl.com.dbs.reports.report;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.text.BadLocationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.util.FileCopyUtils;

import pl.com.dbs.reports.api.report.ReportValidationException;
import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternFactory;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternValidator;
import pl.com.dbs.reports.api.support.db.SqlExecuteException;
import pl.com.dbs.reports.api.support.db.SqlExecutor;
import pl.com.dbs.reports.profile.dao.ProfileDao;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.pattern.domain.PatternFactoryDefault;
import pl.com.dbs.reports.report.pattern.domain.PatternProduceContextDefault;
import pl.com.dbs.reports.report.pattern.domain.validator.PatternContentFormValidator;
import pl.com.dbs.reports.security.domain.SessionContext;

import com.google.common.io.Files;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SessionContext.class)
public class PatternFactoryDefaultTest {
	private ProfileDao profileDao;
	private PatternFactory pfactory;
	private Profile profile; 
	
	@Mock
	private SqlExecutor executor;
	
	
	@Before
	public void doBeforeEachTestCase() {
		MockitoAnnotations.initMocks(this);
		profileDao = mock(ProfileDao.class);
		profile = mock(Profile.class);
		when(profile.getId()).thenReturn(1L);
		
		PowerMockito.mockStatic(SessionContext.class);
	    PowerMockito.when(SessionContext.getProfile()).thenReturn(profile);		
		
		when(profileDao.find(anyLong())).thenReturn(profile);
	}
	
	@After  
    public void tearDown() {  
		pfactory = null;
		executor = null;  
    } 	
	
	@Test
	public void testDefinicjaTest3() throws PatternValidationException, ReportValidationException, SqlExecuteException, SQLException, ClassNotFoundException, IOException, BadLocationException {
		pfactory = new PatternFactoryDefault(profileDao, null, Collections.<PatternValidator>emptyList());
		
		File file = read("pl/com/dbs/reports/pattern/test3/test3.zip");
		PatternProduceContextDefault pcontext = mock(PatternProduceContextDefault.class);
		when(pcontext.getFile()).thenReturn(file);
		Pattern pattern = pfactory.produce(pcontext);
		
		assertTrue(pattern.getFactory().equals(PatternFactoryDefault.class.getCanonicalName()));
		//assertTrue(pattern.getManifest().getNameTemplate());
		assertTrue(pattern.getTransformates().size()==1);
		assertTrue(pattern.getTransformates().get(0).getName().equals("wzor.rtf"));
		
		File transformate1 = read("pl/com/dbs/reports/pattern/test3/wzor.rtf");
		byte[] btransformate1 = readFile(transformate1);
		System.out.println(new String(btransformate1));
		assertTrue(btransformate1.length==pattern.getTransformates().get(0).getContent().length);
		
		
//		RTFEditorKit parser = new RTFEditorKit();
//		Document document = parser.createDefaultDocument();
//       	parser.read(new ByteArrayInputStream(pattern.getTransformates().get(0).getContent()), document, 0);
//       	//System.out.println(document.getText(0, document.getLength()));
		
//		File transformate2 = bytesToFile(pattern.getTransformates().get(0).getName(), 
//				new File("C:\\workspace\\dbs-reports-assets\\tests"),
//				pattern.getTransformates().get(0).getContent());
//		assertTrue(transformate2.length()==transformate1.length());
	}
	

	@Test
	public void testDefinicjaTest5() throws PatternValidationException, ReportValidationException, SqlExecuteException, SQLException, ClassNotFoundException, IOException, BadLocationException {
		pfactory = new PatternFactoryDefault(profileDao, null, Arrays.asList(new PatternValidator[] {new PatternContentFormValidator()}));
		
		File file = read("pl/com/dbs/reports/pattern/test5/test5.zip");
		PatternProduceContextDefault pcontext = mock(PatternProduceContextDefault.class);
		when(pcontext.getFile()).thenReturn(file);
		Pattern pattern = pfactory.produce(pcontext);
		
		File form1 = read("pl/com/dbs/reports/pattern/test5/form.xml");
		byte[] bform1 = readFile(form1);
		System.out.println(new String(bform1));
		assertTrue(bform1.length==pattern.getForms().get(0).getContent().length);
	}
	
	//@Test(expected=PatternValidationException.class)
	public void testDefinicjaTest5NotValidForm() throws PatternValidationException, ReportValidationException, SqlExecuteException, SQLException, ClassNotFoundException, IOException, BadLocationException {
		pfactory = new PatternFactoryDefault(profileDao, null, Arrays.asList(new PatternValidator[] {new PatternContentFormValidator()}));
		
		File file = read("pl/com/dbs/reports/pattern/test5/test5.1.zip");
		PatternProduceContextDefault pcontext = mock(PatternProduceContextDefault.class);
		when(pcontext.getFile()).thenReturn(file);
		pfactory.produce(pcontext);
	}	
	

	private File read(String src) {
		File file = null;
		try {
			URL url = PatternFactoryDefaultTest.class.getClassLoader().getResource(src);
			file = new File(url.toURI());
		} catch (URISyntaxException e) {}
		if (!file.exists()) throw new IllegalStateException(); 
		return file;
	}
	
	private byte[] readFile(final File file) throws IOException {
		InputStream input = new FileInputStream(file);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) 
	        output.write(buffer, 0, bytesRead);
	    output.close();
	    input.close();
	    return output.toByteArray();
	}	
	
	private File bytesToFile(String name, File dir, byte[] content) throws IOException {
		File tmpFile = new File(dir, name);
		tmpFile.deleteOnExit();
		FileCopyUtils.copy(content, new FileOutputStream(tmpFile));
		return tmpFile;
	}
	
    private File bytesToFile(String name, byte[] content) throws IOException {
    	File dir = Files.createTempDir();
    	return bytesToFile(name, dir, content);
    } 		
	
}
