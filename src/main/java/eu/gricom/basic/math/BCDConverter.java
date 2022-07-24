package eu.gricom.basic.math;

public class BCDConverter {
    public static byte[] toBCD(double fRealValue) {
        int iNumOfDigits = 0;
        boolean bNegRealValue = false;

        // Remember whether the number if negative. The algorithm uses positive values.
        if (fRealValue < 0) {
            bNegRealValue = true;
            fRealValue *= -1;
        }

        // Count the number of digits.
        double fTemp = fRealValue;
        while (fTemp != 0) {
            iNumOfDigits++;
            fTemp /= 10;
        }

        int iByteLen = iNumOfDigits % 2 == 0 ? iNumOfDigits / 2 : (iNumOfDigits + 1) / 2;

        byte[] abyteBcd = new byte[iByteLen];

        for (int iX = 0; iX < iNumOfDigits; iX++) {
            byte byteTmp = (byte) (fRealValue % 10);

            if (iX % 2 == 0) {
                abyteBcd[iX / 2] = byteTmp;
            } else {
                abyteBcd[iX / 2] |= (byte) (byteTmp << 4);
            }

            fRealValue /= 10;
        }

        for (int iX = 0; iX < iByteLen / 2; iX++) {
            byte tmp = abyteBcd[iX];
            abyteBcd[iX] = abyteBcd[iByteLen - iX - 1];
            abyteBcd[iByteLen - iX - 1] = tmp;
        }

        return abyteBcd;
    }

    public static double toReal(byte[] abyteBCDValue) {
        return 0;
    }
}
