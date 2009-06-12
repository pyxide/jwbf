/**
 * 
 */
package net.sourceforge.jwbf.live.mediawiki;

import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import net.sourceforge.jwbf.LiveTestFather;
import net.sourceforge.jwbf.actions.mediawiki.MediaWiki.Version;
import net.sourceforge.jwbf.actions.mediawiki.queries.RecentchangeTitles;
import net.sourceforge.jwbf.actions.util.ActionException;
import net.sourceforge.jwbf.actions.util.ProcessException;
import net.sourceforge.jwbf.bots.MediaWikiAdapterBot;
import net.sourceforge.jwbf.bots.MediaWikiBot;
import net.sourceforge.jwbf.contentRep.Article;
import net.sourceforge.jwbf.contentRep.SimpleArticle;

import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Thomas Stock
 *
 */
public class RecentChangesTest extends LiveTestFather {
	private MediaWikiAdapterBot bot = null;
	private static final int COUNT = 13;
	private static final int LIMIT = COUNT * 2;
	/**
	 * 
	 * @throws Exception a
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		PropertyConfigurator.configureAndWatch("test4log4j.properties",
				60 * 1000);
		addInitSupporterVersions(RecentchangeTitles.class);
	}
	
	
	/**
	 * Test.
	 * @throws Exception a
	 */
	@Test
	public final void recentChangesWikiMW1x09() throws Exception {
		
		bot = new MediaWikiAdapterBot(getValue("wikiMW1_09_url"));
		bot.login(getValue("wikiMW1_09_user"), getValue("wikiMW1_09_pass"));
		doRegularTest(bot);
		doSpecialCharTest(bot);
		assertTrue("Wrong Wiki Version " + bot.getVersion(), Version.MW1_09.equals(bot.getVersion()));
	}
	/**
	 * Test.
	 * @throws Exception a
	 */
	@Test
	public final void recentChangesWikiMW1x10() throws Exception {
		
		bot = new MediaWikiAdapterBot(getValue("wikiMW1_10_url"));
		bot.login(getValue("wikiMW1_10_user"), getValue("wikiMW1_10_pass"));
		doRegularTest(bot);
		doSpecialCharTest(bot);
		assertTrue("Wrong Wiki Version " + bot.getVersion(), Version.MW1_10.equals(bot.getVersion()));
	}
	
	/**
	 * Test.
	 * @throws Exception a
	 */
	@Test
	public final void recentChangesWikiMW1x11() throws Exception {
		
		bot = new MediaWikiAdapterBot(getValue("wikiMW1_11_url"));
		bot.login(getValue("wikiMW1_11_user"), getValue("wikiMW1_11_pass"));
		doRegularTest(bot);
		doSpecialCharTest(bot);
		assertTrue("Wrong Wiki Version " + bot.getVersion(), Version.MW1_11.equals(bot.getVersion()));
	}
	
	/**
	 * Test.
	 * @throws Exception a
	 */
	@Test
	public final void recentChangesWikiMW1x12() throws Exception {
		
		bot = new MediaWikiAdapterBot(getValue("wikiMW1_12_url"));
		bot.login(getValue("wikiMW1_12_user"), getValue("wikiMW1_12_pass"));
		doRegularTest(bot);
		doSpecialCharTest(bot);
		assertTrue("Wrong Wiki Version " + bot.getVersion(), Version.MW1_12.equals(bot.getVersion()));
	}
	/**
	 * Test.
	 * @throws Exception a
	 */
	@Test
	public final void recentChangesWikiMW1x13() throws Exception {
		bot = new MediaWikiAdapterBot(getValue("wikiMW1_13_url"));
		bot.login(getValue("wikiMW1_13_user"), getValue("wikiMW1_13_pass"));
		doRegularTest(bot);
		doSpecialCharTest(bot);
		assertTrue("Wrong Wiki Version " + bot.getVersion(), Version.MW1_13.equals(bot.getVersion()));
	}
	
