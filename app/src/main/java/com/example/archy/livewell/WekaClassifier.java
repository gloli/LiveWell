// Generated with Weka 3.7.12
//
// This code is public domain and comes with no warranty.
//
// Timestamp: Tue Feb 17 15:24:24 EST 2015

package com.example.archy.livewell;

class WekaClassifier {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier.N2d432c0c0(i);
        return p;
    }
    static double N2d432c0c0(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 53.734691) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 53.734691) {
            p = WekaClassifier.N1500df0b1(i);
        }
        return p;
    }
    static double N1500df0b1(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 435.803916) {
            p = WekaClassifier.N6c3355f22(i);
        } else if (((Double) i[0]).doubleValue() > 435.803916) {
            p = WekaClassifier.N7dad858216(i);
        }
        return p;
    }
    static double N6c3355f22(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 112.458902) {
            p = WekaClassifier.N1e4fba5d3(i);
        } else if (((Double) i[0]).doubleValue() > 112.458902) {
            p = WekaClassifier.N4543f374(i);
        }
        return p;
    }
    static double N1e4fba5d3(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 0;
        } else if (((Double) i[3]).doubleValue() <= 2.096006) {
            p = 0;
        } else if (((Double) i[3]).doubleValue() > 2.096006) {
            p = 1;
        }
        return p;
    }
    static double N4543f374(Object []i) {
        double p = Double.NaN;
        if (i[13] == null) {
            p = 1;
        } else if (((Double) i[13]).doubleValue() <= 9.111253) {
            p = WekaClassifier.N370236195(i);
        } else if (((Double) i[13]).doubleValue() > 9.111253) {
            p = WekaClassifier.N329bbe6614(i);
        }
        return p;
    }
    static double N370236195(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 1;
        } else if (((Double) i[4]).doubleValue() <= 19.295697) {
            p = WekaClassifier.N722dbf856(i);
        } else if (((Double) i[4]).doubleValue() > 19.295697) {
            p = WekaClassifier.N1fe2e22510(i);
        }
        return p;
    }
    static double N722dbf856(Object []i) {
        double p = Double.NaN;
        if (i[15] == null) {
            p = 1;
        } else if (((Double) i[15]).doubleValue() <= 2.320926) {
            p = 1;
        } else if (((Double) i[15]).doubleValue() > 2.320926) {
            p = WekaClassifier.N1978ef537(i);
        }
        return p;
    }
    static double N1978ef537(Object []i) {
        double p = Double.NaN;
        if (i[26] == null) {
            p = 0;
        } else if (((Double) i[26]).doubleValue() <= 0.89977) {
            p = 0;
        } else if (((Double) i[26]).doubleValue() > 0.89977) {
            p = WekaClassifier.N52a83f358(i);
        }
        return p;
    }
    static double N52a83f358(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 1;
        } else if (((Double) i[3]).doubleValue() <= 8.864152) {
            p = WekaClassifier.N23cddaf89(i);
        } else if (((Double) i[3]).doubleValue() > 8.864152) {
            p = 1;
        }
        return p;
    }
    static double N23cddaf89(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 0;
        } else if (((Double) i[6]).doubleValue() <= 5.429293) {
            p = 0;
        } else if (((Double) i[6]).doubleValue() > 5.429293) {
            p = 1;
        }
        return p;
    }
    static double N1fe2e22510(Object []i) {
        double p = Double.NaN;
        if (i[28] == null) {
            p = 1;
        } else if (((Double) i[28]).doubleValue() <= 1.731447) {
            p = 1;
        } else if (((Double) i[28]).doubleValue() > 1.731447) {
            p = WekaClassifier.N686cfb7d11(i);
        }
        return p;
    }
    static double N686cfb7d11(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 67.793642) {
            p = WekaClassifier.N4af3011a12(i);
        } else if (((Double) i[2]).doubleValue() > 67.793642) {
            p = WekaClassifier.N17cd786713(i);
        }
        return p;
    }
    static double N4af3011a12(Object []i) {
        double p = Double.NaN;
        if (i[16] == null) {
            p = 2;
        } else if (((Double) i[16]).doubleValue() <= 1.985916) {
            p = 2;
        } else if (((Double) i[16]).doubleValue() > 1.985916) {
            p = 1;
        }
        return p;
    }
    static double N17cd786713(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 330.76262) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 330.76262) {
            p = 2;
        }
        return p;
    }
    static double N329bbe6614(Object []i) {
        double p = Double.NaN;
        if (i[7] == null) {
            p = 0;
        } else if (((Double) i[7]).doubleValue() <= 12.659948) {
            p = 0;
        } else if (((Double) i[7]).doubleValue() > 12.659948) {
            p = WekaClassifier.Nef1347f15(i);
        }
        return p;
    }
    static double Nef1347f15(Object []i) {
        double p = Double.NaN;
        if (i[25] == null) {
            p = 0;
        } else if (((Double) i[25]).doubleValue() <= 2.201093) {
            p = 0;
        } else if (((Double) i[25]).doubleValue() > 2.201093) {
            p = 1;
        }
        return p;
    }
    static double N7dad858216(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 1;
        } else if (((Double) i[64]).doubleValue() <= 12.523609) {
            p = 1;
        } else if (((Double) i[64]).doubleValue() > 12.523609) {
            p = WekaClassifier.Nb185a4417(i);
        }
        return p;
    }
    static double Nb185a4417(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 669.63333) {
            p = WekaClassifier.N6bce2c0c18(i);
        } else if (((Double) i[0]).doubleValue() > 669.63333) {
            p = 2;
        }
        return p;
    }
    static double N6bce2c0c18(Object []i) {
        double p = Double.NaN;
        if (i[16] == null) {
            p = 2;
        } else if (((Double) i[16]).doubleValue() <= 12.460893) {
            p = WekaClassifier.N2cadff1f19(i);
        } else if (((Double) i[16]).doubleValue() > 12.460893) {
            p = 1;
        }
        return p;
    }
    static double N2cadff1f19(Object []i) {
        double p = Double.NaN;
        if (i[13] == null) {
            p = 1;
        } else if (((Double) i[13]).doubleValue() <= 2.909026) {
            p = 1;
        } else if (((Double) i[13]).doubleValue() > 2.909026) {
            p = 2;
        }
        return p;
    }
}
