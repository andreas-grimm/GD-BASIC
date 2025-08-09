package eu.gricom.basic.math;

public class BCDConverter {
    /**
     * Converts a non-fractional double value to its Binary-Coded Decimal (BCD) representation.
     *
     * The input value is converted to its absolute value before encoding. Each decimal digit is packed into a byte array, with two digits per byte (lower nibble for the less significant digit, upper nibble for the more significant digit). The resulting array represents the BCD encoding of the integer part of the input value.
     *
     * @param fRealValue the double value to convert to BCD; only the integer part is encoded, and negative values are treated as positive
     * @return a byte array containing the BCD representation of the input value
     */
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

    /**
     * Converts a BCD-encoded byte array to its corresponding double value.
     *
     * @param abyteBCDValue the byte array containing the BCD-encoded number
     * @return the decoded double value; currently always returns 0 as this method is not implemented
     */
    public static double toReal(byte[] abyteBCDValue) {
        return 0;
    }
}
