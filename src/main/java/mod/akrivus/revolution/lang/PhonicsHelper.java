package mod.akrivus.revolution.lang;

import java.util.List;
import java.util.Random;

import scala.actors.threadpool.Arrays;

public class PhonicsHelper {
	private static final Random RANDOM = new Random();
	private static final String[] CONSOS = new String[] {
		"b",
		"br",
		"c",
		"ch",
		"cr",
		"d",
		"dg",
		"dh",
		"dt",
		"dr",
		"f",
		"fl",
		"fr",
		"fw",
		"g",
		"gh",
		"gl",
		"gm",
		"gn",
		"gr",
		"gs",
		"gz",
		"gw",
		"h",
		"j",
		"jh",
		"k",
		"kl",
		"kh",
		"kw",
		"kr",
		"l",
		"m",
		"mk",
		"n",
		"nk",
		"p",
		"ph",
		"pr",
		"ps",
		"q",
		"r",
		"rs",
		"s",
		"sc",
		"sh",
		"sk",
		"sp",
		"sr",
		"st",
		"sw",
		"t",
		"th",
		"tr",
		"ts",
		"v",
		"vr",
		"w",
		"x",
		"y",
		"z",
		"zh",
		"zr",
		"'"
	};
	private static final String[] VOWELS = new String[] {
		"a",
		"aa",
		"e",
		"ee",
		"i",
		"o",
		"oo",
		"u"
	};
	public static String generateName(int length, int margin, String... blacklist) {
		length = length + RANDOM.nextInt(margin) - margin;
		List offset = Arrays.asList(blacklist);
		boolean vowel = RANDOM.nextBoolean();
		String buildon = "";
		for (int i = 0; i < length; ++i) {
			vowel = !vowel;
			if (vowel) {
				String ch = VOWELS[RANDOM.nextInt(VOWELS.length)];
				if (offset.contains(ch)) {
					vowel = !vowel;
					--i; continue;
				}
				buildon += ch;
			}
			else {
				String ch = CONSOS[RANDOM.nextInt(CONSOS.length)];
				if (offset.contains(ch)) {
					vowel = !vowel;
					--i; continue;
				}
				buildon += ch;
			}
		}
		return new StringBuilder().append(buildon.substring(0, 1).toUpperCase()).append(buildon.substring(1)).toString();
	}
	public static String generateName(int length, int margin) {
		return generateName(length, margin, "-");
	}
	public static String generateName(String... blacklist) {
		return generateName(10, 2, blacklist);
	}
	public static String generateName() {
		return generateName(10, 2);
	}
}
