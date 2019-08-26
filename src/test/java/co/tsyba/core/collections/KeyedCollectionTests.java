package co.tsyba.core.collections;

import co.tsyba.core.collections.data.MapCollection;
import java.util.ArrayList;
import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Aug 5, 2019.
 */
public class KeyedCollectionTests {
	@Test
	public void checksEmpty() {
		final var entries = collect(
				"f", "askdmana",
				"o", "alskaman");

		assert entries.isEmpty() == false;
	}

	@Test
	public void returnsEntryCount() {
		final var entries = collect(
				"v", "aamananw",
				"T", "posjne",
				"K", "fbdbsvaua");

		assert entries.getCount() == 3;
	}

	@Test
	public void checksKeyContainment() {
		final var entries = collect(
				"g", "amnsnaj",
				"M", "aoaii",
				"e", "bvgsgw",
				"O", "psoocclme");

		// key is present
		assert entries.contains("e");
		// key is absent
		assert entries.contains("E") == false;
	}

	@Test
	public void checksEntryContainment() {
		final var entries = collect(
				"M", "nndbd",
				"O", "sndnnjss",
				"P", "tbdbdhs",
				"i", "nfbfbsus");

		// entry is present
		assert entries.contains("P", "tbdbdhs");
		// entry with a key is present
		assert entries.contains("i", "ngbbdjs") == false;
		// entry with a value is present
		assert entries.contains("L", "nfbfbsus") == false;
		// entry is absent
		assert entries.contains("U", "kdjdj") == false;
	}

	@Test
	public void checksEntriesContainment() {
		final var entries = collect(
				"T", "mdndnbs",
				"t", "anaabbaah",
				"M", "bvcvxhw",
				"w", "azzxxyssssa");

		// all entries are present
		assert entries.contains(collect(
				"t", "anaabbaah",
				"M", "bvcvxhw",
				"w", "azzxxyssssa"));

		// some entries are present
		assert entries.contains(collect(
				"t", "nbbfvdpeoe",
				"n", "bvcvxhw",
				"w", "azzxxyssssa")) == false;

		// contains itself
		assert entries.contains(entries);
	}

	@Test
	public void checksNoEntryMatch() {
		final var entries = collect(
				"f", "nanaba",
				"a", "poaopa",
				"s", "iuiqha",
				"t", "annaj");

		// contains no entries where value starts with key
		assert entries.noneMatches((key, value)
				-> value.startsWith(key));

		// does not contain no entries where value length is less than 6
		assert entries.noneMatches((key, value)
				-> value.length() < 6) == false;
	}

	@Test
	public void checksAnyEntryMatch() {
		final var entries = collect(
				"I", "dndndbxj",
				"F", "nfxsuus",
				"Z", "xbxbxhsyya",
				"E", "aananzt");

		// contains entry with upper case key and value containing z
		assert entries.anyMatches((key, value)
				-> isUpperCase(key) && value.contains("z"));

		// does not contain entry with upper case key value containing i
		assert entries.anyMatches((key, value)
				-> isLowerCase(key) && value.contains("i")) == false;
	}

	@Test
	public void checksEachEntryMatch() {
		final var entries = collect(
				"a", "andndbxa",
				"g", "gfxsuug",
				"c", "cbxbxhsyyc",
				"w", "wananzr");

		// every entry has value which starts with its key
		assert entries.eachMatches((key, value)
				-> value.startsWith(key));

		// not every entry has value which ends with its key
		assert entries.eachMatches((key, value)
				-> value.endsWith(key)) == false;
	}

	@Test
	public void iteratesEntries() {
		final var entries = collect(
				"g", "mvnn",
				"d", "paposmmmw",
				"W", "asmffks",
				"a", "asdww");

		final var keys = new ArrayList<String>();
		final var values = new ArrayList<String>();
		entries.iterate((key, value) -> {
			keys.add(key);
			values.add(value);
		});

		// keys and values must be at the same indexes
		for (var entry : entries) {
			assert keys.indexOf(entry.key) == values.indexOf(entry.value);
		}
	}

	@Test
	public void combinesEntries() {
		final var entries = collect(
				"k", "paospoen",
				"m", "appaollek",
				"y", "mdnjs",
				"x", "oolfkkfid",
				"z", "aamaio");

		// combines into total character count of entries
		final var characterCount = entries.combine(11, (count, key, value)
				-> count + key.length() + value.length());

		assert characterCount == 53;
	}

	@Test
	public void joinsEntries() {
		final var entries = collect(
				"v", "wsisja",
				"e", "kfnxnu");

		final var joinedEntries = entries.join(": ", ", ");
		assert joinedEntries.equals(""
				+ "e: kfnxnu, "
				+ "v: wsisja");
	}

	private static <T> KeyedCollection<T, T> collect(T... values) {
		return new MapCollection<>(values);
	}

	private static boolean isLowerCase(String string) {
		return string.toLowerCase().equals(string);
	}

	private static boolean isUpperCase(String string) {
		return string.toUpperCase().equals(string);
	}
}
