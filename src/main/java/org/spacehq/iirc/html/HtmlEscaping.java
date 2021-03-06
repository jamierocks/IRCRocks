/*
 * This file is part of IRCRocks, licensed under the MIT License (MIT).
 *
 * Copyright (C) 2014-2015 Steveice10
 * Copyright (c) 2015, Jamie Mansfield <https://www.jamierocks.uk/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spacehq.iirc.html;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class HtmlEscaping {
    private static final Map<Integer, String> ENTITIES = new HashMap<Integer, String>();

    static {
        ENTITIES.put(34, "quot");
        ENTITIES.put(38, "amp");
        ENTITIES.put(60, "lt");
        ENTITIES.put(62, "gt");
        ENTITIES.put(161, "iexcl");
        ENTITIES.put(162, "cent");
        ENTITIES.put(163, "pound");
        ENTITIES.put(164, "curren");
        ENTITIES.put(165, "yen");
        ENTITIES.put(166, "brvbar");
        ENTITIES.put(167, "sect");
        ENTITIES.put(168, "uml");
        ENTITIES.put(169, "copy");
        ENTITIES.put(170, "ordf");
        ENTITIES.put(171, "laquo");
        ENTITIES.put(172, "not");
        ENTITIES.put(173, "shy");
        ENTITIES.put(174, "reg");
        ENTITIES.put(175, "macr");
        ENTITIES.put(176, "deg");
        ENTITIES.put(177, "plusmn");
        ENTITIES.put(178, "sup2");
        ENTITIES.put(179, "sup3");
        ENTITIES.put(180, "acute");
        ENTITIES.put(181, "micro");
        ENTITIES.put(182, "para");
        ENTITIES.put(183, "middot");
        ENTITIES.put(184, "cedil");
        ENTITIES.put(185, "sup1");
        ENTITIES.put(186, "ordm");
        ENTITIES.put(187, "raquo");
        ENTITIES.put(188, "frac14");
        ENTITIES.put(189, "frac12");
        ENTITIES.put(190, "frac34");
        ENTITIES.put(191, "iquest");
        ENTITIES.put(192, "Agrave");
        ENTITIES.put(193, "Aacute");
        ENTITIES.put(194, "Acirc");
        ENTITIES.put(195, "Atilde");
        ENTITIES.put(196, "Auml");
        ENTITIES.put(197, "Aring");
        ENTITIES.put(198, "AElig");
        ENTITIES.put(199, "Ccedil");
        ENTITIES.put(200, "Egrave");
        ENTITIES.put(201, "Eacute");
        ENTITIES.put(202, "Ecirc");
        ENTITIES.put(203, "Euml");
        ENTITIES.put(204, "Igrave");
        ENTITIES.put(205, "Iacute");
        ENTITIES.put(206, "Icirc");
        ENTITIES.put(207, "Iuml");
        ENTITIES.put(208, "ETH");
        ENTITIES.put(209, "Ntilde");
        ENTITIES.put(210, "Ograve");
        ENTITIES.put(211, "Oacute");
        ENTITIES.put(212, "Ocirc");
        ENTITIES.put(213, "Otilde");
        ENTITIES.put(214, "Ouml");
        ENTITIES.put(215, "times");
        ENTITIES.put(216, "Oslash");
        ENTITIES.put(217, "Ugrave");
        ENTITIES.put(218, "Uacute");
        ENTITIES.put(219, "Ucirc");
        ENTITIES.put(220, "Uuml");
        ENTITIES.put(221, "Yacute");
        ENTITIES.put(222, "THORN");
        ENTITIES.put(223, "szlig");
        ENTITIES.put(224, "agrave");
        ENTITIES.put(225, "aacute");
        ENTITIES.put(226, "acirc");
        ENTITIES.put(227, "atilde");
        ENTITIES.put(228, "auml");
        ENTITIES.put(229, "aring");
        ENTITIES.put(230, "aelig");
        ENTITIES.put(231, "ccedil");
        ENTITIES.put(232, "egrave");
        ENTITIES.put(233, "eacute");
        ENTITIES.put(234, "ecirc");
        ENTITIES.put(235, "euml");
        ENTITIES.put(236, "igrave");
        ENTITIES.put(237, "iacute");
        ENTITIES.put(238, "icirc");
        ENTITIES.put(239, "iuml");
        ENTITIES.put(240, "eth");
        ENTITIES.put(241, "ntilde");
        ENTITIES.put(242, "ograve");
        ENTITIES.put(243, "oacute");
        ENTITIES.put(244, "ocirc");
        ENTITIES.put(245, "otilde");
        ENTITIES.put(246, "ouml");
        ENTITIES.put(247, "divide");
        ENTITIES.put(248, "oslash");
        ENTITIES.put(249, "ugrave");
        ENTITIES.put(250, "uacute");
        ENTITIES.put(251, "ucirc");
        ENTITIES.put(252, "uuml");
        ENTITIES.put(253, "yacute");
        ENTITIES.put(254, "thorn");
        ENTITIES.put(255, "yuml");
        ENTITIES.put(402, "fnof");
        ENTITIES.put(913, "Alpha");
        ENTITIES.put(914, "Beta");
        ENTITIES.put(915, "Gamma");
        ENTITIES.put(916, "Delta");
        ENTITIES.put(917, "Epsilon");
        ENTITIES.put(918, "Zeta");
        ENTITIES.put(919, "Eta");
        ENTITIES.put(920, "Theta");
        ENTITIES.put(921, "Iota");
        ENTITIES.put(922, "Kappa");
        ENTITIES.put(923, "Lambda");
        ENTITIES.put(924, "Mu");
        ENTITIES.put(925, "Nu");
        ENTITIES.put(926, "Xi");
        ENTITIES.put(927, "Omicron");
        ENTITIES.put(928, "Pi");
        ENTITIES.put(929, "Rho");
        ENTITIES.put(931, "Sigma");
        ENTITIES.put(932, "Tau");
        ENTITIES.put(933, "Upsilon");
        ENTITIES.put(934, "Phi");
        ENTITIES.put(935, "Chi");
        ENTITIES.put(936, "Psi");
        ENTITIES.put(937, "Omega");
        ENTITIES.put(945, "alpha");
        ENTITIES.put(946, "beta");
        ENTITIES.put(947, "gamma");
        ENTITIES.put(948, "delta");
        ENTITIES.put(949, "epsilon");
        ENTITIES.put(950, "zeta");
        ENTITIES.put(951, "eta");
        ENTITIES.put(952, "theta");
        ENTITIES.put(953, "iota");
        ENTITIES.put(954, "kappa");
        ENTITIES.put(955, "lambda");
        ENTITIES.put(956, "mu");
        ENTITIES.put(957, "nu");
        ENTITIES.put(958, "xi");
        ENTITIES.put(959, "omicron");
        ENTITIES.put(960, "pi");
        ENTITIES.put(961, "rho");
        ENTITIES.put(962, "sigmaf");
        ENTITIES.put(963, "sigma");
        ENTITIES.put(964, "tau");
        ENTITIES.put(965, "upsilon");
        ENTITIES.put(966, "phi");
        ENTITIES.put(967, "chi");
        ENTITIES.put(968, "psi");
        ENTITIES.put(969, "omega");
        ENTITIES.put(977, "thetasym");
        ENTITIES.put(978, "upsih");
        ENTITIES.put(982, "piv");
        ENTITIES.put(8226, "bull");
        ENTITIES.put(8230, "hellip");
        ENTITIES.put(8242, "prime");
        ENTITIES.put(8243, "Prime");
        ENTITIES.put(8254, "oline");
        ENTITIES.put(8260, "frasl");
        ENTITIES.put(8472, "weierp");
        ENTITIES.put(8465, "image");
        ENTITIES.put(8476, "real");
        ENTITIES.put(8482, "trade");
        ENTITIES.put(8501, "alefsym");
        ENTITIES.put(8592, "larr");
        ENTITIES.put(8593, "uarr");
        ENTITIES.put(8594, "rarr");
        ENTITIES.put(8595, "darr");
        ENTITIES.put(8596, "harr");
        ENTITIES.put(8629, "crarr");
        ENTITIES.put(8656, "lArr");
        ENTITIES.put(8657, "uArr");
        ENTITIES.put(8658, "rArr");
        ENTITIES.put(8659, "dArr");
        ENTITIES.put(8660, "hArr");
        ENTITIES.put(8704, "forall");
        ENTITIES.put(8706, "part");
        ENTITIES.put(8707, "exist");
        ENTITIES.put(8709, "empty");
        ENTITIES.put(8711, "nabla");
        ENTITIES.put(8712, "isin");
        ENTITIES.put(8713, "notin");
        ENTITIES.put(8715, "ni");
        ENTITIES.put(8719, "prod");
        ENTITIES.put(8721, "sum");
        ENTITIES.put(8722, "minus");
        ENTITIES.put(8727, "lowast");
        ENTITIES.put(8730, "radic");
        ENTITIES.put(8733, "prop");
        ENTITIES.put(8734, "infin");
        ENTITIES.put(8736, "ang");
        ENTITIES.put(8743, "and");
        ENTITIES.put(8744, "or");
        ENTITIES.put(8745, "cap");
        ENTITIES.put(8746, "cup");
        ENTITIES.put(8747, "int");
        ENTITIES.put(8756, "there4");
        ENTITIES.put(8764, "sim");
        ENTITIES.put(8773, "cong");
        ENTITIES.put(8776, "asymp");
        ENTITIES.put(8800, "ne");
        ENTITIES.put(8801, "equiv");
        ENTITIES.put(8804, "le");
        ENTITIES.put(8805, "ge");
        ENTITIES.put(8834, "sub");
        ENTITIES.put(8835, "sup");
        ENTITIES.put(8838, "sube");
        ENTITIES.put(8839, "supe");
        ENTITIES.put(8853, "oplus");
        ENTITIES.put(8855, "otimes");
        ENTITIES.put(8869, "perp");
        ENTITIES.put(8901, "sdot");
        ENTITIES.put(8968, "lceil");
        ENTITIES.put(8969, "rceil");
        ENTITIES.put(8970, "lfloor");
        ENTITIES.put(8971, "rfloor");
        ENTITIES.put(9001, "lang");
        ENTITIES.put(9002, "rang");
        ENTITIES.put(9674, "loz");
        ENTITIES.put(9824, "spades");
        ENTITIES.put(9827, "clubs");
        ENTITIES.put(9829, "hearts");
        ENTITIES.put(9830, "diams");
        ENTITIES.put(338, "OElig");
        ENTITIES.put(339, "oelig");
        ENTITIES.put(352, "Scaron");
        ENTITIES.put(353, "scaron");
        ENTITIES.put(376, "Yuml");
        ENTITIES.put(710, "circ");
        ENTITIES.put(732, "tilde");
        ENTITIES.put(8194, "ensp");
        ENTITIES.put(8195, "emsp");
        ENTITIES.put(8201, "thinsp");
        ENTITIES.put(8204, "zwnj");
        ENTITIES.put(8205, "zwj");
        ENTITIES.put(8206, "lrm");
        ENTITIES.put(8207, "rlm");
        ENTITIES.put(8211, "ndash");
        ENTITIES.put(8212, "mdash");
        ENTITIES.put(8216, "lsquo");
        ENTITIES.put(8217, "rsquo");
        ENTITIES.put(8218, "sbquo");
        ENTITIES.put(8220, "ldquo");
        ENTITIES.put(8221, "rdquo");
        ENTITIES.put(8222, "bdquo");
        ENTITIES.put(8224, "dagger");
        ENTITIES.put(8225, "Dagger");
        ENTITIES.put(8240, "permil");
        ENTITIES.put(8249, "lsaquo");
        ENTITIES.put(8250, "rsaquo");
        ENTITIES.put(8364, "euro");
    }

    public static String escape(String str) {
        StringWriter writer = new StringWriter((int) (str.length() + (str.length() * 0.1)));
        int len = str.length();
        for(int i = 0; i < len; i++) {
            char c = str.charAt(i);
            String entityName = ENTITIES.get((int) c);
            if(entityName == null) {
                if(c > 0x7F) {
                    writer.write("&#");
                    writer.write(Integer.toString(c, 10));
                    writer.write(';');
                } else {
                    writer.write(c);
                }
            } else {
                writer.write('&');
                writer.write(entityName);
                writer.write(';');
            }
        }

        return writer.toString();
    }
}