	/**
	 * Test.
	 * @throws Exception a
	 */
	@Test
	public final void recentChangesWikiMW1x14() throws Exception {
		bot = new MediaWikiAdapterBot(getValue("wikiMW1_14_url"));
		bot.login(getValue("wikiMW1_14_user"), getValue("wikiMW1_14_pass"));
		doRegularTest(bot);
		doSpecialCharTest(bot);
		assertTrue("Wrong Wiki Version " + bot.getVersion(), Version.MW1_14.equals(bot.getVersion()));
	}
	/**
	 * Test.
	 * @throws Exception a
	 */
	@Test
	public final void recentChangesWikiMW1x15() throws Exception {
		bot = new MediaWikiAdapterBot(getValue("wikiMW1_15_url"));
		bot.login(getValue("wikiMW1_15_user"), getValue("wikiMW1_15_pass"));
		doRegularTest(bot);
		doSpecialCharTest(bot);
		assertTrue("Wrong Wiki Version " + bot.getVersion(), Version.MW1_15.equals(bot.getVersion()));
	}
	
	private void prepareWiki(MediaWikiBot bot) throws ActionException,
	ProcessException {
		SimpleArticle a = new SimpleArticle("Change", "0");
		for (int i = 0; i < 5 + 1; i++) {
			String label = getRandom(10);
			for (char c : MediaWikiBot.INVALID_LABEL_CHARS) {
				label = label.replace(c + "", "");
			}
			a.setLabel(label);
			a.setText(getRandom(255));
			bot.writeContent(a);
		}

	}
	private void doSpecialCharTest(MediaWikiBot bot) throws ActionException,
	ProcessException {
		Article sa;
		String testText = getRandom(255);

		
		Collection<String> specialChars = getSpecialChars();
		try {
			for (String label1 : specialChars) {
				sa = new Article(bot, testText, label1);
				sa.save();
			}
		} catch (ActionException e) {
			boolean found = false;
			for (char ch : MediaWikiBot.INVALID_LABEL_CHARS) {
				if (e.getMessage().contains(ch + "")) {
					found = true;
					break;
				}
			}
			assertTrue("should be a know invalid char",  found);
		}

			RecentchangeTitles rc = new RecentchangeTitles(bot);
			
			Iterator<String> is = rc.iterator();
			int i = 0;
			int size = specialChars.size();
			
			while (is.hasNext() && i < (size * 1.2)) {
				String nx = is.next();
				System.err.println("rm " + nx);
				specialChars.remove(nx);
				i++;
			}
			for (char c : MediaWikiBot.INVALID_LABEL_CHARS) {
				specialChars.remove(c + "");
			}
			
			assertTrue("tc sould be empty but is: " + specialChars, specialChars.isEmpty());
		
	}
	
	private void doRegularTest(MediaWikiBot bot) throws ActionException,
			ProcessException {
		prepareWiki(bot);
		RecentchangeTitles rc = new RecentchangeTitles(bot);
		
		Iterator<String> is = rc.iterator();
		int i = 0;
	
		
		
		Vector<Integer> vi = new Vector<Integer>();
		try {
			is = rc.iterator();

			i = 0;
			vi.clear();
			for (int j = 0; j < COUNT; j++) {

				vi.add(j);

			}

			while (is.hasNext()) {
				String s = is.next();
				int x = Integer.parseInt(s.split(" ")[1]);
				// System.out.println(vi);
				vi.remove(new Integer(x));
				i++;
				if (i > LIMIT || vi.isEmpty()) {
					break;
				}
			}
			if (!vi.isEmpty()) {
				throw new Exception();
			}
		} catch (Exception e) {
			change(bot);
			is = rc.iterator();

			i = 0;
			vi.clear();
			for (int j = 0; j < COUNT; j++) {

				vi.add(j);

			}

			while (is.hasNext()) {
				String s = is.next();
				int x = Integer.parseInt(s.split(" ")[1]);
//				System.err.println("rm " + s + " ~= " + x);
				vi.remove(new Integer(x));
				i++;
				if (i > LIMIT || vi.isEmpty()) {
					break;
				}
			}
		}
		assertTrue("shuld be empty but is : " + vi, vi.isEmpty());
		assertTrue("i is: " + i, i > COUNT - 1);
		registerTestedVersion(RecentchangeTitles.class, bot.getVersion());
	}
	private void change(MediaWikiBot bot) throws ActionException, ProcessException {
		SimpleArticle a = new SimpleArticle("Change", "0");
		for (int i = 0; i < COUNT + 1; i++) {
			a.setLabel("Change " + i);
			a.setText(getRandom(255));
			bot.writeContent(a);
		}
	}
}
